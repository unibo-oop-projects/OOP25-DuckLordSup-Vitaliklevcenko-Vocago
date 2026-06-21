package it.unibo.vocago.storage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.user.Profile;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;
import it.unibo.vocago.model.vocabulary.WordEntry;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;
import it.unibo.vocago.storage.api.UserRepository;

public class UserCsvStorage implements UserRepository {

    private static final Path USERS_DIRECTORY = Path.of("data", "users");
    private static final String WORD_SEPARATOR = ",";
    private static final String QUOTE = "\"";
    private static final String ESCAPED_QUOTE = "\"\"";
    private static final int HEADER_LINE_INDEX = 0;
    private static final int HEADER_FIRST_LANGUAGE_INDEX = 0;
    private static final int HEADER_SECOND_LANGUAGE_INDEX = 1;
    private static final int FIRST_VOCABULARY_DATA_LINE_INDEX = 1;
    private static final int FIRST_WORDS_COLUMN_INDEX = 0;
    private static final int SECOND_WORDS_COLUMN_INDEX = 1;
    private static final int FIRST_PROGRESS_COLUMN_INDEX = 2;
    private static final int SECOND_PROGRESS_COLUMN_INDEX = 3;
    private static final int MINIMUM_VOCABULARY_COLUMN_COUNT = 2;
    private static final int PROGRESS_MASTERY_INDEX = 0;
    private static final int PROGRESS_CORRECT_ANSWERS_INDEX = 1;
    private static final int PROGRESS_WRONG_ANSWERS_INDEX = 2;
    private static final Logger LOGGER = Logger.getLogger(UserCsvStorage.class.getName());

    @Override
    public void save(final User user) {
        try {
            Files.createDirectories(USERS_DIRECTORY);

            final List<String> lines = new ArrayList<>();
            lines.add(csvLine(user.getFirstLanguage(), user.getSecondLanguage()));

            for (final VocabularyItem item : user.getVocabulary().getItems()) {
                lines.add(toCsvLine(item));
            }

            Files.write(fileFor(user.getUserName()), lines, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Could not save user: " + user.getUserName(), exception);
        }
    }

    @Override
    public boolean userExists(final String userName) {
        return Files.exists(fileFor(userName));
    }

    @Override
    public boolean deleteUser(final String userName) {
        try {
            return Files.deleteIfExists(fileFor(userName));
        } catch (final IOException exception) {
            throw new UncheckedIOException("Could not delete user: " + userName, exception);
        }
    }

    @Override
    public List<User> getExistingUsers() {
        if (!Files.exists(USERS_DIRECTORY)) {
            return List.of();
        }

        try {
            final List<User> users = new ArrayList<>();
            final List<Path> allFiles = Files.list(USERS_DIRECTORY).toList();

            for (final Path file : allFiles) {
                if (isCsvFile(file)) {
                    try {
                        users.add(loadUser(file));
                        // can use runtimeexception but will require suppress
                    } catch (IllegalArgumentException | IndexOutOfBoundsException | IOException exception) {
                        LOGGER.log(
                                Level.WARNING,
                                "Skipping corrupted profile file: " + file,
                                exception);
                    }
                }
            }
            return users;
        } catch (final IOException exception) {
            throw new UncheckedIOException("Could not load users", exception);
        }
    }

    private User loadUser(final Path file) throws IOException {
        final List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new IOException("Empty user file: " + file);
        }

        final List<String> header = splitCsv(lines.get(HEADER_LINE_INDEX));
        final Path fileName = file.getFileName();
        if (fileName == null) {
            throw new IOException("User file has no file name: " + file);
        }
        final String userName = fileName.toString().replaceFirst("\\.csv$", "");
        final Dictionary vocabulary = new Dictionary();

        for (int i = FIRST_VOCABULARY_DATA_LINE_INDEX; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                vocabulary.addItem(fromCsvLine(lines.get(i)));
            }
        }

        return new Profile(
                userName,
                vocabulary,
                header.get(HEADER_FIRST_LANGUAGE_INDEX),
                header.get(HEADER_SECOND_LANGUAGE_INDEX));
    }

    private boolean isCsvFile(final Path file) {
        return Files.isRegularFile(file) && file.toString().endsWith(".csv");
    }

    private static Path fileFor(final String userName) {
        return USERS_DIRECTORY.resolve(userName.trim() + ".csv");
    }

    private static String toCsvLine(final VocabularyItem item) {
        return csvLine(
                joinWords(item.getFirstLanguageWords()),
                joinWords(item.getSecondLanguageWords()),
                serializeProgress(item.getProgress(Direction.FIRST_TO_SECOND)),
                serializeProgress(item.getProgress(Direction.SECOND_TO_FIRST)));
    }

    private static VocabularyItem fromCsvLine(final String line) {
        final List<String> values = splitCsv(line);
        if (values.size() < MINIMUM_VOCABULARY_COLUMN_COUNT) {
            throw new IllegalArgumentException("Vocabulary row must contain at least two columns.");
        }

        return new DictionaryEntry(
                splitWords(values.get(FIRST_WORDS_COLUMN_INDEX)),
                splitWords(values.get(SECOND_WORDS_COLUMN_INDEX)),
                progressAt(values, FIRST_PROGRESS_COLUMN_INDEX),
                progressAt(values, SECOND_PROGRESS_COLUMN_INDEX));
    }

    private static String joinWords(final List<Word> words) {
        final StringBuilder text = new StringBuilder();

        for (final Word word : words) {
            if (text.length() > 0) {
                text.append(',');
            }
            text.append(word.getWord());
        }

        return text.toString();
    }

    private static List<Word> splitWords(final String text) {
        if (text.isBlank()) {
            return List.of();
        }

        final List<Word> words = new ArrayList<>();
        for (final String word : text.split(WORD_SEPARATOR)) {
            final String trimmed = word.trim();
            if (!trimmed.isBlank()) {
                words.add(new WordEntry(trimmed));
            }
        }
        return words;
    }

    private static Progress progressAt(final List<String> values, final int index) {
        if (values.size() <= index || values.get(index).isBlank()) {
            return new WordProgress();
        }
        return parseProgress(values.get(index));
    }

    private static String serializeProgress(final Progress progress) {
        return progress.getMasteryLevel()
                + ":" + progress.getCorrectAnswers()
                + ":" + progress.getWrongAnswers();
    }

    private static Progress parseProgress(final String text) {
        final String[] parts = text.split(":");
        return new WordProgress(
                MasteryLevel.valueOf(parts[PROGRESS_MASTERY_INDEX]),
                Integer.parseInt(parts[PROGRESS_CORRECT_ANSWERS_INDEX]),
                Integer.parseInt(parts[PROGRESS_WRONG_ANSWERS_INDEX]));
    }

    private static String csvLine(final String... values) {
        final StringBuilder line = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                line.append(',');
            }
            line.append(escapeCsv(values[i]));
        }

        return line.toString();
    }

    private static String escapeCsv(final String value) {
        return QUOTE + value.replace(QUOTE, ESCAPED_QUOTE) + QUOTE;
    }

    private static List<String> splitCsv(final String line) {
        final List<String> values = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            final char ch = line.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
            } else if (ch == ',' && !quoted) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values;
    }
}

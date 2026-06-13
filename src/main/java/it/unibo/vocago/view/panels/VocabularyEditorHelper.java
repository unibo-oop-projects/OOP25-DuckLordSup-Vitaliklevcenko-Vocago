package it.unibo.vocago.view.panels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import it.unibo.vocago.model.progress.WordProgress;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.DictionaryEntry;
import it.unibo.vocago.model.vocabulary.WordEntry;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.model.vocabulary.api.Word;

final class VocabularyEditorHelper {

    static final int FIRST_WORDS_COLUMN = 0;
    static final int SECOND_WORDS_COLUMN = 1;
    static final int MEMORIZATION_COLUMN = 2;
    static final int RECOGNITION_COLUMN = 3;
    static final int FIRST_PROGRESS_COLUMN = 4;
    static final int SECOND_PROGRESS_COLUMN = 5;

    static Object[] emptyRow() {
        return new Object[] {
                "",
                "",
                MasteryLevel.NEW,
                MasteryLevel.NEW,
                new WordProgress(),
                new WordProgress()
        };
    }

    static Object[] rowFromVocabularyItem(final VocabularyItem item) {
        return new Object[] {
                wordsToText(item.getFirstLanguageWords()),
                wordsToText(item.getSecondLanguageWords()),
                item.getProgress(Direction.FIRST_TO_SECOND).getMasteryLevel(),
                item.getProgress(Direction.SECOND_TO_FIRST).getMasteryLevel(),
                item.getProgress(Direction.FIRST_TO_SECOND),
                item.getProgress(Direction.SECOND_TO_FIRST)
        };
    }

    static Vocabulary toVocabulary(final DefaultTableModel model) {
        final List<VocabularyItem> vocabularyItems = new ArrayList<>();

        for (int row = 0; row < model.getRowCount(); row++) {

            final String firstText = cellToText(model.getValueAt(row, FIRST_WORDS_COLUMN));
            final String secondText = cellToText(model.getValueAt(row, SECOND_WORDS_COLUMN));
            if (firstText.isBlank() && secondText.isBlank()) {
                continue;
            }

            final MasteryLevel firstMasteryLevel = masteryLevelAt(model, row, MEMORIZATION_COLUMN);
            final MasteryLevel secondMasteryLevel = masteryLevelAt(model, row, RECOGNITION_COLUMN);

            vocabularyItems.add(new DictionaryEntry(
                    parseWordEntries(firstText),
                    parseWordEntries(secondText),
                    progressWithMastery(model.getValueAt(row, FIRST_PROGRESS_COLUMN), firstMasteryLevel),
                    progressWithMastery(model.getValueAt(row, SECOND_PROGRESS_COLUMN), secondMasteryLevel)));
        }

        return new Dictionary(vocabularyItems);
    }

    static String cellToText(final Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private static List<Word> parseWordEntries(final String text) {
        final List<Word> entries = new ArrayList<>();
        for (final String part : text.split(",")) {
            final String trimmed = part.trim();
            if (!trimmed.isBlank()) {
                entries.add(new WordEntry(trimmed));
            }
        }
        return entries;
    }

    private static String wordsToText(final List<Word> words) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            builder.append(words.get(i).getWord());
            if (i < words.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private static MasteryLevel masteryLevelAt(final DefaultTableModel model, final int row, final int column) {
        return model.getValueAt(row, column) instanceof MasteryLevel masteryLevel ? masteryLevel : MasteryLevel.NEW;
    }

    private static Progress progressWithMastery(final Object value, final MasteryLevel masteryLevel) {
        return value instanceof Progress progress ? new WordProgress(masteryLevel, progress.getCorrectAnswers(),
            progress.getWrongAnswers()) : new WordProgress(masteryLevel);
    }
}

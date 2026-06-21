package it.unibo.vocago.model.user;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

/**
 * Default implementation of {@link User}.
 */
public final class Profile implements User {

    private final String userName;
    private final String firstLanguage;
    private final String secondLanguage;
    private final Vocabulary vocabulary;

    /**
     * Creates a profile with an empty vocabulary.
     *
     * @param userName       the unique profile name; must not be blank or {@code null}
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language the user is studying
     */
    public Profile(final String userName, final String firstLanguage, final String secondLanguage) {
        this(userName, new Dictionary(), firstLanguage, secondLanguage);
    }

    /**
     * Creates a profile with the given vocabulary.
     *
     * @param userName       the unique profile name; must not be blank or {@code null}
     * @param vocabulary     the user's vocabulary; must not be {@code null}
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language the user is studying
     * @throws IllegalArgumentException if the user name is blank or {@code null}
     */
    public Profile(final String userName, final Vocabulary vocabulary, final String firstLanguage,
            final String secondLanguage) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("User name must not be blank or null");
        }

        this.userName = userName.trim();
        this.vocabulary = Objects.requireNonNull(vocabulary, "vocabulary must not be null");
        this.firstLanguage = firstLanguage;
        this.secondLanguage = secondLanguage;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    @SuppressFBWarnings(value = "EI", justification = "The profile intentionally exposes its mutable vocabulary, needs the live vocabulary and not a copy.")
    public Vocabulary getVocabulary() {
        return this.vocabulary;
    }

    @Override
    public String getFirstLanguage() {
        return this.firstLanguage;
    }

    @Override
    public String getSecondLanguage() {
        return this.secondLanguage;
    }
}
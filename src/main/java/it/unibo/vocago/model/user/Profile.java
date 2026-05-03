package it.unibo.vocago.model.user;

import java.util.Objects;

import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.Dictionary;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public class Profile implements User {

    private final String userName;
    private final String firstLanguage;
    private final String secondLanguage;
    private final Vocabulary vocabulary;

    public Profile(final String userName, final String firstLanguage, final String secondLanguage){
        this(userName, new Dictionary(), firstLanguage, secondLanguage);
    }

    public Profile(final String userName, final Vocabulary vocabulary, final String firstLanguage,
            final String secondLanguage) {
        if (userName.equals(null) || userName.isBlank()) {
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

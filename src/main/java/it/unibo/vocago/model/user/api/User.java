package it.unibo.vocago.model.user.api;

import it.unibo.vocago.model.vocabulary.api.Vocabulary;

/**
 * Represents a user profile, identified by a unique name and associated with a
 * personal vocabulary and the pair of languages involved in the study activity.
 */
public interface User {

    /**
     * @return the unique name of this user profile
     */
    String getUserName();

    /**
     * @return the personal vocabulary belonging to this user
     */
    Vocabulary getVocabulary();

    /**
     * @return the language already known by the user
     */
    String getFirstLanguage();

    /**
     * @return the language the user is studying
     */
    String getSecondLanguage();

}
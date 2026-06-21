package it.unibo.vocago.service.profile.api;

import java.util.List;
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.service.learning.api.LearningSession;

/**
 * Service that manages user profiles: creating, selecting and deleting them,
 * and persisting their vocabulary and statistics through the storage layer.
 */
public interface ProfileManager {

    /**
     * Creates a new profile.
     *
     * @param profileName    the profile name
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language to study
     */
    void createProfile(String profileName, String firstLanguage, String secondLanguage);

    /**
     * @param profileName the profile name to look up
     * @return {@code true} if a profile with that name already exists
     */
    boolean profileExists(String profileName);

    /**
     * @return the list of existing profiles
     */
    List<User> getExistingProfiles();

    /**
     * Selects the given profile as the current one.
     *
     * @param profile the profile to select
     */
    void chooseProfile(User profile);

    /**
     * @return {@code true} if a profile is currently selected
     */
    boolean hasCurrentProfile();

    /**
     * @return the currently selected profile
     */
    User getCurrentProfile();

    /**
     * @return {@code true} if the current profile's vocabulary is valid
     */
    boolean vocabularyIsValid();

    /**
     * Saves the given vocabulary for the current profile.
     *
     * @param vocabulary the vocabulary to save
     */
    void saveVocabulary(Vocabulary vocabulary);

    /**
     * Deletes the current profile.
     *
     * @return {@code true} if the profile was deleted
     */
    boolean deleteCurrentProfile();

    /**
     * @return the dashboard statistics for the current profile
     */
    Statistics getDashboardStatistics();

    /** Resets the statistics of the current profile. */
    void resetStatistics();

    /**
     * Saves the results of a finished learning session.
     *
     * @param session the session whose results are to be saved
     */
    void saveLearningStatistics(LearningSession session);

    /** Updates the study streak if it has expired since the last session. */
    void updateExpiredStreak();

    /**
     * @return the daily study goal of the current profile
     */
    int getDailyGoal();

    /**
     * Saves the configuration of a profile.
     *
     * @param profileName    the profile name
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language to study
     * @param dailyGoal      the daily study goal
     */
    void saveProfileConfigurations(String profileName, String firstLanguage,
            String secondLanguage, int dailyGoal);
}

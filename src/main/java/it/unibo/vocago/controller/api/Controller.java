package it.unibo.vocago.controller.api;

import java.util.List;

import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

/**
 * The entry point of the controller in the MVC architecture. It is the single
 * interface used by the view to drive the application, forwarding each request
 * to the appropriate coordinator.
 */
public interface Controller {

    /** Shows the start panel. */
    void showStartPanel();

    /** Shows the panel for creating a new profile. */
    void showCreateNewProfilePanel();

    /** Shows the profile dashboard panel. */
    void showProfileDashboardPanel();

    /** Shows the vocabulary editor panel. */
    void showVocabularyEditorPanel();

    /** Shows the learning panel. */
    void showLearningPanel();

    /** Shows the profile configuration panel. */
    void showConfigureProfilePanel();

    /**
     * @return the prompt of the next question in the current session
     */
    String getNextQuestion();

    /**
     * Evaluates the user's answer to the current question.
     *
     * @param userAnswer the answer typed by the user
     * @return {@code true} if the answer is correct
     */
    boolean evaluateAnswer(String userAnswer);

    /**
     * @return the correct answer to the current question
     */
    String getCorrectAnswer();

    /** Switches the translation direction of the current session. */
    void switchDirection();

    /**
     * @return {@code true} if the current question has already been evaluated
     */
    boolean currentQuestionEvaluated();

    /**
     * @return the current translation direction
     */
    Direction getDirection();

    /**
     * @return the start time of the current learning session, in milliseconds
     */
    long getLearningStartTime();

    /**
     * @return the number of correctly answered questions in the current session
     */
    int getCorrectAnsweredQuestions();

    /**
     * @return the list of existing profiles
     */
    List<User> getExistingProfiles();

    /**
     * Creates a new profile.
     *
     * @param profileName    the profile name
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language to study
     */
    void createProfile(String profileName, String firstLanguage, String secondLanguage);

    /**
     * Saves the given vocabulary for the current profile.
     *
     * @param vocabulary the vocabulary to save
     */
    void saveVocabulary(Vocabulary vocabulary);

    /** Deletes the current profile. */
    void deleteProfile();

    /** Resets the statistics of the current profile. */
    void resetStatistics();

    /**
     * @return {@code true} if the current profile's vocabulary is valid
     */
    boolean vocabularyIsValid();

    /**
     * Saves any pending changes before leaving the current screen.
     *
     * @return a status code describing the outcome of the save
     */
    int saveBeforeLeaving();

    /**
     * Selects the given profile as the current one.
     *
     * @param profile the profile to select
     */
    void chooseProfile(User profile);

    /**
     * @return the currently selected profile
     */
    User getCurrentProfile();

    /**
     * @return the statistics shown on the dashboard for the current profile
     */
    Statistics getDashboardStatistics();

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
    void saveProfileConfigurations(String profileName, String firstLanguage, String secondLanguage, int dailyGoal);

    /** Notifies that the daily goal has been achieved. */
    void dailyGoalAchieved();

    /** Closes the application. */
    void closeApp();
}

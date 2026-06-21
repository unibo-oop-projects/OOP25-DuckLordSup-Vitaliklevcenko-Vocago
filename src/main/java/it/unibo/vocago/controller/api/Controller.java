package it.unibo.vocago.controller.api;

import java.util.List;

import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface Controller {

    void showStartPanel();

    void showCreateNewProfilePanel();

    void showProfileDashboardPanel();

    void showVocabularyEditorPanel();

    void showLearningPanel();

    void showConfigureProfilePanel();

    String getNextQuestion();

    boolean evaluateAnswer(String userAnswer);

    String getCorrectAnswer();

    void switchDirection();

    boolean currentQuestionEvaluated();

    Direction getDirection();

    long getLearningStartTime();

    int getCorrectAnsweredQuestions();

    List<User> getExistingProfiles();

    void createProfile(String profileName, String firstLanguage, String secondLanguage);

    void saveVocabulary(Vocabulary vocabulary);

    void deleteProfile();

    void resetStatistics();

    boolean vocabularyIsValid();

    int saveBeforeLeaving();

    void chooseProfile(User profile);

    User getCurrentProfile();

    Statistics getDashboardStatistics();

    int getDailyGoal();

    void saveProfileConfigurations(String profileName, String firstLanguage, String secondLanguage, int dailyGoal);

    void dailyGoalAchieved();

    void closeApp();
}

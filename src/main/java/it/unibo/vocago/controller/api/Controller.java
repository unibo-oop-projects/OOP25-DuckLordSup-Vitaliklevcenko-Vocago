package it.unibo.vocago.controller.api;

import java.util.List;

import it.unibo.vocago.model.progress.api.Stats;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface Controller {

    void showStartPanel();

    void showCreateNewUserPanel();

    void showUserDashboardPanel();

    void showVocabularyEditorPanel();

    void showLearningPanel();
    
    void closeLearningSession();

    String getNextQuestion();

    boolean evaluateAnswer(String userAnswer);

    String getCorrectAnswer();

    void switchDirection();

    Direction getDirection();

    long getLearningStartTime();

    int getCurrentQuestionNumber();

    List<User> getExistingUsers();

    void createUser(String userName, String firstLanguage, String secondLanguage);

    void saveVocabulary(Vocabulary vocabulary);

    void deleteUser();

    boolean resetStats();

    boolean vocabularyIsValid();

    void chooseUser(User user);

    User getCurrentUser();

    Stats getDashboardStats();

    void saveLearningStats();
}

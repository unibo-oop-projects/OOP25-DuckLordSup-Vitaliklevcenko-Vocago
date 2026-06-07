package it.unibo.vocago.controller;

import java.io.UncheckedIOException;
import java.util.List;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.logic.learning.api.LearningSession;
import it.unibo.vocago.logic.profile.ProfileManagerImpl;
import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.storage.UserCsvStorage;
import it.unibo.vocago.view.AppFrame;

public class ControllerImpl implements Controller {

    final private AppFrame appFrame;
    private LearningSession learningSession;
    private ProfileManager profileManager;

    public ControllerImpl() {
        this.appFrame = new AppFrame(this);
        this.learningSession = null;
        this.profileManager = new ProfileManagerImpl(new UserCsvStorage());//depends on the data base we choose (sql/csv file)
        showStartPanel();
    }

            //panel space//
    public void showStartPanel() {
        if (getExistingUsers().isEmpty()) {
            this.appFrame.showCreateNewUserPanel();
        } else {
            showCreateNewUserPanel();
        }
    }

    public void showCreateNewUserPanel() {
        this.appFrame.showCreateNewUserPanel();
    }
    
        // Learning Session geters //
    public LearningSession getLearningSession() {
        if (this.learningSession == null) {
            throw new IllegalStateException("No active learning session.");
        }
        return this.learningSession;
    }

    public String getNextQuestion() {
        return getLearningSession().getNextQuestion();
    }

    public boolean evaluateAnswer(final String userAnswer) {
        return getLearningSession().evaluateAnswer(userAnswer);
    }

    public String getCorrectAnswer() {
        return getLearningSession().getCorrectAnswer();
    }

    public void switchDirection() {
        getLearningSession().switchDirection();
    }

    public Direction getDirection() {
        return getLearningSession().getDirection();
    }

    public long getLearningStartTime() {
        return getLearningSession().getTime();
    }

    public int getCurrentQuestionNumber() {
        return getLearningSession().getCorrectAnsweredQuestions();
    }

            // profile Manager geters //
    public List<User> getExistingUsers() {
        try {
            return this.profileManager.getExistingUsers();
        } catch (UncheckedIOException exception) {
            //later:this.appFrame.showMessage("Could not load saved user profiles.");
            return List.of();
        }
    }

    public boolean userExists(final String user) {
        return this.profileManager.userExists(user);
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        try {
            this.profileManager.saveVocabulary(vocabulary);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            // later: this.appFrame.showMessage("Could not save vocabulary.");
        }
    }

    public void deleteUser() {
        try { //add warning are you sure?
            if (this.profileManager.deleteCurrentUser()) {
                this.learningSession = null;
                showStartPanel();
            }
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            // later: this.appFrame.showMessage("Could not delete user.");
        }
    }

    public boolean vocabularyIsValid() {
        return this.profileManager.vocabularyIsValid();
    }

    public void chooseUser(final User user) {
        this.profileManager.chooseUser(user);
        this.learningSession = null;
        //stats a new profilePanel
    }

    public User getCurrentUser() {
        return this.profileManager.getCurrentUser();
    }

}

package it.unibo.vocago.controller;


import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.logic.learning.api.LearningSession;
import it.unibo.vocago.logic.profile.ProfileManagerImpl;
import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.progress.ProfileStats;
import it.unibo.vocago.model.progress.api.Stats;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.view.AppFrame;

public class ControllerImpl implements Controller {

    final private AppFrame appFrame;
    private LearningSession learningSession;
    private ProfileManager profileManager;

    public ControllerImpl() {
        this.appFrame = new AppFrame(this);
        this.learningSession = null;
        this.profileManager = new ProfileManagerImpl();//depends on the data base we choose (sql/csv file)
        showStartPanel();
    }

            //panel space//
    public void showStartPanel() {
        if (getExistingUsers().isEmpty()) {
            this.appFrame.showCreateNewUserPanel();
        } else {
            this.appFrame.showStartPanel();
        }
    }

    public void showCreateNewUserPanel() {
        this.appFrame.showCreateNewUserPanel();
    }

    public void showUserDashboardPanel() {
        this.appFrame.showUserDashboardPanel();
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

    // profile Manager geters and setters //
    public List<User> getExistingUsers() {
        try {
            return this.profileManager.getExistingUsers();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage("User Error", "Could not load saved user profiles.", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
    
    public void createUser(final String userName, final String firstLanguage, final String secondLanguage) {
        if (userName == null || userName.trim().isBlank()) {
            this.appFrame.showMessage(
                    "Username Invalid",
                    "Please enter a valid username.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (this.profileManager.userExists(userName)) {
                    this.appFrame.showMessage(
                        "Username Invalid",
                        "This user already exists!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.profileManager.createUser(userName, firstLanguage, secondLanguage);
            showUserDashboardPanel();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage(
                    "User Error",
                    "Could not create user profile, try again!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        try {
            this.profileManager.saveVocabulary(vocabulary);
        } catch (RuntimeException exception) {
            this.appFrame.showMessage("Save Failed", "Could not save changes, try again!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteUser() {
        final int answer = JOptionPane.showConfirmDialog(this.appFrame, "Are you sure?", "Delete Profile",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                if (this.profileManager.deleteCurrentUser()) {
                    this.learningSession = null;
                    showStartPanel();
                }
            } catch (RuntimeException exception) {
                this.appFrame.showMessage("Delete Failed", "The profile could not be deleted, try again!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean vocabularyIsValid() {
        return this.profileManager.vocabularyIsValid();
    }

    public void chooseUser(final User user) {
        this.profileManager.chooseUser(user);
        this.learningSession = null;
        showUserDashboardPanel();
    }

    public User getCurrentUser() {
        return this.profileManager.getCurrentUser();
    }

    //progress file geters//
    public Stats getDashboardStats() {
        try{
            return this.profileManager.getDashboardStats();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage("Progress Error", "Your progress has been corrupted", JOptionPane.ERROR_MESSAGE);
            return new ProfileStats(
                    0,
                    0,
                    0,
                    0,
                    0.0,
                    LocalDate.now(),
                    0,
                    0L);
        }
    }
}

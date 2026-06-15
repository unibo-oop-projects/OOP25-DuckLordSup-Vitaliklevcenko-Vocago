package it.unibo.vocago.controller;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.logic.learning.LearningSessionImpl;
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
        this.profileManager.updateExpiredStreak();
        this.appFrame.showUserDashboardPanel();
    }

    public void showVocabularyEditorPanel() {
        this.appFrame.showVocabularyEditorPanel();
    }

    public void showLearningPanel() {
        if (this.learningSession == null) {
            if (!vocabularyIsValid()) {
                this.appFrame.showMessage(
                        "No Valid Words",
                        "There are no valid words available, add more words to your vocabulary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.learningSession = new LearningSessionImpl(getCurrentUser().getVocabulary());
        }
        this.appFrame.showLearningPanel();
    }
    
    public void showconfigureProfilePanel() {
        this.appFrame.showConfigureProfilePanel();
    }
        // Learning Session getters and setters //
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

    public boolean currentQuestionEvaluated() {
        return getLearningSession().currentQuestionEvaluated();
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
    
    public void closeLearningSession() {
        if (this.learningSession != null) {
            if (this.profileManager.hasCurrentUser() && getCurrentUser().getVocabulary() != null) {
                saveVocabulary(getCurrentUser().getVocabulary());
            }
            saveLearningStats();
            this.learningSession = null;
        }
        showUserDashboardPanel();
    }

    // profile Manager getters and setters //
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
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                if (this.profileManager.deleteCurrentUser()) {
                    this.learningSession = null;
                    showStartPanel();
                }
            } catch (RuntimeException exception) {
                if (getCurrentUser() == null) {
                    this.appFrame.showMessage("Delete Failed", "The progress could not be deleted, try again!",
                            JOptionPane.ERROR_MESSAGE);
                    showStartPanel();
                } else {
                    this.appFrame.showMessage("Delete Failed", "The profile could not be deleted, try again!",
                            JOptionPane.ERROR_MESSAGE);
                }
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

    public int getDailyGoal() {
        try {
            return this.profileManager.getDailyGoal();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            return 10;
        }
    }

    public void saveProfileConfigurations(String userName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        try{
            String originalUserName = getCurrentUser().getUserName();
            userName = (userName == null || userName.trim().isBlank()) ? originalUserName : userName.trim();
            
            if (this.profileManager.userExists(userName) && !userName.equals(originalUserName)) {
                this.appFrame.showMessage(
                        "Username Invalid",
                        "This user already exists!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.profileManager.saveProfileConfigurations(userName, firstLanguage, secondLanguage, dailyGoal);
            this.appFrame.showMessage(
                    "Profile saved",
                    "Profile configuration has been saved successfully!",
                    JOptionPane.INFORMATION_MESSAGE);
            showUserDashboardPanel();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage(
                    "Profile Error",
                    "Could not change profile configuration, try again!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    //progress file getters and setters//
    public Stats getDashboardStats() {
        try {
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

    public void saveLearningStats() {
        if (this.learningSession != null) {
            try {
                this.profileManager.saveLearningStats(learningSession);
            } catch (RuntimeException exception) {
                System.err.println("Could not save progress file");
                exception.printStackTrace();
            }
        }
    }
    
    public boolean resetStats() {
        final int answer = JOptionPane.showConfirmDialog(this.appFrame, "Are you sure?", "Reset Progress",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                this.profileManager.resetStats();
                this.appFrame.showMessage("Progress Reset", "Your progress has been reset",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (RuntimeException exception) {
                this.appFrame.showMessage("Progress Error", "Failed to reset your progress, try again!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public void closeApp() {
        if (this.profileManager.hasCurrentUser() && getCurrentUser().getVocabulary() != null) {
            saveVocabulary(getCurrentUser().getVocabulary());
        }
        if (this.learningSession != null) {
            saveLearningStats();
            this.learningSession = null;
        }
        System.exit(0);
    }
}

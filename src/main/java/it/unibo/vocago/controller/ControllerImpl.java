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
        if (getExistingProfiles().isEmpty()) {
            this.appFrame.showCreateNewProfilePanel();
        } else {
            this.appFrame.showStartPanel();
        }
    }

    public void showCreateNewProfilePanel() {
        this.appFrame.showCreateNewProfilePanel();
    }

    public void showProfileDashboardPanel() {
        closeLearningSession();//if active
        this.profileManager.updateExpiredStreak();
        this.appFrame.showProfileDashboardPanel();
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
            this.learningSession = new LearningSessionImpl(getCurrentProfile().getVocabulary());
        }
        this.appFrame.showLearningPanel();
    }
    
    public void showConfigureProfilePanel() {
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
    
    private void closeLearningSession() {
        if (this.learningSession != null) {
            if (this.profileManager.hasCurrentProfile() && getCurrentProfile().getVocabulary() != null) {
                saveVocabulary(getCurrentProfile().getVocabulary());
            }
            saveLearningStats();
            this.learningSession = null;
        }
    }

    // profile Manager getters and setters //
    public List<User> getExistingProfiles() {
        try {
            return this.profileManager.getExistingProfiles();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage("Profile Error", "Could not load saved profiles.", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
    
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (profileName == null || profileName.trim().isBlank()) {
            this.appFrame.showMessage(
                    "Profile Name Invalid",
                    "Please enter a valid profile name.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (this.profileManager.profileExists(profileName)) {
                    this.appFrame.showMessage(
                        "Profile Name Invalid",
                        "This profile already exists!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.profileManager.createProfile(profileName, firstLanguage, secondLanguage);
            showProfileDashboardPanel();
        } catch (RuntimeException exception) {
            this.appFrame.showMessage(
                    "Profile Error",
                    "Could not create profile, try again!",
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

    public void deleteProfile() {
        final int answer = JOptionPane.showConfirmDialog(this.appFrame, "Are you sure you want to delete your profile?", "Delete Profile",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                if (this.profileManager.deleteCurrentProfile()) {
                    this.learningSession = null;
                    showStartPanel();
                }
            } catch (RuntimeException exception) {
                if (getCurrentProfile() == null) {
                    this.appFrame.showMessage("Delete Failed", "The progress could not be deleted, try again!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    this.appFrame.showMessage("Delete Failed", "The profile could not be deleted, try again!",
                            JOptionPane.ERROR_MESSAGE);
                }
                showStartPanel();
             }
        }
    }

    public boolean vocabularyIsValid() {
        return this.profileManager.vocabularyIsValid();
    }

    public void chooseProfile(final User profile) {
        this.profileManager.chooseProfile(profile);
        this.learningSession = null;
        showProfileDashboardPanel();
    }

    public User getCurrentProfile() {
        return this.profileManager.getCurrentProfile();
    }

    public int getDailyGoal() {
        try {
            return this.profileManager.getDailyGoal();
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            return 10;
        }
    }

    public void saveProfileConfigurations(String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        try{
            String originalProfileName = getCurrentProfile().getUserName();
            profileName = (profileName == null || profileName.trim().isBlank())
                    ? originalProfileName
                    : profileName.trim();
            
            if (this.profileManager.profileExists(profileName) && !profileName.equals(originalProfileName)) {
                this.appFrame.showMessage(
                        "Profile Name Invalid",
                        "This profile already exists!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.profileManager.saveProfileConfigurations(profileName, firstLanguage, secondLanguage, dailyGoal);
            this.appFrame.showMessage(
                    "Profile saved",
                    "Profile configuration has been saved successfully!",
                    JOptionPane.INFORMATION_MESSAGE);
            showProfileDashboardPanel();
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
    
    public void resetStats() {
        final int answer = JOptionPane.showConfirmDialog(this.appFrame, "Are you sure? your streak and study time will be reset", "Reset Progress",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                this.profileManager.resetStats();
                showProfileDashboardPanel();
                this.appFrame.showMessage("Progress Reset", "Your progress has been reset",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException exception) {
                this.appFrame.showMessage("Progress Error", "Failed to reset your progress, try again!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void dailyGoalAchieved() {
        final int answer = JOptionPane.showConfirmDialog(this.appFrame, "you did it, good job! you want to continue to study?", "Daily Goal Achieved",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.NO_OPTION) {
            showProfileDashboardPanel();
        }
    }

    public void closeApp() {
        if (this.profileManager.hasCurrentProfile() && getCurrentProfile().getVocabulary() != null) {
            saveVocabulary(getCurrentProfile().getVocabulary());
        }
        if (this.learningSession != null) {
            saveLearningStats();
            this.learningSession = null;
        }
        System.exit(0);
    }
}

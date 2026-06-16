package it.unibo.vocago.controller;

import java.time.LocalDate;
import java.util.List;
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
import it.unibo.vocago.view.api.AppView;

public class ControllerImpl implements Controller {

    private final AppView appView;
    private LearningSession learningSession;
    private final ProfileManager profileManager;

    public ControllerImpl() {
        this.appView = new AppFrame(this);
        this.learningSession = null;
        this.profileManager = new ProfileManagerImpl();//depends on the data base we choose (sql/csv file)
        showStartPanel();
    }

            //panel space//
    public void showStartPanel() {
        if (getExistingProfiles().isEmpty()) {
            view().showCreateNewProfilePanel();
        } else {
            view().showStartPanel();
        }
    }

    public void showCreateNewProfilePanel() {
        view().showCreateNewProfilePanel();
    }

    public void showProfileDashboardPanel() {
        closeLearningSession();//if active
        this.profileManager.updateExpiredStreak();
        view().showProfileDashboardPanel();
    }

    public void showVocabularyEditorPanel() {
        view().showVocabularyEditorPanel();
    }

    public void showLearningPanel() {
        if (this.learningSession == null) {
            if (!vocabularyIsValid()) {
                view().showWarning(
                        "No Valid Words",
                        "There are no valid words available, add more words to your vocabulary");
                return;
            }
            this.learningSession = new LearningSessionImpl(getCurrentProfile().getVocabulary());
        }
        view().showLearningPanel();
    }
    
    public void showConfigureProfilePanel() {
        view().showConfigureProfilePanel();
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
            view().showError("Profile Error", "Could not load saved profiles.");
            return List.of();
        }
    }
    
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (profileName == null || profileName.trim().isBlank()) {
            view().showWarning(
                    "Profile Name Invalid",
                    "Please enter a valid profile name.");
            return;
        }
        try {
            if (this.profileManager.profileExists(profileName)) {
                    view().showError(
                        "Profile Name Invalid",
                        "This profile already exists!");
                return;
            }
            this.profileManager.createProfile(profileName, firstLanguage, secondLanguage);
            showProfileDashboardPanel();
        } catch (RuntimeException exception) {
            view().showError(
                    "Profile Error",
                    "Could not create profile, try again!");
        }
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        try {
            this.profileManager.saveVocabulary(vocabulary);
        } catch (RuntimeException exception) {
            view().showError("Save Failed", "Could not save changes, try again!");
        }
    }

    public void deleteProfile() {
        if (view().askConfirmation("Delete Profile", "Are you sure you want to delete your profile?")) {
            try {
                if (this.profileManager.deleteCurrentProfile()) {
                    this.learningSession = null;
                    showStartPanel();
                }
            } catch (RuntimeException exception) {
                if (getCurrentProfile() == null) {
                    view().showError("Delete Failed", "The progress could not be deleted, try again!");
                } else {
                    view().showError("Delete Failed", "The profile could not be deleted, try again!");
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
                view().showError(
                        "Profile Name Invalid",
                        "This profile already exists!");
                return;
            }
            this.profileManager.saveProfileConfigurations(profileName, firstLanguage, secondLanguage, dailyGoal);
            view().showInfo(
                    "Profile saved",
                    "Profile configuration has been saved successfully!");
            showProfileDashboardPanel();
        } catch (RuntimeException exception) {
            view().showError(
                    "Profile Error",
                    "Could not change profile configuration, try again!");
        }
    }
    //progress file getters and setters//
    public Stats getDashboardStats() {
        try {
            return this.profileManager.getDashboardStats();
        } catch (RuntimeException exception) {
            view().showError("Progress Error", "Your progress has been corrupted");
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
        if (view().askConfirmation("Reset Progress", "Are you sure? your streak and study time will be reset")) {
            try {
                this.profileManager.resetStats();
                showProfileDashboardPanel();
                view().showInfo("Progress Reset", "Your progress has been reset");
            } catch (RuntimeException exception) {
                view().showError("Progress Error", "Failed to reset your progress, try again!");
            }
        }
    }

    public void dailyGoalAchieved() {
        if (!view().askConfirmation("Daily Goal Achieved", "You did it, good job! Do you want to continue to study?")) {
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

    private AppView view() {
        return this.appView;
    }
}

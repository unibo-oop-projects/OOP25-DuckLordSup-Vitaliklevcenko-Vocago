package it.unibo.vocago.controller;

import java.time.LocalDate;
import java.util.List;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.controller.coordinators.LearningCoordinator;
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
    private final ProfileManager profileManager;
    private final LearningCoordinator learningCoordinator;

    public ControllerImpl() {
        this.appView = new AppFrame(this);
        this.profileManager = new ProfileManagerImpl();//depends on the data base we choose (sql/csv file)
        this.learningCoordinator = new LearningCoordinator(this.profileManager, this.appView);
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
        this.learningCoordinator.closeLearningSession();
        this.profileManager.updateExpiredStreak();
        view().showProfileDashboardPanel();
    }

    public void showVocabularyEditorPanel() {
        view().showVocabularyEditorPanel();
    }

    public void showLearningPanel() {
        this.learningCoordinator.showLearningPanel();
    }
    
    public void showConfigureProfilePanel() {
        view().showConfigureProfilePanel();
    }

    public String getNextQuestion() {
        return this.learningCoordinator.getNextQuestion();
    }

    public boolean evaluateAnswer(final String userAnswer) {
        return this.learningCoordinator.evaluateAnswer(userAnswer);
    }

    public String getCorrectAnswer() {
        return this.learningCoordinator.getCorrectAnswer();
    }

    public void switchDirection() {
        this.learningCoordinator.switchDirection();
    }

    public boolean currentQuestionEvaluated() {
        return this.learningCoordinator.currentQuestionEvaluated();
    }

    public Direction getDirection() {
        return this.learningCoordinator.getDirection();
    }

    public long getLearningStartTime() {
        return this.learningCoordinator.getLearningStartTime();
    }

    public int getCurrentQuestionNumber() {
        return this.learningCoordinator.getCurrentQuestionNumber();
    }

    public void saveLearningStats() {
        this.learningCoordinator.saveLearningStats();
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
                    this.learningCoordinator.resetSession();
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
        this.learningCoordinator.resetSession();
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

        saveLearningStats();
        this.learningCoordinator.resetSession();
        System.exit(0);
    }

    private AppView view() {
        return this.appView;
    }
}

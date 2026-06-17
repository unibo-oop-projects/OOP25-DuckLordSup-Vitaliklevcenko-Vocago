package it.unibo.vocago.controller;

import java.time.LocalDate;
import java.util.List;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.controller.coordinators.LearningCoordinator;
import it.unibo.vocago.controller.coordinators.ProfileCoordinator;
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
    private final ProfileCoordinator profileCoordinator;

    public ControllerImpl() {
        this.appView = new AppFrame(this);
        this.profileManager = new ProfileManagerImpl();//depends on the data base we choose (sql/csv file)
        this.learningCoordinator = new LearningCoordinator(this.profileManager, this.appView);
        this.profileCoordinator = new ProfileCoordinator(this.profileManager, this.appView);
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
        this.profileCoordinator.updateExpiredStreak();
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
        return this.profileCoordinator.getExistingProfiles();
    }
    
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (this.profileCoordinator.createProfile(profileName, firstLanguage, secondLanguage)) {
            showProfileDashboardPanel();
        }
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        this.profileCoordinator.saveVocabulary(vocabulary);
    }

    public void deleteProfile() {
        if (this.profileCoordinator.deleteProfile()) {
            this.learningCoordinator.stopLearningSession();
            showStartPanel();
        }
    }

    public boolean vocabularyIsValid() {
        return this.profileCoordinator.vocabularyIsValid();
    }

    public void chooseProfile(final User profile) {
        this.learningCoordinator.closeLearningSession();
        this.profileCoordinator.chooseProfile(profile);
        showProfileDashboardPanel();
    }

    public User getCurrentProfile() {
        return this.profileCoordinator.getCurrentProfile();
    }

    public int getDailyGoal() {
        return this.profileCoordinator.getDailyGoal();
    }

    public void saveProfileConfigurations(String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        if (this.profileCoordinator.saveProfileConfigurations(profileName, firstLanguage, secondLanguage, dailyGoal)) {
            showProfileDashboardPanel();
        }
    }

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
        if (this.profileCoordinator.hasCurrentProfile() && getCurrentProfile().getVocabulary() != null) {
            saveVocabulary(getCurrentProfile().getVocabulary());
        }
        saveLearningStats();
        this.learningCoordinator.stopLearningSession();
        System.exit(0);
    }

    private AppView view() {
        return this.appView;
    }
}
package it.unibo.vocago.controller;

import java.util.List;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.controller.coordinators.LearningCoordinator;
import it.unibo.vocago.controller.coordinators.ProfileCoordinator;
import it.unibo.vocago.controller.coordinators.StatsCoordinator;
import it.unibo.vocago.controller.coordinators.VocabularyCoordinator;
import it.unibo.vocago.logic.profile.ProfileManagerImpl;
import it.unibo.vocago.logic.profile.api.ProfileManager;
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
    private final StatsCoordinator statsCoordinator;
    private final VocabularyCoordinator vocabularyCoordinator;

    public ControllerImpl() {
        this.appView = new AppFrame(this);
        this.profileManager = new ProfileManagerImpl();//depends on the data base we choose (sql/csv file)
        this.learningCoordinator = new LearningCoordinator(this.profileManager, this.appView);
        this.profileCoordinator = new ProfileCoordinator(this.profileManager, this.appView);
        this.statsCoordinator = new StatsCoordinator(this.profileManager, this.appView);
        this.vocabularyCoordinator = new VocabularyCoordinator(this.profileManager, this.appView);
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
        this.vocabularyCoordinator.saveVocabulary(vocabulary);
    }

    public boolean vocabularyIsValid() {
        return this.vocabularyCoordinator.vocabularyIsValid();
    }

    public void deleteProfile() {
        if (this.profileCoordinator.deleteProfile()) {
            this.learningCoordinator.stopLearningSession();
            showStartPanel();
        }
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
        return this.statsCoordinator.getDashboardStats();
    }
    
    public void resetStats() {
        if (this.statsCoordinator.resetStats()) {
            showProfileDashboardPanel();
        }
    }

    public void dailyGoalAchieved() {
        if (!this.learningCoordinator.continueAfterDailyGoalIfReached()) {
            showProfileDashboardPanel();
        }
    }
    
    public void closeApp() {
        this.learningCoordinator.closeLearningSession();
        if (this.profileCoordinator.hasCurrentProfile() && getCurrentProfile().getVocabulary() != null) {
            saveVocabulary(getCurrentProfile().getVocabulary());
        }
        System.exit(0);
    }

    private AppView view() {
        return this.appView;
    }
}

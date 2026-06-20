package it.unibo.vocago.controller;

import java.util.List;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.controller.coordinators.LearningCoordinator;
import it.unibo.vocago.controller.coordinators.ProfileCoordinator;
import it.unibo.vocago.controller.coordinators.StatisticsCoordinator;
import it.unibo.vocago.controller.coordinators.VocabularyCoordinator;
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.service.profile.ProfileManagerImpl;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.AppFrame;
import it.unibo.vocago.view.api.AppView;

public class ControllerImpl implements Controller {

    private final AppView appView;
    private final ProfileManager profileManager;
    private final LearningCoordinator learningCoordinator;
    private final ProfileCoordinator profileCoordinator;
    private final StatisticsCoordinator statisticsCoordinator;
    private final VocabularyCoordinator vocabularyCoordinator;

    public ControllerImpl() {
        this.appView = new AppFrame(this);
        this.profileManager = new ProfileManagerImpl();
        this.learningCoordinator = new LearningCoordinator(this.profileManager, this.appView);
        this.profileCoordinator = new ProfileCoordinator(this.profileManager, this.appView);
        this.statisticsCoordinator = new StatisticsCoordinator(this.profileManager, this.appView);
        this.vocabularyCoordinator = new VocabularyCoordinator(this.profileManager, this.appView);
        init();
    }

    private void init() {
        if (this.profileCoordinator.getExistingProfiles().isEmpty()) {
            view().showCreateNewProfilePanel();
        } else {
            view().showStartPanel();
        }
    }
    
    @Override
    public void showStartPanel() {
        init();
    }
    
    @Override
    public void showCreateNewProfilePanel() {
        view().showCreateNewProfilePanel();
    }

    @Override
    public void showProfileDashboardPanel() {
        this.learningCoordinator.closeLearningSession();
        this.profileCoordinator.updateExpiredStreak();
        view().showProfileDashboardPanel();
    }

    @Override
    public void showVocabularyEditorPanel() {
        view().showVocabularyEditorPanel();
    }

    @Override
    public void showLearningPanel() {
        this.learningCoordinator.showLearningPanel();
    }
    
    @Override
    public void showConfigureProfilePanel() {
        view().showConfigureProfilePanel();
    }

    @Override
    public String getNextQuestion() {
        return this.learningCoordinator.getNextQuestion();
    }

    @Override
    public boolean evaluateAnswer(final String userAnswer) {
        return this.learningCoordinator.evaluateAnswer(userAnswer);
    }

    @Override
    public String getCorrectAnswer() {
        return this.learningCoordinator.getCorrectAnswer();
    }

    @Override
    public void switchDirection() {
        this.learningCoordinator.switchDirection();
    }

    @Override
    public boolean currentQuestionEvaluated() {
        return this.learningCoordinator.currentQuestionEvaluated();
    }

    @Override
    public Direction getDirection() {
        return this.learningCoordinator.getDirection();
    }

    @Override
    public long getLearningStartTime() {
        return this.learningCoordinator.getLearningStartTime();
    }

    @Override
    public int getCorrectAnsweredQuestions() {
        return this.learningCoordinator.getCorrectAnsweredQuestions();
    }

    // profile Manager getters and setters //
    @Override
    public List<User> getExistingProfiles() {
        return this.profileCoordinator.getExistingProfiles();
    }
    
    @Override
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (this.profileCoordinator.createProfile(profileName, firstLanguage, secondLanguage)) {
            showProfileDashboardPanel();
        }
    }

    @Override
    public void saveVocabulary(final Vocabulary vocabulary) {
        this.vocabularyCoordinator.saveVocabulary(vocabulary);
    }

    @Override
    public boolean vocabularyIsValid() {
        return this.vocabularyCoordinator.vocabularyIsValid();
    }

    @Override
    public int saveBeforeLeaving() {
        return this.vocabularyCoordinator.saveBeforeLeaving();
    }

    @Override
    public void deleteProfile() {
        if (this.profileCoordinator.deleteProfile()) {
            this.learningCoordinator.stopLearningSession();
            showStartPanel();
        }
    }

    @Override
    public void chooseProfile(final User profile) {
        this.learningCoordinator.closeLearningSession();
        this.profileCoordinator.chooseProfile(profile);
        showProfileDashboardPanel();
    }

    @Override
    public User getCurrentProfile() {
        return this.profileManager.getCurrentProfile();
    }

    @Override
    public int getDailyGoal() {
        return this.profileCoordinator.getDailyGoal();
    }

    @Override
    public void saveProfileConfigurations(String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        if (this.profileCoordinator.saveProfileConfigurations(profileName, firstLanguage, secondLanguage, dailyGoal)) {
            showProfileDashboardPanel();
        }
    }

    @Override
    public Statistics getDashboardStatistics() {
        return this.statisticsCoordinator.getDashboardStatistics();
    }
    
    @Override
    public void resetStatistics() {
        if (this.statisticsCoordinator.resetStatistics()) {
            showProfileDashboardPanel();
        }
    }

    @Override
    public void dailyGoalAchieved() {
        if (!this.learningCoordinator.continueAfterDailyGoalIfReached()) {
            showProfileDashboardPanel();
        }
    }
    
    @Override
    @SuppressFBWarnings(value = "DM_EXIT", justification = "Closing the desktop app intentionally exits the JVM.")
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

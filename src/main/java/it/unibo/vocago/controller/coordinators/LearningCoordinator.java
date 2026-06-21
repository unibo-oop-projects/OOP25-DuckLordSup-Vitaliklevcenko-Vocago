package it.unibo.vocago.controller.coordinators;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.service.learning.LearningSessionImpl;
import it.unibo.vocago.service.learning.api.LearningSession;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

public final class LearningCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;
    private LearningSession learningSession;
    private boolean dailyGoalNotified;

    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public LearningCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
        this.learningSession = null;
        this.dailyGoalNotified = false;
    }

    public void showLearningPanel() {
        if (this.learningSession == null) {
            if (!this.profileManager.vocabularyIsValid()) {
                this.appView.showWarning(
                        "No Valid Words",
                        "There are no valid words available, add more words to your vocabulary");
                return;
            }
            this.learningSession = new LearningSessionImpl(this.profileManager.getCurrentProfile().getVocabulary());
            this.dailyGoalNotified = false;
        }
        this.appView.showLearningPanel();
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
        return getLearningSession().getStartTime();
    }

    public int getCorrectAnsweredQuestions() {
        return getLearningSession().getCorrectAnsweredQuestions();
    }

    public void stopLearningSession() {
        this.learningSession = null;
        this.dailyGoalNotified = false;
    }

    public void closeLearningSession() {
        if (this.learningSession != null) {
            if (this.profileManager.hasCurrentProfile()
                    && this.profileManager.getCurrentProfile().getVocabulary() != null) {
                this.profileManager.saveVocabulary(this.profileManager.getCurrentProfile().getVocabulary());
            }
            saveLearningStatistics();
            stopLearningSession();
        }
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void saveLearningStatistics() {
        if (this.learningSession != null) {
            // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
            // CHECKSTYLE: IllegalCatch OFF
            try {
                this.profileManager.saveLearningStatistics(learningSession);
            } catch (RuntimeException exception) {
                this.appView.showWarning(
                        "Save statistics failed",
                        "Could not save statistics file");
            }
            // CHECKSTYLE: IllegalCatch ON
        }
    }

    public boolean continueAfterDailyGoalIfReached() {
        if (this.learningSession == null || this.dailyGoalNotified
                || this.learningSession.getCorrectAnsweredQuestions() < this.profileManager.getDailyGoal()) {
            return true;
        }
        this.dailyGoalNotified = true;
        return this.appView.askConfirmation(
                "Daily Goal Achieved",
                "You did it, good job! Do you want to continue to study?");
    }

    private LearningSession getLearningSession() {
        if (this.learningSession == null) {
            throw new IllegalStateException("No active learning session.");
        }
        return this.learningSession;
    }
}

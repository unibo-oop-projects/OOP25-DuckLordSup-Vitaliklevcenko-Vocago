package it.unibo.vocago.controller.coordinators;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.service.learning.LearningSessionImpl;
import it.unibo.vocago.service.learning.api.LearningSession;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

/**
 * Coordinates the learning flow on behalf of the controller. Owns the current
 * {@link LearningSession}, creating it when a session starts and clearing it
 * when it ends, and forwards each learning operation to that session.
 */
public final class LearningCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;
    private LearningSession learningSession;
    private boolean dailyGoalNotified;

    /**
     * Creates a learning coordinator with no active session.
     *
     * @param profileManager the profile manager providing the current vocabulary
     * @param appView        the view used to report feedback to the user
     */
    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public LearningCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
        this.learningSession = null;
        this.dailyGoalNotified = false;
    }

    /**
     * Starts a new learning session if none is active and the vocabulary is
     * valid, then shows the learning panel.
     */
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

    /**
     * @return the prompt of the next question in the current session
     */
    public String getNextQuestion() {
        return getLearningSession().getNextQuestion();
    }

    /**
     * Evaluates the user's answer to the current question.
     *
     * @param userAnswer the answer typed by the user
     * @return {@code true} if the answer is correct
     */
    public boolean evaluateAnswer(final String userAnswer) {
        return getLearningSession().evaluateAnswer(userAnswer);
    }

    /**
     * @return the correct answer to the current question
     */
    public String getCorrectAnswer() {
        return getLearningSession().getCorrectAnswer();
    }

    /**
     * Switches the translation direction of the current session.
     */
    public void switchDirection() {
        getLearningSession().switchDirection();
    }

    /**
     * @return {@code true} if the current question has already been evaluated
     */
    public boolean currentQuestionEvaluated() {
        return getLearningSession().currentQuestionEvaluated();
    }

    /**
     * @return the current translation direction
     */
    public Direction getDirection() {
        return getLearningSession().getDirection();
    }

    /**
     * @return the start time of the current session, in milliseconds
     */
    public long getLearningStartTime() {
        return getLearningSession().getStartTime();
    }

    /**
     * @return the number of correctly answered questions in the current session
     */
    public int getCorrectAnsweredQuestions() {
        return getLearningSession().getCorrectAnsweredQuestions();
    }

    /**
     * Discards the current session without saving.
     */
    public void stopLearningSession() {
        this.learningSession = null;
        this.dailyGoalNotified = false;
    }

    /**
     * Saves the vocabulary and statistics of the current session, then discards
     * it. Does nothing if no session is active.
     */
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

    /**
     * Saves the statistics of the current session, showing a warning if the
     * save fails. Does nothing if no session is active.
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void saveLearningStatistics() {
        if (this.learningSession != null) {
            // Final UI boundary: IllegalCatch - intentional catch convert unexpected
            // failures into user feedback.
            // CHECKSTYLE: IllegalCatch OFF
            try {
                this.profileManager.saveLearningStatistics(learningSession);
            } catch (final RuntimeException exception) {
                this.appView.showWarning(
                        "Save statistics failed",
                        "Could not save statistics file");
            }
            // CHECKSTYLE: IllegalCatch ON
        }
    }

    /**
     * Checks whether the daily goal has just been reached and, if so, asks the
     * user whether to keep studying.
     *
     * @return {@code true} if studying should continue
     */
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
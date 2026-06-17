package it.unibo.vocago.controller.coordinators;

import it.unibo.vocago.logic.learning.LearningSessionImpl;
import it.unibo.vocago.logic.learning.api.LearningSession;
import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.view.api.AppView;

public final class LearningCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;
    private LearningSession learningSession;

    public LearningCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
        this.learningSession = null;
    }

    public void showLearningPanel() {
        if (this.learningSession == null) {
            if (!this.profileManager.vocabularyIsValid()) {
                this.appView.showWarning(
                        "No Valid Words",
                        "There are no valid words available, add more words to your vocabulary");
                return;
            }
            this.learningSession = new LearningSessionImpl(
                    this.profileManager.getCurrentProfile().getVocabulary());
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
        return getLearningSession().getTime();
    }

    public int getCurrentQuestionNumber() {
        return getLearningSession().getCorrectAnsweredQuestions();
    }

    public void stopLearningSession() {
        this.learningSession = null;
    }

    public void closeLearningSession() {
        if (this.learningSession != null) {
            if (this.profileManager.hasCurrentProfile()
                    && this.profileManager.getCurrentProfile().getVocabulary() != null) {
                this.profileManager.saveVocabulary(
                        this.profileManager.getCurrentProfile().getVocabulary());
            }
            saveLearningStats();
            stopLearningSession();
        }
    }

    public void saveLearningStats() {
        if (this.learningSession != null) {
            try {
                this.profileManager.saveLearningStats(this.learningSession);
            } catch (RuntimeException exception) {
                System.err.println("Could not save progress file");
                exception.printStackTrace();
            }
        }
    }

    private LearningSession getLearningSession() {
        if (this.learningSession == null) {
            throw new IllegalStateException("No active learning session.");
        }
        return this.learningSession;
    }
}

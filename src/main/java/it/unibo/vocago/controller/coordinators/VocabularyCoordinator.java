package it.unibo.vocago.controller.coordinators;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

/**
 * Coordinates vocabulary related operations on behalf of the controller,
 * delegating persistence to the {@link ProfileManager} and reporting failures
 * to the user through the {@link AppView}.
 */
public final class VocabularyCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    /**
     * Creates a vocabulary coordinator.
     *
     * @param profileManager the profile manager used to save the vocabulary
     * @param appView        the view used to report feedback to the user
     */
    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public VocabularyCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    /**
     * Saves the given vocabulary, showing an error to the user if it fails.
     *
     * @param vocabulary the vocabulary to save
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void saveVocabulary(final Vocabulary vocabulary) {
        // Final UI boundary: convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            this.profileManager.saveVocabulary(vocabulary);
        } catch (final RuntimeException exception) {
            this.appView.showError("Save Failed", "Could not save changes, try again!");
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * @return {@code true} if the current profile's vocabulary is valid
     */
    public boolean vocabularyIsValid() {
        return this.profileManager.vocabularyIsValid();
    }

    /**
     * Asks the user whether to save changes before leaving the editor.
     *
     * @return the user's confirmation choice
     */
    public int saveBeforeLeaving() {
        return this.appView.askConfirmationWithCancel("Before Exit", "Save changes?");
    }
}
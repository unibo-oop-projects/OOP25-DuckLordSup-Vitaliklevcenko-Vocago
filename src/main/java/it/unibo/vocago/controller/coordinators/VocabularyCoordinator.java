package it.unibo.vocago.controller.coordinators;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

public final class VocabularyCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public VocabularyCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        try {
            this.profileManager.saveVocabulary(vocabulary);
        } catch (RuntimeException exception) {
            this.appView.showError("Save Failed", "Could not save changes, try again!");
        }
    }

    public boolean vocabularyIsValid() {
        return this.profileManager.vocabularyIsValid();
    }

    public int saveBeforeLeaving() {
        return this.appView.askConfirmationWithCancel("Before Exit", "Save changes?");
    }
}

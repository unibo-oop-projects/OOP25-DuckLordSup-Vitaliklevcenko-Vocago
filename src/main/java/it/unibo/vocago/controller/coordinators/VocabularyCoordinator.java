package it.unibo.vocago.controller.coordinators;

import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.view.api.AppView;

public final class VocabularyCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

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

    
}
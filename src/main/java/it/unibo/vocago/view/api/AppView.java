package it.unibo.vocago.view.api;

public interface AppView {
    void showStartPanel();

    void showCreateNewProfilePanel();

    void showProfileDashboardPanel();

    void showVocabularyEditorPanel();

    void showLearningPanel();

    void showConfigureProfilePanel();

    void showInfo(String title, String message);

    void showWarning(String title, String message);

    void showError(String title, String message);

    boolean askConfirmation(String title, String message);
}

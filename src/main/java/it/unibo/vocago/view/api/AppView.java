package it.unibo.vocago.view.api;

/**
 * The view in the MVC architecture. Defines the operations the controller uses
 * to display the application's panels and to show feedback dialogs to the user,
 * keeping the controller independent of the specific graphical toolkit.
 */
public interface AppView {

    /** Shows the start panel. */
    void showStartPanel();

    /** Shows the panel for creating a new profile. */
    void showCreateNewProfilePanel();

    /** Shows the profile dashboard panel. */
    void showProfileDashboardPanel();

    /** Shows the vocabulary editor panel. */
    void showVocabularyEditorPanel();

    /** Shows the learning panel. */
    void showLearningPanel();

    /** Shows the profile configuration panel. */
    void showConfigureProfilePanel();

    /**
     * Shows an informational dialog.
     *
     * @param title   the dialog title
     * @param message the message to display
     */
    void showInfo(String title, String message);

    /**
     * Shows a warning dialog.
     *
     * @param title   the dialog title
     * @param message the message to display
     */
    void showWarning(String title, String message);

    /**
     * Shows an error dialog.
     *
     * @param title   the dialog title
     * @param message the message to display
     */
    void showError(String title, String message);

    /**
     * Shows a yes/no confirmation dialog.
     *
     * @param title   the dialog title
     * @param message the message to display
     * @return {@code true} if the user confirmed
     */
    boolean askConfirmation(String title, String message);

    /**
     * Shows a confirmation dialog with yes, no and cancel options.
     *
     * @param title   the dialog title
     * @param message the message to display
     * @return the option chosen by the user
     */
    int askConfirmationWithCancel(String title, String message);
}

package it.unibo.vocago.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.api.AppView;
import it.unibo.vocago.view.panels.ConfigureProfilePanel;
import it.unibo.vocago.view.panels.CreateNewProfilePanel;
import it.unibo.vocago.view.panels.LearningPanel;
import it.unibo.vocago.view.panels.ProfileDashboardPanel;
import it.unibo.vocago.view.panels.StartPanel;
import it.unibo.vocago.view.panels.VocabularyEditorPanel;
import it.unibo.vocago.view.util.UIFactory;

public class AppFrame extends JFrame implements AppView {

    private static final Dimension SMALL_WINDOW = new Dimension(800, 600);
    private static final Dimension DASHBOARD_WINDOW = new Dimension(1280, 720);

    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Controller controller;

    private StartPanel startPanel;
    private ProfileDashboardPanel profileDashboardPanel;
    private LearningPanel learningPanel;
    private VocabularyEditorPanel vocabularyEditorPanel;
    private CreateNewProfilePanel createNewProfilePanel;
    private ConfigureProfilePanel configureProfilePanel;

    public AppFrame(final Controller controller) {

        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.mainPanel = UIFactory.createPanel(this.cardLayout);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Vocago");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        ImageIcon icon = UIFactory.loadIcon("data/resources/pictures/wizard.png");
        this.setIconImage(icon.getImage());
        this.startPanel = null;
        this.profileDashboardPanel = null;
        this.learningPanel = null;
        this.createNewProfilePanel = null;
        this.vocabularyEditorPanel = null;
        this.configureProfilePanel = null;
        add(this.mainPanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.closeApp();
            }
        });
        
        this.setVisible(true);
    }

    @Override
    public void showStartPanel() {
        resizeWindow(SMALL_WINDOW);
        this.startPanel = replacePanel(
                this.startPanel,
                new StartPanel(this.controller),
                "StartPanel");
    }

    @Override
    public void showCreateNewProfilePanel() {
        resizeWindow(SMALL_WINDOW);
        this.createNewProfilePanel = replacePanel(
                this.createNewProfilePanel,
                new CreateNewProfilePanel(this.controller),
                "CreateNewProfilePanel");
    }

    @Override
    public void showProfileDashboardPanel() {
        resizeWindow(DASHBOARD_WINDOW);
        this.profileDashboardPanel = replacePanel(
                this.profileDashboardPanel,
                new ProfileDashboardPanel(this.controller),
                "ProfileDashboardPanel");
    }

    @Override
    public void showVocabularyEditorPanel() {
        this.vocabularyEditorPanel = replacePanel(
                this.vocabularyEditorPanel,
                new VocabularyEditorPanel(this.controller),
                "VocabularyEditorPanel");
    }

    @Override
    public void showLearningPanel() {
        this.learningPanel = replacePanel(
                this.learningPanel,
                new LearningPanel(this.controller),
                "LearningPanel");
    }

    @Override
    public void showConfigureProfilePanel() {
        resizeWindow(SMALL_WINDOW);
        this.configureProfilePanel = replacePanel(
                this.configureProfilePanel,
                new ConfigureProfilePanel(this.controller),
                "ConfigureProfilePanel");
    }

    @Override
    public void showInfo(final String title, final String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showWarning(final String title, final String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showError(final String title, final String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean askConfirmation(final String title, final String message) {
        return JOptionPane.showConfirmDialog(
                this,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    @Override
    public int askConfirmationWithCancel(final String title, final String message) {
        return JOptionPane.showConfirmDialog(
                this,
                message,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
    }

    private void resizeWindow(final Dimension size) {
        this.setSize(size);
        this.setLocationRelativeTo(null);
    }

    private <T extends JPanel> T replacePanel(final T oldPanel, final T newPanel, final String cardName) {
        if (oldPanel != null) {
            this.mainPanel.remove(oldPanel);
        }
        showPanel(newPanel, cardName);
        return newPanel;
    }

    private void showPanel(final JPanel panel, final String cardName) {
        this.mainPanel.add(panel, cardName);
        this.cardLayout.show(this.mainPanel, cardName);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }
}

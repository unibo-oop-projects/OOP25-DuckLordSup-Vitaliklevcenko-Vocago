package it.unibo.vocago.view;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.ConfigureProfilePanel;
import it.unibo.vocago.view.panels.CreateNewUserPanel;
import it.unibo.vocago.view.panels.LearningPanel;
import it.unibo.vocago.view.panels.StartPanel;
import it.unibo.vocago.view.panels.UserDashboardPanel;
import it.unibo.vocago.view.panels.VocabularyEditorPanel;
import it.unibo.vocago.view.util.UIFactory;

public class AppFrame extends JFrame {

    private static final Dimension SMALL_WINDOW = new Dimension(800, 600);
    private static final Dimension DASHBOARD_WINDOW = new Dimension(1280, 720);

    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Controller controller;

    private StartPanel startPanel;
    private UserDashboardPanel userDashboardPanel;
    private LearningPanel learningPanel;
    private VocabularyEditorPanel vocabularyEditorPanel;
    private CreateNewUserPanel createNewUserPanel;
    private ConfigureProfilePanel configureProfilePanel;

    public AppFrame(final Controller controller) {

        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.mainPanel = UIFactory.createPanel(this.cardLayout);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //SHOULD ADD CONTROLLER CLOSEDAPP LISTENER
        
        this.setTitle("Vocago");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        ImageIcon icon = UIFactory.loadIcon("data/pictures/wizard.png");
        this.setIconImage(icon.getImage());

        // nulling the panels
        this.startPanel = null;
        this.userDashboardPanel = null;
        this.learningPanel = null;
        this.createNewUserPanel = null;
        this.vocabularyEditorPanel = null;
        this.configureProfilePanel = null;

        add(this.mainPanel);

        this.setVisible(true);
    }

    public void showStartPanel() {
        resizeWindow(SMALL_WINDOW);
        this.startPanel = replacePanel(
                this.startPanel,
                new StartPanel(this.controller),
                "StartPanel");
    }

    public void showCreateNewUserPanel() {
        resizeWindow(SMALL_WINDOW);
        this.createNewUserPanel = replacePanel(
                this.createNewUserPanel,
                new CreateNewUserPanel(this.controller),
                "CreateNewUserPanel");
    }

    public void showUserDashboardPanel() {
        resizeWindow(DASHBOARD_WINDOW);
        this.userDashboardPanel = replacePanel(
                this.userDashboardPanel,
                new UserDashboardPanel(this.controller),
                "UserDashboardPanel");
    }

    public void showVocabularyEditorPanel() {
        this.vocabularyEditorPanel = replacePanel(
                this.vocabularyEditorPanel,
                new VocabularyEditorPanel(this.controller),
                "VocabularyEditorPanel");
    }

    public void showLearningPanel() {
        this.learningPanel = replacePanel(
                this.learningPanel,
                new LearningPanel(this.controller),
                "LearningPanel");
    }

    public void showConfigureProfilePanel() {
        resizeWindow(SMALL_WINDOW);
        this.configureProfilePanel = replacePanel(
                this.configureProfilePanel,
                new ConfigureProfilePanel(this.controller),
                "ConfigureProfilePanel");
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

    public void showMessage(final String title, final String message, final int option) {
        JOptionPane.showMessageDialog(this, message, title, option);
    }

}

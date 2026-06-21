package it.unibo.vocago.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

/**
 * Panel for creating a new profile, collecting the profile name and the two
 * languages.
 */
public final class CreateNewProfilePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TITLE_VERTICAL_GAP = 40;
    private static final int USERNAME_HORIZONTAL_GAP = 12;
    private static final int USERNAME_VERTICAL_GAP = 40;
    private static final int COMBOBOX_HEIGHT = 32;
    private static final int COMBOBOX_WIDTH = 220;
    private static final int ZERO_GAP = 0;
    private final transient Controller controller;
    private final JButton createNewProfileButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;

    /**
     * Creates the panel, wiring it to the given controller.
     *
     * @param controller the controller user actions are forwarded to
     */
    @SuppressFBWarnings(value = "EI2", justification = "The panel intentionally shares the app controller.")
    public CreateNewProfilePanel(final Controller controller) {

        this.controller = controller;
        UIFactory.stylePanel(this);
        this.createNewProfileButton = UIFactory.createButton("Create");
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png",
                UIConstants.BACK_BUTTON_ICON_SIZE, UIConstants.BACKGROUND,
                UIConstants.BACK_BUTTON_HEIGHT, UIConstants.BACK_BUTTON_WIDTH,
                true, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
        this.firstLanguageComboBox = UIFactory.createComboBox(new String[] {
                "English", "Italian", "German", "French", "Spanish",
                "Portuguese", "Dutch", "Polish", "Japanese", "Chinese",
        });

        this.secondLanguageComboBox = UIFactory.createComboBox(new String[] {
                "Italian", "English", "German", "French", "Spanish",
                "Portuguese", "Dutch", "Polish", "Japanese", "Chinese",
        });

        buildLayout();
        this.usernameTextField.addActionListener(e -> buttonActionRegister());
        this.createNewProfileButton.addActionListener(e -> buttonActionRegister());
        this.goBackButton.addActionListener(e -> this.controller.showStartPanel());
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        final JLabel titleLabel = UIFactory.createLabel("Create New Profile", UIConstants.TITLE_FONT);
        final JPanel titlePanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER,
                ZERO_GAP, TITLE_VERTICAL_GAP));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        final JPanel contentPanel = UIFactory.createPanel();
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));

        final Dimension comboSize = new Dimension(COMBOBOX_WIDTH, COMBOBOX_HEIGHT);
        this.firstLanguageComboBox.setMaximumSize(comboSize);
        this.secondLanguageComboBox.setMaximumSize(comboSize);

        final JPanel languagePanel = UIFactory.createPanel();
        UIFactory.brighter(languagePanel);

        languagePanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));
        languagePanel.add(UIFactory.createLabel("choose a language you want to study", UIConstants.FONT));
        languagePanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        languagePanel.add(this.secondLanguageComboBox);
        languagePanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));
        languagePanel.add(UIFactory.createLabel("choose a language you already know", UIConstants.FONT));
        languagePanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        languagePanel.add(this.firstLanguageComboBox);
        languagePanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));

        contentPanel.add(languagePanel);
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));

        final JPanel usernamePanel = UIFactory.createPanel(new FlowLayout(
                FlowLayout.CENTER, USERNAME_HORIZONTAL_GAP, USERNAME_VERTICAL_GAP));
        usernamePanel.add(UIFactory.createLabel("nickname:", UIConstants.FONT));
        usernamePanel.add(this.usernameTextField);
        usernamePanel.add(this.createNewProfileButton);

        contentPanel.add(usernamePanel);
        add(contentPanel, BorderLayout.CENTER);

        if (!this.controller.getExistingProfiles().isEmpty()) {
            final JPanel bottomPanel = UIFactory.createPanel(new FlowLayout(
                    FlowLayout.LEFT, UIConstants.SPACING_LARGE, UIConstants.SPACING_LARGE));
            bottomPanel.add(this.goBackButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }

    private void buttonActionRegister() {
        this.controller.createProfile(
                this.usernameTextField.getText(),
                (String) this.firstLanguageComboBox.getSelectedItem(),
                (String) this.secondLanguageComboBox.getSelectedItem());
    }

}

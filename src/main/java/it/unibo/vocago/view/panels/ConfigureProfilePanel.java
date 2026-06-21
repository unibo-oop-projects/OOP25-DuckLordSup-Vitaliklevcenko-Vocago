package it.unibo.vocago.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

/**
 * Panel for configuring the current profile, such as renaming it, changing the
 * languages and setting the daily study goal.
 */
public final class ConfigureProfilePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final String[] LANGUAGES = {
            "English", "Italian", "German", "French", "Spanish",
            "Portuguese", "Dutch", "Polish", "Japanese", "Chinese",
    };
    private static final int PANEL_WIDTH = 660;
    private static final int ACTION_BUTTON_HEIGHT = 42;
    private static final int SAVE_BUTTON_WIDTH = 450;
    private static final int DELETE_ICON_SIZE = 20;
    private static final int DELETE_BUTTON_WIDTH = 200;
    private static final int HEADER_HEIGHT = 90;
    private static final int ACCOUNT_PANEL_HEIGHT = 100;
    private static final int LANGUAGE_PANEL_HEIGHT = 130;
    private static final int DAILY_GOAL_PANEL_HEIGHT = 135;
    private static final int CONTENT_BOTTOM_PADDING = 18;
    private static final int ACTION_SECTION_SPACING = 12;
    private static final int ACCOUNT_ROW_HEIGHT = 30;
    private static final int ACCOUNT_ROW_VERTICAL_GAP = 5;
    private static final int LANGUAGE_COLUMN_GAP = 24;
    private static final int ZERO_PADDING = 0;
    private static final int GOAL_SLIDER_TOP_GAP = 8;
    private static final int SLIDER_TICK_SPACING = 5;
    private static final int ICON_SIZE = 1;
    private static final int GRID_SIZE = 2;
    private final transient Controller controller;
    private final JButton saveChangesButton;
    private final JButton deleteProfileButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;
    private final JSlider dailyGoalSlider;
    private final JLabel dailyGoalValueLabel;

    /**
     * Creates the panel, wiring it to the given controller.
     *
     * @param controller the controller user actions are forwarded to
     */
    @SuppressFBWarnings(value = "EI2", justification = "The panel intentionally shares the app controller.")
    public ConfigureProfilePanel(final Controller controller) {
        this.controller = controller;
        UIFactory.stylePanel(this);
        this.saveChangesButton = UIFactory.createButton("Save Changes", "",
                ICON_SIZE,
                UIConstants.BLUE, ACTION_BUTTON_HEIGHT, SAVE_BUTTON_WIDTH,
                true, false, true, UIConstants.FONT);
        this.deleteProfileButton = UIFactory.createButton("Delete Profile", "data/resources/pictures/bin.png",
                DELETE_ICON_SIZE, UIConstants.RED, ACTION_BUTTON_HEIGHT, DELETE_BUTTON_WIDTH,
                true, true, true, UIConstants.FONT);
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png",
                UIConstants.BACK_BUTTON_ICON_SIZE, UIConstants.BACKGROUND,
                UIConstants.BACK_BUTTON_HEIGHT,
                UIConstants.BACK_BUTTON_WIDTH,
                false, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
        this.firstLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        this.secondLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        centerRenderer(this.firstLanguageComboBox);
        centerRenderer(this.secondLanguageComboBox);
        this.firstLanguageComboBox.setSelectedItem(this.controller.getCurrentProfile().getFirstLanguage());
        this.secondLanguageComboBox.setSelectedItem(this.controller.getCurrentProfile().getSecondLanguage());
        this.dailyGoalSlider = new JSlider(
                DailyGoalSettings.MIN,
                DailyGoalSettings.MAX,
                this.controller.getDailyGoal());
        this.dailyGoalValueLabel = UIFactory.createLabel(Integer.toString(this.controller.getDailyGoal()), UIConstants.FONT);

        buildLayout();
        buttonActionRegister();
    }

    private static void centerRenderer(final JComboBox<?> comboBox) {
        if (comboBox.getRenderer() instanceof JLabel label) {
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        add(headerPanel(), BorderLayout.NORTH);

        final JPanel contentPanel = UIFactory.createPanel();
        contentPanel.setBorder(new EmptyBorder(ZERO_PADDING, ZERO_PADDING, CONTENT_BOTTOM_PADDING, ZERO_PADDING));
        contentPanel.add(accountDetailsPanel());
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        contentPanel.add(languagePreferencesPanel());
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        contentPanel.add(dailyGoalPanel());
        contentPanel.add(Box.createVerticalStrut(ACTION_SECTION_SPACING));
        contentPanel.add(actionButtonsPanel());
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel headerPanel() {
        final JPanel headerPanel = UIFactory.createPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
        headerPanel.add(this.goBackButton, BorderLayout.WEST);
        headerPanel.add(UIFactory.createLabel("Configure Profile", UIConstants.TITLE_FONT), BorderLayout.CENTER);
        headerPanel.add(Box.createHorizontalStrut(UIConstants.BACK_BUTTON_WIDTH), BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel accountDetailsPanel() {
        final JPanel panel = createPanel("ACCOUNT DETAILS", ACCOUNT_PANEL_HEIGHT);
        final JPanel nicknameRow = UIFactory.createPanel(
                new BorderLayout(UIConstants.SPACING_SMALL, ACCOUNT_ROW_VERTICAL_GAP));
        nicknameRow.setBackground(panel.getBackground());
        nicknameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, ACCOUNT_ROW_HEIGHT));
        nicknameRow.add(UIFactory.createLabel("Nickname:", UIConstants.FONT), BorderLayout.WEST);
        nicknameRow.add(this.usernameTextField, BorderLayout.CENTER);
        panel.add(nicknameRow);
        return panel;
    }

    private JPanel languagePreferencesPanel() {
        final JPanel panel = createPanel("LANGUAGE PREFERENCES", LANGUAGE_PANEL_HEIGHT);
        final JPanel languageRow = UIFactory.createPanel(new GridLayout(
                GRID_SIZE, GRID_SIZE, LANGUAGE_COLUMN_GAP, ZERO_PADDING));
        languageRow.setBackground(panel.getBackground());
        languageRow.add(UIFactory.createLabel("Language you study:", UIConstants.FONT));
        languageRow.add(UIFactory.createLabel("Language you already know:", UIConstants.FONT));
        languageRow.add(this.secondLanguageComboBox);
        languageRow.add(this.firstLanguageComboBox);
        panel.add(languageRow);
        return panel;
    }

    private JPanel dailyGoalPanel() {
        final JPanel panel = createPanel("DAILY GOAL", DAILY_GOAL_PANEL_HEIGHT);
        final JPanel wordsRow = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, ZERO_PADDING, ZERO_PADDING));
        wordsRow.setBackground(panel.getBackground());
        wordsRow.add(UIFactory.createLabel("Words per day: ", UIConstants.FONT));
        wordsRow.add(this.dailyGoalValueLabel);
        panel.add(wordsRow);
        panel.add(Box.createVerticalStrut(GOAL_SLIDER_TOP_GAP));
        panel.add(sliderPanel(panel.getBackground()));
        return panel;
    }

    private JPanel actionButtonsPanel() {
        final JPanel actionsPanel = UIFactory.createPanel(
                new FlowLayout(FlowLayout.CENTER, ACTION_SECTION_SPACING, ZERO_PADDING));
        actionsPanel.add(this.saveChangesButton);
        actionsPanel.add(this.deleteProfileButton);
        return actionsPanel;
    }

    private JPanel sliderPanel(final Color background) {
        this.dailyGoalSlider.setBackground(background);
        this.dailyGoalSlider.setForeground(UIConstants.TEXT_COLOR);
        this.dailyGoalSlider.setMajorTickSpacing(SLIDER_TICK_SPACING);
        this.dailyGoalSlider.setMinorTickSpacing(SLIDER_TICK_SPACING);
        this.dailyGoalSlider.setPaintTicks(true);

        final JPanel panel = UIFactory.createPanel(new BorderLayout());
        panel.setBackground(background);
        panel.add(this.dailyGoalSlider, BorderLayout.CENTER);
        final JPanel limitsPanel = UIFactory.createPanel(new BorderLayout());
        limitsPanel.setBackground(background);
        limitsPanel.add(UIFactory.createLabel(Integer.toString(DailyGoalSettings.MIN), UIConstants.FONT),
                BorderLayout.WEST);
        limitsPanel.add(UIFactory.createLabel(Integer.toString(DailyGoalSettings.MAX), UIConstants.FONT),
                BorderLayout.EAST);
        panel.add(limitsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPanel(final String title, final int height) {
        final JPanel panel = UIFactory.createPanel();
        UIFactory.brighter(panel);
        panel.setAlignmentX(CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, height));
        panel.setMaximumSize(new Dimension(PANEL_WIDTH, height));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.PANEL_BORDER),
                new EmptyBorder(UIConstants.SPACING_SMALL, UIConstants.SPACING_LARGE,
                        UIConstants.SPACING_SMALL, UIConstants.SPACING_LARGE)));
        panel.add(UIFactory.createLabel(title, UIConstants.FONT.deriveFont(Font.BOLD)));
        panel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        return panel;
    }

    private void buttonActionRegister() {
        this.saveChangesButton.addActionListener(e -> this.controller.saveProfileConfigurations(
            this.usernameTextField.getText(),
            (String) this.firstLanguageComboBox.getSelectedItem(),
            (String) this.secondLanguageComboBox.getSelectedItem(),
            this.dailyGoalSlider.getValue()));

        this.goBackButton.addActionListener(e -> this.controller.showProfileDashboardPanel());
        this.deleteProfileButton.addActionListener(e -> this.controller.deleteProfile());
        this.dailyGoalSlider.addChangeListener(
                e -> this.dailyGoalValueLabel.setText(Integer.toString(this.dailyGoalSlider.getValue())));
    }
}

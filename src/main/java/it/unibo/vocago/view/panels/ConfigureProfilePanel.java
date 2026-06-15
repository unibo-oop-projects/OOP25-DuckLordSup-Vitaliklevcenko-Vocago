package it.unibo.vocago.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.border.EmptyBorder;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class ConfigureProfilePanel extends JPanel implements PanelLayout {

    private static final String[] LANGUAGES = {
            "English", "Italian", "German", "French", "Spanish",
            "Portuguese", "Dutch", "Polish", "Japanese", "Chinese"
    };
    private static final int PANEL_WIDTH = 660;
    private static final int DAILY_GOAL_MIN = 5;
    private static final int DAILY_GOAL_MAX = 40;
    private static final int DEFAULT_DAILY_GOAL = 10;
    private static final int BUTTON_WIDTH = 70;
    private final Controller controller;
    private final JButton saveChangesButton;
    private final JButton resetProgressButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;
    private final JSlider dailyGoalSlider;
    private final JLabel dailyGoalValueLabel;

    public ConfigureProfilePanel(final Controller controller) {
        this.controller = controller;
        UIFactory.stylePanel(this);
        this.saveChangesButton = UIFactory.createButton("Save Changes", "", 1,
                UIConstants.BLUE, 42, 430, true, false, true, UIConstants.FONT);
        this.resetProgressButton = UIFactory.createButton("Reset Progress", "", 1,
                UIConstants.BUTTON_BACKGROUND, 42, 210, true, false, true, UIConstants.FONT);
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png", 60, UIConstants.BACKGROUND,
                60,
                BUTTON_WIDTH,
                false, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
        this.firstLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        this.secondLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        this.firstLanguageComboBox.setSelectedItem(this.controller.getCurrentUser().getFirstLanguage());
        this.secondLanguageComboBox.setSelectedItem(this.controller.getCurrentUser().getSecondLanguage());
        this.dailyGoalSlider = new JSlider(DAILY_GOAL_MIN, DAILY_GOAL_MAX, this.controller.getDailyGoal());
        this.dailyGoalValueLabel = UIFactory.createLabel(Integer.toString(this.controller.getDailyGoal()), UIConstants.FONT);

        buildLayout();
        this.usernameTextField.addActionListener(e -> buttonActionRegister());
        this.saveChangesButton.addActionListener(e -> buttonActionRegister());
        this.goBackButton.addActionListener(e -> this.controller.showUserDashboardPanel());
        this.dailyGoalSlider.addChangeListener(
                e -> this.dailyGoalValueLabel.setText(Integer.toString(this.dailyGoalSlider.getValue())));
    }

    @Override
    public void buildLayout() {
        setLayout(new BorderLayout());
        add(headerPanel(), BorderLayout.NORTH);

        final JPanel contentPanel = UIFactory.createPanel();
        contentPanel.setBorder(new EmptyBorder(0, 0, 18, 0));
        contentPanel.add(accountDetailsPanel());
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(languagePreferencesPanel());
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(dailyGoalPanel());
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(actionButtonsPanel());
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel headerPanel() {
        final JPanel headerPanel = UIFactory.createPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 90));
        headerPanel.add(this.goBackButton, BorderLayout.WEST);
        headerPanel.add(UIFactory.createLabel("Configure Profile", UIConstants.TITLE_FONT), BorderLayout.CENTER);
        headerPanel.add(Box.createHorizontalStrut(BUTTON_WIDTH), BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel accountDetailsPanel() {
        final JPanel panel = createPanel("ACCOUNT DETAILS", 100);
        final JPanel nicknameRow = UIFactory.createPanel(new BorderLayout(10, 5));
        nicknameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nicknameRow.add(UIFactory.createLabel("Nickname:", UIConstants.FONT), BorderLayout.WEST);
        nicknameRow.add(this.usernameTextField, BorderLayout.CENTER);
        panel.add(nicknameRow);
        return panel;
    }

    private JPanel languagePreferencesPanel() {
        final JPanel panel = createPanel("LANGUAGE PREFERENCES", 130);
        final JPanel languageRow = UIFactory.createPanel(new GridLayout(2, 2, 24, 0));
        languageRow.add(UIFactory.createLabel("Language you study:", UIConstants.FONT));
        languageRow.add(UIFactory.createLabel("Language you already know:", UIConstants.FONT));
        languageRow.add(this.firstLanguageComboBox);
        languageRow.add(this.secondLanguageComboBox);
        panel.add(languageRow);
        return panel;
    }

    private JPanel dailyGoalPanel() {
        final JPanel panel = createPanel("DAILY GOAL", 135);
        final JPanel wordsRow = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wordsRow.add(UIFactory.createLabel("Words per day: ", UIConstants.FONT));
        wordsRow.add(this.dailyGoalValueLabel);
        panel.add(wordsRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(sliderPanel(panel.getBackground()));
        return panel;
    }

    private JPanel actionButtonsPanel() {
        final JPanel actionsPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        actionsPanel.add(this.saveChangesButton);
        actionsPanel.add(this.resetProgressButton);
        return actionsPanel;
    }

    private JPanel sliderPanel(final Color background) {
        this.dailyGoalSlider.setBackground(background);
        this.dailyGoalSlider.setForeground(UIConstants.TEXT_COLOR);
        this.dailyGoalSlider.setMajorTickSpacing(5);
        this.dailyGoalSlider.setMinorTickSpacing(5);
        this.dailyGoalSlider.setPaintTicks(true);
        this.dailyGoalSlider.setOpaque(false);
        final JPanel panel = UIFactory.createPanel(new BorderLayout());
        panel.add(this.dailyGoalSlider, BorderLayout.CENTER);

        final JPanel limitsPanel = UIFactory.createPanel(new BorderLayout());
        limitsPanel.add(UIFactory.createLabel(Integer.toString(DAILY_GOAL_MIN), UIConstants.FONT), BorderLayout.WEST);
        limitsPanel.add(UIFactory.createLabel(Integer.toString(DAILY_GOAL_MAX), UIConstants.FONT), BorderLayout.EAST);
        panel.add(limitsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPanel(final String title, final int height) {
        final JPanel panel = UIFactory.createPanel();
        UIFactory.brighter(panel);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, height));
        panel.setMaximumSize(new Dimension(PANEL_WIDTH, height));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.PANEL_BORDER),
                new EmptyBorder(10, 20, 10, 20)));
        panel.add(UIFactory.createLabel(title, UIConstants.FONT.deriveFont(Font.BOLD)));
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private void buttonActionRegister() {

    }
}

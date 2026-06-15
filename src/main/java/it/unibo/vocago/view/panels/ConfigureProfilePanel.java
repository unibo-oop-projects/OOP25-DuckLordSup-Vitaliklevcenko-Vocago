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
import javax.swing.SwingConstants;
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

    private final Controller controller;
    private final JButton saveChangesButton;
    private final JButton resetProgressButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;


    public ConfigureProfilePanel(final Controller controller) {
        this.controller = controller;
        UIFactory.stylePanel(this);
        this.saveChangesButton = UIFactory.createButton("Save Changes", "", 1,
                UIConstants.BLUE, 42, 430, true, false, true, UIConstants.FONT);
        this.resetProgressButton = UIFactory.createButton("Reset Progress", "", 1,
                UIConstants.BUTTON_BACKGROUND, 42, 210, true, false, true, UIConstants.FONT);
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png", 60, UIConstants.BACKGROUND,
                60, 70,
                false, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
        this.firstLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        this.secondLanguageComboBox = UIFactory.createComboBox(LANGUAGES);
        this.secondLanguageComboBox.setSelectedItem("Italian");//should cahnge to the defaul


        buildLayout();
        this.usernameTextField.addActionListener(e -> buttonActionRegister());
        this.saveChangesButton.addActionListener(e -> buttonActionRegister());
        this.goBackButton.addActionListener(e -> this.controller.showUserDashboardPanel());

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
        headerPanel.add(Box.createHorizontalStrut(this.goBackButton.getWidth()), BorderLayout.EAST);// why is not working
        return headerPanel;
    }

    private JPanel accountDetailsPanel() {
        final JPanel panel = createPanel("ACCOUNT DETAILS", 100);
        final JPanel nicknameRow = createRowPanel(new BorderLayout(10, 5), panel.getBackground());
        nicknameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nicknameRow.add(UIFactory.createLabel("Nickname:", UIConstants.FONT), BorderLayout.WEST);
        nicknameRow.add(this.usernameTextField, BorderLayout.CENTER);
        panel.add(nicknameRow);
        return panel;
    }

    private JPanel languagePreferencesPanel() {
        final JPanel panel = createPanel("LANGUAGE PREFERENCES", 130);
        final JPanel languageRow = createRowPanel(new GridLayout(2, 2, 24, 0), panel.getBackground());
        languageRow.add(UIFactory.createLabel("Language you study:", UIConstants.FONT));
        languageRow.add(UIFactory.createLabel("Language you already know:", UIConstants.FONT));
        languageRow.add(this.firstLanguageComboBox);
        languageRow.add(this.secondLanguageComboBox);
        panel.add(languageRow);
        return panel;
    }

    private JPanel dailyGoalPanel() {
        final JPanel panel = createPanel("DAILY GOAL", 135);

        return panel;
    }

    private JPanel actionButtonsPanel() {
        final JPanel actionsPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        actionsPanel.setMaximumSize(new Dimension(PANEL_WIDTH, 46));
        actionsPanel.add(this.saveChangesButton);
        actionsPanel.add(this.resetProgressButton);
        return actionsPanel;
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

    private JPanel createRowPanel(final java.awt.LayoutManager layout, final Color background) {
        final JPanel panel = UIFactory.createPanel(layout);
        panel.setBackground(background);
        return panel;
    }



    private void buttonActionRegister() {

    }
}

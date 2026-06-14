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

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class ConfigureProfilePanel  extends JPanel implements PanelLayout {

    private final Controller controller;
    private final JButton createNewUserButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;
    private final JComboBox<Integer> dailyGoal; 
    
    public ConfigureProfilePanel(final Controller controller) {
        this.controller = controller;
        UIFactory.stylePanel(this);
        this.createNewUserButton = UIFactory.createButton("Change");
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png", 60, UIConstants.BACKGROUND, 60, 70,
                true, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
        this.dailyGoal = UIFactory.createComboBox(new Integer[] { 5, 10, 20, 40 });
        this.firstLanguageComboBox = UIFactory.createComboBox(new String[] {
                "English", "Italian", "German", "French", "Spanish",
                "Portuguese", "Dutch", "Polish", "Japanese", "Chinese"
        });
        this.secondLanguageComboBox = UIFactory.createComboBox(new String[] {
                "Italian", "English", "German", "French", "Spanish",
                "Portuguese", "Dutch", "Polish", "Japanese", "Chinese"
        });

        buildLayout();
        this.usernameTextField.addActionListener(e -> buttonActionRegister());
        this.createNewUserButton.addActionListener(e -> buttonActionRegister());
        this.goBackButton.addActionListener(e -> this.controller.showStartPanel());
    }

    @Override
    public void buildLayout() {
        setLayout(new BorderLayout());
        JLabel titleLabel = UIFactory.createLabel("Configure User", UIConstants.TITLE_FONT);
        JPanel titlePanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = UIFactory.createPanel();
        contentPanel.add(Box.createVerticalStrut(10));

        Dimension comboSize = new Dimension(220, 32);
        this.firstLanguageComboBox.setMaximumSize(comboSize);
        this.secondLanguageComboBox.setMaximumSize(comboSize);
        this.dailyGoal.setMaximumSize(comboSize);

        JPanel LanguagePanel = UIFactory.createPanel();
        UIFactory.brighter(LanguagePanel);

        LanguagePanel.add(Box.createVerticalStrut(20));
        LanguagePanel.add(UIFactory.createLabel("choose the language you study", UIConstants.FONT));
        LanguagePanel.add(Box.createVerticalStrut(10));
        LanguagePanel.add(this.firstLanguageComboBox);
        LanguagePanel.add(Box.createVerticalStrut(20));
        LanguagePanel.add(UIFactory.createLabel("choose the language you already know", UIConstants.FONT));
        LanguagePanel.add(Box.createVerticalStrut(10));
        LanguagePanel.add(this.secondLanguageComboBox);
        LanguagePanel.add(Box.createVerticalStrut(20));
        LanguagePanel.add(UIFactory.createLabel("Choose the Daily Goal", UIConstants.FONT));
        LanguagePanel.add(Box.createVerticalStrut(10));
        LanguagePanel.add(this.dailyGoal);
        LanguagePanel.add(Box.createVerticalStrut(10));

        contentPanel.add(LanguagePanel);
        //contentPanel.add(Box.createVerticalStrut(10));

        JPanel usernamePanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 12, 40));
        usernamePanel.add(UIFactory.createLabel("new nickname:", UIConstants.FONT));
        usernamePanel.add(this.usernameTextField);
        usernamePanel.add(this.createNewUserButton);

        contentPanel.add(usernamePanel);

        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));

        bottomPanel.add(this.goBackButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void buttonActionRegister() {
        
    }
}

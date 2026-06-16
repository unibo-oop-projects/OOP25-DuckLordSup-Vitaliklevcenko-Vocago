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
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class CreateNewProfilePanel extends JPanel{
    
    private final Controller controller;
    private final JButton createNewProfileButton;
    private final JTextField usernameTextField;
    private final JComboBox<String> firstLanguageComboBox;
    private final JComboBox<String> secondLanguageComboBox;
    private final JButton goBackButton;

    public CreateNewProfilePanel(final Controller controller) {

        this.controller = controller;
        UIFactory.stylePanel(this);
        this.createNewProfileButton = UIFactory.createButton("Create");
        this.goBackButton = UIFactory.createButton("", "data/resources/pictures/back.png", 60, UIConstants.BACKGROUND, 60, 70,
                true, true, true, UIConstants.FONT);
        this.usernameTextField = UIFactory.createTextField();
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
        this.createNewProfileButton.addActionListener(e -> buttonActionRegister());
        this.goBackButton.addActionListener(e -> this.controller.showStartPanel());
    }

    public void buildLayout() {
        setLayout(new BorderLayout());
        JLabel titleLabel = UIFactory.createLabel("Create New Profile", UIConstants.TITLE_FONT);
        JPanel titlePanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = UIFactory.createPanel();
        contentPanel.add(Box.createVerticalStrut(10));

        Dimension comboSize = new Dimension(220, 32);
        this.firstLanguageComboBox.setMaximumSize(comboSize);
        this.secondLanguageComboBox.setMaximumSize(comboSize);

        JPanel LanguagePanel = UIFactory.createPanel();
        UIFactory.brighter(LanguagePanel);

        LanguagePanel.add(Box.createVerticalStrut(20));
        LanguagePanel.add(UIFactory.createLabel("choose a language you want to study", UIConstants.FONT));
        LanguagePanel.add(Box.createVerticalStrut(10));
        LanguagePanel.add(this.firstLanguageComboBox);
        LanguagePanel.add(Box.createVerticalStrut(20));
        LanguagePanel.add(UIFactory.createLabel("choose a language you already know", UIConstants.FONT));
        LanguagePanel.add(Box.createVerticalStrut(10));
        LanguagePanel.add(this.secondLanguageComboBox);
        LanguagePanel.add(Box.createVerticalStrut(20));

        contentPanel.add(LanguagePanel);
        contentPanel.add(Box.createVerticalStrut(10));

        JPanel usernamePanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 12, 40));
        usernamePanel.add(UIFactory.createLabel("nickname:", UIConstants.FONT));
        usernamePanel.add(this.usernameTextField);
        usernamePanel.add(this.createNewProfileButton);

        contentPanel.add(usernamePanel);
        add(contentPanel, BorderLayout.CENTER);

        if (this.controller.getExistingProfiles().size() > 0) {
            JPanel bottomPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
            bottomPanel.add(this.goBackButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }

    private void buttonActionRegister() {
        this.controller.createProfile(
                (String) this.usernameTextField.getText(),
                (String) this.firstLanguageComboBox.getSelectedItem(),
                (String) this.secondLanguageComboBox.getSelectedItem());
    }
    
}

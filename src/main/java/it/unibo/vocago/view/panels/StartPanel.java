package it.unibo.vocago.view.panels;

import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class StartPanel extends JPanel{

    private static final int MAX_USERS = 4;
    private static final String ADD_ICON = "data/resources/pictures/plus.png";
    private static final String[] PROFILE_ICONS = {
            "data/resources/pictures/bunny.png",
            "data/resources/pictures/owl.png",
            "data/resources/pictures/fox.png",
            "data/resources/pictures/bear.png"
    };
    final private Controller controller;
    
    public StartPanel(final Controller controller) {
        this.controller = controller;
        buildLayout();
    }
    
    private void buildLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        UIFactory.stylePanel(this);

        final List<User> profiles = this.controller.getExistingProfiles();

        final JPanel titlePanel = UIFactory.createPanel(new BorderLayout());
        titlePanel.add(UIFactory.createLabel("Welcome to VocaGo!", UIConstants.TITLE_FONT));
        UIFactory.highlight(titlePanel);
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        add(titlePanel);

        final JPanel contentPanel = UIFactory.createPanel();
        UIFactory.brighter(contentPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(UIFactory.createLabel("Select your profile:", UIConstants.FONT));
        contentPanel.add(Box.createVerticalStrut(15));

        final JPanel profilesPanel = UIFactory.createPanel();
        UIFactory.brighter(profilesPanel);

        final int visibleProfiles = Math.min(profiles.size(), MAX_USERS);

        for (int index = 0; index < visibleProfiles; index++) {
            profilesPanel.add(createProfileSelectionButton(profiles.get(index), PROFILE_ICONS[index]));
            profilesPanel.add(Box.createVerticalStrut(10));
        }

        for (int index = visibleProfiles; index < MAX_USERS; index++) {
            profilesPanel.add(createAddProfileButton());
            profilesPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.add(profilesPanel);

        if (profiles.size() >= MAX_USERS) {
            contentPanel.add(UIFactory.createLabel(
                    "Maximum of 4 profiles reached. Delete a profile to add a new one.",
                    UIConstants.FONT));
        }

        contentPanel.add(Box.createVerticalStrut(20));
        add(contentPanel);
    }

    private JButton createProfileSelectionButton(final User profile, final String iconPath) {
        final JButton button = createProfileButton("   Profile:  " + profile.getUserName(), iconPath);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(event -> this.controller.chooseProfile(profile));
        return button;
    }

    private JButton createAddProfileButton() {
        final JButton button = createProfileButton("ADD NEW PROFILE", ADD_ICON);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(event -> this.controller.showCreateNewProfilePanel());
        return button;
    }

    private JButton createProfileButton(final String text, final String iconPath) {
        final JButton button = UIFactory.createButton(text,iconPath,60,UIConstants.BUTTON_BACKGROUND,90,
                400, true, true, true, UIConstants.FONT);
        button.setBorderPainted(true);
        return button;
    }
}

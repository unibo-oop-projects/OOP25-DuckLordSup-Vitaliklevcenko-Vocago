package it.unibo.vocago.view.panels;

import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

/**
 * Panel showing the start screen, where the user selects an existing profile or
 * chooses to create a new one, in case of absence of profiles, it will start from create new profile panel.
 */
public final class StartPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int MAX_USERS = 4;
    private static final int TITLE_PANEL_HEIGHT = 100;
    private static final int PROFILE_BUTTON_ICON_SIZE = 60;
    private static final int PROFILE_BUTTON_HEIGHT = 90;
    private static final int PROFILE_BUTTON_WIDTH = 400;
    private static final String ADD_ICON = "data/resources/pictures/plus.png";
    private static final String[] PROFILE_ICONS = {
            "data/resources/pictures/bunny.png",
            "data/resources/pictures/owl.png",
            "data/resources/pictures/fox.png",
            "data/resources/pictures/bear.png",
    };
    private final transient Controller controller;

    /**
     * Creates the panel, wiring it to the given controller.
     *
     * @param controller the controller user actions are forwarded to
     */
    @SuppressFBWarnings(value = "EI2", justification = "The panel intentionally shares the app controller.")
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
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, TITLE_PANEL_HEIGHT));
        add(titlePanel);

        final JPanel contentPanel = UIFactory.createPanel();
        UIFactory.brighter(contentPanel);
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_MEDIUM));
        contentPanel.add(UIFactory.createLabel("Select your profile:", UIConstants.FONT));
        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_MEDIUM));

        final JPanel profilesPanel = UIFactory.createPanel();
        UIFactory.brighter(profilesPanel);

        final int visibleProfiles = Math.min(profiles.size(), MAX_USERS);

        for (int index = 0; index < visibleProfiles; index++) {
            profilesPanel.add(createProfileSelectionButton(profiles.get(index), PROFILE_ICONS[index]));
            profilesPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        }

        for (int index = visibleProfiles; index < MAX_USERS; index++) {
            profilesPanel.add(createAddProfileButton());
            profilesPanel.add(Box.createVerticalStrut(UIConstants.SPACING_SMALL));
        }

        contentPanel.add(profilesPanel);

        if (profiles.size() >= MAX_USERS) {
            contentPanel.add(UIFactory.createLabel(
                    "Maximum of 4 profiles reached. Delete a profile to add a new one.",
                    UIConstants.FONT));
        }

        contentPanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));
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
        final JButton button = UIFactory.createButton(text, iconPath, PROFILE_BUTTON_ICON_SIZE,
                UIConstants.BUTTON_BACKGROUND, PROFILE_BUTTON_HEIGHT, PROFILE_BUTTON_WIDTH,
                true, true, true, UIConstants.FONT);
        button.setBorderPainted(true);
        return button;
    }
}

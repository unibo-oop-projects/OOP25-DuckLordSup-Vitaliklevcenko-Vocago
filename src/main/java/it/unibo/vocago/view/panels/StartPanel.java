package it.unibo.vocago.view.panels;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

public class StartPanel extends JPanel implements PanelLayout {

    private static final String[] PROFILE_ICONS = {
            "src/main/data/resources/pictures/bunny.png",
            "src/main/data/resources/pictures/owl.png",
            "src/main/data/resources/pictures/fox.png",
            "src/main/data/resources/pictures/bear.png"
    };

    private static final String ADD_ICON = "src/main/data/resources/pictures/plus.png";

    final private Controller controller;
    public StartPanel(final Controller controller) {
        this.controller = controller;
        buildLayout();
    }
    
    public void buildLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        UIFactory.stylePanel(this);

        final List<User> users = this.controller.getExistingUsers();

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

        final JPanel usersPanel = UIFactory.createPanel();
        UIFactory.brighter(usersPanel);

        final int visibleUsers = Math.min(users.size(), MAX_USERS);

        for (int index = 0; index < visibleUsers; index++) {
            usersPanel.add(createUserButton(users.get(index), PROFILE_ICONS[index]));
            usersPanel.add(Box.createVerticalStrut(10));
        }

        for (int index = visibleUsers; index < MAX_USERS; index++) {
            usersPanel.add(createAddProfileButton());
            usersPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.add(usersPanel);

        if (users.size() >= MAX_USERS) {
            contentPanel.add(UIFactory.createLabel(
                    "Maximum of 4 profiles reached. Delete a profile to add a new one.",
                    UIConstants.FONT));
        }

        contentPanel.add(Box.createVerticalStrut(20));
        add(contentPanel);
    }

    private JButton createUserButton(final User user, final String iconPath) {
        final JButton button = createProfileButton("   Profile:  " + user.getUserName(), iconPath);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(event -> this.controller.chooseUser(user));
        return button;
    }

    private JButton createAddProfileButton() {
        final JButton button = createProfileButton("ADD NEW PROFILE", ADD_ICON);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(event -> this.controller.showCreateNewUserPanel());
        return button;
    }

    private JButton createProfileButton(final String text, final String iconPath) {
        final JButton button = UIFactory.createButton(text,iconPath,60,UIConstants.BUTTON_BACKGROUND,90,
                400, true, true, true, UIConstants.FONT);
        button.setBorderPainted(true);
        return button;
    }
}

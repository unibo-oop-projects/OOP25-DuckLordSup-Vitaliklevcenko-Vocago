package it.unibo.vocago.view.panels;

import java.awt.Dimension;
import java.io.IOException;

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
    
    final private Controller controller;
    public StartPanel(final Controller controller) {
        this.controller = controller;
        buildLayout();
    }
    
    private JPanel showExistingUsers() throws IOException {

        JPanel usersPanel = UIFactory.createPanel();
        UIFactory.brighter(usersPanel);

        int usersCount = 0;
        for (User user : this.controller.getExistingUsers()) {
            if (usersCount >= MAX_USERS) {
                break;
            }
            String iconPath;

            switch (usersCount) {
                case 0:
                    iconPath = "src/main/data/resources/pictures/bunny.png";
                    break;
                case 1:
                    iconPath = "src/main/data/resources/pictures/owl.png";
                    break;
                case 2:
                    iconPath = "src/main/data/resources/pictures/fox.png";
                    break;
                case 3:
                    iconPath = "src/main/data/resources/pictures/bear.png";
                    break;
                default:
                    iconPath = "src/main/data/resources/pictures/plus.png";
                    break;
            }

            JButton userButton = UIFactory.createButton("   Profile:  " + user.getUserName(), iconPath,
                    60, UIConstants.BUTTON_BACKGROUND, 90, 400,true,true, true, UIConstants.FONT);
            userButton.setHorizontalAlignment(SwingConstants.LEFT);
            userButton.setBorderPainted(true);
            usersPanel.add(userButton);
            userButton.addActionListener(e -> this.controller.chooseUser(user));

            usersPanel.add(Box.createVerticalStrut(10));

            usersCount++;
        }

        for (int i = usersCount; i < MAX_USERS; i++) {
            JButton button = UIFactory.createButton("ADD NEW PROFILE", "data/pictures/plus.png",
                    60, UIConstants.BUTTON_BACKGROUND, 90, 400,true,true, true, UIConstants.FONT);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setBorderPainted(true);
            button.addActionListener(e -> this.controller.showCreateNewUserPanel());
            usersPanel.add(button);
            usersPanel.add(Box.createVerticalStrut(10));
        }
        return usersPanel;
    }

    @Override
    public void buildLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        UIFactory.stylePanel(this);

        JPanel titlePanel = UIFactory.createPanel(new BorderLayout());
        titlePanel.add(UIFactory.createLabel("Welcome to VocaGo!", UIConstants.TITLE_FONT));
        UIFactory.highlight(titlePanel);
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        add(titlePanel);

        JPanel contentPanel = UIFactory.createPanel();
        UIFactory.brighter(contentPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        contentPanel.add(UIFactory.createLabel("Select your profile:", UIConstants.FONT));
        contentPanel.add(Box.createVerticalStrut(15));

        int usersCount = 0;
        try {
            usersCount = this.controller.getExistingUsers().size();
            contentPanel.add(showExistingUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (usersCount >= MAX_USERS) {
            contentPanel.add(
                    UIFactory.createLabel("Maximum of 4 profiles reached. Delete a profile to add a new one.",
                            UIConstants.FONT));
        }
        contentPanel.add(Box.createVerticalStrut(20));
        add(contentPanel);

        actionRegister();

    }

    @Override
    public void actionRegister() {

    }
}

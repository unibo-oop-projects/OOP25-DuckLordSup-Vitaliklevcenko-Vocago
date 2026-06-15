package it.unibo.vocago.view.panels;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.progress.api.Stats;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;
import java.awt.Image;

public class UserDashboardPanel extends JPanel implements PanelLayout {

        private final Controller controller;
        private final JButton switchUserButton;
        private final JButton startButton;
        private final JButton editVocabularyButton;
        private final JButton deleteUserButton;
        private final JButton configureProfileButton;

        public UserDashboardPanel(final Controller controller) {
                this.controller = controller;
                this.editVocabularyButton = UIFactory.createButton("Edit Vocabulary",
                                "data/resources/pictures/edit.png",
                                40, UIConstants.BLUE, 70, 500, true, true, true, UIConstants.PROMPT_FONT);
                this.startButton = UIFactory.createButton("START LEARNING", "data/resources/pictures/start.png",
                                70, UIConstants.GREEN, 140, 500, true, true, true, UIConstants.TITLE_FONT);
                this.deleteUserButton = UIFactory.createButton("Reset Stats", "data/resources/pictures/reset.png",
                                20, UIConstants.RED, 30, 160, true, true, true, UIConstants.FONT);
                this.configureProfileButton = UIFactory.createButton("Configure Profile",
                                "data/resources/pictures/settings.png",
                                30, UIConstants.AMBER, 50, 230, true, true, true, UIConstants.FONT);
                this.switchUserButton = UIFactory.createButton("Switch Profile", "data/resources/pictures/profile.png",
                                25, UIConstants.TEAL, 50, 230, true, true, true, UIConstants.FONT);
                buildLayout();
                ButtonActionRegister();
        }

        @Override
        public void buildLayout() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                JPanel titlePanel = UIFactory.createPanel(new BorderLayout());
                titlePanel.add(UIFactory.createLabel(
                                "WELCOME BACK, " + this.controller.getCurrentUser().getUserName() + "!",
                                UIConstants.TITLE_FONT));
                titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 130));
                titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
                add(titlePanel);

                JPanel centerPanel = UIFactory.createPanel(new GridLayout(1, 2));
                centerPanel.add(leftPanel());
                centerPanel.add(rightPanel());
                add(centerPanel);

        }

        private JPanel leftPanel() {

                JPanel leftPanel = UIFactory.createPanel();
                UIFactory.brighter(leftPanel);

                leftPanel.add(Box.createVerticalStrut(40));

                leftPanel.add(UIFactory.createLabel("YOUR STATS", UIConstants.TITLE_FONT));

                JPanel statsPanel = UIFactory.createPanel(new GridLayout(2, 2, 15, 15));
                statsPanel.setMaximumSize(new Dimension(500, 250));
                statsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                UIFactory.brighter(statsPanel);

                Stats dashboardStats = this.controller.getDashboardStats();
                statsPanel.add(statPanel("Mastered", dashboardStats.getMasteredItems() + " Words",
                                "data/resources/pictures/star.png"));
                statsPanel.add(statPanel("Accuracy", String.format("%.1f%%", dashboardStats.getAccuracyPercent()),
                                "data/resources/pictures/graph.png"));
                statsPanel.add(statPanel("Streak", dashboardStats.getCurrentStreak() + " Days",
                                "data/resources/pictures/streak.png"));
                statsPanel.add(statPanel("Time Study", formatStudyTime(dashboardStats.getTotalStudyTime()),
                                "data/resources/pictures/clock.png"));
                leftPanel.add(statsPanel);

                leftPanel.add(Box.createVerticalStrut(10));
                leftPanel.add(UIFactory.createLabel("Account Management", UIConstants.TITLE_FONT));
                leftPanel.add(Box.createVerticalStrut(10));

                JPanel managementPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
                UIFactory.brighter(managementPanel);
                managementPanel.setMaximumSize(new Dimension(500, 70));
                managementPanel.setPreferredSize(new Dimension(500, 70));
                managementPanel.add(this.switchUserButton);
                managementPanel.add(this.configureProfileButton);
                leftPanel.add(managementPanel);
                leftPanel.add(Box.createVerticalStrut(20));
                leftPanel.add(this.deleteUserButton);
                leftPanel.add(Box.createVerticalStrut(20));
                UIFactory.highlight(leftPanel);
                return leftPanel;
        }

        public JPanel statPanel(String title, String text, String iconPath) {
                JPanel stat = UIFactory.createPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
                UIFactory.brighter(stat);
                ImageIcon icon = UIFactory.loadIcon(iconPath);
                Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);

                JLabel iconLabel = UIFactory.createLabel("", UIConstants.PROMPT_FONT);
                iconLabel.setIcon(new ImageIcon(scaledImage));

                JPanel textPanel = UIFactory.createPanel();
                UIFactory.brighter(textPanel);
                UIFactory.brighter(textPanel);
                textPanel.add(UIFactory.createLabel(title, UIConstants.FONT));
                textPanel.add(UIFactory.createLabel(text, UIConstants.FONT));
                stat.add(iconLabel);
                stat.add(textPanel);

                UIFactory.brighter(stat);
                // UIFactory.brightPanel(textPanel);
                return stat;
        }

        private JPanel rightPanel() {
                JPanel rightPanel = UIFactory.createPanel();
                rightPanel.add(Box.createVerticalStrut(110));
                rightPanel.add(this.startButton);
                rightPanel.add(Box.createVerticalStrut(90));
                rightPanel.add(this.editVocabularyButton);
                rightPanel.add(Box.createVerticalStrut(30));
                // should add some info about vocabulary, like how many words or something like
                // that or make calendar
                // make a pdf/excel file of the vocabulary to take
                UIFactory.brighter(rightPanel);
                UIFactory.highlight(rightPanel);
                return rightPanel;
        }

        private String formatStudyTime(final long totalStudyTime) {
                final long hours = totalStudyTime / 3600;
                final long minutes = (totalStudyTime % 3600) / 60;
                return String.format("%02d:%02d", hours, minutes);
        }

        private void ButtonActionRegister() {
                this.editVocabularyButton.addActionListener(e -> this.controller.showVocabularyEditorPanel());
                this.startButton.addActionListener(e -> this.controller.showLearningPanel());
                this.deleteUserButton.addActionListener(e -> this.controller.resetStats());
                this.switchUserButton.addActionListener(e -> this.controller.showStartPanel());
                this.configureProfileButton.addActionListener(e -> this.controller.showconfigureProfilePanel());
        }
}

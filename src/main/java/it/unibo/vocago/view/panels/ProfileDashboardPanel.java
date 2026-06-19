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
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;
import java.awt.Image;

public class ProfileDashboardPanel extends JPanel{

        private final Controller controller;
        private final JButton switchProfileButton;
        private final JButton startButton;
        private final JButton editVocabularyButton;
        private final JButton resetStatisticsButton;
        private final JButton configureProfileButton;

        public ProfileDashboardPanel(final Controller controller) {
                this.controller = controller;
                this.editVocabularyButton = UIFactory.createButton("Edit Vocabulary",
                                "data/resources/pictures/edit.png",
                                40, UIConstants.BLUE, 70, 500, true, true, true, UIConstants.PROMPT_FONT);
                this.startButton = UIFactory.createButton("START LEARNING", "data/resources/pictures/start.png",
                                70, UIConstants.GREEN, 140, 500, true, true, true, UIConstants.TITLE_FONT);
                this.resetStatisticsButton = UIFactory.createButton("Reset Statistics", "data/resources/pictures/reset.png",
                                20, UIConstants.RED, 30, 180, true, true, true, UIConstants.FONT);
                this.configureProfileButton = UIFactory.createButton("Settings",
                                "data/resources/pictures/settings.png",
                                30, UIConstants.AMBER, 50, 230, true, true, true, UIConstants.FONT);
                this.switchProfileButton = UIFactory.createButton("Switch Profile", "data/resources/pictures/profile.png",
                                25, UIConstants.TEAL, 50, 230, true, true, true, UIConstants.FONT);
                buildLayout();
                ButtonActionRegister();
        }

        public void buildLayout() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                JPanel titlePanel = UIFactory.createPanel(new BorderLayout());
                titlePanel.add(UIFactory.createLabel(
                                "WELCOME BACK, " + this.controller.getCurrentProfile().getUserName() + "!",
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

                leftPanel.add(UIFactory.createLabel("YOUR STATISTICS", UIConstants.TITLE_FONT));

                JPanel statisticsPanel = UIFactory.createPanel(new GridLayout(2, 2, 15, 15));
                statisticsPanel.setMaximumSize(new Dimension(500, 250));
                statisticsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                UIFactory.brighter(statisticsPanel);

                Statistics dashboardStatistics = this.controller.getDashboardStatistics();
                statisticsPanel.add(createStatisticPanel("Mastered", dashboardStatistics.getMasteredItems() + " Words",
                                "data/resources/pictures/star.png"));
                statisticsPanel.add(createStatisticPanel("Accuracy",
                                String.format("%.1f%%", dashboardStatistics.getAccuracyPercent()),
                                "data/resources/pictures/graph.png"));
                statisticsPanel.add(createStatisticPanel("Streak", dashboardStatistics.getCurrentStreak() + " Days",
                                "data/resources/pictures/streak.png"));
                statisticsPanel.add(createStatisticPanel("Time Study",
                                formatStudyTime(dashboardStatistics.getTotalStudyTime()),
                                "data/resources/pictures/clock.png"));
                leftPanel.add(statisticsPanel);

                leftPanel.add(Box.createVerticalStrut(10));
                leftPanel.add(UIFactory.createLabel("Account Management", UIConstants.TITLE_FONT));
                leftPanel.add(Box.createVerticalStrut(10));

                JPanel managementPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
                UIFactory.brighter(managementPanel);
                managementPanel.setMaximumSize(new Dimension(500, 70));
                managementPanel.setPreferredSize(new Dimension(500, 70));
                managementPanel.add(this.switchProfileButton);
                managementPanel.add(this.configureProfileButton);
                leftPanel.add(managementPanel);
                leftPanel.add(Box.createVerticalStrut(20));
                leftPanel.add(this.resetStatisticsButton);
                leftPanel.add(Box.createVerticalStrut(20));
                UIFactory.highlight(leftPanel);
                return leftPanel;
        }

        private JPanel createStatisticPanel(String title, String text, String iconPath) {
                JPanel statisticPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
                UIFactory.brighter(statisticPanel);
                ImageIcon icon = UIFactory.loadIcon(iconPath);
                Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);

                JLabel iconLabel = UIFactory.createLabel("", UIConstants.PROMPT_FONT);
                iconLabel.setIcon(new ImageIcon(scaledImage));

                JPanel textPanel = UIFactory.createPanel();
                UIFactory.brighter(textPanel);
                UIFactory.brighter(textPanel);
                textPanel.add(UIFactory.createLabel(title, UIConstants.FONT));
                textPanel.add(UIFactory.createLabel(text, UIConstants.FONT));
                statisticPanel.add(iconLabel);
                statisticPanel.add(textPanel);

                UIFactory.brighter(statisticPanel);
                // UIFactory.brightPanel(textPanel);
                return statisticPanel;
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
                this.resetStatisticsButton.addActionListener(e -> this.controller.resetStatistics());
                this.switchProfileButton.addActionListener(e -> this.controller.showStartPanel());
                this.configureProfileButton.addActionListener(e -> this.controller.showConfigureProfilePanel());
        }
}

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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;
import java.awt.Image;

public final class ProfileDashboardPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private static final int EDIT_BUTTON_ICON_SIZE = 40;
        private static final int EDIT_BUTTON_HEIGHT = 70;
        private static final int START_BUTTON_ICON_SIZE = 70;
        private static final int START_BUTTON_HEIGHT = 140;
        private static final int DASHBOARD_CONTENT_WIDTH = 500;
        private static final int RESET_BUTTON_ICON_SIZE = 20;
        private static final int RESET_BUTTON_HEIGHT = 30;
        private static final int RESET_BUTTON_WIDTH = 180;
        private static final int SETTINGS_BUTTON_ICON_SIZE = 30;
        private static final int PROFILE_ACTION_BUTTON_HEIGHT = 50;
        private static final int PROFILE_ACTION_BUTTON_WIDTH = 230;
        private static final int SWITCH_PROFILE_BUTTON_ICON_SIZE = 25;
        private static final int TITLE_PANEL_HEIGHT = 130;
        private static final int LEFT_PANEL_TOP_GAP = 40;
        private static final int STATISTICS_PANEL_HEIGHT = 250;
        private static final int SECTION_GAP = 10;
        private static final int MANAGEMENT_PANEL_HEIGHT = 70;
        private static final int STATISTIC_HORIZONTAL_GAP = 25;
        private static final int STATISTIC_ICON_SIZE = 60;
        private static final int START_BUTTON_TOP_GAP = 110;
        private static final int EDIT_BUTTON_TOP_GAP = 90;
        private static final int EDIT_BUTTON_BOTTOM_GAP = 30;
        private static final int SECONDS_PER_HOUR = 3600;
        private static final int SECONDS_PER_MINUTE = 60;
        private final transient Controller controller;
        private final JButton switchProfileButton;
        private final JButton startButton;
        private final JButton editVocabularyButton;
        private final JButton resetStatisticsButton;
        private final JButton configureProfileButton;

        @SuppressFBWarnings(value = "EI2", justification = "The panel intentionally shares the app controller.")
        public ProfileDashboardPanel(final Controller controller) {
                this.controller = controller;
                this.editVocabularyButton = UIFactory.createButton("Edit Vocabulary",
                                "data/resources/pictures/edit.png",
                                EDIT_BUTTON_ICON_SIZE, UIConstants.BLUE, EDIT_BUTTON_HEIGHT, DASHBOARD_CONTENT_WIDTH,
                                true, true, true, UIConstants.PROMPT_FONT);
                this.startButton = UIFactory.createButton("START LEARNING", "data/resources/pictures/start.png",
                                START_BUTTON_ICON_SIZE, UIConstants.GREEN, START_BUTTON_HEIGHT, DASHBOARD_CONTENT_WIDTH,
                                true, true, true, UIConstants.TITLE_FONT);
                this.resetStatisticsButton = UIFactory.createButton("Reset Statistics", "data/resources/pictures/reset.png",
                                RESET_BUTTON_ICON_SIZE, UIConstants.RED, RESET_BUTTON_HEIGHT, RESET_BUTTON_WIDTH,
                                true, true, true, UIConstants.FONT);
                this.configureProfileButton = UIFactory.createButton("Settings",
                                "data/resources/pictures/settings.png",
                                SETTINGS_BUTTON_ICON_SIZE, UIConstants.AMBER, PROFILE_ACTION_BUTTON_HEIGHT,
                                PROFILE_ACTION_BUTTON_WIDTH, true, true, true, UIConstants.FONT);
                this.switchProfileButton = UIFactory.createButton("Switch Profile", "data/resources/pictures/profile.png",
                                SWITCH_PROFILE_BUTTON_ICON_SIZE, UIConstants.TEAL, PROFILE_ACTION_BUTTON_HEIGHT,
                                PROFILE_ACTION_BUTTON_WIDTH, true, true, true, UIConstants.FONT);
                buildLayout();
                buttonActionRegister();
        }

        private void buildLayout() {

                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                final JPanel titlePanel = UIFactory.createPanel(new BorderLayout());
                titlePanel.add(UIFactory.createLabel(
                                "WELCOME BACK, " + this.controller.getCurrentProfile().getUserName() + "!",
                                UIConstants.TITLE_FONT));
                titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, TITLE_PANEL_HEIGHT));
                titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, TITLE_PANEL_HEIGHT));
                add(titlePanel);

                final JPanel centerPanel = UIFactory.createPanel(new GridLayout(1, 2));
                centerPanel.add(leftPanel());
                centerPanel.add(rightPanel());
                add(centerPanel);

        }

        private JPanel leftPanel() {

                final JPanel leftPanel = UIFactory.createPanel();
                UIFactory.brighter(leftPanel);

                leftPanel.add(Box.createVerticalStrut(LEFT_PANEL_TOP_GAP));

                leftPanel.add(UIFactory.createLabel("YOUR STATISTICS", UIConstants.TITLE_FONT));

                final JPanel statisticsPanel = UIFactory.createPanel(
                                new GridLayout(2, 2, UIConstants.SPACING_MEDIUM, UIConstants.SPACING_MEDIUM));
                statisticsPanel.setMaximumSize(new Dimension(DASHBOARD_CONTENT_WIDTH, STATISTICS_PANEL_HEIGHT));
                statisticsPanel.setBorder(new EmptyBorder(UIConstants.SPACING_MEDIUM, UIConstants.SPACING_MEDIUM,
                                UIConstants.SPACING_MEDIUM, UIConstants.SPACING_MEDIUM));
                UIFactory.brighter(statisticsPanel);

                final Statistics dashboardStatistics = this.controller.getDashboardStatistics();
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

                leftPanel.add(Box.createVerticalStrut(SECTION_GAP));
                leftPanel.add(UIFactory.createLabel("Account Management", UIConstants.TITLE_FONT));
                leftPanel.add(Box.createVerticalStrut(SECTION_GAP));

                final JPanel managementPanel = UIFactory.createPanel(
                                new FlowLayout(FlowLayout.CENTER, UIConstants.SPACING_MEDIUM, SECTION_GAP));
                UIFactory.brighter(managementPanel);
                managementPanel.setMaximumSize(new Dimension(DASHBOARD_CONTENT_WIDTH, MANAGEMENT_PANEL_HEIGHT));
                managementPanel.setPreferredSize(new Dimension(DASHBOARD_CONTENT_WIDTH, MANAGEMENT_PANEL_HEIGHT));
                managementPanel.add(this.switchProfileButton);
                managementPanel.add(this.configureProfileButton);
                leftPanel.add(managementPanel);
                leftPanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));
                leftPanel.add(this.resetStatisticsButton);
                leftPanel.add(Box.createVerticalStrut(UIConstants.SPACING_LARGE));
                UIFactory.highlight(leftPanel);
                return leftPanel;
        }

        private JPanel createStatisticPanel(final String title, final String text, final String iconPath) {
                final JPanel statisticPanel = UIFactory.createPanel(
                                new FlowLayout(FlowLayout.LEFT, STATISTIC_HORIZONTAL_GAP, UIConstants.SPACING_LARGE));
                UIFactory.brighter(statisticPanel);
                final ImageIcon icon = UIFactory.loadIcon(iconPath);
                final Image scaledImage = icon.getImage().getScaledInstance(
                                STATISTIC_ICON_SIZE, STATISTIC_ICON_SIZE, Image.SCALE_SMOOTH);

                final JLabel iconLabel = UIFactory.createLabel("", UIConstants.PROMPT_FONT);
                iconLabel.setIcon(new ImageIcon(scaledImage));

                final JPanel textPanel = UIFactory.createPanel();
                UIFactory.brighter(textPanel);
                UIFactory.brighter(textPanel);
                textPanel.add(UIFactory.createLabel(title, UIConstants.FONT));
                textPanel.add(UIFactory.createLabel(text, UIConstants.FONT));
                statisticPanel.add(iconLabel);
                statisticPanel.add(textPanel);

                UIFactory.brighter(statisticPanel);
                return statisticPanel;
        }

        private JPanel rightPanel() {
                final JPanel rightPanel = UIFactory.createPanel();
                rightPanel.add(Box.createVerticalStrut(START_BUTTON_TOP_GAP));
                rightPanel.add(this.startButton);
                rightPanel.add(Box.createVerticalStrut(EDIT_BUTTON_TOP_GAP));
                rightPanel.add(this.editVocabularyButton);
                rightPanel.add(Box.createVerticalStrut(EDIT_BUTTON_BOTTOM_GAP));
                UIFactory.brighter(rightPanel);
                UIFactory.highlight(rightPanel);
                return rightPanel;
        }

        private String formatStudyTime(final long totalStudyTime) {
                final long hours = totalStudyTime / SECONDS_PER_HOUR;
                final long minutes = totalStudyTime % SECONDS_PER_HOUR / SECONDS_PER_MINUTE;
                return String.format("%02d:%02d", hours, minutes);
        }

        private void buttonActionRegister() {
                this.editVocabularyButton.addActionListener(e -> this.controller.showVocabularyEditorPanel());
                this.startButton.addActionListener(e -> this.controller.showLearningPanel());
                this.resetStatisticsButton.addActionListener(e -> this.controller.resetStatistics());
                this.switchProfileButton.addActionListener(e -> this.controller.showStartPanel());
                this.configureProfileButton.addActionListener(e -> this.controller.showConfigureProfilePanel());
        }
}

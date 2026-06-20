package it.unibo.vocago.view.panels;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;

public final class LearningPanel extends JPanel{

    private static final long serialVersionUID = 1L;
    private static final int HEADER_HEIGHT = 60;
    private static final int ANSWER_PANEL_HEIGHT = 120;
    private static final int BUTTON_PANEL_HEIGHT = 50;
    private static final int LANGUAGE_PANEL_HEIGHT = 260;

    private final Controller controller;
    private final JButton nextWordButton;
    private final JButton revealAnswerButton;
    private final JButton goBackButton;
    private JButton switchLanguageButton;
    private final JLabel answerLabel;
    private final JTextField userAnswer;
    private final JPanel answerPanel;
    private final JLabel timerLabel;
    private Timer timer;
    private final long startTime;

    @SuppressFBWarnings(value = "EI2", justification = "The panel intentionally shares the app controller.")
    public LearningPanel(final Controller controller) {

        
        
        this.controller = controller;
        this.startTime = controller.getLearningStartTime();
        UIFactory.stylePanel(this);

        this.userAnswer = UIFactory.createTextField(UIConstants.PROMPT_FONT);
        this.answerLabel = UIFactory.createLabel("Press Enter to check", UIConstants.PROMPT_FONT);
        this.timerLabel = UIFactory.createLabel("00:00", UIConstants.TITLE_FONT);
        this.answerPanel = UIFactory.createPanel(new GridBagLayout());

        this.switchLanguageButton = UIFactory.createButton(
                "SWITCH LANGUAGE",
                "data/resources/pictures/arrow.png",
                40,
                UIConstants.BACKGROUND,
                100,
                160,
                false,
                true,
                true,
                UIConstants.FONT);

        this.nextWordButton = UIFactory.createButton(
                "SKIP TO NEXT WORD",
                "",
                0,
                UIConstants.BUTTON_BACKGROUND,
                100,
                150,
                true,
                false,
                true,
                UIConstants.FONT);

        this.revealAnswerButton = UIFactory.createButton(
                "REVEAL ANSWER",
                "",
                0,
                UIConstants.BUTTON_BACKGROUND,
                100,
                150,
                true,
                false,
                true,
                UIConstants.FONT);

        this.goBackButton = UIFactory.createButton(
                "",
                "data/resources/pictures/back.png",
                60,
                UIConstants.BACKGROUND,
                180,
                70,
                false,
                true,
                true,
                UIConstants.FONT);

        buildLayout();
        actionRegister();
        startTimer();
        SwingUtilities.invokeLater(() -> this.userAnswer.requestFocusInWindow());
    }
    
    private void buildLayout() {
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        final JPanel welcomPanel = UIFactory.createPanel(new GridLayout());
        welcomPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));

        final JPanel leftPanel = UIFactory.createPanel(new BorderLayout());
        leftPanel.add(this.goBackButton, BorderLayout.WEST);
        welcomPanel.add(leftPanel);

        final int dailyGoal = this.controller.getDailyGoal();
        if (dailyGoal > this.controller.getCorrectAnsweredQuestions()) {
            welcomPanel.add(UIFactory.createLabel(
                    "WORD " + this.controller.getCorrectAnsweredQuestions() + " OUT OF " + dailyGoal,
                    UIConstants.TITLE_FONT), BorderLayout.CENTER);
        } else {
            welcomPanel.add(UIFactory.createLabel("GOOD JOB!", UIConstants.TITLE_FONT), BorderLayout.CENTER);
        }

        final JPanel rightPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        rightPanel.add(this.timerLabel);
        welcomPanel.add(rightPanel, BorderLayout.EAST);
        return welcomPanel;
    }
    
    private JPanel createMainPanel() {
        final JPanel mainPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));

        final JPanel firstLanguagePanel = UIFactory.createPanel(new BorderLayout());
        firstLanguagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));
        firstLanguagePanel.setPreferredSize(new Dimension(500, 200));
        UIFactory.highlight(firstLanguagePanel);

        final JPanel secondLanguagePanel = UIFactory.createPanel(new BorderLayout());
        secondLanguagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));
        secondLanguagePanel.setPreferredSize(new Dimension(500, 200));
        UIFactory.highlight(secondLanguagePanel);

        firstLanguagePanel.add(UIFactory.createLabel(this.controller.getCurrentProfile().getFirstLanguage(),
                UIConstants.PROMPT_FONT), BorderLayout.NORTH);
        secondLanguagePanel.add(UIFactory.createLabel(this.controller.getCurrentProfile().getSecondLanguage(),
                UIConstants.PROMPT_FONT), BorderLayout.NORTH);

        final JPanel textFieldPanel = UIFactory.createPanel(new GridBagLayout());
        UIFactory.brighter(textFieldPanel);
        textFieldPanel.add(this.userAnswer);

        if (this.controller.getDirection() == Direction.FIRST_TO_SECOND) {
            final JPanel labelPanel = UIFactory.createPanel(new GridLayout());
            labelPanel.add(UIFactory.createLabel(this.controller.getNextQuestion(), UIConstants.BIG_PROMT_FONT));
            UIFactory.brighter(labelPanel);
            firstLanguagePanel.add(labelPanel);
            secondLanguagePanel.add(textFieldPanel);
            this.switchLanguageButton = UIFactory.createButton("Switch Language", "data/resources/pictures/arrow.png", 40,
                    UIConstants.BACKGROUND, 100, 160, false, true, true, UIConstants.FONT);
        } else {
            firstLanguagePanel.add(textFieldPanel);
            final JPanel labelPanel = UIFactory.createPanel(new GridLayout());
            labelPanel.add(UIFactory.createLabel(this.controller.getNextQuestion(), UIConstants.BIG_PROMT_FONT));
            UIFactory.brighter(labelPanel);
            secondLanguagePanel.add(labelPanel);
            this.switchLanguageButton = UIFactory.createButton("Switch Language", "data/resources/pictures/back.png", 40,
                    UIConstants.BACKGROUND, 100, 160, false, true, true, UIConstants.FONT);
        }
        this.switchLanguageButton.setHorizontalTextPosition(SwingConstants.CENTER);
        this.switchLanguageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        mainPanel.add(firstLanguagePanel);
        mainPanel.add(this.switchLanguageButton);
        mainPanel.add(secondLanguagePanel);

        this.answerPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, ANSWER_PANEL_HEIGHT));
        this.answerPanel.add(this.answerLabel);
        final JPanel centerPanel = UIFactory.createPanel();
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(mainPanel);
        centerPanel.add(this.answerPanel);
        return centerPanel;
    }
    
    private JPanel createButtonsPanel() {
        final JPanel buttonsPanel = UIFactory.createPanel(new GridLayout());
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_PANEL_HEIGHT));
        buttonsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, BUTTON_PANEL_HEIGHT));
        UIFactory.highlight(buttonsPanel);
        this.revealAnswerButton.setBorderPainted(true);
        this.nextWordButton.setBorderPainted(true);
        buttonsPanel.add(this.revealAnswerButton);
        buttonsPanel.add(this.nextWordButton);
        return buttonsPanel;
    }
    
    private void actionRegister() {
        this.goBackButton.addActionListener(e -> this.controller.showProfileDashboardPanel());
        this.userAnswer.addActionListener(e -> checkAnswer());
        this.revealAnswerButton.addActionListener(e -> {
            this.controller.evaluateAnswer("");
            showFeedback(UIConstants.BLUE, "The correct answer is: " + this.controller.getCorrectAnswer());
            this.controller.saveVocabulary(this.controller.getCurrentProfile().getVocabulary());
        });
        this.nextWordButton.addActionListener(e -> this.controller.showLearningPanel());
        this.switchLanguageButton.addActionListener(e -> {
            this.controller.switchDirection();
            this.controller.showLearningPanel();
        });
    }

    private void checkAnswer() {
        if (this.controller.currentQuestionEvaluated()) {
            this.controller.showLearningPanel();
            return;
        }

        final String answer = this.userAnswer.getText().trim();
        if (answer.isEmpty()) {
            showFeedback(UIConstants.BLUE, "Please enter an answer first!");
            return;
        }

        if (this.controller.evaluateAnswer(answer)) {
            showFeedback(UIConstants.GREEN, "Correct! Press Enter for the next word.");
        } else {
            showFeedback(UIConstants.RED, "the correct answer is: (" + this.controller.getCorrectAnswer() + "), Press Enter for the next word." );
        }
        this.controller.saveVocabulary(this.controller.getCurrentProfile().getVocabulary());
        this.controller.dailyGoalAchieved();
    }

    private void startTimer() {
        updateTimer();
        this.timer = new Timer(1000, e -> updateTimer());
        this.timer.start();
    }

    private void updateTimer() {
        final long elapsedSeconds = (System.currentTimeMillis() - this.startTime) / 1000;
        final long minutes = elapsedSeconds / 60;
        final long seconds = elapsedSeconds % 60;
        this.timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void showFeedback(final Color color, final String text) {
        this.answerPanel.setBackground(color);
        this.answerLabel.setText(text);
    }

    @Override
    public void removeNotify() {
        if (this.timer != null) {
            this.timer.stop();
        }
        super.removeNotify();
    }
}


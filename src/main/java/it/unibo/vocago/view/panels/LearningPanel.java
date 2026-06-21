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
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Panel for a learning session, showing the current question and collecting the
 * user's answer, with controls to reveal the answer, skip, and switch
 * direction.
 */
public final class LearningPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int HEADER_HEIGHT = 60;
    private static final int ANSWER_PANEL_HEIGHT = 120;
    private static final int BUTTON_PANEL_HEIGHT = 50;
    private static final int LANGUAGE_PANEL_HEIGHT = 260;
    private static final int SWITCH_LANGUAGE_ICON_SIZE = 40;
    private static final int SWITCH_LANGUAGE_BUTTON_WIDTH = 160;
    private static final int LEARNING_ACTION_BUTTON_HEIGHT = 100;
    private static final int ACTION_BUTTON_WIDTH = 150;
    private static final int LEARNING_BACK_BUTTON_HEIGHT = 180;
    private static final int LANGUAGE_CARD_WIDTH = 500;
    private static final int LANGUAGE_CARD_HEIGHT = 200;
    private static final int CONTENT_GAP = 5;
    private static final int MAIN_PANEL_TOP_SPACING = 60;
    private static final int TIMER_PANEL_GAP = 12;
    private static final int TIMER_DELAY_MILLISECONDS = 1000;
    private static final int SECONDS_PER_MINUTE = 60;

    private final transient Controller controller;
    private final JLabel answerLabel;
    private final JTextField userAnswer;
    private final JPanel answerPanel;
    private final JLabel timerLabel;
    private final long startTime;
    private final JButton nextWordButton;
    private final JButton revealAnswerButton;
    private final JButton goBackButton;
    private JButton switchLanguageButton;
    private Timer timer;

    /**
     * Creates the panel, wiring it to the given controller.
     *
     * @param controller the controller user actions are forwarded to
     */
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
                "pictures/arrow.png",
                SWITCH_LANGUAGE_ICON_SIZE,
                UIConstants.BACKGROUND,
                LEARNING_ACTION_BUTTON_HEIGHT,
                SWITCH_LANGUAGE_BUTTON_WIDTH,
                false,
                true,
                true,
                UIConstants.FONT);

        this.nextWordButton = UIFactory.createButton(
                "SKIP TO NEXT WORD",
                "",
                0,
                UIConstants.BUTTON_BACKGROUND,
                LEARNING_ACTION_BUTTON_HEIGHT,
                ACTION_BUTTON_WIDTH,
                true,
                false,
                true,
                UIConstants.FONT);

        this.revealAnswerButton = UIFactory.createButton(
                "REVEAL ANSWER",
                "",
                0,
                UIConstants.BUTTON_BACKGROUND,
                LEARNING_ACTION_BUTTON_HEIGHT,
                ACTION_BUTTON_WIDTH,
                true,
                false,
                true,
                UIConstants.FONT);

        this.goBackButton = UIFactory.createButton(
                "",
                "pictures/back.png",
                UIConstants.BACK_BUTTON_ICON_SIZE,
                UIConstants.BACKGROUND,
                LEARNING_BACK_BUTTON_HEIGHT,
                UIConstants.BACK_BUTTON_WIDTH,
                false,
                true,
                true,
                UIConstants.FONT);

        buildLayout();
        actionRegister();
        startTimer();
        SwingUtilities.invokeLater(this.userAnswer::requestFocusInWindow);
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

        final JPanel rightPanel = UIFactory.createPanel(
                new FlowLayout(FlowLayout.RIGHT, TIMER_PANEL_GAP, TIMER_PANEL_GAP));
        rightPanel.add(this.timerLabel);
        welcomPanel.add(rightPanel, BorderLayout.EAST);
        return welcomPanel;
    }

    private JPanel createMainPanel() {
        final JPanel mainPanel = UIFactory.createPanel(new FlowLayout(FlowLayout.CENTER, CONTENT_GAP, CONTENT_GAP));
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));

        final JPanel firstLanguagePanel = UIFactory.createPanel(new BorderLayout());
        firstLanguagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));
        firstLanguagePanel.setPreferredSize(new Dimension(LANGUAGE_CARD_WIDTH, LANGUAGE_CARD_HEIGHT));
        UIFactory.highlight(firstLanguagePanel);

        final JPanel secondLanguagePanel = UIFactory.createPanel(new BorderLayout());
        secondLanguagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, LANGUAGE_PANEL_HEIGHT));
        secondLanguagePanel.setPreferredSize(new Dimension(LANGUAGE_CARD_WIDTH, LANGUAGE_CARD_HEIGHT));
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
            this.switchLanguageButton = UIFactory.createButton("Switch Language", "pictures/arrow.png",
                    SWITCH_LANGUAGE_ICON_SIZE, UIConstants.BACKGROUND, LEARNING_ACTION_BUTTON_HEIGHT,
                    SWITCH_LANGUAGE_BUTTON_WIDTH, false, true, true, UIConstants.FONT);
        } else {
            firstLanguagePanel.add(textFieldPanel);
            final JPanel labelPanel = UIFactory.createPanel(new GridLayout());
            labelPanel.add(UIFactory.createLabel(this.controller.getNextQuestion(), UIConstants.BIG_PROMT_FONT));
            UIFactory.brighter(labelPanel);
            secondLanguagePanel.add(labelPanel);
            this.switchLanguageButton = UIFactory.createButton("Switch Language", "pictures/back.png",
                    SWITCH_LANGUAGE_ICON_SIZE, UIConstants.BACKGROUND, LEARNING_ACTION_BUTTON_HEIGHT,
                    SWITCH_LANGUAGE_BUTTON_WIDTH, false, true, true, UIConstants.FONT);
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
        centerPanel.add(Box.createVerticalStrut(MAIN_PANEL_TOP_SPACING));
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
            showFeedback(UIConstants.RED,
                    "the correct answer is: (" + this.controller.getCorrectAnswer() + "), Press Enter for the next word.");
        }
        this.controller.saveVocabulary(this.controller.getCurrentProfile().getVocabulary());
        this.controller.dailyGoalAchieved();
    }

    private void startTimer() {
        updateTimer();
        this.timer = new Timer(TIMER_DELAY_MILLISECONDS, e -> updateTimer());
        this.timer.start();
    }

    private void updateTimer() {
        final long elapsedSeconds = (System.currentTimeMillis() - this.startTime) / TIMER_DELAY_MILLISECONDS;
        final long minutes = elapsedSeconds / SECONDS_PER_MINUTE;
        final long seconds = elapsedSeconds % SECONDS_PER_MINUTE;
        this.timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void showFeedback(final Color color, final String text) {
        this.answerPanel.setBackground(color);
        this.answerLabel.setText(text);
    }

    /**
     * Stops the session timer when the panel is removed from the window.
     */
    @Override
    public void removeNotify() {
        if (this.timer != null) {
            this.timer.stop();
        }
        super.removeNotify();
    }
}

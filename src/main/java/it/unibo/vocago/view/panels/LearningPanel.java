package it.unibo.vocago.view.panels;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.view.panels.api.PanelLayout;
import it.unibo.vocago.view.util.UIConstants;
import it.unibo.vocago.view.util.UIFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class LearningPanel extends JPanel implements PanelLayout {

    private final Controller controller;

    private final JButton nextWordButton;
    private final JButton showAnswerButton;
    private final JButton goBackButton;
    private JButton switchLanguageButton;
    private JLabel answerLabel;
    private JTextField userAnswer;
    private JPanel answerPanel;
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;

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
                "data/pictures/arrow.png",
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

        this.showAnswerButton = UIFactory.createButton(
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
                "data/pictures/back.png",
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
    }
    
    @Override
    public void buildLayout() {
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {

    }
    
    private JPanel createMainPanel() {

    }
    
    private JPanel createButtonsPanel() {

    }
    
    public void actionRegister() {
        this.goBackButton.addActionListener(e -> {
            this.controller.saveLearningStats();// should update the timer
            this.controller.saveVocabulary(this.controller.getCurrentUser().getVocabulary());
            this.controller.showUserDashboardPanel();
        });

        this.switchLanguageButton.addActionListener(e -> {
            this.controller.switchDirection();
            this.controller.showLearningPanel();
        });

        this.userAnswer.addActionListener(e -> {
            if (this.userAnswer.getText().isEmpty()) {
                this.answerPanel.setBackground(UIConstants.BLUE);
                this.answerLabel.setText("Please enter an answer first!");
            } else {
                // should skip to the next question if correct
            }

        });

        this.showAnswerButton.addActionListener(e -> {
            this.answerPanel.setBackground(UIConstants.BLUE);
            this.answerLabel.setText("the correct answer is: " + this.controller.getCorrectAnswer());
        });

        this.nextWordButton.addActionListener(e -> {
            this.controller.showLearningPanel();
        });
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


    
}


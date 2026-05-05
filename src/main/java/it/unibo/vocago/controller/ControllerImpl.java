package it.unibo.vocago.controller;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.logic.learning.api.LearningSession;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.view.AppFrame;

public class ControllerImpl implements Controller {

    final private AppFrame appFrame;
    private User user;
    private LearningSession learningSession;

    public ControllerImpl() {
        this.appFrame = new AppFrame(this);
        this.user = null;
        this.learningSession = null;
    }

    public void showStartPanel() {
        this.appFrame.showStartPanel();
    }
}

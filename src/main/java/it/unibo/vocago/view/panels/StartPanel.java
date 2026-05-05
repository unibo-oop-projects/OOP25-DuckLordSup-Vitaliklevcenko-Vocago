package it.unibo.vocago.view.panels;

import javax.swing.JPanel;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;

public class StartPanel extends JPanel implements PanelLayout {

    final private Controller controller;
    public StartPanel(final Controller controller) {
        this.controller = controller;
        buildLayout();
    }
    
    @Override
    public void buildLayout() {

    }

    @Override
    public void actionRegister() {

    }
}

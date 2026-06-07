package it.unibo.vocago.view.panels;

import javax.swing.JPanel;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;

public class UserDashboardPanel extends JPanel implements PanelLayout {
    
    private final Controller controller;
    public UserDashboardPanel(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public void buildLayout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildLayout'");
    }

    
    public void actionRegister() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionRegister'");
    }
}

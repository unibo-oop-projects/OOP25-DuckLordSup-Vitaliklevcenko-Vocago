package it.unibo.vocago.view.panels;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;

public class CreateNewUserPanel implements PanelLayout {
    
    final private Controller controller;
    public CreateNewUserPanel(final Controller controller) {
        this.controller = controller;
        buildLayout();
    }
    @Override
    public void buildLayout() {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'buildLayout'");
    }

    @Override
    public void actionRegister() {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'actionRegister'");
    }

}

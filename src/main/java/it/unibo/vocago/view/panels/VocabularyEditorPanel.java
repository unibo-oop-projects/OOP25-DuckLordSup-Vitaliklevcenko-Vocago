package it.unibo.vocago.view.panels;

import javax.swing.JPanel;

import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.api.PanelLayout;

public class VocabularyEditorPanel extends JPanel implements PanelLayout {
    
    private final Controller controller;

    public VocabularyEditorPanel(final Controller controller) {

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

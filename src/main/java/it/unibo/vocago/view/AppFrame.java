package it.unibo.vocago.view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import it.unibo.vocago.controller.api.Controller;
import it.unibo.vocago.view.panels.StartPanel;

public class AppFrame extends JFrame {
    
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Controller controller;
    private StartPanel startPanel;

    public AppFrame(final Controller controller) {
        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vocago");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        add(this.mainPanel);
        this.setVisible(true);
    }
    
    public void showStartPanel() {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        if (this.startPanel != null) {
            this.mainPanel.remove(this.startPanel);
        }
        this.startPanel = new StartPanel(this.controller);
        showPanel(this.startPanel, "StartPanel");
    }

    private void showPanel(final JPanel panel, final String cardName) {
        this.mainPanel.add(panel, cardName);
        this.cardLayout.show(this.mainPanel, cardName);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }
    
}

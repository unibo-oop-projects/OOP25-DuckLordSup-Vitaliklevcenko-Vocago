package it.unibo.vocago.view.frame;


import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import it.unibo.vocago.controller.api.Controller;

public class AppFrame extends JFrame {
    
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Controller controller;

    public AppFrame(final Controller controller){
        this.controller = controller;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vocago");
        this.setSize(800, 520);
        this.setLocationRelativeTo(null);

        add(this.mainPanel);
        this.setVisible(true);
    }
}

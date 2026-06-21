package it.unibo.vocago;

import it.unibo.vocago.controller.ControllerImpl;

/**
 * Application entry point. Starts Vocago by creating the controller, which in
 * turn builds the view and shows the initial screen.
 */
public final class VocagoApp {

    /**
     * Starts the application.
     *
     * @param args the command-line arguments (unused)
     */
    public static void main(final String[] args) {
        new ControllerImpl();
    }

    private VocagoApp(){}
}

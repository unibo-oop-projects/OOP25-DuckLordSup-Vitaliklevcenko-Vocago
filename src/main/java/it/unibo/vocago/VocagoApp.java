package it.unibo.vocago;

import it.unibo.vocago.controller.ControllerImpl;

public class VocagoApp {

    private VocagoApp() {}
    public static void main(final String[] args){
        new ControllerImpl();
    }
}

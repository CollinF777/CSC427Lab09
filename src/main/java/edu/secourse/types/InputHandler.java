package edu.secourse.types;

import edu.secourse.controllers.AppointmentController;
import edu.secourse.controllers.UserController;

import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * Helper type for main CLI.
 *
 * @author Matt Robinson
 */
public class InputHandler extends HashMap<String, BiConsumer<Pair<UserController, AppointmentController>, String[]>> {
    public InputHandler() {
        super();
    }
}
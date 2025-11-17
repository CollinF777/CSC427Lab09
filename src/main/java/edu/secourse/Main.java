package edu.secourse;

import edu.secourse.controllers.AppointmentController;
import edu.secourse.controllers.UserController;
import edu.secourse.exceptions.AppointmentDoesNotExistException;
import edu.secourse.exceptions.InvalidIdException;
import edu.secourse.models.Appointment;
import edu.secourse.models.User;
import edu.secourse.services.AppointmentService;
import edu.secourse.services.UserService;
import edu.secourse.types.InputHandler;
import edu.secourse.types.Pair;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/**
 * Program entry point. Handles interactions from the Admin. It is
 * assumed that the Admin is already logged in.
 */
public class Main {
    private static InputHandler HANDLERS = new InputHandler();

    /*
    Defines the input handlers for user commands
    */
    static {
        /*
        Handlers for user
         */
        HANDLERS.put("user create", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            User newUser = pair.t().createUser(
                    data[2],
                    data[3],
                    joinStringsAfter(6, data),
                    data[4],
                    data[5]
            );

            System.out.printf("Created user with ID '%d'\n", newUser.getAccNum());
        });

        HANDLERS.put("user delete", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            pair.t().removeUser(Integer.parseInt(data[2]));
        });

        HANDLERS.put("user show", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            User user = pair.t().getUser(Integer.parseInt(data[2]));
            System.out.printf(
                    "ID: %d\nUsername: %s\nName: %s\nRole: %s\nEmail: %s\n",
                    user.getAccNum(),
                    user.getUsername(),
                    user.getName(),
                    user.getRole(),
                    user.getEmail()
            );
        });

        HANDLERS.put("user username", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            if (data.length == 3) {
                System.out.printf(
                        "User '%s' has username '%s'.\n",
                        data[2],
                        pair.t().getUser(Integer.parseInt(data[2])).getUsername()
                );
                return;
            }
            pair.t().updateUsername(Integer.parseInt(data[2]), data[3]);
        });

        HANDLERS.put("user password", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            if (data.length == 3) {
                System.out.printf(
                        "User '%s' has password '%s'.\n",
                        data[2],
                        pair.t().getUser(Integer.parseInt(data[2])).getPassword()
                );
                return;
            }
            pair.t().updatePassword(Integer.parseInt(data[2]), data[3]);
        });

        HANDLERS.put("user name", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            if (data.length == 3) {
                System.out.printf(
                        "User '%s' has name '%s'.\n",
                        data[2],
                        pair.t().getUser(Integer.parseInt(data[2])).getName()
                );
                return;
            }
            pair.t().updateName(Integer.parseInt(data[2]), joinStringsAfter(3, data));
        });

        HANDLERS.put("user email", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            if (data.length == 3) {
                System.out.printf(
                        "User '%s' has email '%s'.\n",
                        data[2],
                        pair.t().getUser(Integer.parseInt(data[2])).getEmail()
                );
                return;
            }
            pair.t().updateEmailAddress(Integer.parseInt(data[2]), data[3]);
        });

        /*
        Handlers for appointment
         */
        HANDLERS.put("appt create", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            Appointment newAppt = pair.k().createAppointment(
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]),
                    new Date(joinStringsAfter(4, data))
            );

            System.out.printf("Created appointment with ID '%d'\n", newAppt.getAppointmentId());
        });

        HANDLERS.put("appt cancel", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            pair.k().cancelAppointment(Integer.parseInt(data[2]));
        });

        HANDLERS.put("appt delete", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            pair.k().deleteAppointment(Integer.parseInt(data[2]));
        });

        HANDLERS.put("appt date", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            pair.k().rescheduleAppointment(Integer.parseInt(data[2]), new Date(joinStringsAfter(3, data)));
        });

        HANDLERS.put("appt show", (Pair<UserController, AppointmentController> pair, String[] data) -> {
            Appointment appt = pair.k().getAppointment(Integer.parseInt(data[2]));
            System.out.printf(
                    "ID: %s\nPatient ID: %d\nDoctor ID: %d\nDate: %s\nStatus: %s\n",
                    appt.getAppointmentId(),
                    appt.getPatient().getAccNum(),
                    appt.getDoctor().getAccNum(),
                    appt.getStartDateTime(),
                    appt.getStatus()
            );
        });
    }

    /**
     * Program entry point
     */
    public static void main(String[] args) {
        // get environment
        Pair<UserController, AppointmentController> environment = generateEnvironment();

        // command loop
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            String[] input = scanner.nextLine().split(" ");

            if ("exit".equals(input[0])) {
                break;
            }

            if (input.length < 2 || !HANDLERS.containsKey(input[0] + " " + input[1])) {
                System.out.println("Invalid request.");
                continue;
            }

            try {
                HANDLERS.get(input[0] + " " + input[1]).accept(environment, input);
                System.out.println("\n*Operation OK*");
            } catch (Exception ex) {
                if (ex instanceof InvalidIdException || ex instanceof AppointmentDoesNotExistException) {
                    System.out.println("An error occurred while executing your command:\n" + ex.getMessage());
                }
                System.out.println("The application failed to execute your command. Check your query and ensure it is formatted correctly.");
            }
        }
    }

    /**
     * Joins an array of strings after a given index
     * @param index The index to start joining
     * @param strings The array of strings
     * @return The strings at and after index, concatenated with " "
     */
    private static String joinStringsAfter(int index, String[] strings) {
        return String.join(" ", Arrays.copyOfRange(strings, index, strings.length));
    }

    /**
     * Generates a test environment
     * @return The UserController and AppointmentController as a Pair
     */
    private static Pair<UserController, AppointmentController> generateEnvironment() {
        UserService userService = new UserService();
        AppointmentService appointmentService = new AppointmentService();

        return new Pair<>(
                new UserController(userService),
                new AppointmentController(appointmentService, userService)
        );
    }
}

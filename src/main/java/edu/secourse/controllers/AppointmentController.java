package edu.secourse.controllers;

import edu.secourse.exceptions.AppointmentDoesNotExistException;
import edu.secourse.exceptions.InvalidIdException;
import edu.secourse.models.Appointment;
import edu.secourse.models.Doctor;
import edu.secourse.models.Patient;
import edu.secourse.models.User;
import edu.secourse.services.AppointmentService;
import edu.secourse.services.UserService;

import java.util.Date;

/**
 * Handles user input operations for appointments and passes them into AppointmentService.
 *
 * @author Matt Robinson
 */
public class AppointmentController {
    private AppointmentService appointmentService;
    private UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    public Appointment createAppointment(int patientId, int doctorId, Date startTime) {
        // check that patient exists
        User patient = userService.getUser(patientId);
        if (!(patient instanceof Patient)) {
            throw new InvalidIdException(String.format(
                    "User with ID %d either doesn't exist or isn't a patient.",
                    patientId
            ));
        }

        // check that doctor exists
        User doctor = userService.getUser(doctorId);
        if (!(doctor instanceof Doctor)) {
            throw new InvalidIdException(String.format(
                    "User with ID %d either doesn't exist or isn't a doctor.",
                    doctorId
            ));
        }

        // create appointment
        return appointmentService.createAppointment(
                (Patient) patient, (Doctor) doctor, startTime, Appointment.Status.ACTIVE);
    }

    public void cancelAppointment(int appointmentId) {
        // check that appointment exists
        Appointment appt = appointmentService.getAppointment(appointmentId);
        if (appt == null) {
            throw new InvalidIdException(String.format(
                    "Appointment with ID %d doesn't exist.",
                    appointmentId
            ));
        }

        // check that appointment isn't cancelled
        if (appt.getStatus() == Appointment.Status.CANCELLED) {
            throw new AppointmentDoesNotExistException(String.format(
                    "Appointment with ID %d was already cancelled or no longer exists.",
                    appointmentId
            ));
        }

        // cancel appointment
        appt.setStatus(Appointment.Status.CANCELLED);
    }

    public void deleteAppointment(int appointmentId) {
        // check that appointment exists
        Appointment appt = appointmentService.getAppointment(appointmentId);
        if (appt == null) {
            throw new InvalidIdException(String.format(
                    "Appointment with ID %d doesn't exist.",
                    appointmentId
            ));
        }

        // delete appointment
        appointmentService.deleteAppointment(appointmentId);
    }

    public void changeAppointmentStartTime(int appointmentId, Date startTime) {
        // check that appointment exists
        Appointment appt = appointmentService.getAppointment(appointmentId);
        if (appt == null) {
            throw new InvalidIdException(String.format(
                    "Appointment with ID %d doesn't exist.",
                    appointmentId
            ));
        }

        // change start time
        appt.setStartDateTime(startTime);
        appointmentService.updateAppointment(appt);
    }
}

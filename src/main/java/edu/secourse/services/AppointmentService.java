package edu.secourse.services;

import java.util.ArrayList;
import edu.secourse.models.Appointment;
import edu.secourse.models.Doctor;
import edu.secourse.models.Patient;
import java.util.Date;

/**
 * Handles CRUD operations for Appointment model, as well as retaining a cache of Appointments.
 *
 * @author Collin Fair
 */
public class AppointmentService {
    private ArrayList<Appointment> appointments;
    private static int aptIdTracker = 0;

    public AppointmentService() {
        appointments = new ArrayList<>();
    }

    /**
     * Creates a user, assigning it a unique aptId, storing the model in the AppointmentService cache, and returning
     * @param patient
     * @param doctor
     * @param startDateTine
     * @param status
     * @return
     */
    public Appointment createAppointment(Patient patient, Doctor doctor, Date startDateTine, Appointment.Status status) {
        int aptId = aptIdTracker++;

        Appointment newApt = new Appointment(aptId, patient, doctor, startDateTine, status);
        appointments.add(newApt);
        return newApt;
    }

    /**
     * Gets an appointment based off the appointments id and returns said appointment
     * @param id
     * @return
     */
    public Appointment getAppointment(int id) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId() == id) {
                return appointments.get(i);
            }
        }
        return null;
    }

    /**
     * Takes in an appointment and matches it with an appointment currently saved in the cache then updates it with
     * the new appointment data
     * @param appointment
     */
    public void updateAppointment (Appointment appointment) {
        for (int i  = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId() == appointment.getAppointmentId()) {
                appointments.set(i, appointment);
                return;
            }
        }

        throw new RuntimeException("Cannot update appointment");
    }

    /**
     * Deletes an appointment from the cache based off the appointments id
     * @param id
     * @return
     */
    public boolean deleteAppointment(int id) {
        Appointment apt = getAppointment(id);

        if (apt == null) {
            return false;
        }

        appointments.remove(apt);

        return true;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
}

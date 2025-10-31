package edu.secourse.services;

import java.util.ArrayList;
import edu.secourse.models.Appointment;
import edu.secourse.models.Doctor;
import edu.secourse.models.Patient;
import java.util.Date;

public class AppointmentService {
    private ArrayList<Appointment> appointments;
    private static int aptIdTracker = 0;

    public AppointmentService() {
        appointments = new ArrayList<>();
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, Date startDateTine, Appointment.Status status) {
        int aptId = aptIdTracker++;

        Appointment newApt = new Appointment(aptId, patient, doctor, startDateTine, status);
        appointments.add(newApt);
        return newApt;
    }

    public Appointment getAppointment(int id) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId() == id) {
                return appointments.get(i);
            }
        }
        return null;
    }
}

package edu.secourse.services;

import edu.secourse.services.AppointmentService;
import edu.secourse.models.Appointment;
import edu.secourse.models.Patient;
import edu.secourse.models.Doctor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {
    AppointmentService aptService;

    @BeforeEach
    void createAptService() {
        aptService = new AppointmentService();

    }

    @Test
    @DisplayName("createAppointment(Patient patient, Doctor doctor, Date startDateTine, Appointment.Status status): Create a new appointment")
    void createAptTest() {
        Patient p1 = new Patient(1, "cfair4", "123", "Collin", "cfair4@brockport.edu");
        Doctor d1 = new Doctor(1, "GMoney527", "123", "Stan", "GMoney527@goat.com");
        Date date = new Date();

        Appointment apt = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);
        Appointment apt2 = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);

        assertAll(
                // Test apt exists
                () -> assertNotNull(apt),
                // Test id matches
                () -> assertEquals(0, apt.getAppointmentId()),
                // Test size of the arraylist to check both apts were added
                () -> assertEquals(2, aptService.getAppointments().size()),
                // Test that apt is in apt service
                () -> assertSame(apt, aptService.getAppointments().get(0)),
                // Test that new apt has increased id
                () -> assertEquals(1, apt2.getAppointmentId())
        );
    }

    @Test
    @DisplayName("getAppointment(int id): Gets appointment from id")
    void getAptTest() {
        Patient p1 = new Patient(1, "cfair4", "123", "Collin", "cfair4@brockport.edu");
        Doctor d1 = new Doctor(1, "GMoney527", "123", "Stan", "GMoney527@goat.com");
        Date date = new Date();

        Appointment apt = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);

        assertAll(
                // Test apt exists
                () -> assertNotNull(apt),
                // Tests apt is gotten from the getAppointment
                () -> assertSame(apt, aptService.getAppointment(apt.getAppointmentId()))
        );
    }

    @Test
    @DisplayName("updateAppointment (Appointment appointment): Update an existing appointment")
    void updateAptTest() {
        Patient p1 = new Patient(1, "cfair4", "123", "Collin", "cfair4@brockport.edu");
        Doctor d1 = new Doctor(1, "GMoney527", "123", "Stan", "GMoney527@goat.com");
        Date date = new Date();

        Appointment apt1 = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);
        Appointment uapt1 = new Appointment(apt1.getAppointmentId(), p1, d1, date, Appointment.Status.CANCELLED);

        Patient p2 = new Patient(1, "updated", "1234", "Ryan", "rfant1@brockport.edu");
        Appointment apt2 = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);
        Appointment uapt2 = new Appointment(apt2.getAppointmentId(), p2, d1, date, Appointment.Status.ACTIVE);

        aptService.updateAppointment(uapt1);
        aptService.updateAppointment(uapt2);

        assertAll(
                // Test updated apt holds correct id
                () -> assertEquals(0, uapt1.getAppointmentId()),
                // Test updated appointments did not change arraylist size
                () -> assertEquals(2, aptService.getAppointments().size()),
                // Check apt1 and apt2 no longer are in the arraylist
                () -> assertNotSame(apt1, aptService.getAppointments().get(0)),
                () -> assertNotSame(apt2, aptService.getAppointments().get(1)),
                // Check updated appointments are in the arraylist
                () -> assertSame(uapt1, aptService.getAppointments().get(0)),
                () -> assertSame(uapt2, aptService.getAppointments().get(1))
        );
    }

    @Test
    @DisplayName("deleteAppointment(int id): Deletes an appointment")
    void deleteAptTest() {
        Patient p1 = new Patient(1, "cfair4", "123", "Collin", "cfair4@brockport.edu");
        Doctor d1 = new Doctor(1, "GMoney527", "123", "Stan", "GMoney527@goat.com");
        Date date = new Date();

        Appointment apt1 = aptService.createAppointment(p1, d1, date, Appointment.Status.ACTIVE);
        aptService.deleteAppointment(apt1.getAppointmentId());

        assertAll(
                // Tests that apt1 exists
                () -> assertNotNull(apt1),
                // Tests that apt was deleted from the service
                () -> assertEquals(0, aptService.getAppointments().size())
        );
    }

}

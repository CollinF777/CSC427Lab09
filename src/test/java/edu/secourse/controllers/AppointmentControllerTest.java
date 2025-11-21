package edu.secourse.controllers;

import edu.secourse.exceptions.AppointmentDoesNotExistException;
import edu.secourse.exceptions.InvalidIdException;
import edu.secourse.models.Appointment;
import edu.secourse.models.Doctor;
import edu.secourse.models.Patient;
import edu.secourse.services.AppointmentService;
import edu.secourse.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentControllerTest {

    private AppointmentController controller;
    private AppointmentService appointmentService;
    private UserService userService;
    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();
        userService = new UserService();
        controller = new AppointmentController(appointmentService, userService);

        // Create sample users via UserService
        patient = (Patient) userService.createUser("patientUser", "pass", "Patient Name", "patient@example.com", "patient");
        doctor = (Doctor) userService.createUser("doctorUser", "pass", "Doctor Name", "doctor@example.com", "doctor");
    }

    @Test
    void testCreateAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        assertNotNull(appt);
        assertEquals(patient, appt.getPatient());
        assertEquals(doctor, appt.getDoctor());
        assertEquals(Appointment.Status.ACTIVE, appt.getStatus());
    }

    @Test
    void testCreateAppointmentInvalidPatient() {
        Doctor fakeDoctor = (Doctor) userService.createUser("fakeDoctor", "pass", "Fake Doc", "fake@example.com", "doctor");

        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.createAppointment(fakeDoctor.getAccNum(), doctor.getAccNum(), new Date()));
        assertTrue(ex.getMessage().contains("either doesn't exist or isn't a patient"));
    }

    @Test
    void testCreateAppointmentInvalidDoctor() {
        Patient fakePatient = (Patient) userService.createUser("fakePatient", "pass", "Fake Patient", "fake@example.com", "patient");

        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.createAppointment(patient.getAccNum(), fakePatient.getAccNum(), new Date()));
        assertTrue(ex.getMessage().contains("either doesn't exist or isn't a doctor"));
    }

    @Test
    void testCancelAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        controller.cancelAppointment(appt.getAppointmentId());

        Appointment updated = appointmentService.getAppointment(appt.getAppointmentId());
        assertEquals(Appointment.Status.CANCELLED, updated.getStatus());
    }

    @Test
    void testCancelAppointmentAlreadyCancelled() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        controller.cancelAppointment(appt.getAppointmentId());

        AppointmentDoesNotExistException ex = assertThrows(AppointmentDoesNotExistException.class,
                () -> controller.cancelAppointment(appt.getAppointmentId()));
        assertTrue(ex.getMessage().contains("was already cancelled"));
    }

    @Test
    void testCancelAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.cancelAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void testDeleteAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        controller.deleteAppointment(appt.getAppointmentId());

        assertNull(appointmentService.getAppointment(appt.getAppointmentId()));
    }

    @Test
    void testDeleteAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.deleteAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void testRescheduleAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        Date newTime = new Date(System.currentTimeMillis() + 3600_000); // +1 hour
        controller.rescheduleAppointment(appt.getAppointmentId(), newTime);

        Appointment updated = appointmentService.getAppointment(appt.getAppointmentId());
        assertEquals(newTime, updated.getStartDateTime());
    }

    @Test
    void testRescheduleAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.rescheduleAppointment(999, new Date()));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void testGetAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        Appointment fetched = controller.getAppointment(appt.getAppointmentId());

        assertEquals(appt, fetched);
    }

    @Test
    void testGetAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.getAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void testMultipleAppointmentsForSamePatientDoctor() {
        Date now = new Date();
        Appointment appt1 = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), now);
        Appointment appt2 = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date(now.getTime() + 3600_000)); // +1 hour

        // Check both appointments exist in service
        assertEquals(appt1, appointmentService.getAppointment(appt1.getAppointmentId()));
        assertEquals(appt2, appointmentService.getAppointment(appt2.getAppointmentId()));

        // Ensure they are distinct
        assertNotEquals(appt1.getAppointmentId(), appt2.getAppointmentId());
        assertNotEquals(appt1.getStartDateTime(), appt2.getStartDateTime());
    }

}

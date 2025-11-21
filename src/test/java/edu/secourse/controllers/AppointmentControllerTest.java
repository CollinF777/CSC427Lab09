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

    /**
     * Sets up a fresh controller, services, and test users before each test.
     * Ensures each test runs in isolation with a clean state.
     */
    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();
        userService = new UserService();
        controller = new AppointmentController(appointmentService, userService);

        // Create a test patient and doctor for use in appointment tests
        patient = (Patient) userService.createUser("patientUser", "pass", "Patient Name", "patient@example.com", "patient");
        doctor = (Doctor) userService.createUser("doctorUser", "pass", "Doctor Name", "doctor@example.com", "doctor");
    }

    // ---------- CREATE APPOINTMENT TESTS ----------

    /**
     * Tests that creating an appointment with valid patient and doctor succeeds
     * and sets the correct appointment status.
     */
    @Test
    void testCreateAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        assertNotNull(appt);
        assertEquals(patient, appt.getPatient());
        assertEquals(doctor, appt.getDoctor());
        assertEquals(Appointment.Status.ACTIVE, appt.getStatus());
    }

    /**
     * Tests creating an appointment with an invalid patient.
     * Expects InvalidIdException since the user is not a patient.
     */
    @Test
    void testCreateAppointmentInvalidPatient() {
        Doctor fakeDoctor = (Doctor) userService.createUser("fakeDoctor", "pass", "Fake Doc", "fake@example.com", "doctor");

        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.createAppointment(fakeDoctor.getAccNum(), doctor.getAccNum(), new Date()));
        assertTrue(ex.getMessage().contains("either doesn't exist or isn't a patient"));
    }

    /**
     * Tests creating an appointment with an invalid doctor.
     * Expects InvalidIdException since the user is not a doctor.
     */
    @Test
    void testCreateAppointmentInvalidDoctor() {
        Patient fakePatient = (Patient) userService.createUser("fakePatient", "pass", "Fake Patient", "fake@example.com", "patient");

        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.createAppointment(patient.getAccNum(), fakePatient.getAccNum(), new Date()));
        assertTrue(ex.getMessage().contains("either doesn't exist or isn't a doctor"));
    }

    // ---------- CANCEL APPOINTMENT TESTS ----------

    /**
     * Tests successful cancellation of an existing appointment.
     * The appointment status should change to CANCELLED.
     */
    @Test
    void testCancelAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        controller.cancelAppointment(appt.getAppointmentId());

        Appointment updated = appointmentService.getAppointment(appt.getAppointmentId());
        assertEquals(Appointment.Status.CANCELLED, updated.getStatus());
    }

    /**
     * Tests cancelling an appointment that is already cancelled.
     * Expects AppointmentDoesNotExistException to be thrown.
     */
    @Test
    void testCancelAppointmentAlreadyCancelled() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        // Manually cancel to hit the exception branch
        appt.setStatus(Appointment.Status.CANCELLED);
        appointmentService.updateAppointment(appt);

        AppointmentDoesNotExistException ex = assertThrows(AppointmentDoesNotExistException.class,
                () -> controller.cancelAppointment(appt.getAppointmentId()));
        assertTrue(ex.getMessage().contains("was already cancelled"));
    }

    /**
     * Tests cancelling an appointment with an invalid ID.
     * Expects InvalidIdException to be thrown.
     */
    @Test
    void testCancelAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.cancelAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // ---------- DELETE APPOINTMENT TESTS ----------

    /**
     * Tests successful deletion of an existing appointment.
     * After deletion, the appointment should no longer exist in the service.
     */
    @Test
    void testDeleteAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        controller.deleteAppointment(appt.getAppointmentId());
        assertNull(appointmentService.getAppointment(appt.getAppointmentId()));
    }

    /**
     * Tests deleting an appointment with an invalid ID.
     * Expects InvalidIdException to be thrown.
     */
    @Test
    void testDeleteAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.deleteAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // ---------- RESCHEDULE APPOINTMENT TESTS ----------

    /**
     * Tests rescheduling an existing appointment to a new time.
     * The appointment start date/time should be updated.
     */
    @Test
    void testRescheduleAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        Date newTime = new Date(System.currentTimeMillis() + 3600_000); // +1 hour
        controller.rescheduleAppointment(appt.getAppointmentId(), newTime);

        Appointment updated = appointmentService.getAppointment(appt.getAppointmentId());
        assertEquals(newTime, updated.getStartDateTime());
    }

    /**
     * Tests rescheduling an appointment with an invalid ID.
     * Expects InvalidIdException to be thrown.
     */
    @Test
    void testRescheduleAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.rescheduleAppointment(999, new Date()));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // ---------- GET APPOINTMENT TESTS ----------

    /**
     * Tests fetching an existing appointment by its ID.
     * The fetched appointment should match the created appointment.
     */
    @Test
    void testGetAppointmentSuccess() {
        Appointment appt = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date());
        Appointment fetched = controller.getAppointment(appt.getAppointmentId());
        assertEquals(appt, fetched);
    }

    /**
     * Tests fetching an appointment with an invalid ID.
     * Expects InvalidIdException to be thrown.
     */
    @Test
    void testGetAppointmentInvalidId() {
        InvalidIdException ex = assertThrows(InvalidIdException.class,
                () -> controller.getAppointment(999));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    // ---------- MULTIPLE APPOINTMENTS ----------

    /**
     * Tests creating multiple appointments for the same patient and doctor.
     * Ensures each appointment is distinct and stored correctly.
     */
    @Test
    void testMultipleAppointmentsForSamePatientDoctor() {
        Date now = new Date();
        Appointment appt1 = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), now);
        Appointment appt2 = controller.createAppointment(patient.getAccNum(), doctor.getAccNum(), new Date(now.getTime() + 3600_000));

        assertEquals(appt1, appointmentService.getAppointment(appt1.getAppointmentId()));
        assertEquals(appt2, appointmentService.getAppointment(appt2.getAppointmentId()));
        assertNotEquals(appt1.getAppointmentId(), appt2.getAppointmentId());
        assertNotEquals(appt1.getStartDateTime(), appt2.getStartDateTime());
    }
}

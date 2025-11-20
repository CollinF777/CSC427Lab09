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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AppointmentController
 */
@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private UserService userService;

    @Mock
    private Patient mockPatient;

    @Mock
    private Doctor mockDoctor;

    private AppointmentController appointmentController;
    private Appointment mockAppointment;
    private Date testDate;

    @BeforeEach
    void setUp() {
        appointmentController = new AppointmentController(appointmentService, userService);

        testDate = new Date();
        mockAppointment = new Appointment(1, mockPatient, mockDoctor, testDate, Appointment.Status.ACTIVE);
    }

    // ========== createAppointment Tests ==========

    @Test
    void testCreateAppointment_Success() {
        // Arrange
        int patientId = 1;
        int doctorId = 2;

        when(userService.getUser(patientId)).thenReturn(mockPatient);
        when(userService.getUser(doctorId)).thenReturn(mockDoctor);
        when(appointmentService.createAppointment(mockPatient, mockDoctor, testDate, Appointment.Status.ACTIVE))
                .thenReturn(mockAppointment);

        // Act
        Appointment result = appointmentController.createAppointment(patientId, doctorId, testDate);

        // Assert
        assertNotNull(result);
        assertEquals(mockAppointment, result);
        verify(userService).getUser(patientId);
        verify(userService).getUser(doctorId);
        verify(appointmentService).createAppointment(mockPatient, mockDoctor, testDate, Appointment.Status.ACTIVE);
    }

    @Test
    void testCreateAppointment_PatientDoesNotExist() {
        // Arrange
        int patientId = 999;
        int doctorId = 2;

        when(userService.getUser(patientId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.createAppointment(patientId, doctorId, testDate);
        });

        verify(userService).getUser(patientId);
        verify(appointmentService, never()).createAppointment(any(), any(), any(), any());
    }

    @Test
    void testCreateAppointment_UserIsNotPatient() {
        // Arrange
        int patientId = 1;
        int doctorId = 2;
        Doctor notAPatient = mock(Doctor.class);

        when(userService.getUser(patientId)).thenReturn(notAPatient);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.createAppointment(patientId, doctorId, testDate);
        });

        verify(appointmentService, never()).createAppointment(any(), any(), any(), any());
    }

    @Test
    void testCreateAppointment_DoctorDoesNotExist() {
        // Arrange
        int patientId = 1;
        int doctorId = 999;

        when(userService.getUser(patientId)).thenReturn(mockPatient);
        when(userService.getUser(doctorId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.createAppointment(patientId, doctorId, testDate);
        });

        verify(appointmentService, never()).createAppointment(any(), any(), any(), any());
    }

    @Test
    void testCreateAppointment_UserIsNotDoctor() {
        // Arrange
        int patientId = 1;
        int doctorId = 2;
        Patient notADoctor = mock(Patient.class);

        when(userService.getUser(patientId)).thenReturn(mockPatient);
        when(userService.getUser(doctorId)).thenReturn(notADoctor);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.createAppointment(patientId, doctorId, testDate);
        });

        verify(appointmentService, never()).createAppointment(any(), any(), any(), any());
    }

    // ========== cancelAppointment Tests ==========

    @Test
    void testCancelAppointment_Success() {
        // Arrange
        int appointmentId = 1;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(mockAppointment);

        // Act
        appointmentController.cancelAppointment(appointmentId);

        // Assert
        assertEquals(Appointment.Status.CANCELLED, mockAppointment.getStatus());
        verify(appointmentService).getAppointment(appointmentId);
        verify(appointmentService).updateAppointment(mockAppointment);
    }

    @Test
    void testCancelAppointment_AppointmentDoesNotExist() {
        // Arrange
        int appointmentId = 999;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.cancelAppointment(appointmentId);
        });

        verify(appointmentService, never()).updateAppointment(any());
    }

    @Test
    void testCancelAppointment_AlreadyCancelled() {
        // Arrange
        int appointmentId = 1;
        Appointment cancelledAppointment = new Appointment(1, mockPatient, mockDoctor, testDate, Appointment.Status.CANCELLED);

        when(appointmentService.getAppointment(appointmentId)).thenReturn(cancelledAppointment);

        // Act & Assert
        assertThrows(AppointmentDoesNotExistException.class, () -> {
            appointmentController.cancelAppointment(appointmentId);
        });

        verify(appointmentService, never()).updateAppointment(any());
    }

    // ========== deleteAppointment Tests ==========

    @Test
    void testDeleteAppointment_Success() {
        // Arrange
        int appointmentId = 1;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(mockAppointment);

        // Act
        appointmentController.deleteAppointment(appointmentId);

        // Assert
        verify(appointmentService).getAppointment(appointmentId);
        verify(appointmentService).deleteAppointment(appointmentId);
    }

    @Test
    void testDeleteAppointment_AppointmentDoesNotExist() {
        // Arrange
        int appointmentId = 999;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.deleteAppointment(appointmentId);
        });

        verify(appointmentService, never()).deleteAppointment(anyInt());
    }

    // ========== rescheduleAppointment Tests ==========

    @Test
    void testRescheduleAppointment_Success() {
        // Arrange
        int appointmentId = 1;
        Date newDate = new Date(testDate.getTime() + 86400000); // +1 day

        when(appointmentService.getAppointment(appointmentId)).thenReturn(mockAppointment);

        // Act
        appointmentController.rescheduleAppointment(appointmentId, newDate);

        // Assert
        assertEquals(newDate, mockAppointment.getStartDateTime());
        verify(appointmentService).getAppointment(appointmentId);
        verify(appointmentService).updateAppointment(mockAppointment);
    }

    @Test
    void testRescheduleAppointment_AppointmentDoesNotExist() {
        // Arrange
        int appointmentId = 999;
        Date newDate = new Date();

        when(appointmentService.getAppointment(appointmentId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.rescheduleAppointment(appointmentId, newDate);
        });

        verify(appointmentService, never()).updateAppointment(any());
    }

    // ========== getAppointment Tests ==========

    @Test
    void testGetAppointment_Success() {
        // Arrange
        int appointmentId = 1;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(mockAppointment);

        // Act
        Appointment result = appointmentController.getAppointment(appointmentId);

        // Assert
        assertNotNull(result);
        assertEquals(mockAppointment, result);
        verify(appointmentService).getAppointment(appointmentId);
    }

    @Test
    void testGetAppointment_AppointmentDoesNotExist() {
        // Arrange
        int appointmentId = 999;

        when(appointmentService.getAppointment(appointmentId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidIdException.class, () -> {
            appointmentController.getAppointment(appointmentId);
        });
    }
}
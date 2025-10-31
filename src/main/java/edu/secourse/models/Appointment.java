package edu.secourse.models;

import java.util.Date;

/**
 * Model for Appointment.
 *
 * @author Matt Robinson
 */
public class Appointment {
    public enum Status {
        CANCELLED,
        ACTIVE
    }

    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Date startDateTime;
    private Status status;

    public Appointment(int appointmentId, Patient patient, Doctor doctor, Date startDateTime, Status status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.startDateTime = startDateTime;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

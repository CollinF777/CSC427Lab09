package edu.secourse.models;

public class Patient extends User {
    private String role = "Patient";

    public Patient(int accNum, String username, String password, String name, String email) {
        super(accNum, username, password, name, email);
    }

    public String getRole() {
        return role;
    }
}

package edu.secourse.models;

/**
 * Model for Doctor.
 *
 * @author Collin Fair
 */
public class Doctor extends User {
    private String role = "Doctor";

    public Doctor(int accNum, String username, String password, String name, String email) {
        super(accNum, username, password, name, email);
    }

    public String getRole() {
        return role;
    }
}

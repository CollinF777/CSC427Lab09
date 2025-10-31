package edu.secourse.models;

public class Admin extends User {
    private String role = "Admin";

    public Admin(int accNum, String username, String password, String name, String email) {
        super(accNum, username, password, name, email);
    }

    public String getRole() {
        return role;
    }
}

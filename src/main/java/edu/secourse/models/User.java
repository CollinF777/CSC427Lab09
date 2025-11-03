package edu.secourse.models;

public abstract class User {
    private int accNum;
    private String username;
    private String password;
    private String name;
    private String email;

    public User(int accNum, String username, String password, String name, String email) {
        this.accNum = accNum;
        this.password = password;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public int getAccNum() {
        return accNum;
    }

    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

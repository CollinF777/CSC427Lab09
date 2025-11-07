package edu.secourse.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController uController = new UserController();

    @Test
    void createUser() {
        assertAll(
                () -> assertDoesNotThrow(() -> {uController.createUser("testGuy123", "tester7854", "John Doe", "testguy@testers.com", "Doctor");})
        );
    }

    @Test
    void updateUsername() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void updateName() {
    }

    @Test
    void updateEmailAddress() {
    }

    @Test
    void removeUser() {
    }
}
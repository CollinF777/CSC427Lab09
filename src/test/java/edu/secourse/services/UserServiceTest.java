package edu.secourse.services;

import edu.secourse.models.Patient;
import edu.secourse.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Test
    void createUser() {
        final UserService service = new UserService();

        assertAll(
                // Test that a doctor can be created successfully
                () -> assertDoesNotThrow(() -> service.createUser(
                        "username1",
                        "Password1!",
                        "User One",
                        "user1@test.com",
                        "doctor"
                )),
                // Test that a patient can be created successfully
                () -> assertDoesNotThrow(() -> service.createUser(
                        "username2",
                        "Password2!",
                        "User Two",
                        "user2@test.com",
                        "patient"
                )),
                // Test that an admin can be created successfully
                () -> assertDoesNotThrow(() -> service.createUser(
                        "username3",
                        "Password3!",
                        "User Three",
                        "user3@test.com",
                        "admin"
                )),
                // Test that a user cannot have an invalid role
                () -> assertThrows(RuntimeException.class, () -> service.createUser(
                        "username4",
                        "Password4!",
                        "User Four",
                        "user4@test.com",
                        "notarole"
                ))
        );
    }

    @Test
    void getUser() {
        final UserService service = new UserService();

        User user1 = service.createUser(
                "username1",
                "Password1!",
                "User One",
                "user1@test.com",
                "admin"
        );

        User user2 = service.createUser(
                "username2",
                "Password2!",
                "User Two",
                "user2@test.com",
                "patient"
        );

        assertAll(
                // Test that the ID cannot be a negative integer
                () -> assertNull(service.getUser(-1)),
                // check that an existing user can be retrieved
                () -> assertNotNull(service.getUser(user1.getAccNum())),
                () -> assertNotNull(service.getUser(user2.getAccNum()))
        );
    }

    @Test
    void updateUser() {
        final UserService service = new UserService();

        User user1 = service.createUser(
                "username1",
                "Password1!",
                "User One",
                "user1@test.com",
                "admin"
        );

        assertAll(
                // test that a user must exist for them to be updated
                () -> assertThrows(RuntimeException.class, () -> service.updateUser(new Patient(
                        -1,
                        "no",
                        "notGoingtoWork!!1",
                        "ThisWont Work",
                        "thiswont@work.atall"
                ))),
                // test that a user can be updated
                () -> assertDoesNotThrow(() -> {
                    user1.setUsername("newUsername");
                    service.updateUser(user1);
                })
        );
    }

    @Test
    void deleteUser() {
        final UserService service = new UserService();

        User user1 = service.createUser(
                "username1",
                "Password1!",
                "User One",
                "user1@test.com",
                "admin"
        );

        assertAll(
                // test that a user that doesn't exist cannot be deleted
                () -> assertFalse(service.deleteUser(user1.getAccNum() + 1)),
                // test that a user that does exist can be deleted
                () -> assertTrue(service.deleteUser(user1.getAccNum()))
        );
    }
}
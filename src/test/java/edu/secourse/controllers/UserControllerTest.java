package edu.secourse.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import edu.secourse.models.*;
import edu.secourse.services.UserService;

class UserControllerTest {
    // Create UserService to be used in UserController
    UserService uService = new UserService();
    // Create UserController object for use throughout tests
    UserController uController = new UserController(uService);

    // Test data
    String[] usernames = {"JohnJimbo", "AliceTheTechWiz", "JimboJazz", "Maurice", "ChesterStone", "ChesterCheetah", "blueee", "Smafty", "MattaRama", "TheDrunkNinja"};
    String[] passwords = {"grabtooski4321", "elifThunder847", "#terbonotho69", "morthontog521", "FunGuy8008135", "tungustonAlloy531", "imposterAmogusSyndrome67", "thisIsAReallySecurePassword1234", "heyThisPasswordIsWaaaaaaaaaaaaayMoreSecure7960039", "notSecurePassword2"};
    String[] names = {"John Jim", "Alice Sanchez", "Jim Jazz", "Mark Stone", "Chester Stone", "Chester Cheetah", "Samprada Prahdan", "Corey Suhr", "Matthew Robinson", "Collin Fair"};
    String[] emails = {"johnman@company.com", "aTech@company.com", "jjazz@company.com", "maurice@company.com", "cstone@company.com", "chheata@company.com", "sprad@company.com", "csuhr@company.com", "mrobi@company.com", "cfair@company.com"};
    String[] roles = {"Doctor", "Patient", "Doctor", "Doctor", "Patient", "Patient", "Admin", "Admin", "Admin", "Admin"};

    // Makes ArrayList of users to easily pull information
    ArrayList<User> users = new ArrayList<User>();

    @Test
    void createUser() {
        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.createUser("testGuy123", "tester7854", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a username that is too long
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy1234567891234567890123456789123456789123456789123456789123456", "tester7854", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a username that is empty
                () -> assertThrows(Exception.class, () -> {uController.createUser("", "tester7854", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a password that is too short
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "test1", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a password that does not have numbers
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "cooltesterman", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a password that does not have letters
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "80000008135", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a password that is too long
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "tester7854123456789876543456789876545678876545678765456787654567893456789kjnbvfghjnbvfdrtyhbvcde4567ygfr5678765rfhu76tfvbu76rfcvbhu76trfvbu76tbhujnkijnjihytrer56765678", "John Doe", "testguy@testers.com", "Doctor");}),

                // Tests for a name that does not start with a capital letter
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "tester7854", "john doe", "testguy@testers.com", "Doctor");}),

                // Tests for a name that contains chars that aren't letters
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "tester7854", "La4she_ Doe", "testguy@testers.com", "Doctor");}),

                // Tests for an email address that is not correctly formatted
                () -> assertThrows(Exception.class, () -> {uController.createUser("testGuy123", "tester7854", "John Doe", "testguy@tester", "Doctor");})
        );
    }

    @Test
    void updateUsername() {
        populateUsers();

        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.updateUsername(users.get(0).getAccNum(), "jerma985");}),

                // Tests for a username that is empty
                () -> assertThrows(Exception.class, () -> {uController.updateUsername(users.get(1).getAccNum(), "");}),

                // Tests for a username that is too long
                () -> assertThrows(Exception.class, () -> {uController.updateUsername(users.get(2).getAccNum(), "longusername123456789123456789123456789123456789123456789123456789123456789123456789");})
        );
    }

    @Test
    void updatePassword() {
        populateUsers();

        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.updatePassword(users.get(0).getAccNum(), "amazingPaSSword5278");}),

                // Tests for a password that does not have numbers
                () -> assertThrows(Exception.class, () -> {uController.updatePassword(users.get(1).getAccNum(), "badpassword");}),

                // Tests for a password that does not have letters
                () -> assertThrows(Exception.class, () -> {uController.updatePassword(users.get(2).getAccNum(), "123456789");}),

                // Tests for a password that is too short
                () -> assertThrows(Exception.class, () -> {uController.updatePassword(users.get(3).getAccNum(), "pswd12");}),

                // Tests for a password that is too long
                () -> assertThrows(Exception.class, () -> {uController.updatePassword(users.get(4).getAccNum(), "longPasswordForTesting12121212121277456834659873920184038569083094834095834095809288208405983049589873459839809580938227502939890843");})
        );
    }

    @Test
    void updateName() {
        populateUsers();

        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.updateName(users.get(0).getAccNum(), "Archie Dude");}),

                // Tests for a name that does not start with a capital
                () -> assertThrows(Exception.class, () -> {uController.updateName(users.get(1).getAccNum(), "greg greg");}),

                // Tests for a name that contains chars that aren't letters
                () -> assertThrows(Exception.class, () -> {uController.updateName(users.get(2).getAccNum(), "La4she_ Doe");})
        );
    }

    @Test
    void updateEmailAddress() {
        populateUsers();

        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.updateEmailAddress(users.get(0).getAccNum(), "jarko@tests.com");}),

                // Tests for an email address that is not correctly formatted
                () -> assertThrows(Exception.class, () -> {uController.updateEmailAddress(users.get(1).getAccNum(), "thisIsAnEmail");})
        );
    }

    @Test
    void removeUser() {
        populateUsers();

        boolean isUnique;
        int uniqueID = users.get(0).getAccNum() + 1;

        // Finds user id that is not currently being used
        do{
            isUnique = true;
            for(int i = 0; i < users.size(); i++){
                if(uniqueID == users.get(i).getAccNum()){
                    isUnique = false;
                    uniqueID++;
                    break;
                }
            }
        } while(!isUnique);

        // Make final testID to pass into method
        final int testID = uniqueID;

        assertAll(
                // Tests correct functionality
                () -> assertDoesNotThrow(() -> {uController.removeUser(users.get(0).getAccNum());}),

                // Tests for a user that does not exist
                () -> assertThrows(Exception.class, () -> {uController.removeUser(testID);})
        );
    }

    private void populateUsers(){
        // Populate uController with users and add them to User ArrayList
        for(int i = 0; i < usernames.length; i++){
            users.add(uController.createUser(usernames[i], passwords[i], names[i], emails[i], roles[i]));
        }
    }
}
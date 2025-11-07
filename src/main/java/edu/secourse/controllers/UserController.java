package edu.secourse.controllers;

import edu.secourse.models.User;
import edu.secourse.services.UserService;

import java.util.regex.*;

/**
 * Handles controller operations on User.
 *
 * @author Corey Suhr
 */
public class UserController {
    UserService uService = new UserService();

    /**
     * Creates a user after successfully validating all the data
     * @param username: max 64 chars, not empty
     * @param password: min 8 chars, max 128 chars, at least 1 letter and 1 number
     * @param name: starts with capital, only consists of letters
     * @param emailAddress: in the regex format "\w+@\w+\.\w+"
     * @param role: use try catch block to check for exceptions in call to UserController.createUser
     * @return
     */
    public User createUser(String username, String password, String name, String emailAddress, String role){
        // Check username format
        if(username.isEmpty() || username.length() > 64){
            throw new RuntimeException("Username is formatted incorrectly.");
        }

        // Check password format
        if(
                password.length() < 8 ||
                password.length() > 128 ||
                !password.matches(".*[a-zA-Z].*") ||
                !password.matches(".*\\d.*")
        ){
            throw new RuntimeException("Password is formatted incorrectly.");
        }

        // Check name format
        if(!Character.isUpperCase(name.charAt(0)) || !name.matches("[a-zA-Z ]+")){
            throw new RuntimeException("Name is formatted incorrectly.");
        }

        //Check emailAddress format
        if(!emailAddress.matches("\\w+@\\w+\\.\\w+")){
            throw new RuntimeException("Email address is formatted incorrectly.");
        }

        try{
            return uService.createUser(username, password, name, emailAddress, role);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Updates the username for a user after the new username is successfully validated
     * @param id
     * @param newUsername
     */
    public void updateUsername(int id, String newUsername){
        // Check new username format
        if(newUsername.isEmpty() || newUsername.length() > 64){
            throw new RuntimeException("Username is formatted incorrectly.");
        }

        // Get user
        User user = uService.getUser(id);

        // Update username
        user.setUsername(newUsername);

        // Pass updated user to UserService
        uService.updateUser(user);
    }

    /**
     * Updates password after making sure the new password follows the required format
     * @param id
     * @param newPassword
     */
    public void updatePassword(int id, String newPassword){
        // Check format of new password
        if(
                newPassword.length() < 8 ||
                        newPassword.length() > 128 ||
                        !newPassword.matches(".*[a-zA-Z].*") ||
                        !newPassword.matches(".*\\d.*")
        ){
            throw new RuntimeException("Password is formatted incorrectly.");
        }

        // Get user
        User user = uService.getUser(id);

        // Update password
        user.setPassword(newPassword);

        // Pass updated user to UserService
        uService.updateUser(user);
    }

    /**
     * Updates a users name after making sure the name is properly formatted
     * @param id
     * @param newName
     */
    public void updateName(int id, String newName){
        // Check format of new name
        if(!Character.isUpperCase(newName.charAt(0)) || !newName.matches("[a-zA-Z ]+")){
            throw new RuntimeException("Name is formatted incorrectly.");
        }

        // Get user
        User user = uService.getUser(id);

        // Update name
        user.setName(newName);

        // Pass updated user to UserService
        uService.updateUser(user);
    }

    /**
     * Updates user's email after checking that the email is formatted like an email
     * @param id
     * @param newEmail
     */
    public void updateEmailAddress(int id, String newEmail){
        // Check format of new email address
        if(!newEmail.matches("\\w+@\\w+\\.\\w+")){
            throw new RuntimeException("Email address is formatted incorrectly.");
        }

        // Get user
        User user = uService.getUser(id);

        // Update email
        user.setEmail(newEmail);

        // Pass updated user to UserService
        uService.updateUser(user);  
    }

    /**
     * Deletes a user based off their user id
     * @param id
     */
    public void removeUser(int id){
        boolean x = uService.deleteUser(id);

        if(!x){
            throw new RuntimeException("Cannot delete a user that does not exist.");
        }
    }
}

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

    public User createUser(String username, String password, String name, String emailAddress, String role){
        /**
         * username: max 64 chars, not empty
         * password, min 8 chars, max 128 chars, at least 1 letter and 1 number
         * name: starts with capital, only consists of letters
         * emailAddress: in the regex format "\w+@\w+\.\w+"
         * role: use try catch block to check for exceptions in call to UserController.createUser
         */

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
        if(!Character.isUpperCase(name.charAt(0)) || !name.matches("[a-zA-Z]")){
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
    public void updateUser(User user){
        uService.updateUser(user);
    }
    public void removeUser(int id){
        boolean x = uService.deleteUser(id);

        if(!x){
            throw new RuntimeException("Cannot delete a user that does not exist.");
        }
    }
}

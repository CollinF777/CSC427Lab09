package edu.secourse.controllers;

import edu.secourse.models.User;
import edu.secourse.services.UserService;

/**
 * Handles controller operations on User.
 *
 * @author Corey Suhr
 */

public class UserController {
    UserService uService = new UserService();

    public User createUser(String username, String password, String name, String emailAddress, String role){
        return uService.createUser(username, password, name, emailAddress, role);
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

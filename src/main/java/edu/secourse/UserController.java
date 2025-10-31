package edu.secourse;

import edu.secourse.models.User;
import edu.secourse.services.UserService;

import java.util.ArrayList;

/**
 * Handles controller operations on User.
 *
 * @author Corey Suhr
 */

public class UserController {
    UserService uService = new UserService();

    public User createUser(String username, String password, String name, String emailAddress, String role){
        User newUser = uService.createUser(username, password, name, emailAddress, role);
        return newUser;
    }
    public void updateUser(User user){
        uService.updateUser(user);
    }
    public void removeUser(int id){
        User user = uService.getUser(id);
        boolean x = uService.deleteUser(user);

        if(x == false){
            throw new RuntimeException("Cannot delete a user that does not exist.");
        }
    }
}

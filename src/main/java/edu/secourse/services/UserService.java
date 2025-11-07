package edu.secourse.services;

import edu.secourse.models.Admin;
import edu.secourse.models.Doctor;
import edu.secourse.models.Patient;
import edu.secourse.models.User;

import java.util.ArrayList;

/**
 * Handles CRUD operations for User models, as well as retaining a cache of Users.
 *
 * @author Matt Robinson
 */
public class UserService {
    private ArrayList<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    /**
     * Creates a user, assigning it a random accId, storing the model in the UserService cache, and returning
     * @param username
     * @param password
     * @param name
     * @param email
     * @param role
     * @return
     */
    public User createUser(String username, String password, String name, String email, String role) {
        int userId = getUniqueId();

        User newUser;
        switch (role.toLowerCase()) {
            case "patient":
                newUser = new Patient(userId, username, password, name, email);
                break;
            case "doctor":
                newUser = new Doctor(userId, username, password, name, email);
                break;
            case "admin":
                newUser = new Admin(userId, username, password, name, email);
                break;
            default:
                throw new RuntimeException("Invalid role for user: " + role);
        }

        users.add(newUser);

        return newUser;
    }

    /**
     * Returns a unique numerical id that is not currently in the cache
     * @return
     */
    private int getUniqueId() {
        int ret;

        do {
            ret = (int)(Math.random() * 1000000);
        } while(getUser(ret) != null);

        return ret;
    }

    /**
     * Gets a user based off their id and returns said user
     * @param id
     * @return
     */
    public User getUser(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAccNum() == id) {
                return users.get(i);
            }
        }

        return null;
    }

    /**
     * Takes in a user, finds a user from the cache whose id matches theirs and updates the user in the cache with
     * the new information
     * @param user
     */
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAccNum() == user.getAccNum()) {
                users.set(i, user);
                return;
            }
        }
        throw new RuntimeException("Cannot perform update operation on user that does not yet exist.");
    }

    /**
     * Finds a user based off their id and removes them from the cache
     * @param id
     * @return
     */
    public boolean deleteUser(int id) {
        User user = getUser(id);

        if (user == null) {
            return false;
        }

        users.remove(user);

        return true;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

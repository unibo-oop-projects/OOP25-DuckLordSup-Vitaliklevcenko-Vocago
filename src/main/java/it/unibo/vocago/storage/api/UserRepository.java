package it.unibo.vocago.storage.api;

import java.nio.file.Path;
import java.util.List;
import it.unibo.vocago.model.user.api.User;

/**
 * Defines the persistence contract for user profiles, hiding the underlying
 * storage mechanism from the rest of the application.
 */
public interface UserRepository {

    /** The directory where user data is stored. */
    Path USERS_DIRECTORY = Path.of("data", "users");

    /**
     * Saves the given user, creating or overwriting its stored data.
     *
     * @param user the user to save
     */
    void save(User user);

    /**
     * @param userName the user name to look up
     * @return {@code true} if a user with that name is stored
     */
    boolean userExists(String userName);

    /**
     * Deletes the stored data of the given user.
     *
     * @param userName the name of the user to delete
     * @return {@code true} if the user was deleted
     */
    boolean deleteUser(String userName);

    /**
     * @return the list of all stored users
     */
    List<User> getExistingUsers();
}
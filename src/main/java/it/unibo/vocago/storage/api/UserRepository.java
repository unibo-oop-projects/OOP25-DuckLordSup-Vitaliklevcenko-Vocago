package it.unibo.vocago.storage.api;

import java.util.ArrayList;

import it.unibo.vocago.model.user.api.User;

public interface UserRepository {

    void createUser(User user);

    void saveVocabulary(User user);

    boolean userExists(String userName);

    boolean deleteUser(String userName);

    ArrayList<User> getExistingUsers();
}

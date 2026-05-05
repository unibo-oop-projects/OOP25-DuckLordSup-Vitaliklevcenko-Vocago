package it.unibo.vocago.storage.api;

import java.util.List;
import it.unibo.vocago.model.user.api.User;

public interface UserRepository {
    void save(User user);

    boolean userExists(String userName);

    boolean deleteUser(String userName);

    List<User> getExistingUsers();
}

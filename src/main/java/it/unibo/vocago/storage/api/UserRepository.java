package it.unibo.vocago.storage.api;

import java.nio.file.Path;
import java.util.List;
import it.unibo.vocago.model.user.api.User;

public interface UserRepository {
    
    static final Path USERS_DIRECTORY = Path.of("data", "users");

    void save(User user);

    boolean userExists(String userName);

    boolean deleteUser(String userName);

    List<User> getExistingUsers();
}

package it.unibo.vocago.storage;

import java.util.List;

import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.storage.api.UserRepository;

public class UserCsvStorage implements UserRepository{

    @Override
    public void save(User user) {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public boolean userExists(String userName) {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public boolean deleteUser(String userName) {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public List<User> getExistingUsers() {
        // TODO
        throw new UnsupportedOperationException("Unimplemented method 'getExistingUsers'");
    }

 

}

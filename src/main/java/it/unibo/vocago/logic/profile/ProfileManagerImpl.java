package it.unibo.vocago.logic.profile;

import java.util.List;
import java.util.Objects;

import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.user.Profile;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.storage.api.UserRepository;

public class ProfileManagerImpl implements ProfileManager{

    private final UserRepository userRepository;
    private User currentUser;

    public ProfileManagerImpl(final UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentUser = null;
    }

    public void createUser(final String userName, final String firstLanguage, final String secondLanguage) {
        final User user = new Profile(userName, firstLanguage, secondLanguage);
        this.userRepository.save(user);
        this.currentUser = user;
    }

    public boolean userExists(final String userName) {
        return this.userRepository.userExists(userName);
    }

    public List<User> getExistingUsers() {
        return this.userRepository.getExistingUsers();
    }

    public void chooseUser(final User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean hasCurrentUser() {
        return this.currentUser != null;
    }

    public boolean vocabularyIsValid() {
        return hasCurrentUser() && this.currentUser.getVocabulary().isValid();
    }

    public void saveVocabulary(final Vocabulary vocabulary) {
        this.currentUser = new Profile(this.currentUser.getUserName(), vocabulary,
                this.currentUser.getFirstLanguage(), this.currentUser.getSecondLanguage());
        this.userRepository.save(this.currentUser);
    }

    public boolean deleteCurrentUser() {
        if (!hasCurrentUser()) {
            return false;
        }
        final String userName = this.currentUser.getUserName();
        if (!this.userRepository.deleteUser(userName)) {
            return false;
        }
        this.currentUser = null;
        return true;
    }
}

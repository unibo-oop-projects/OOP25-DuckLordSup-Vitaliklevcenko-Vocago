package it.unibo.vocago.logic.profile.api;

import java.util.List;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface ProfileManager {

    void createUser(String userName, String firstLanguage, String secondLanguage);

    boolean userExists(String userName);

    List<User> getExistingUsers();

    void chooseUser(User user);

    User getCurrentUser();

    boolean hasCurrentUser();

    boolean vocabularyIsValid();

    void saveVocabulary(Vocabulary vocabulary);

    boolean deleteCurrentUser();
}

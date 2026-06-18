package it.unibo.vocago.service.profile.api;

import java.util.List;

import it.unibo.vocago.model.progress.api.Statistics;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.service.learning.api.LearningSession;

public interface ProfileManager {

    void createProfile(String profileName, String firstLanguage, String secondLanguage);

    boolean profileExists(String profileName);

    List<User> getExistingProfiles();

    void chooseProfile(User profile);

    User getCurrentProfile();

    boolean hasCurrentProfile();

    boolean vocabularyIsValid();

    void saveVocabulary(Vocabulary vocabulary);

    boolean deleteCurrentProfile();

    Statistics getDashboardStats();

    void resetStats();

    void saveLearningProgress(LearningSession session);

    void updateExpiredStreak();

    int getDailyGoal();

    void saveProfileConfigurations(String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal);
}

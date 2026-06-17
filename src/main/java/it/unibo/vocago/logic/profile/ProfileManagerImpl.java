package it.unibo.vocago.logic.profile;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import it.unibo.vocago.logic.learning.api.LearningSession;
import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.progress.ProfileStats;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.progress.api.Stats;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.user.Profile;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.storage.ProgressFileStorage;
import it.unibo.vocago.storage.UserCsvStorage;
import it.unibo.vocago.storage.api.ProgressRepository;
import it.unibo.vocago.storage.api.UserRepository;

public class ProfileManagerImpl implements ProfileManager{

    private static final int DEFAULT_DAILY_GOAL = 10;
    private static final int MIN_DAILY_GOAL = 5;
    private static final int MAX_DAILY_GOAL = 40;

    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;
    private User currentProfile;

    public ProfileManagerImpl() {
        this(new UserCsvStorage(), new ProgressFileStorage());
    }

    public ProfileManagerImpl(
        final UserRepository userRepository,
        final ProgressRepository progressRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.progressRepository = Objects.requireNonNull(progressRepository);
        this.currentProfile = null;
    }

    @Override
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        final User profile = new Profile(profileName, firstLanguage, secondLanguage);
        this.userRepository.save(profile);

        try {
            this.progressRepository.createProgressFile(profile.getUserName());
        } catch (RuntimeException exception) {
            System.err.println("Could not create progress file for profile: " + profileName);
            exception.printStackTrace();
        }
        this.currentProfile = profile;
    }

    @Override
    public boolean profileExists(final String profileName) {
        return this.userRepository.userExists(profileName);
    }

    @Override
    public List<User> getExistingProfiles() {
        return this.userRepository.getExistingUsers();
    }

    @Override
    public void chooseProfile(final User profile) {
        this.currentProfile = profile;
    }

    @Override
    public User getCurrentProfile() {
        return this.currentProfile;
    }

    @Override
    public boolean hasCurrentProfile() {
        return this.currentProfile != null;
    }

    @Override
    public boolean vocabularyIsValid() {
        return hasCurrentProfile() && this.currentProfile.getVocabulary() != null
                && this.currentProfile.getVocabulary().isValid();
    }

    @Override
    public void saveVocabulary(final Vocabulary vocabulary) {
        if (!hasCurrentProfile() || vocabulary == null) {
            return;
        }

        this.currentProfile = new Profile(
                this.currentProfile.getUserName(),
                vocabulary,
                this.currentProfile.getFirstLanguage(),
                this.currentProfile.getSecondLanguage());

        this.userRepository.save(this.currentProfile);
    }

    @Override
    public boolean deleteCurrentProfile() {
        if (!hasCurrentProfile()) {
            return false;
        }
        final String profileName = this.currentProfile.getUserName();
        if (!this.userRepository.deleteUser(profileName)) {
            return false;
        }
        this.currentProfile = null;

        try {
            this.progressRepository.deleteProgress(profileName);
        } catch (RuntimeException exception) {
            // The profile was deleted; leftover progress should not block deletion.
            System.err.println("Could not delete progress file for profile: " + profileName);
            exception.printStackTrace();
        }
        return true;
    }

    @Override
    public Stats getDashboardStats() {
        if (!hasCurrentProfile()) {
            throw new IllegalStateException("No current profile selected.");
        }
        if (!vocabularyIsValid()) {
            return new ProfileStats(
                    0,
                    0,
                    0,
                    0,
                    0,
                    this.progressRepository.getLastStudyDate(this.currentProfile.getUserName()),
                    this.progressRepository.getCurrentStreak(this.currentProfile.getUserName()),
                    this.progressRepository.getTotalStudyTime(this.currentProfile.getUserName()));
        }

        final Vocabulary vocabulary = this.currentProfile.getVocabulary();
        int countMasteryItems = 0;
        int countCorrectAnswers = 0;
        int countWrongAnswers = 0;
        int wordCount = 0;
        double correctRatio = 0;

        for (VocabularyItem item : vocabulary.getItems()) {
            wordCount++;
            final Progress wordProgress = item.getProgress(Direction.FIRST_TO_SECOND);
            if (wordProgress.getMasteryLevel() == MasteryLevel.MASTER) {
                countMasteryItems++;
            }
            countCorrectAnswers += wordProgress.getCorrectAnswers();
            countWrongAnswers += wordProgress.getWrongAnswers();
        }

        if (countWrongAnswers > 0 || countCorrectAnswers > 0) {
            correctRatio = (countCorrectAnswers * 100.0) / (countWrongAnswers + countCorrectAnswers);
        }

        return new ProfileStats(
                countMasteryItems,
                countCorrectAnswers,
                countWrongAnswers,
                wordCount,
                correctRatio,
                this.progressRepository.getLastStudyDate(this.currentProfile.getUserName()),
                this.progressRepository.getCurrentStreak(this.currentProfile.getUserName()),
                this.progressRepository.getTotalStudyTime(this.currentProfile.getUserName()));
    }
    
    @Override
    public void resetStats() {
        if (hasCurrentProfile()) {
            this.progressRepository.saveStats(this.currentProfile.getUserName(), LocalDate.now(), 0, 0L);
        }
    }

    @Override
    public void saveLearningProgress(final LearningSession session) {
        if (!hasCurrentProfile() || session == null) {
            return;
        }
        final String profileName = this.currentProfile.getUserName();
        final LocalDate today = LocalDate.now();
        final LocalDate lastStudyDate = this.progressRepository.getLastStudyDate(profileName);
        int streak = this.progressRepository.getCurrentStreak(profileName);

        if (today.equals(lastStudyDate)) {
            streak = Math.max(streak, 1);
        } else if (lastStudyDate != null && lastStudyDate.equals(today.minusDays(1)) &&
        session.getCorrectAnsweredQuestions() >= getDailyGoal()) {
            streak++;
        } else {
            streak = 0;
        }

        this.progressRepository.saveStats(
                profileName,
                today,
                streak,
                this.progressRepository.getTotalStudyTime(profileName)
                        + (System.currentTimeMillis() - session.getTime()) / 1000);
    }

    @Override
    public int getDailyGoal() {
        if (!hasCurrentProfile()) {
            throw new IllegalStateException("No current profile selected.");
        }
        return this.progressRepository.getDailyGoal(this.currentProfile.getUserName());
    }
    
    @Override
    public void saveProfileConfigurations(String newProfileName, final String firstLanguage,
            final String secondLanguage, int dailyGoal) {
        if (hasCurrentProfile()) {
            final String originalProfileName = this.currentProfile.getUserName();
            final String targetProfileName = newProfileName == null || newProfileName.trim().isBlank()
                    ? originalProfileName
                    : newProfileName.trim();
            if (dailyGoal < MIN_DAILY_GOAL || dailyGoal > MAX_DAILY_GOAL) {
                dailyGoal = DEFAULT_DAILY_GOAL;
            }
            this.progressRepository.saveProfileConfigurations(originalProfileName, targetProfileName, dailyGoal);
            final User updatedUser = new Profile(
                    targetProfileName,
                    this.currentProfile.getVocabulary(),
                    firstLanguage,
                    secondLanguage);
            this.userRepository.save(updatedUser);
            if (!originalProfileName.equals(targetProfileName)) {
                this.userRepository.deleteUser(originalProfileName);
            }
            this.currentProfile = updatedUser;
        }
    }

    @Override
    public void updateExpiredStreak() {
        if (!hasCurrentProfile()) {
            return;
        }
        final String profileName = this.currentProfile.getUserName();
        final LocalDate today = LocalDate.now();
        final LocalDate lastStudyDate = this.progressRepository.getLastStudyDate(profileName);
        final int currentStreak = this.progressRepository.getCurrentStreak(profileName);
        if (currentStreak > 0 && !lastStudyDate.equals(today) && !lastStudyDate.equals(today.minusDays(1))) {
            this.progressRepository.saveStats(
                    profileName,
                    lastStudyDate,
                    0,
                    this.progressRepository.getTotalStudyTime(profileName));
        }
    }
}

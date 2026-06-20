package it.unibo.vocago.service.profile;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import it.unibo.vocago.model.progress.api.Progress;
import it.unibo.vocago.model.statistics.ProfileStatistics;
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.model.types.Direction;
import it.unibo.vocago.model.types.MasteryLevel;
import it.unibo.vocago.model.user.Profile;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.model.vocabulary.api.Vocabulary;
import it.unibo.vocago.model.vocabulary.api.VocabularyItem;
import it.unibo.vocago.service.learning.api.LearningSession;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.storage.StatisticsFileStorage;
import it.unibo.vocago.storage.UserCsvStorage;
import it.unibo.vocago.storage.api.StatisticsRepository;
import it.unibo.vocago.storage.api.UserRepository;

public class ProfileManagerImpl implements ProfileManager{

    private static final Logger LOGGER = Logger.getLogger(ProfileManagerImpl.class.getName());

    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;
    private User currentProfile;

    public ProfileManagerImpl() {
        this(new UserCsvStorage(), new StatisticsFileStorage());
    }

    public ProfileManagerImpl(
        final UserRepository userRepository,
        final StatisticsRepository statisticsRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.statisticsRepository = Objects.requireNonNull(statisticsRepository);
        this.currentProfile = null;
    }

    @Override
    public void createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        final User profile = new Profile(profileName, firstLanguage, secondLanguage);
        this.userRepository.save(profile);

        try {
            this.statisticsRepository.createStatisticsFile(profile.getUserName());
        } catch (RuntimeException exception) {
            //can continue with the default values
            LOGGER.log(
                    Level.WARNING,
                    "Could not create statistics file for profile: " + profileName,
                    exception);
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
        return findCurrentProfile()
                .orElseThrow(() -> new IllegalStateException("No current profile selected."));
    }

    @Override
    public boolean hasCurrentProfile() {
        return findCurrentProfile().isPresent();
    }

    @Override
    public boolean vocabularyIsValid() {
        return findCurrentProfile()
                .map(User::getVocabulary)
                .filter(Vocabulary::isValid)
                .isPresent();
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
            this.statisticsRepository.deleteStatistics(profileName);
        } catch (RuntimeException exception) {
            // The profile was deleted; leftover statistics should not block deletion.
            LOGGER.log(
                    Level.WARNING,
                    "Could not delete statistics file for profile: " + profileName,
                    exception);
        }
        return true;
    }

    @Override
    public Statistics getDashboardStatistics() {
        final User profile = getCurrentProfile();
        final String profileName = profile.getUserName();
        if (!vocabularyIsValid()) {
            return new ProfileStatistics(
                    0,
                    0,
                    0,
                    0,
                    0,
                    this.statisticsRepository.getLastStudyDate(profileName),
                    this.statisticsRepository.getCurrentStreak(profileName),
                    this.statisticsRepository.getTotalStudyTime(profileName));
        }

        final Vocabulary vocabulary = profile.getVocabulary();
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

        return new ProfileStatistics(
                countMasteryItems,
                countCorrectAnswers,//for future use
                countWrongAnswers,
                wordCount,
                correctRatio,
                this.statisticsRepository.getLastStudyDate(profileName),
                this.statisticsRepository.getCurrentStreak(profileName),
                this.statisticsRepository.getTotalStudyTime(profileName));
    }
    
    @Override
    public void resetStatistics() {
        if (hasCurrentProfile()) {
            this.statisticsRepository.saveStatistics(this.currentProfile.getUserName(), LocalDate.now(), 0, 0L);
        }
    }

    @Override
    public void saveLearningStatistics(final LearningSession session) {
        if (!hasCurrentProfile() || session == null) {
            return;
        }
        final String profileName = this.currentProfile.getUserName();
        final LocalDate today = LocalDate.now();
        final LocalDate lastStudyDate = this.statisticsRepository.getLastStudyDate(profileName);
        int streak = this.statisticsRepository.getCurrentStreak(profileName);

        if (today.equals(lastStudyDate)) {
            streak = Math.max(streak, 1);
        } else if (lastStudyDate != null && lastStudyDate.equals(today.minusDays(1)) &&
        session.getCorrectAnsweredQuestions() >= getDailyGoal()) {
            streak++;
        } else {
            streak = 0;
        }

        this.statisticsRepository.saveStatistics(
                profileName,
                today,
                streak,
                this.statisticsRepository.getTotalStudyTime(profileName)
                        + (System.currentTimeMillis() - session.getStartTime()) / 1000);
    }

    @Override
    public int getDailyGoal() {
        return this.statisticsRepository.getDailyGoal(getCurrentProfile().getUserName());
    }
    
    @Override
    public void saveProfileConfigurations(String newProfileName, final String firstLanguage,
            final String secondLanguage, int dailyGoal) {
        if (hasCurrentProfile()) {
            final String originalProfileName = this.currentProfile.getUserName();
            final String targetProfileName = newProfileName == null || newProfileName.trim().isBlank()
                    ? originalProfileName
                    : newProfileName.trim();
            dailyGoal = DailyGoalSettings.normalize(dailyGoal);
            this.statisticsRepository.saveProfileConfigurations(originalProfileName, targetProfileName, dailyGoal);
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
        final LocalDate lastStudyDate = this.statisticsRepository.getLastStudyDate(profileName);
        final int currentStreak = this.statisticsRepository.getCurrentStreak(profileName);
        if (currentStreak > 0 && !lastStudyDate.equals(today) && !lastStudyDate.equals(today.minusDays(1))) {
            this.statisticsRepository.saveStatistics(
                    profileName,
                    lastStudyDate,
                    0,
                    this.statisticsRepository.getTotalStudyTime(profileName));
        }
    }

    private Optional<User> findCurrentProfile() {
        return Optional.ofNullable(this.currentProfile);
    }
}

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

    private final UserRepository userRepository;
    private final ProgressRepository progressRepository;
    private User currentUser;

    public ProfileManagerImpl() {
        this(new UserCsvStorage(), new ProgressFileStorage());
    }

    public ProfileManagerImpl(
        final UserRepository userRepository,
        final ProgressRepository progressRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.progressRepository = Objects.requireNonNull(progressRepository);
        this.currentUser = null;
    }

    public void createUser(final String userName, final String firstLanguage, final String secondLanguage) {
        final User user = new Profile(userName, firstLanguage, secondLanguage);
        this.userRepository.save(user);
        this.progressRepository.createProgressFile(userName);
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
        return hasCurrentUser() && this.currentUser.getVocabulary() != null && this.currentUser.getVocabulary().isValid();
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
        this.progressRepository.deleteProgress(userName);
        return true;
    }

    @Override
    public Stats getDashboardStats() {
        if (!hasCurrentUser()) {
            throw new IllegalStateException("No current user selected.");
        }
        if (!vocabularyIsValid()) {
            return new ProfileStats(
                    0,
                    0,
                    0,
                    0,
                    0,
                    this.progressRepository.getLastStudyDate(this.currentUser.getUserName()),
                    this.progressRepository.getCurrentStreak(this.currentUser.getUserName()),
                    this.progressRepository.getTotalStudyTime(this.currentUser.getUserName()));
        }

        final Vocabulary vocabulary = this.currentUser.getVocabulary();
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
                this.progressRepository.getLastStudyDate(this.currentUser.getUserName()),
                this.progressRepository.getCurrentStreak(this.currentUser.getUserName()),
                this.progressRepository.getTotalStudyTime(this.currentUser.getUserName()));
    }
    
    public void resetStats() {
        this.progressRepository.saveStats(this.currentUser.getUserName(), LocalDate.now(), 0, 0L);
    }

    public void saveLearningStats(final LearningSession session, final int requiredCorrectAnswers) {
        if (session == null || session.getCorrectAnsweredQuestions() < requiredCorrectAnswers) {
            return;
        }

        final LocalDate today = LocalDate.now();
        final LocalDate lastStudyDate = this.progressRepository.getLastStudyDate(this.currentUser.getUserName());
        int streak = this.progressRepository.getCurrentStreak(this.currentUser.getUserName());

        if (today.equals(lastStudyDate)) {
            streak = Math.max(streak, 1);
        } else if (lastStudyDate != null && lastStudyDate.equals(today.minusDays(1))) {
            streak++;
        } else {
            streak = 1;
        }

        final String userName = this.currentUser.getUserName();
        this.progressRepository.saveStats(
                userName,
                today,
                streak,
                this.progressRepository.getTotalStudyTime(userName) + (System.currentTimeMillis() - session.getTime()) / 1000);
    }

}

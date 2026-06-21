package it.unibo.vocago.controller.coordinators;

import java.util.List;

import javax.swing.JOptionPane;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

/**
 * Coordinates profile related operations on behalf of the controller: listing,
 * creating, choosing, deleting and configuring profiles. Failures from the
 * underlying {@link ProfileManager} are caught at this boundary and reported to
 * the user through the {@link AppView}.
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class ProfileCoordinator {

    private static final String PROFILE_ERROR_TITLE = "Profile Error";
    private static final String INVALID_PROFILE_NAME_TITLE = "Profile Name Invalid";

    private final ProfileManager profileManager;
    private final AppView appView;

    /**
     * Creates a profile coordinator.
     *
     * @param profileManager the profile manager that performs the operations
     * @param appView        the view used to report feedback to the user
     */
    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public ProfileCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    /**
     * Returns the list of existing profiles, or an empty list and an error
     * message if they cannot be loaded.
     *
     * @return the existing profiles
     */
    public List<User> getExistingProfiles() {
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected
        // failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.getExistingProfiles();
        } catch (final RuntimeException exception) {
            this.appView.showError(PROFILE_ERROR_TITLE, "Could not load saved profiles.");
            return List.of();
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Creates a new profile after validating the name and checking that it does
     * not already exist.
     *
     * @param profileName    the requested profile name
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language to study
     * @return {@code true} if the profile was created
     */
    public boolean createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (profileName == null || profileName.isBlank()) {
            this.appView.showWarning(
                    INVALID_PROFILE_NAME_TITLE,
                    "Please enter a valid profile name.");
            return false;
        }
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            if (this.profileManager.profileExists(profileName)) {
                this.appView.showError(
                        INVALID_PROFILE_NAME_TITLE,
                        "This profile already exists!");
                return false;
            }
            this.profileManager.createProfile(profileName, firstLanguage, secondLanguage);
            return true;
        } catch (final RuntimeException exception) {
            this.appView.showError(
                    PROFILE_ERROR_TITLE,
                    "Could not create profile, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Asks the user to confirm and, if confirmed, deletes the current profile.
     *
     * @return {@code true} if the profile was deleted
     */
    public boolean deleteProfile() {
        final int answer = this.appView.askConfirmationWithCancel(
                "Delete Profile",
                "Are you sure you want to delete your profile?");
        if (answer != JOptionPane.YES_OPTION) {
            return false;
        }
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected
        // failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.deleteCurrentProfile();
        } catch (final RuntimeException exception) {
            this.appView.showError("Delete Failed", "The profile could not be deleted, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Selects the given profile as the current one.
     *
     * @param profile the profile to select
     */
    public void chooseProfile(final User profile) {
        this.profileManager.chooseProfile(profile);
    }

    /**
     * @return {@code true} if a profile is currently selected
     */
    public boolean hasCurrentProfile() {
        return this.profileManager.hasCurrentProfile();
    }

    /**
     * Returns the daily study goal of the current profile, or the default value
     * if it cannot be retrieved.
     *
     * @return the daily study goal
     */
    public int getDailyGoal() {
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected
        // failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.getDailyGoal();
        } catch (final RuntimeException exception) {
            return DailyGoalSettings.DEFAULT;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Saves the configuration of a profile, validating the (possibly changed)
     * name before applying the changes.
     *
     * @param profileName    the new profile name, or blank to keep the current one
     * @param firstLanguage  the language already known by the user
     * @param secondLanguage the language to study
     * @param dailyGoal      the daily study goal
     * @return {@code true} if the configuration was saved
     */
    public boolean saveProfileConfigurations(final String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            final String originalProfileName = this.profileManager.getCurrentProfile().getUserName();
            final String normalizedProfileName = (profileName == null || profileName.isBlank())
                    ? originalProfileName
                    : profileName.trim();

            if (this.profileManager.profileExists(normalizedProfileName)
                    && !normalizedProfileName.equals(originalProfileName)) {
                this.appView.showError(
                        INVALID_PROFILE_NAME_TITLE,
                        "This profile already exists!");
                return false;
            }
            this.profileManager.saveProfileConfigurations(
                    normalizedProfileName,
                    firstLanguage,
                    secondLanguage,
                    dailyGoal);
            this.appView.showInfo(
                    "Profile saved",
                    "Profile configuration has been saved successfully!");
            return true;
        } catch (final RuntimeException exception) {
            this.appView.showError(
                    PROFILE_ERROR_TITLE,
                    "Could not change profile configuration, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Updates the study streak if it has expired since the last session.
     */
    public void updateExpiredStreak() {
        this.profileManager.updateExpiredStreak();
    }
}

package it.unibo.vocago.controller.coordinators;

import java.util.List;

import javax.swing.JOptionPane;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class ProfileCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public ProfileCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    public List<User> getExistingProfiles() {
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.getExistingProfiles();
        } catch (final RuntimeException exception) {
            this.appView.showError("Profile Error", "Could not load saved profiles.");
            return List.of();
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    public boolean createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (profileName == null || profileName.isBlank()) {
            this.appView.showWarning(
                    "Profile Name Invalid",
                    "Please enter a valid profile name.");
            return false;
        }
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            if (this.profileManager.profileExists(profileName)) {
                this.appView.showError(
                        "Profile Name Invalid",
                        "This profile already exists!");
                return false;
            }
            this.profileManager.createProfile(profileName, firstLanguage, secondLanguage);
            return true;
        } catch (final RuntimeException exception) {
            this.appView.showError(
                    "Profile Error",
                    "Could not create profile, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    public boolean deleteProfile() {
        final int answer = this.appView.askConfirmationWithCancel(
                "Delete Profile",
                "Are you sure you want to delete your profile?");
        if (answer != JOptionPane.YES_OPTION) {
            return false;
        }
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.deleteCurrentProfile();
        } catch (final RuntimeException exception) {
            this.appView.showError("Delete Failed", "The profile could not be deleted, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    public void chooseProfile(final User profile) {
        this.profileManager.chooseProfile(profile);
    }

    public boolean hasCurrentProfile() {
        return this.profileManager.hasCurrentProfile();
    }

    public int getDailyGoal() {
        // Final UI boundary: IllegalCatch - intentional catch convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.getDailyGoal();
        } catch (final RuntimeException exception) {
            return DailyGoalSettings.DEFAULT;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

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
                        "Profile Name Invalid",
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
                    "Profile Error",
                    "Could not change profile configuration, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    public void updateExpiredStreak() {
        this.profileManager.updateExpiredStreak();
    }

}

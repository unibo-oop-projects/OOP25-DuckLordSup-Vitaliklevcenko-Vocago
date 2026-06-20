package it.unibo.vocago.controller.coordinators;

import java.util.List;

import javax.swing.JOptionPane;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.types.DailyGoalSettings;
import it.unibo.vocago.model.user.api.User;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

public final class ProfileCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public ProfileCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    public List<User> getExistingProfiles() {
        try {
            return this.profileManager.getExistingProfiles();
        } catch (RuntimeException exception) {
            this.appView.showError("Profile Error", "Could not load saved profiles.");
            return List.of();
        }
    }

    public boolean createProfile(final String profileName, final String firstLanguage, final String secondLanguage) {
        if (profileName == null || profileName.trim().isBlank()) {
            this.appView.showWarning(
                    "Profile Name Invalid",
                    "Please enter a valid profile name.");
            return false;
        }
        try {
            if (this.profileManager.profileExists(profileName)) {
                this.appView.showError(
                        "Profile Name Invalid",
                        "This profile already exists!");
                return false;
            }
            this.profileManager.createProfile(profileName, firstLanguage, secondLanguage);
            return true;
        } catch (RuntimeException exception) {
            this.appView.showError(
                    "Profile Error",
                    "Could not create profile, try again!");
            return false;
        }
    }

    public boolean deleteProfile() {
        final int answer = this.appView.askConfirmationWithCancel(
                "Delete Profile",
                "Are you sure you want to delete your profile?");
        if (answer != JOptionPane.YES_OPTION) {
            return false;
        }
        try {
            return this.profileManager.deleteCurrentProfile();
        } catch (RuntimeException exception) {
            this.appView.showError("Delete Failed", "The profile could not be deleted, try again!");
            return false;
        }
    }

    public void chooseProfile(final User profile) {
        this.profileManager.chooseProfile(profile);
    }

    public boolean hasCurrentProfile() {
        return this.profileManager.hasCurrentProfile();
    }

    public int getDailyGoal() {
        try {
            return this.profileManager.getDailyGoal();
        } catch (RuntimeException exception) {
            return DailyGoalSettings.DEFAULT;
        }
    }

    public boolean saveProfileConfigurations(String profileName, final String firstLanguage,
            final String secondLanguage, final int dailyGoal) {
        try {
            final String originalProfileName = this.profileManager.getCurrentProfile().getUserName();
            profileName = (profileName == null || profileName.trim().isBlank())
                    ? originalProfileName
                    : profileName.trim();

            if (this.profileManager.profileExists(profileName) && !profileName.equals(originalProfileName)) {
                this.appView.showError(
                        "Profile Name Invalid",
                        "This profile already exists!");
                return false;
            }
            this.profileManager.saveProfileConfigurations(profileName, firstLanguage, secondLanguage, dailyGoal);
            this.appView.showInfo(
                    "Profile saved",
                    "Profile configuration has been saved successfully!");
            return true;
        } catch (RuntimeException exception) {
            this.appView.showError(
                    "Profile Error",
                    "Could not change profile configuration, try again!");
            return false;
        }
    }

    public void updateExpiredStreak() {
        this.profileManager.updateExpiredStreak();
    }
    
}

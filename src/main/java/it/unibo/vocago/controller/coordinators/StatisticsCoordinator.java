package it.unibo.vocago.controller.coordinators;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.vocago.model.statistics.ProfileStatistics;
import it.unibo.vocago.model.statistics.api.Statistics;
import it.unibo.vocago.service.profile.api.ProfileManager;
import it.unibo.vocago.view.api.AppView;

/**
 * Coordinates statistics related operations on behalf of the controller,
 * retrieving and resetting the current profile's statistics and reporting any
 * failure to the user through the {@link AppView}.
 */
public final class StatisticsCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    /**
     * Creates a statistics coordinator.
     *
     * @param profileManager the profile manager providing the statistics
     * @param appView        the view used to report feedback to the user
     */
    @SuppressFBWarnings(value = "EI2", justification = "The coordinator intentionally shares the profile manager.")
    public StatisticsCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    /**
     * Returns the dashboard statistics for the current profile, falling back to
     * empty statistics and an error message if they cannot be loaded.
     *
     * @return the statistics to display on the dashboard
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Statistics getDashboardStatistics() {
        // Final UI boundary: convert unexpected failures into user feedback and safe
        // data.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            return this.profileManager.getDashboardStatistics();
        } catch (final RuntimeException exception) {
            this.appView.showError("Statistics Error", "Your statistics have been corrupted");
            return new ProfileStatistics(
                    0,
                    0,
                    0,
                    0,
                    0.0,
                    LocalDate.now(),
                    0,
                    0L);
        }
        // CHECKSTYLE: IllegalCatch ON
    }

    /**
     * Asks the user to confirm and, if confirmed, resets the current profile's
     * statistics.
     *
     * @return {@code true} if the statistics were reset
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public boolean resetStatistics() {
        final int answer = this.appView.askConfirmationWithCancel(
                "Reset Statistics",
                "Are you sure? Your streak and study time will be reset");
        if (answer != JOptionPane.YES_OPTION) {
            return false;
        }

        // Final UI boundary: convert unexpected failures into user feedback.
        // CHECKSTYLE: IllegalCatch OFF
        try {
            this.profileManager.resetStatistics();
            this.appView.showInfo("Statistics Reset", "Your statistics have been reset");
            return true;
        } catch (final RuntimeException exception) {
            this.appView.showError("Statistics Error", "Failed to reset your statistics, try again!");
            return false;
        }
        // CHECKSTYLE: IllegalCatch ON
    }
}

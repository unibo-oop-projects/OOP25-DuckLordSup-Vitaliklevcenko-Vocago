package it.unibo.vocago.controller.coordinators;

import java.time.LocalDate;

import it.unibo.vocago.logic.profile.api.ProfileManager;
import it.unibo.vocago.model.progress.ProfileStats;
import it.unibo.vocago.model.progress.api.Stats;
import it.unibo.vocago.view.api.AppView;

public final class StatsCoordinator {

    private final ProfileManager profileManager;
    private final AppView appView;

    public StatsCoordinator(final ProfileManager profileManager, final AppView appView) {
        this.profileManager = profileManager;
        this.appView = appView;
    }

    public Stats getDashboardStats() {
        try {
            return this.profileManager.getDashboardStats();
        } catch (RuntimeException exception) {
            this.appView.showError("Stats Error", "Your Stats has been corrupted");
            return new ProfileStats(
                    0,
                    0,
                    0,
                    0,
                    0.0,
                    LocalDate.now(),
                    0,
                    0L);
        }
    }

    public boolean resetStats() {
        if (!this.appView.askConfirmation(
                "Reset Stats",
                "Are you sure? your streak and study time will be reset")) {
            return false;
        }

        try {
            this.profileManager.resetStats();
            this.appView.showInfo("Stats Reset", "Your Stats has been reset");
            return true;
        } catch (RuntimeException exception) {
            this.appView.showError("Stats Error", "Failed to reset your Stats, try again!");
            return false;
        }
    }
}

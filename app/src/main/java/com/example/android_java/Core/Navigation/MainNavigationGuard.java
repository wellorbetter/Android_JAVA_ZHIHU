package com.example.android_java.Core.Navigation;

import com.example.android_java.Core.Session.SessionRepository;

public class MainNavigationGuard {

    private final SessionRepository sessionRepository;

    public MainNavigationGuard(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public NavigationDecision decide(MainDestination targetDestination) {
        boolean blockedByPrivateMode = sessionRepository.isPrivateModeEnabled()
                && !sessionRepository.isLoggedIn()
                && targetDestination.requireLoginInPrivateMode();

        if (blockedByPrivateMode) {
            return new NavigationDecision(MainDestination.PERSONAL, true);
        }
        return new NavigationDecision(targetDestination, false);
    }

    public static class NavigationDecision {
        private final MainDestination destination;
        private final boolean blockedByPrivateMode;

        public NavigationDecision(MainDestination destination, boolean blockedByPrivateMode) {
            this.destination = destination;
            this.blockedByPrivateMode = blockedByPrivateMode;
        }

        public MainDestination getDestination() {
            return destination;
        }

        public boolean isBlockedByPrivateMode() {
            return blockedByPrivateMode;
        }
    }
}

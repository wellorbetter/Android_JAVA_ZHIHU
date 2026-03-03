package com.example.android_java.Core.Navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.android_java.Core.Session.SessionRepository;

import org.junit.Test;

public class MainNavigationGuardTest {

    @Test
    public void whenPrivateModeOff_shouldAllowHome() {
        MainNavigationGuard guard = new MainNavigationGuard(new FakeSessionRepository(false, false));

        MainNavigationGuard.NavigationDecision decision = guard.decide(MainDestination.HOME);

        assertEquals(MainDestination.HOME, decision.getDestination());
        assertFalse(decision.isBlockedByPrivateMode());
    }

    @Test
    public void whenPrivateModeOnAndLoggedOut_shouldRedirectHomeToPersonal() {
        MainNavigationGuard guard = new MainNavigationGuard(new FakeSessionRepository(true, false));

        MainNavigationGuard.NavigationDecision decision = guard.decide(MainDestination.HOME);

        assertEquals(MainDestination.PERSONAL, decision.getDestination());
        assertTrue(decision.isBlockedByPrivateMode());
    }

    @Test
    public void whenPrivateModeOnAndLoggedOut_shouldAllowPersonal() {
        MainNavigationGuard guard = new MainNavigationGuard(new FakeSessionRepository(true, false));

        MainNavigationGuard.NavigationDecision decision = guard.decide(MainDestination.PERSONAL);

        assertEquals(MainDestination.PERSONAL, decision.getDestination());
        assertFalse(decision.isBlockedByPrivateMode());
    }

    @Test
    public void whenPrivateModeOnAndLoggedIn_shouldAllowIssue() {
        MainNavigationGuard guard = new MainNavigationGuard(new FakeSessionRepository(true, true));

        MainNavigationGuard.NavigationDecision decision = guard.decide(MainDestination.ISSUE);

        assertEquals(MainDestination.ISSUE, decision.getDestination());
        assertFalse(decision.isBlockedByPrivateMode());
    }

    private static class FakeSessionRepository implements SessionRepository {
        private boolean privateMode;
        private boolean loggedIn;

        private String accessToken = "";

        FakeSessionRepository(boolean privateMode, boolean loggedIn) {
            this.privateMode = privateMode;
            this.loggedIn = loggedIn;
        }

        @Override
        public boolean isPrivateModeEnabled() {
            return privateMode;
        }

        @Override
        public void setPrivateModeEnabled(boolean enabled) {
            privateMode = enabled;
        }

        @Override
        public boolean isLoggedIn() {
            return loggedIn;
        }

        @Override
        public void setLoggedIn(boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        @Override
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void clearSession() {
            loggedIn = false;
            accessToken = "";
        }
    }
}

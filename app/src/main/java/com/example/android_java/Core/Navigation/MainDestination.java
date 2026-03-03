package com.example.android_java.Core.Navigation;

public enum MainDestination {
    HOME(0, true),
    DISCOVER(1, true),
    ISSUE(2, true),
    SPECIAL(3, true),
    PERSONAL(4, false);

    private final int pageIndex;
    private final boolean requireLoginInPrivateMode;

    MainDestination(int pageIndex, boolean requireLoginInPrivateMode) {
        this.pageIndex = pageIndex;
        this.requireLoginInPrivateMode = requireLoginInPrivateMode;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public boolean requireLoginInPrivateMode() {
        return requireLoginInPrivateMode;
    }
}

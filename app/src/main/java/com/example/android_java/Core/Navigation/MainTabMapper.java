package com.example.android_java.Core.Navigation;

import com.example.android_java.R;

public final class MainTabMapper {

    private MainTabMapper() {
    }

    public static MainDestination fromMenuId(int menuId) {
        if (menuId == R.id.home_bottom_0) {
            return MainDestination.HOME;
        }
        if (menuId == R.id.home_bottom_1) {
            return MainDestination.DISCOVER;
        }
        if (menuId == R.id.home_bottom_2) {
            return MainDestination.ISSUE;
        }
        if (menuId == R.id.home_bottom_3) {
            return MainDestination.SPECIAL;
        }
        return MainDestination.PERSONAL;
    }
}

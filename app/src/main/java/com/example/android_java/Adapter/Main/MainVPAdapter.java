package com.example.android_java.Adapter.Main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MainVPAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments;

    public MainVPAdapter(@NonNull FragmentManager fragmentManager,
                         @NonNull Lifecycle lifecycle,
                         @NonNull List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < 0 || position >= fragments.size()) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}

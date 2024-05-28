package com.example.android_java.Adapter.Home.Recommend;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class RecommendVPAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;

    public RecommendVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments == null ? 0 : fragments.size();
    }
}

package com.example.android_java.Adapter.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/* 用来适配新闻Fragment内部，不同分区Fragment的切换 */
public class HomeVPAdapter extends FragmentStateAdapter {

    List<Fragment> mFragment;

    public HomeVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> mFragment) {
        super(fragmentManager, lifecycle);
        this.mFragment = mFragment;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return mFragment == null ? null : mFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragment == null ? 0 : mFragment.size();
    }
}

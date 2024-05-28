package com.example.android_java.Pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_java.Adapter.Home.HomeVPAdapter;
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.Pages.Home.RecommendedFragment;
import com.example.android_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ViewPager2 vp_home;
    private TabLayout tb_home;
    private BottomNavigationView bt_home;
    private List<HomePageRecommendedNewsItem> items;
    private List<String> title;
    private List<Fragment> fragments;
    private List<String> tags;

    public HomeFragment() {

    }


    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initEvent() {

    }

    private void initData() {
        items = new ArrayList<>();
        title = new ArrayList<>();
        tags = new ArrayList<>();
        fragments = new ArrayList<>();
        for (int i = 0; i < 6; i ++ ) {
            items.add(new HomePageRecommendedNewsItem("问题名" + i, "问题的具体内容"));
            title.add("问题名"  + i);
            tags.add("类别" + i);
        }
        for (int i = 0; i < 6; i ++ ) {
            Fragment fragment = new RecommendedFragment(items, tags);
            fragments.add(fragment);
        }
    }

    private void initView(View view) {
        vp_home = view.findViewById(R.id.vp_home);
        tb_home = view.findViewById(R.id.tb_home);
        FragmentManager manager = getChildFragmentManager();
        Lifecycle lifecycle = getLifecycle();

        HomeVPAdapter adapter = new HomeVPAdapter(manager, lifecycle, fragments);
        vp_home.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tb_home, vp_home, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(title.get(position));
            }
        });
        mediator.attach();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
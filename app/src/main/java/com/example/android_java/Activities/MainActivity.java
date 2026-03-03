package com.example.android_java.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_java.Adapter.Main.MainVPAdapter;
import com.example.android_java.Core.AppContainer;
import com.example.android_java.Core.Navigation.MainDestination;
import com.example.android_java.Core.Navigation.MainNavigationGuard;
import com.example.android_java.Core.Navigation.MainTabMapper;
import com.example.android_java.Pages.DiscoverFragment;
import com.example.android_java.Pages.HomeFragment;
import com.example.android_java.Pages.IssueFragment;
import com.example.android_java.Pages.PersonalFragment;
import com.example.android_java.Pages.SpecialFragment;
import com.example.android_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 mainViewPager;
    private BottomNavigationView mainBottomNavigation;
    private final List<Fragment> fragments = new ArrayList<>();

    private MainNavigationGuard mainNavigationGuard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainNavigationGuard = new MainNavigationGuard(AppContainer.sessionRepository(this));

        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        mainViewPager.setUserInputEnabled(false);
        mainBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                MainDestination target = MainTabMapper.fromMenuId(item.getItemId());
                MainNavigationGuard.NavigationDecision decision = mainNavigationGuard.decide(target);

                mainViewPager.setCurrentItem(decision.getDestination().getPageIndex(), false);
                if (decision.isBlockedByPrivateMode()) {
                    mainBottomNavigation.setSelectedItemId(R.id.home_bottom_4);
                    Toast.makeText(MainActivity.this, R.string.private_mode_tips, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void initData() {
        fragments.add(new HomeFragment());
        fragments.add(new DiscoverFragment());
        fragments.add(new IssueFragment());
        fragments.add(new SpecialFragment());
        fragments.add(new PersonalFragment());
    }

    private void initView() {
        mainBottomNavigation = findViewById(R.id.bt_main);
        mainViewPager = findViewById(R.id.vp_main);
        mainViewPager.setAdapter(new MainVPAdapter(getSupportFragmentManager(), getLifecycle(), fragments));
    }
}

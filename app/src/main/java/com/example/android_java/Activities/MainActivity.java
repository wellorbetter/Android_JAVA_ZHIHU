package com.example.android_java.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.android_java.Adapter.Main.MainVPAdapter;
import com.example.android_java.Pages.HomeFragment;
import com.example.android_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

// 这里不对，这里写的是HomeFragment的内容，写错了
public class MainActivity extends AppCompatActivity {
    private ViewPager2 vp_main;
    private BottomNavigationView bt_main;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        vp_main.setUserInputEnabled(false);
        bt_main.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId= item.getItemId();
                if (itemId == R.id.home_bottom_0) {
                    vp_main.setCurrentItem(0);
                } else if (itemId == R.id.home_bottom_1) {
                    vp_main.setCurrentItem(1);
                } else if (itemId == R.id.home_bottom_2) {
                    vp_main.setCurrentItem(2);
                } else if (itemId == R.id.home_bottom_3) {
                    vp_main.setCurrentItem(3);
                } else if (itemId == R.id.home_bottom_4) {
                    vp_main.setCurrentItem(4);
                }
                return true;
            }
        });
    }

    private void initData() {
        fragments = new ArrayList<>();
        for (int i = 0; i < 5; i ++ ) {
            HomeFragment fragment = new HomeFragment();
            fragments.add(fragment);
        }
    }

    private void initView() {
        bt_main = findViewById(R.id.bt_main);
        vp_main = findViewById(R.id.vp_main);

        MainVPAdapter adapter = new MainVPAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        vp_main.setAdapter(adapter);
    }

}
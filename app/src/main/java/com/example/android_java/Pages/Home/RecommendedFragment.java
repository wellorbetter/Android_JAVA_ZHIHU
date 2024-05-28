package com.example.android_java.Pages.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.android_java.Adapter.Home.Recommend.RecommendRVAdapter;
import com.example.android_java.Adapter.Home.Recommend.RecommendVPAdapter;
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.Pages.Home.Recommend.RecommendTypeFragment;
import com.example.android_java.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class RecommendedFragment extends Fragment {


    private TabLayout tb_recommend;
    private ViewPager2 vp_recommend;
    List<HomePageRecommendedNewsItem> items;
    List<String> tags;

    // 这个是具体不同类别的Tag的新闻页面
    List<Fragment> fragments;
    public RecommendedFragment() {}
    public RecommendedFragment(List<HomePageRecommendedNewsItem> items, List<String> tags) {
        this.tags = tags;
        this.items = items;
    }

    public static RecommendedFragment newInstance(List<HomePageRecommendedNewsItem> items, List<String> tags) {
        return new RecommendedFragment(items, tags);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommended, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initEvent() {
        RecommendVPAdapter adapter = new RecommendVPAdapter(getChildFragmentManager(), getLifecycle(), fragments);
        vp_recommend.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tb_recommend, vp_recommend, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tags.get(position));
            }
        });
        mediator.attach();


    }

    private void initView(View view) {
        tb_recommend = view.findViewById(R.id.tb_recommend);
        vp_recommend = view.findViewById(R.id.vp_recommend);
    }

    private void initData() {
        fragments = new ArrayList<>();
        for (int i = 0; i < tags.size(); i ++ ) {
            RecommendTypeFragment fragment = new RecommendTypeFragment(items);
            fragments.add(fragment);
        }
    }
}
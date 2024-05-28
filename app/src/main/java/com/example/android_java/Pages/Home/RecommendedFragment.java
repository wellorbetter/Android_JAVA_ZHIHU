package com.example.android_java.Pages.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_java.Adapter.Home.HomeFragmentRVAdapter;
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.R;

import java.util.List;

public class RecommendedFragment extends Fragment {


    private RecyclerView rv_home_recommend;
    private List<HomePageRecommendedNewsItem> items;
    public RecommendedFragment() {}
    public RecommendedFragment(List<HomePageRecommendedNewsItem> items) {

        this.items = items;
    }

    public static RecommendedFragment newInstance(List<HomePageRecommendedNewsItem> items) {
        RecommendedFragment fragment = new RecommendedFragment(items);
        return fragment;
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
        rv_home_recommend = view.findViewById(R.id.rv_home_recommend);
        HomeFragmentRVAdapter adapter = new HomeFragmentRVAdapter(items);
        rv_home_recommend.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        rv_home_recommend.setLayoutManager(manager);
    }
}
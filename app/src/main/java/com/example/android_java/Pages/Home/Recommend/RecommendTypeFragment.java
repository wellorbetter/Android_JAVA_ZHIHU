package com.example.android_java.Pages.Home.Recommend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_java.Adapter.Home.Recommend.RecommendRVAdapter;
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.Pages.Home.RecommendedFragment;
import com.example.android_java.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendTypeFragment extends Fragment {

    private RecyclerView rv_home_recommend;
    private List<HomePageRecommendedNewsItem> items;
    public RecommendTypeFragment() {}
    public RecommendTypeFragment(List<HomePageRecommendedNewsItem> items) {

        this.items = items;
    }

    public static RecommendTypeFragment newInstance(List<HomePageRecommendedNewsItem> items) {
        RecommendTypeFragment fragment = new RecommendTypeFragment(items);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_home_recommend = view.findViewById(R.id.rv_home_recommend);
        RecommendRVAdapter adapter = new RecommendRVAdapter(items);
        rv_home_recommend.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        rv_home_recommend.setLayoutManager(manager);
    }
}

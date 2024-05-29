package com.example.android_java.Pages.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_java.R;

// 关注页面实际上和推荐页面差不多，只不过多了一些细节
// 比如头像更大，有名字，回答的时间，然后这里还有什么什么为你推荐
// 所以这两页用的数据的格式应该是一样的

// 右上角有关注
// 最上面有自己关注的人的头像排列
// 下面一点有三个tab 精选 最新 想法
public class ConcernedFragment extends Fragment {

    public ConcernedFragment() {
        // Required empty public constructor
    }

    public static ConcernedFragment newInstance(String param1, String param2) {
        ConcernedFragment fragment = new ConcernedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_concerned, container, false);
    }
}
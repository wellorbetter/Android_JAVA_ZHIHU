package com.example.android_java.Adapter.Home.Recommend;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.R;

import java.util.List;

/* 用来适配新闻数据和新闻Fragment的适配器 */
public class RecommendRVAdapter extends RecyclerView.Adapter<RecommendRVAdapter.ViewHolder> {
    private final List<HomePageRecommendedNewsItem> items;

    public RecommendRVAdapter(List<HomePageRecommendedNewsItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recommend_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomePageRecommendedNewsItem item = items.get(position);
        String question = item.getQuestionName();
        String questionDetail = item.getQuestionDetail();
        holder.tv_home_question.setText(question);
        holder.tv_home_questionDetail.setText(questionDetail);

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_home_question;
        private final TextView tv_home_questionDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_home_question = itemView.findViewById(R.id.tv_home_question);
            tv_home_questionDetail = itemView.findViewById(R.id.tv_home_questionDetail);
        }
    }
}
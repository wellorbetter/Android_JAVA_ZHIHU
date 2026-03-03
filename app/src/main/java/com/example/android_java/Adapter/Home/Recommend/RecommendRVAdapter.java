package com.example.android_java.Adapter.Home.Recommend;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.R;

import java.util.ArrayList;
import java.util.List;

/* 用来适配新闻数据和新闻Fragment的适配器 */
public class RecommendRVAdapter extends RecyclerView.Adapter<RecommendRVAdapter.ViewHolder> {
    public interface OnLikeClickListener {
        void onLikeClick(HomePageRecommendedNewsItem item);
    }
    public interface OnCommentClickListener {
        void onCommentClick(HomePageRecommendedNewsItem item);
    }

    private final List<HomePageRecommendedNewsItem> items = new ArrayList<>();
    private final OnLikeClickListener onLikeClickListener;
    private final OnCommentClickListener onCommentClickListener;
    private String highlightContentId;

    public RecommendRVAdapter(List<HomePageRecommendedNewsItem> items) {
        this(items, null, null);
    }

    public RecommendRVAdapter(List<HomePageRecommendedNewsItem> items, OnLikeClickListener onLikeClickListener, OnCommentClickListener onCommentClickListener) {
        this.onLikeClickListener = onLikeClickListener;
        this.onCommentClickListener = onCommentClickListener;
        updateItems(items);
    }

    public void updateItems(List<HomePageRecommendedNewsItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setHighlightContentId(String contentId) {
        highlightContentId = contentId;
        notifyDataSetChanged();
    }

    public int findPositionByContentId(String contentId) {
        if (contentId == null || contentId.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < items.size(); i++) {
            if (contentId.equals(items.get(i).getContentId())) {
                return i;
            }
        }
        return -1;
    }

    public void updateLikeCount(String contentId, int likeCount) {
        int position = findPositionByContentId(contentId);
        if (position < 0) {
            return;
        }
        items.get(position).setLikeCount(likeCount);
        notifyItemChanged(position);
    }

    public void updateCommentCount(String contentId, int commentCount) {
        int position = findPositionByContentId(contentId);
        if (position < 0) {
            return;
        }
        items.get(position).setCommentCount(commentCount);
        notifyItemChanged(position);
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
        String topicName = item.getTopicName();
        holder.tv_home_topic.setText(topicName == null || topicName.isEmpty() ? "All" : topicName);
        holder.tv_home_question.setText(question);
        holder.tv_home_questionDetail.setText(questionDetail);
        holder.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
        holder.tvCommentCount.setText(String.valueOf(item.getCommentCount()));
        boolean highlighted = item.getContentId() != null && item.getContentId().equals(highlightContentId);
        holder.itemView.setBackgroundColor(highlighted ? 0xFFF3F9FF : 0x00000000);
        holder.btLike.setOnClickListener(v -> {
            if (onLikeClickListener != null) {
                onLikeClickListener.onLikeClick(item);
            }
        });
        holder.btComment.setOnClickListener(v -> {
            if (onCommentClickListener != null) {
                onCommentClickListener.onCommentClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_home_question;
        private final TextView tv_home_questionDetail;
        private final TextView tv_home_topic;
        private final TextView tvLikeCount;
        private final TextView tvCommentCount;
        private final View btLike;
        private final View btComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_home_topic = itemView.findViewById(R.id.tv_home_topic);
            tv_home_question = itemView.findViewById(R.id.tv_home_question);
            tv_home_questionDetail = itemView.findViewById(R.id.tv_home_questionDetail);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            btLike = itemView.findViewById(R.id.bt_like);
            btComment = itemView.findViewById(R.id.bt_comment);
        }
    }
}

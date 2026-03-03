package com.example.android_java.Adapter.Home.Recommend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_java.R;

import java.util.ArrayList;
import java.util.List;

public class TopicFilterAdapter extends RecyclerView.Adapter<TopicFilterAdapter.TopicViewHolder> {

    public interface OnTopicSelectedListener {
        void onTopicSelected(String topicName);
    }

    private final List<String> topics = new ArrayList<>();
    private final OnTopicSelectedListener listener;
    private int selectedPosition = 0;

    public TopicFilterAdapter(OnTopicSelectedListener listener) {
        this.listener = listener;
    }

    public void updateTopics(List<String> newTopics) {
        topics.clear();
        topics.addAll(newTopics);
        selectedPosition = 0;
        notifyDataSetChanged();
    }

    public void selectTopic(String topicName, boolean notifyListener) {
        if (topicName == null || topicName.isEmpty() || topics.isEmpty()) {
            return;
        }
        int target = -1;
        for (int i = 0; i < topics.size(); i++) {
            if (topicName.equalsIgnoreCase(topics.get(i))) {
                target = i;
                break;
            }
        }
        if (target < 0) {
            return;
        }
        int old = selectedPosition;
        selectedPosition = target;
        if (old >= 0 && old < topics.size()) {
            notifyItemChanged(old);
        }
        notifyItemChanged(selectedPosition);
        if (notifyListener) {
            listener.onTopicSelected(topics.get(selectedPosition));
        }
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_filter, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        String topic = topics.get(position);
        holder.topicText.setText(topic);
        boolean isSelected = position == selectedPosition;
        holder.topicText.setSelected(isSelected);
        holder.topicText.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onTopicSelected(topic);
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        final TextView topicText;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicText = itemView.findViewById(R.id.tv_topic_name);
        }
    }
}

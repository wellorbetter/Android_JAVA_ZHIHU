package com.example.android_java.Adapter.Home.Recommend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_java.Feature.Content.ContentDtos;
import com.example.android_java.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.ViewHolder> {

    private final List<ContentDtos.CommentItem> items = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public void updateItems(List<ContentDtos.CommentItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void appendItems(List<ContentDtos.CommentItem> moreItems) {
        if (moreItems == null || moreItems.isEmpty()) {
            return;
        }
        int start = items.size();
        items.addAll(moreItems);
        notifyItemRangeInserted(start, moreItems.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentDtos.CommentItem item = items.get(position);
        holder.tvAuthor.setText(item.authorName == null ? "unknown" : item.authorName);
        holder.tvContent.setText(item.comment == null ? "" : item.comment);
        holder.tvTime.setText(dateFormat.format(new Date(item.createdAt)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAuthor;
        final TextView tvContent;
        final TextView tvTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_comment_author);
            tvContent = itemView.findViewById(R.id.tv_comment_content);
            tvTime = itemView.findViewById(R.id.tv_comment_time);
        }
    }
}

package com.example.android_java.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android_java.Adapter.Home.Recommend.CommentRVAdapter;
import com.example.android_java.Core.AppContainer;
import com.example.android_java.Feature.Content.ContentApi;
import com.example.android_java.Feature.Content.ContentDtos;
import com.example.android_java.Feature.Content.ContentGateway;
import com.example.android_java.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT_ID = "extra_content_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_COMMENT_COUNT = "extra_comment_count";

    private String contentId;
    private int latestCommentCount;
    private ContentGateway contentGateway;

    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etComment;
    private CommentRVAdapter adapter;
    private Button btLoadMore;
    private Button btSend;
    private Button btDeleteLatest;
    private String nextCursor;
    private boolean hasMore;
    private boolean loadingComments;
    private boolean sendingComment;
    private boolean deletingComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        contentId = getIntent().getStringExtra(EXTRA_CONTENT_ID);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        latestCommentCount = getIntent().getIntExtra(EXTRA_COMMENT_COUNT, 0);

        ContentApi contentApi = AppContainer.apiClientProvider(this).create(ContentApi.class);
        contentGateway = new ContentGateway(contentApi);

        TextView tvTitle = findViewById(R.id.tv_comments_title);
        tvTitle.setText(title == null ? getString(R.string.comments_title) : title);

        swipeRefreshLayout = findViewById(R.id.swipe_comments);
        RecyclerView recyclerView = findViewById(R.id.rv_comments);
        etComment = findViewById(R.id.et_comment_input);
        btSend = findViewById(R.id.bt_comment_send);
        btDeleteLatest = findViewById(R.id.bt_comment_delete_latest);
        btLoadMore = findViewById(R.id.bt_comment_load_more);

        adapter = new CommentRVAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadComments);
        btSend.setOnClickListener(v -> sendComment());
        btDeleteLatest.setOnClickListener(v -> deleteLatestOwnComment());
        btLoadMore.setOnClickListener(v -> loadMoreComments());
        updateActionButtons();

        loadComments();
    }

    private void loadComments() {
        if (TextUtils.isEmpty(contentId) || loadingComments) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        loadingComments = true;
        swipeRefreshLayout.setRefreshing(true);
        updateActionButtons();
        contentGateway.listComments(contentId, null, 20, new ContentGateway.Callback<ContentDtos.CommentListData>() {
            @Override
            public void onSuccess(ContentDtos.CommentListData data) {
                List<ContentDtos.CommentItem> items = data.items == null ? new ArrayList<>() : data.items;
                latestCommentCount = data.totalCount;
                adapter.updateItems(items);
                nextCursor = data.nextCursor;
                hasMore = data.hasMore;
                loadingComments = false;
                swipeRefreshLayout.setRefreshing(false);
                updateActionButtons();
                updateResult();
            }

            @Override
            public void onError(String message) {
                loadingComments = false;
                swipeRefreshLayout.setRefreshing(false);
                updateActionButtons();
                Toast.makeText(CommentsActivity.this, R.string.comments_load_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreComments() {
        if (TextUtils.isEmpty(contentId) || TextUtils.isEmpty(nextCursor) || !hasMore || loadingComments) {
            updateActionButtons();
            return;
        }
        loadingComments = true;
        updateActionButtons();
        contentGateway.listComments(contentId, nextCursor, 20, new ContentGateway.Callback<ContentDtos.CommentListData>() {
            @Override
            public void onSuccess(ContentDtos.CommentListData data) {
                List<ContentDtos.CommentItem> items = data.items == null ? new ArrayList<>() : data.items;
                latestCommentCount = data.totalCount;
                adapter.appendItems(items);
                nextCursor = data.nextCursor;
                hasMore = data.hasMore;
                loadingComments = false;
                updateActionButtons();
                updateResult();
            }

            @Override
            public void onError(String message) {
                loadingComments = false;
                updateActionButtons();
                Toast.makeText(CommentsActivity.this, R.string.comments_load_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendComment() {
        if (sendingComment) {
            return;
        }
        String comment = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, R.string.comments_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        sendingComment = true;
        updateActionButtons();
        contentGateway.createComment(contentId, comment, new ContentGateway.Callback<ContentDtos.CreateCommentData>() {
            @Override
            public void onSuccess(ContentDtos.CreateCommentData data) {
                sendingComment = false;
                updateActionButtons();
                if (data.commented) {
                    latestCommentCount = data.commentCount;
                    etComment.setText("");
                    Toast.makeText(CommentsActivity.this, R.string.comments_send_success, Toast.LENGTH_SHORT).show();
                    loadComments();
                } else {
                    Toast.makeText(CommentsActivity.this, R.string.comments_send_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                sendingComment = false;
                updateActionButtons();
                Toast.makeText(CommentsActivity.this, R.string.comments_send_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLatestOwnComment() {
        if (deletingComment) {
            return;
        }
        deletingComment = true;
        updateActionButtons();
        contentGateway.deleteLatestOwnComment(contentId, new ContentGateway.Callback<ContentDtos.DeleteCommentData>() {
            @Override
            public void onSuccess(ContentDtos.DeleteCommentData data) {
                deletingComment = false;
                updateActionButtons();
                if (data.deleted) {
                    latestCommentCount = data.commentCount;
                    Toast.makeText(CommentsActivity.this, R.string.comments_delete_success, Toast.LENGTH_SHORT).show();
                    loadComments();
                } else {
                    Toast.makeText(CommentsActivity.this, R.string.comments_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                deletingComment = false;
                updateActionButtons();
                Toast.makeText(CommentsActivity.this, R.string.comments_delete_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateActionButtons() {
        boolean busy = loadingComments || sendingComment || deletingComment;
        btLoadMore.setEnabled(!busy && hasMore && !TextUtils.isEmpty(nextCursor));
        etComment.setEnabled(!busy);
        btSend.setEnabled(!busy);
        btDeleteLatest.setEnabled(!busy);
    }

    private void updateResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONTENT_ID, contentId);
        intent.putExtra(EXTRA_COMMENT_COUNT, latestCommentCount);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void finish() {
        updateResult();
        super.finish();
    }
}

package com.example.android_java.Pages.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_java.Activities.CommentsActivity;
import com.example.android_java.Adapter.Home.Recommend.RecommendRVAdapter;
import com.example.android_java.Adapter.Home.Recommend.TopicFilterAdapter;
import com.example.android_java.Bean.HomePage.HomePageRecommendedNewsItem;
import com.example.android_java.Core.AppContainer;
import com.example.android_java.Feature.Content.ContentApi;
import com.example.android_java.Feature.Content.ContentDtos;
import com.example.android_java.Feature.Content.ContentGateway;
import com.example.android_java.Feature.Home.KmpHomeGateway;
import com.example.android_java.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendedFragment extends Fragment {
    private static final String ARG_FEED_CHANNEL = "feed_channel";
    private static final String DEFAULT_FEED_CHANNEL = "recommend";

    private RecyclerView rvTopicFilter;
    private RecyclerView rvRecommendList;
    private TextView tvRecommendEmpty;
    private TopicFilterAdapter topicFilterAdapter;
    private RecommendRVAdapter recommendRVAdapter;
    private KmpHomeGateway kmpHomeGateway;
    private ContentGateway contentGateway;
    private String feedChannel = DEFAULT_FEED_CHANNEL;
    private String selectedTopic = "All";
    private String pendingExternalTopic;
    private String pendingHighlightContentId;
    private ActivityResultLauncher<Intent> commentsLauncher;
    private boolean hasLoadedOnce;

    public RecommendedFragment() {
    }

    public static RecommendedFragment newInstance(String feedChannel) {
        RecommendedFragment fragment = new RecommendedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FEED_CHANNEL, feedChannel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            feedChannel = args.getString(ARG_FEED_CHANNEL, DEFAULT_FEED_CHANNEL);
        }
        commentsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getData() == null) {
                return;
            }
            Intent data = result.getData();
            String contentId = data.getStringExtra(CommentsActivity.EXTRA_CONTENT_ID);
            int commentCount = data.getIntExtra(CommentsActivity.EXTRA_COMMENT_COUNT, -1);
            if (contentId != null && commentCount >= 0 && recommendRVAdapter != null) {
                recommendRVAdapter.updateCommentCount(contentId, commentCount);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommended, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGateway();
        initView(view);
        loadTopics();
    }

    private void initGateway() {
        kmpHomeGateway = new KmpHomeGateway(requireContext());
        ContentApi contentApi = AppContainer.apiClientProvider(requireContext()).create(ContentApi.class);
        contentGateway = new ContentGateway(contentApi);
    }

    private void initView(View view) {
        rvTopicFilter = view.findViewById(R.id.rv_topic_filter);
        rvRecommendList = view.findViewById(R.id.rv_home_recommend);
        tvRecommendEmpty = view.findViewById(R.id.tv_recommend_empty);

        topicFilterAdapter = new TopicFilterAdapter(this::loadFeedByTopic);
        rvTopicFilter.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rvTopicFilter.setAdapter(topicFilterAdapter);

        recommendRVAdapter = new RecommendRVAdapter(new ArrayList<>(), this::likeItem, this::commentItem);
        rvRecommendList.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRecommendList.setAdapter(recommendRVAdapter);
    }

    private void loadTopics() {
        kmpHomeGateway.loadTopics(new KmpHomeGateway.Callback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                List<String> topics = new ArrayList<>();
                topics.add("All");
                topics.addAll(data);
                topicFilterAdapter.updateTopics(topics);
                if (pendingExternalTopic != null) {
                    topicFilterAdapter.selectTopic(pendingExternalTopic, true);
                    pendingExternalTopic = null;
                } else {
                    loadFeedByTopic(selectedTopic);
                }
            }

            @Override
            public void onError(String message) {
                topicFilterAdapter.updateTopics(defaultTopics());
                if (pendingExternalTopic != null) {
                    loadFeedByTopic(pendingExternalTopic);
                    pendingExternalTopic = null;
                } else {
                    loadFeedByTopic(selectedTopic);
                }
            }
        });
    }

    private void loadFeedByTopic(String topicName) {
        selectedTopic = topicName;
        kmpHomeGateway.loadFeed(feedChannel, topicName, new KmpHomeGateway.Callback<List<HomePageRecommendedNewsItem>>() {
            @Override
            public void onSuccess(List<HomePageRecommendedNewsItem> data) {
                recommendRVAdapter.updateItems(data);
                tvRecommendEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                hasLoadedOnce = true;
                if (pendingHighlightContentId != null && !pendingHighlightContentId.isEmpty()) {
                    recommendRVAdapter.setHighlightContentId(pendingHighlightContentId);
                    int position = recommendRVAdapter.findPositionByContentId(pendingHighlightContentId);
                    if (position >= 0) {
                        rvRecommendList.scrollToPosition(position);
                    }
                    pendingHighlightContentId = null;
                }
            }

            @Override
            public void onError(String message) {
                recommendRVAdapter.updateItems(new ArrayList<>());
                tvRecommendEmpty.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "推荐流加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeItem(HomePageRecommendedNewsItem item) {
        String contentId = item.getContentId();
        if (contentId == null || contentId.trim().isEmpty()) {
            Toast.makeText(requireContext(), "该内容暂不支持点赞", Toast.LENGTH_SHORT).show();
            return;
        }
        contentGateway.like(contentId, new ContentGateway.Callback<ContentDtos.LikeData>() {
            @Override
            public void onSuccess(ContentDtos.LikeData data) {
                recommendRVAdapter.updateLikeCount(contentId, data.likeCount);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), "点赞失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void commentItem(HomePageRecommendedNewsItem item) {
        String contentId = item.getContentId();
        if (contentId == null || contentId.trim().isEmpty()) {
            Toast.makeText(requireContext(), "该内容暂不支持评论", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(requireContext(), CommentsActivity.class);
        intent.putExtra(CommentsActivity.EXTRA_CONTENT_ID, contentId);
        intent.putExtra(CommentsActivity.EXTRA_TITLE, item.getQuestionName());
        intent.putExtra(CommentsActivity.EXTRA_COMMENT_COUNT, item.getCommentCount());
        commentsLauncher.launch(intent);
    }

    private List<String> defaultTopics() {
        List<String> topics = new ArrayList<>();
        topics.add("All");
        topics.add("Android");
        topics.add("iOS");
        topics.add("Web");
        topics.add("Backend");
        topics.add("AI");
        return topics;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (kmpHomeGateway != null) {
            kmpHomeGateway.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (kmpHomeGateway != null && !hasLoadedOnce) {
            loadFeedByTopic(selectedTopic);
        }
    }

    public void requestRefreshWithTopic(String topicName, String contentId) {
        if (topicName == null || topicName.trim().isEmpty()) {
            topicName = "All";
        }
        selectedTopic = topicName;
        pendingExternalTopic = topicName;
        pendingHighlightContentId = contentId;
        if (topicFilterAdapter != null) {
            topicFilterAdapter.selectTopic(topicName, true);
        }
    }
}

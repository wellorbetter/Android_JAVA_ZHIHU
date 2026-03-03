package com.example.android_java.Pages;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_java.Core.AppContainer;
import com.example.android_java.Feature.Content.ContentApi;
import com.example.android_java.Feature.Content.ContentDtos;
import com.example.android_java.Feature.Content.ContentGateway;
import com.example.android_java.Feature.Home.HomeRefreshBus;
import com.example.android_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class IssueFragment extends Fragment {

    private EditText titleInput;
    private EditText summaryInput;
    private Spinner topicSpinner;
    private ContentGateway contentGateway;

    public IssueFragment() {
    }

    public static IssueFragment newInstance() {
        return new IssueFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentApi contentApi = AppContainer.apiClientProvider(requireContext()).create(ContentApi.class);
        contentGateway = new ContentGateway(contentApi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleInput = view.findViewById(R.id.et_issue_post_title);
        summaryInput = view.findViewById(R.id.et_issue_post_summary);
        topicSpinner = view.findViewById(R.id.sp_issue_topic);
        Button publishButton = view.findViewById(R.id.bt_issue_publish);

        List<String> topics = Arrays.asList("Android", "iOS", "Web", "Backend", "AI");
        topicSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, topics));

        publishButton.setOnClickListener(v -> publishPost());
    }

    private void publishPost() {
        String title = titleInput.getText().toString().trim();
        String summary = summaryInput.getText().toString().trim();
        String topic = String.valueOf(topicSpinner.getSelectedItem());

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(summary)) {
            Toast.makeText(requireContext(), R.string.issue_publish_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        contentGateway.createPost(title, summary, topic, new ContentGateway.Callback<ContentDtos.CreatePostData>() {
            @Override
            public void onSuccess(ContentDtos.CreatePostData data) {
                if (data.created) {
                    titleInput.setText("");
                    summaryInput.setText("");
                    Toast.makeText(requireContext(), R.string.issue_publish_success, Toast.LENGTH_SHORT).show();
                    HomeRefreshBus.publish(topic, data.contentId);
                    BottomNavigationView navigationView = requireActivity().findViewById(R.id.bt_main);
                    if (navigationView != null) {
                        navigationView.setSelectedItemId(R.id.home_bottom_0);
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.issue_publish_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), R.string.issue_publish_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.android_java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.android_java.Core.AppContainer;
import com.example.android_java.Core.Session.SessionRepository;
import com.example.android_java.R;

public class PersonalFragment extends Fragment {

    private TextView sessionStatusText;
    private SwitchCompat privateModeSwitch;
    private SessionRepository sessionRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionRepository = AppContainer.sessionRepository(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionStatusText = view.findViewById(R.id.tv_session_status);
        privateModeSwitch = view.findViewById(R.id.switch_private_mode);
        Button loginButton = view.findViewById(R.id.bt_login);
        Button logoutButton = view.findViewById(R.id.bt_logout);

        privateModeSwitch.setChecked(sessionRepository.isPrivateModeEnabled());

        privateModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionRepository.setPrivateModeEnabled(isChecked);
            renderSessionStatus();
        });

        loginButton.setOnClickListener(v -> {
            sessionRepository.setLoggedIn(true);
            sessionRepository.setAccessToken("mock_access_token_for_android_client");
            renderSessionStatus();
        });

        logoutButton.setOnClickListener(v -> {
            sessionRepository.clearSession();
            renderSessionStatus();
        });

        renderSessionStatus();
    }

    private void renderSessionStatus() {
        String mode = sessionRepository.isPrivateModeEnabled()
                ? getString(R.string.private_mode_on)
                : getString(R.string.private_mode_off);

        String auth = sessionRepository.isLoggedIn()
                ? getString(R.string.user_logged_in)
                : getString(R.string.user_not_logged_in);

        String tokenStatus = sessionRepository.getAccessToken().isEmpty()
                ? getString(R.string.token_status_empty)
                : getString(R.string.token_status_ready);

        sessionStatusText.setText(getString(R.string.session_status, mode, auth, tokenStatus));
    }
}

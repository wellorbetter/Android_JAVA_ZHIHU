package com.example.android_java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.android_java.Core.AppContainer;
import com.example.android_java.Core.Network.ApiClientProvider;
import com.example.android_java.Core.Session.SessionRepository;
import com.example.android_java.Data.Model.ApiResponse;
import com.example.android_java.Feature.Auth.AuthApi;
import com.example.android_java.Feature.Auth.AuthDtos;
import com.example.android_java.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends Fragment {

    private TextView sessionStatusText;
    private SwitchCompat privateModeSwitch;
    private SessionRepository sessionRepository;
    private AuthApi authApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionRepository = AppContainer.sessionRepository(requireContext());
        ApiClientProvider apiClientProvider = AppContainer.apiClientProvider(requireContext());
        authApi = apiClientProvider.create(AuthApi.class);
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
            AuthDtos.LoginRequest request = new AuthDtos.LoginRequest();
            request.mobile = "android_user";
            request.password = "";
            authApi.login(request).enqueue(new Callback<ApiResponse<AuthDtos.LoginData>>() {
                @Override
                public void onResponse(Call<ApiResponse<AuthDtos.LoginData>> call, Response<ApiResponse<AuthDtos.LoginData>> response) {
                    if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                        Toast.makeText(requireContext(), R.string.session_login_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sessionRepository.setLoggedIn(true);
                    sessionRepository.setAccessToken(response.body().getData().accessToken);
                    renderSessionStatus();
                    Toast.makeText(requireContext(), R.string.session_login_success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse<AuthDtos.LoginData>> call, Throwable t) {
                    Toast.makeText(requireContext(), R.string.session_login_failed, Toast.LENGTH_SHORT).show();
                }
            });
        });

        logoutButton.setOnClickListener(v -> {
            authApi.logout().enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    sessionRepository.clearSession();
                    renderSessionStatus();
                    Toast.makeText(requireContext(), R.string.session_logout_done, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    sessionRepository.clearSession();
                    renderSessionStatus();
                    Toast.makeText(requireContext(), R.string.session_logout_done, Toast.LENGTH_SHORT).show();
                }
            });
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

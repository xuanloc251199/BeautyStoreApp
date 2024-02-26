package com.example.beautystoreapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.response.VerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private Runnable runnable;
    private final int DELAY = 30000; // 30 seconds
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        Button buttonCheckEmailVerified = findViewById(R.id.buttonCheckEmailVerified);
        buttonCheckEmailVerified.setOnClickListener(v -> checkEmailVerificationStatus());

        // Bắt đầu tự động kiểm tra xác thực
        startTime = System.currentTimeMillis();
        startCheckingEmailVerification();
    }

    private void checkEmailVerificationStatus() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        apiService.checkEmailVerification(email).enqueue(new Callback<VerificationResponse>() {
            @Override
            public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                if (response.isSuccessful() && response.body().isVerified()) {
                    // Email đã được xác thực, chuyển đến MainActivity
                    Intent intent = new Intent(EmailVerificationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Hiển thị thông báo hoặc cho phép gửi lại email xác thực
                    Toast.makeText(EmailVerificationActivity.this, "Email chưa được xác thực. Vui lòng kiểm tra lại.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerificationResponse> call, Throwable t) {
                Toast.makeText(EmailVerificationActivity.this, "Lỗi khi kiểm tra xác thực email.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCheckingEmailVerification() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - startTime > 300000) { // 5 phút
                    // Dừng kiểm tra sau 5 phút
                    handler.removeCallbacks(runnable);
                    return;
                }
                checkEmailVerificationStatus();
                // Lặp lại sau mỗi 30 giây
                handler.postDelayed(this, DELAY);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
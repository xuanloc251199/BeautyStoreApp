package com.example.beautystoreapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.beautystoreapp.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, 3000);


    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppName", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("token", null);
        if (accessToken == null) {
            // Người dùng chưa đăng nhập, chuyển hướng đến LoginActivity
            redirectToLoginActivity();
        } else {
            redirectToMainActivity();
        }
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Đảm bảo người dùng không trở lại màn hình trước bằng cách nhấn nút Back
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Đảm bảo người dùng không trở lại màn hình trước bằng cách nhấn nút Back
    }


}
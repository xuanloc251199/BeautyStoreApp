package com.example.beautystoreapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beautystoreapp.api.response.AuthResponse;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.databinding.ActivitySignUpBinding;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private EditText edtName, edtEmail, edtPassword, edtReenterPassword;
    private TextView tvStatus;
    private Button btnRegister;
    private String URL = "http://10.0.2.2/beauty_store_be/login.php";
    private String name, email, password, reenterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtEmail = binding.edtEmail;
        edtPassword = binding.edtPassword;
        edtName = binding.edtUsername;
        edtReenterPassword = binding.edtConfirmPassword;

        name = email = password = reenterPassword = "";

        setOnClickListener();
    }

    private void setOnClickListener() {

        binding.btnSignUp.setOnClickListener(v -> {
            registerUser();
        });

        binding.tvLogin.setOnClickListener(this::login);

    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String password_confirmation = edtReenterPassword.getText().toString().trim();

        // Khởi tạo Retrofit và AuthService
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

//        Users newUser = new Users(name, email, password, password_confirmation);
//
//        Call<AuthResponse> call = apiService.registerUser(name, email, password, password_confirmation);
//
//        call.enqueue(new Callback<AuthResponse>() {
//            @Override
//            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công. Vui lòng xác thực email.", Toast.LENGTH_LONG).show();
//                    // Chuyển qua EmailVerificationActivity
//                    Intent intent = new Intent(SignUpActivity.this, EmailVerificationActivity.class);
//                    intent.putExtra("email", email);
//                    startActivity(intent);
//
//
//                } else {
//                    // Xử lý lỗi
//                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AuthResponse> call, Throwable t) {
//                Log.e("API Error", t.getMessage());
//            }
//        });

        // Gọi API đăng ký
        Call<AuthResponse> call = apiService.registerUser(name, email, password, password_confirmation);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, retrofit2.Response<AuthResponse> response) {
                if (response.isSuccessful() && "success".equals(response.body().getStatus())) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    // Lưu dữ liệu người dùng
                    saveUserData(response.body());

                    // Chuyển hướng người dùng đến trang chính
                    redirectToHomePage();
                } else {
                    // Xử lý trường hợp đăng ký không thành công
                    Toast.makeText(SignUpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Đăng ký không thành công, hiển thị thông báo lỗi
                Toast.makeText(SignUpActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void saveUserData(AuthResponse authResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", "Bearer " + authResponse.getAccess_token());
        // Lưu thêm thông tin người dùng nếu cần
        editor.putString("user_name", authResponse.getUser().getName());
        editor.apply();
    }

    private void redirectToHomePage() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Đảm bảo người dùng không trở lại màn hình đăng ký bằng cách nhấn nút Back
    }

    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Chuyển đến MainActivity sau khi nhấn OK
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
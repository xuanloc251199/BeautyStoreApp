package com.example.beautystoreapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.databinding.ActivityDetailProductBinding;
import com.example.beautystoreapp.databinding.ActivityInfoAccountBinding;
import com.example.beautystoreapp.model.UserDetails;
import com.example.beautystoreapp.model.Users;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoAccountActivity extends AppCompatActivity {

    ActivityInfoAccountBinding binding;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setDefaultData();

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.btnSave.setOnClickListener(v -> {
            saveUserInfomation();
        });
    }

    private void saveUserInfomation() {
        UserDetails userDetails = new UserDetails(binding.edtName.getText().toString(), binding.edtAddress.getText().toString(), binding.edtPhoneNumber.getText().toString(), "Avatar");

        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateUserDetails("Bearer " + getTokenFromSharedPreferences(), userDetails);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(InfoAccountActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                Log.d("ApiINFO", response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InfoAccountActivity.this, "Cập nhật thông tin không thành công", Toast.LENGTH_SHORT).show();
                Log.e("ApiINFO error", t.getMessage());
            }
        });
    }

    private void setDefaultData(){
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Users> call = apiService.getUserInfo("Bearer " + getTokenFromSharedPreferences());
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Users user = response.body();
                    binding.tvEmail.setText(user.getEmail());
                    binding.edtName.setText(user.getName());
                    binding.edtAddress.setText((user.getUser_details() != null) ? user.getUser_details().getAddress() : "Địa chỉ không xác định");
                    binding.edtPhoneNumber.setText((user.getUser_details() != null) ? user.getUser_details().getPhone_number() : "Số điện thoại không xác định");
                } else {
                    // Xử lý lỗi
                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                // Xử lý lỗi
                Log.e("API Error", t.getMessage());
            }
        });
    }

    public String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppName", MODE_PRIVATE);
        return sharedPreferences.getString("token", ""); // Lấy token đã lưu
    }
}
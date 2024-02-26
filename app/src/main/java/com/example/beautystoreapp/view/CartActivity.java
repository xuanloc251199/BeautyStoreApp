package com.example.beautystoreapp.view;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.adapter.CartAdapter;
import com.example.beautystoreapp.adapter.CartProductAdapter;
import com.example.beautystoreapp.adapter.ProductAdapter;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.response.CartResponse;
import com.example.beautystoreapp.databinding.ActivityCartBinding;
import com.example.beautystoreapp.model.CartItem;
import com.example.beautystoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    private List<CartItem> product = new ArrayList<>();
    RecyclerView recyclerView;
    CartAdapter adapter;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickListener();

        setUpRecyclerView();
    }

    private void onClickListener() {
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        recyclerView = binding.rvCartProduct;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<CartResponse> call = apiService.showCartByUser("Bearer " + getTokenFromSharedPreferences());
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    product = response.body().getData();
                    adapter = new CartAdapter(CartActivity.this, product);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged(); // Thông báo dữ liệu đã thay đổi để cập nhật UI
                } else {
                    // Xử lý lỗi
                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
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

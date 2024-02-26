package com.example.beautystoreapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.R;
import com.example.beautystoreapp.adapter.CommentAdapter;
import com.example.beautystoreapp.api.request.AddToCartRequest;
import com.example.beautystoreapp.api.response.AddToCartResponse;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.response.CartItemCountResponse;
import com.example.beautystoreapp.databinding.ActivityDetailProductBinding;
import com.example.beautystoreapp.helper.CurrencyFormatter;
import com.example.beautystoreapp.model.Comment;
import com.example.beautystoreapp.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    private final String ARG_PRODUCT_ID = "idProduct";

    private int id;
    private ApiService apiService;

    private final List<Comment> comments = new ArrayList<>();

    private
    ActivityDetailProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        id = intent.getIntExtra(ARG_PRODUCT_ID, 0);

        binding.tvTest.setText(String.valueOf(id));


        onClickButton();

        getExtraData();

        setUpCommentRecyclerView();

        cartItemCount();
    }

    private void setUpCommentRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter adapter = new CommentAdapter(comments); // `comments` là danh sách comment đã tải từ API
        recyclerView.setAdapter(adapter);

        apiService.getCommentsByProductId(id).enqueue(new Callback<List<Comment>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comments.clear();
                    comments.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Cập nhật dữ liệu trên RecyclerView
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Xử lý khi request thất bại
            }
        });
    }

    private void onClickButton() {
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            addToCartPressed();
        });
    }

    private void addToCartPressed() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<AddToCartResponse> call = apiService.addToCart(getTokenFromSharedPreferences(), new AddToCartRequest(id, 1));
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi request thành công
                    cartItemCount();
                    Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", "Success: " + response.message());
                } else {
                    // Xử lý lỗi
                    Toast.makeText(DetailProductActivity.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.d("AddToCart", "Error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.d("AddToCart", "Failure: " + t.getMessage());
            }
        });
    }

    private void getExtraData() {

        apiService = RetrofitClient.getClient( ).create(ApiService.class);
        Call<Product> call = apiService.getProductsById(id);

        call.enqueue(new Callback<Product>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                Product product = response.body();

                if (response.isSuccessful()) {
                    if (product.getFeatured_image() != null && !product.getFeatured_image().isEmpty()) {
                        Log.e("IMG", product.getFeatured_image());
                        Glide.with(DetailProductActivity.this)
                                .load("file:///android_asset/" + product.getFeatured_image())
                                .into(binding.imgProduct); // imageViewProductImage là ImageView trong layout của item
                    }

                    binding.tvPrdName.setText(product.getName());
                    binding.tvPrdPrice.setText(CurrencyFormatter.formatVND(product.getPrice()));
                    binding.tvPrdDetail.setText(product.getDescription());

                    // Tạo chuỗi sao dựa trên số sao đánh giá
                    StringBuilder starsHtml = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        if (product.getStar() != null) {
                            if (i < product.getStar()) {
                                starsHtml.append("<font color='#FFD700'>&#9733;</font>"); // Màu vàng cho ngôi sao đã đánh giá
                            } else {
                                starsHtml.append("<font color='#FFFFFF'>&#9733;</font>"); // Màu trắng cho ngôi sao chưa đánh giá
                            }
                        } else i = 0;

                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        binding.tvRate.setText(Html.fromHtml(starsHtml.toString(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        binding.tvRate.setText(Html.fromHtml(starsHtml.toString()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });

    }

    public String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppName", MODE_PRIVATE);
        return sharedPreferences.getString("token", ""); // Lấy token đã lưu
    }

    public void updateCartItemCount(int itemCount) {
        if (itemCount > 0) {
            binding.tvItemCount.setText(String.valueOf(itemCount));
            binding.tvItemCount.setVisibility(View.VISIBLE);
        } else {
            binding.tvItemCount.setVisibility(View.GONE);
        }
    }

    private void cartItemCount() {
        apiService = RetrofitClient.getClient( ).create(ApiService.class);

        Call<CartItemCountResponse> call = apiService.getCartItemCount(getTokenFromSharedPreferences());
        call.enqueue(new Callback<CartItemCountResponse>() {
            @Override
            public void onResponse(Call<CartItemCountResponse> call, Response<CartItemCountResponse> response) {
                if (response.isSuccessful()) {
                    CartItemCountResponse itemCount = response.body();
                    updateCartItemCount(itemCount.getItem_count()); // Cập nhật UI
                } else {
                    // Xử lý lỗi
                    Log.e("CartItemCount", "Lỗi lấy số lượng sản phẩm trong giỏ hàng");
                }
            }

            @Override
            public void onFailure(Call<CartItemCountResponse> call, Throwable t) {
                // Xử lý lỗi
                Log.e("API Error", t.getMessage());
            }
        });
    }
}
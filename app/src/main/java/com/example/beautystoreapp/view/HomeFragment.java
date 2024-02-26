package com.example.beautystoreapp.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.adapter.ProductAdapter;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.response.CartItemCountResponse;
import com.example.beautystoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private View mView;
    private ApiService apiService;

    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private ImageView cartButton;
    private TextView tvItemCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        cartItemCount();

        setUpRecyclerView();

        loadProducts();

        onClickListener();

        return mView;
    }

    private void onClickListener() {

        cartButton = mView.findViewById(R.id.cartButton);
        tvItemCount = mView.findViewById(R.id.tvItemCount);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.rvProduct);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProducts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList.clear();
                productList.addAll(response.body());
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
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

    public void updateCartItemCount(int itemCount) {
        if (itemCount > 0) {
            tvItemCount.setText(String.valueOf(itemCount));
            tvItemCount.setVisibility(View.VISIBLE);
        } else {
            tvItemCount.setVisibility(View.GONE);
        }
    }

    public String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppName", MODE_PRIVATE);
        return sharedPreferences.getString("token", ""); // Lấy token đã lưu
    }
}
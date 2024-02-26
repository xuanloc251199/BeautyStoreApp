package com.example.beautystoreapp.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.adapter.CategoryPagerAdapter;
//import com.example.beautystoreapp.adapter.ProductsViewPagerAdapter;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.response.CartItemCountResponse;
import com.example.beautystoreapp.model.Category;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;

    private CategoryPagerAdapter adapter;

    private ApiService apiService;
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

        mView = inflater.inflate(R.layout.fragment_product, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartButton = mView.findViewById(R.id.cartButton);
        tvItemCount = mView.findViewById(R.id.tvItemCount);

        cartItemCount();

        onClickListener();

        tabLayout = mView.findViewById(R.id.tab_layout);
        viewPager = mView.findViewById(R.id.view_pager);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Gọi API để lấy danh sách category
        loadCategories();

    }
    private void onClickListener() {
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    for (Category category : categories) {
                        setupViewPager(viewPager, categories);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                } else {
                    // Xử lý trường hợp response không thành công
                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, List<Category> categories) {
        CategoryPagerAdapter adapter = new CategoryPagerAdapter(getFragmentManager(), categories);
        viewPager.setAdapter(adapter);
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
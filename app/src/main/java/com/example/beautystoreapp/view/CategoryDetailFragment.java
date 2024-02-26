package com.example.beautystoreapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.adapter.ProductAdapter;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryDetailFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "category_id";
    private int categoryId;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    private ApiService apiService;

    public static CategoryDetailFragment newInstance(int categoryId) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        recyclerView = view.findViewById(R.id.rvProductByCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);

        loadProductsByCategory(categoryId);
    }

    private void loadProductsByCategory(int categoryId) {
        apiService.getProductsByCategory(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    // Xử lý trường hợp không thành công
                    Log.e("API Response", "Code: " + response.code() + " Message: " + response.message());
                    return;
                }

                productList.clear();
                productList.addAll(response.body());
                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Xử lý lỗi
                Log.e("API Error", t.getMessage());
            }
        });
    }

}
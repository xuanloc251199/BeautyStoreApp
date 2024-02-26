package com.example.beautystoreapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.R;
import com.example.beautystoreapp.api.ApiService;
import com.example.beautystoreapp.api.RetrofitClient;
import com.example.beautystoreapp.api.request.AddToCartRequest;
import com.example.beautystoreapp.api.request.RemoveFromCartRequest;
import com.example.beautystoreapp.api.response.AddToCartResponse;
import com.example.beautystoreapp.api.response.CartItemCountResponse;
import com.example.beautystoreapp.api.response.CartResponse;
import com.example.beautystoreapp.api.response.RemoveFromCartResponse;
import com.example.beautystoreapp.helper.CurrencyFormatter;
import com.example.beautystoreapp.model.CartItem;
import com.example.beautystoreapp.model.Product;
import com.example.beautystoreapp.view.DetailProductActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private ApiService apiService;
    CartItem product;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItemList = cartItems;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        product = cartItemList.get(position);

        holder.tvPrdName.setText(product.getName());
        holder.tvPrdPrice.setText(CurrencyFormatter.formatVND(product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        holder.tvPrdTotalPrice.setText(CurrencyFormatter.formatVND(product.getTotalPrice()));

        if (product.getFeaturedImage() != null && !product.getFeaturedImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + product.getFeaturedImage())
                    .into(holder.imgPrd); // imageViewProductImage là ImageView trong layout của item
        }

        holder.cvCartProduct.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("idProduct", cartItemList.get(position).getProductId());
            context.startActivity(intent);
        });

        // Tạo chuỗi sao dựa trên số sao đánh giá
        StringBuilder starsHtml = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < product.getStar()) {
                starsHtml.append("<font color='#FFD700'>&#9733;</font>"); // Màu vàng cho ngôi sao đã đánh giá
            } else {
                starsHtml.append("<font color='#FFFFFF'>&#9733;</font>"); // Màu trắng cho ngôi sao chưa đánh giá
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.tvStar.setText(Html.fromHtml(starsHtml.toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvStar.setText(Html.fromHtml(starsHtml.toString()));
        }

        //Thêm sản phẩm
        holder.cvIncrement.setOnClickListener(v -> {
            apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<AddToCartResponse> call = apiService.addToCart(getTokenFromSharedPreferences(), new AddToCartRequest(cartItemList.get(position).getProductId(), 1));
            call.enqueue(new Callback<AddToCartResponse>() {
                @Override
                public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                    if (response.isSuccessful()) {
                        // Xử lý khi request thành công
                        Call<CartItemCountResponse> cartItemCountResponseCall = apiService.getCartItemCount(getTokenFromSharedPreferences());
                        cartItemCountResponseCall.enqueue(new Callback<CartItemCountResponse>() {
                            @Override
                            public void onResponse(Call<CartItemCountResponse> call, Response<CartItemCountResponse> response) {
                                if (response.isSuccessful()) {
                                    if (cartItemList.get(position).getQuantity() > 0) {
                                        Call<CartResponse> cartResponseCall = apiService.showCartByUser("Bearer " + getTokenFromSharedPreferences());
                                        cartResponseCall.enqueue(new Callback<CartResponse>() {
                                            @Override
                                            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                                                if (response.isSuccessful()) {
                                                    holder.tvQuantity.setText(String.valueOf(response.body().getData().get(position).getQuantity()));
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<CartResponse> call, Throwable t) {
                                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                                Log.d("AddToCart", "Failure: " + t.getMessage());
                                            }
                                        });
                                    }
                                } else {
                                    // Xử lý lỗi
                                    Log.e("CartItemCount", response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<CartItemCountResponse> call, Throwable t) {
                                // Xử lý lỗi
                                Log.e("API Error", t.getMessage());
                            }
                        });
                        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        Log.d("RemoveFromCart", "Success: " + response.message());
                    } else {
                        // Xử lý lỗi
                        Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("RemoveFromCart", "Error: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    Log.d("RemoveFromCart", "Failure: " + t.getMessage());
                }
            });

        });

        //Xoá sản phẩm
        holder.cvDecrement.setOnClickListener(v -> {
            apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<RemoveFromCartResponse> call = apiService.removeFromCart(getTokenFromSharedPreferences(), new RemoveFromCartRequest(cartItemList.get(position).getProductId(), 1));
            call.enqueue(new Callback<RemoveFromCartResponse>() {
                @Override
                public void onResponse(Call<RemoveFromCartResponse> call, Response<RemoveFromCartResponse> response) {
                    if (response.isSuccessful()) {
                        // Xử lý khi request thành công
                        if (cartItemList.get(position).getQuantity() > 0){

                            Call<CartItemCountResponse> cartItemCountResponseCall = apiService.getCartItemCount(getTokenFromSharedPreferences());
                            cartItemCountResponseCall.enqueue(new Callback<CartItemCountResponse>() {
                                @Override
                                public void onResponse(Call<CartItemCountResponse> call, Response<CartItemCountResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (cartItemList.get(position).getQuantity() > 0) {

                                            Call<CartResponse> cartResponseCall = apiService.showCartByUser("Bearer " + getTokenFromSharedPreferences());
                                            cartResponseCall.enqueue(new Callback<CartResponse>() {
                                                @Override
                                                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                                                    if (response.isSuccessful()) {
                                                        holder.tvQuantity.setText(String.valueOf(response.body().getData().get(position).getQuantity()));
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<CartResponse> call, Throwable t) {
                                                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                                    Log.d("RemoveFromCart", "Failure: " + t.getMessage());
                                                }
                                            });
                                        } else {
                                            cartItemList.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    } else{
                                        // Xử lý lỗi
                                        Log.e("CartItemCount", response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CartItemCountResponse> call, Throwable t) {
                                    // Xử lý lỗi
                                    Log.e("API Error", t.getMessage());
                                }
                            });
                        } else {
                        }
                    } else {
                        // Xử lý lỗi
                        Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("AddToCart", "Error: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RemoveFromCartResponse> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    Log.d("AddToCart", "Failure: " + t.getMessage());
                }
            });

        });

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("nameProduct", product.getName());
            intent.putExtra("priceProduct", product.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppName", MODE_PRIVATE);
        return sharedPreferences.getString("token", ""); // Lấy token đã lưu
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPrd;
        TextView tvPrdName, tvQuantity, tvPrdPrice, tvPrdTotalPrice, tvStar;
        CardView cvDecrement, cvIncrement, cvCartProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrd = itemView.findViewById(R.id.imgProduct);
            tvPrdName = itemView.findViewById(R.id.tvNameProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrdPrice = itemView.findViewById(R.id.tvPriceProduct);
            tvPrdTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStar = itemView.findViewById(R.id.tvStar);
            cvDecrement = itemView.findViewById(R.id.cvDecrement);
            cvIncrement = itemView.findViewById(R.id.cvIncrement);
            cvCartProduct = itemView.findViewById(R.id.cvCartProduct);
        }
    }
}

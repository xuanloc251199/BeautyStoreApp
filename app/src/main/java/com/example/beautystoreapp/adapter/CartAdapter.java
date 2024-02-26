package com.example.beautystoreapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.R;
import com.example.beautystoreapp.helper.CurrencyFormatter;
import com.example.beautystoreapp.model.CartItem;
import com.example.beautystoreapp.model.Product;
import com.example.beautystoreapp.view.DetailProductActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

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
        CartItem product = cartItemList.get(position);

        holder.tvPrdName.setText(product.getName());
        holder.tvPrdPrice.setText(CurrencyFormatter.formatVND(product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        holder.tvPrdTotalPrice.setText(CurrencyFormatter.formatVND(product.getTotalPrice()));

        if (product.getFeaturedImage() != null && !product.getFeaturedImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + product.getFeaturedImage())
                    .into(holder.imgPrd); // imageViewProductImage là ImageView trong layout của item
        }

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


//        holder.itemView.setOnClickListener(v -> {
//
//            Intent intent = new Intent(context, DetailProductActivity.class);
//            intent.putExtra("nameProduct", product.getName());
//            intent.putExtra("priceProduct", product.getPrice());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPrd;
        TextView tvPrdName, tvQuantity, tvPrdPrice, tvPrdTotalPrice, tvStar;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrd = itemView.findViewById(R.id.imgProduct);
            tvPrdName = itemView.findViewById(R.id.tvNameProduct);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrdPrice = itemView.findViewById(R.id.tvPriceProduct);
            tvPrdTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStar = itemView.findViewById(R.id.tvStar);
        }
    }
}

package com.example.beautystoreapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.helper.CurrencyFormatter;
import com.example.beautystoreapp.view.DetailProductActivity;
import com.example.beautystoreapp.R;
import com.example.beautystoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> productArrayList;

    public ProductAdapter(Context context, List<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Product> productArrayList){
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productArrayList.get(position);

        String description = product.getDescription();

        holder.tvPrdName.setText(product.getName());
        if (description != null && description.length() > 20) {
            description = description.substring(0, 20) + "...";
        }
        holder.tvPrdDetail.setText(description);
        holder.tvPrdPrice.setText(CurrencyFormatter.formatVND(product.getPrice()));
        if (product.getFeatured_image() != null && !product.getFeatured_image().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("file:///android_asset/" + product.getFeatured_image())
                    .into(holder.imgPrd); // imageViewProductImage là ImageView trong layout của item
        }

        // Tạo chuỗi sao dựa trên số sao đánh giá
        StringBuilder starsHtml = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (product.getStar() != null){
                if (i < product.getStar()) {
                    starsHtml.append("<font color='#FFD700'>&#9733;</font>"); // Màu vàng cho ngôi sao đã đánh giá
                } else {
                    starsHtml.append("<font color='#FFFFFF'>&#9733;</font>"); // Màu trắng cho ngôi sao chưa đánh giá
                }
            } else i = 0;

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.tvPrdRate.setText(Html.fromHtml(starsHtml.toString(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.tvPrdRate.setText(Html.fromHtml(starsHtml.toString()));
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("idProduct", product.getId());
            intent.putExtra("nameProduct", product.getName());
            intent.putExtra("detailProduct", product.getDescription());
            intent.putExtra("priceProduct", product.getPrice());
            if (product.getFeatured_image() != null && !product.getFeatured_image().isEmpty()) {
                Log.e("IMG", product.getFeatured_image());
                Glide.with(holder.itemView.getContext())
                        .load("file:///android_asset/" + product.getFeatured_image())
                        .into(holder.imgPrd); // imageViewProductImage là ImageView trong layout của item
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPrd;
        TextView tvPrdName, tvPrdDetail, tvPrdPrice, tvPrdRate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPrd = itemView.findViewById(R.id.img_product);
            tvPrdName = itemView.findViewById(R.id.tv_prdName);
            tvPrdDetail = itemView.findViewById(R.id.tv_prdDetail);
            tvPrdPrice = itemView.findViewById(R.id.tv_prdPrice);
            tvPrdRate = itemView.findViewById(R.id.tv_prdRate);

        }
    }
}

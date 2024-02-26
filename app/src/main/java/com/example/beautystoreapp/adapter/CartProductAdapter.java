package com.example.beautystoreapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.model.Product;
import com.example.beautystoreapp.view.DetailProductActivity;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;

    public CartProductAdapter(Context context, ArrayList<Product> productArrayList) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productArrayList.get(position);

        holder.tvPrdName.setText(product.getName());
        holder.tvPrdDetail.setText(product.getBarcode());
        holder.tvPrdPrice.setText(String.valueOf(product.getPrice()));
//        holder.imgPrd.setImageResource(product.getPrdImg());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("nameProduct", product.getName());
            intent.putExtra("detailProduct", product.getBarcode());
            intent.putExtra("priceProduct", product.getPrice());
//            intent.putExtra("imgProduct", product.getPrdImg());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPrd;
        TextView tvPrdName, tvPrdDetail, tvPrdPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPrd = itemView.findViewById(R.id.imgProduct);
            tvPrdName = itemView.findViewById(R.id.tvNameProduct);
            tvPrdPrice = itemView.findViewById(R.id.tvPriceProduct);

        }
    }
}

package com.example.beautystoreapp.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautystoreapp.R;
import com.example.beautystoreapp.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvFullName.setText(comment.getFullname());
        holder.tvComment.setText(comment.getDescription());
        // Tạo chuỗi sao dựa trên số sao đánh giá
        StringBuilder starsHtml = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < comment.getStar()) {
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
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvComment, tvStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvStar = itemView.findViewById(R.id.tvStar);
        }
    }
}


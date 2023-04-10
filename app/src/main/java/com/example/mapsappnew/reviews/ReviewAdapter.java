package com.example.mapsappnew.reviews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapsappnew.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ReviewModel> listReviewModel;

    public ReviewAdapter(List<ReviewModel> listReviewModel) {
        this.listReviewModel = listReviewModel;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModel model = listReviewModel.get(position);

        holder.totalStarRating.setRating(Float.parseFloat(String.valueOf(model.getTotalStarGiven())));
        holder.tvDescReview.setText(model.getReview());
        holder.venueRating.setText(String.valueOf(model.getTimeStamp()));
        holder.venueNameReview.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return listReviewModel.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.venue_name_review)
        TextView venueNameReview;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.total_star_rating)
        MaterialRatingBar totalStarRating;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.venue_rating)
        TextView venueRating;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_desc_review)
        TextView tvDescReview;


        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

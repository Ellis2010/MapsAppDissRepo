package com.example.mapsappnew.product;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mapsappnew.R;
import com.example.mapsappnew.reviews.reviewsClass;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class reviewClassAdapter extends RecyclerView.Adapter<reviewClassAdapter.ProductHolder> {

    public interface OnClickListenerAdapter {
        void onItemClicked(String product);
    }

    private OnClickListenerAdapter onClickListenerAdapter;
    private List<reviewsClass> listProductModel;


    public reviewClassAdapter(List<reviewsClass> listProductModel) {
        this.listProductModel = listProductModel;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_review_main, parent, false);
        return new ProductHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        reviewsClass model = listProductModel.get(position);

        holder.totalStarRating.setRating(Float.parseFloat(String.valueOf(model.getTotalRating())));
        holder.tvProductName.setText(((reviewsClass) model).getProductName());
        holder.cvItem.setOnClickListener(v -> {
            if (onClickListenerAdapter != null) {
                onClickListenerAdapter.onItemClicked(new Gson().toJson(model));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listProductModel.size();
    }

    public void setOnClickListenerAdapter(OnClickListenerAdapter onClickListenerAdapter) {
        this.onClickListenerAdapter = onClickListenerAdapter;
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.total_star_rating)
        MaterialRatingBar totalStarRating;
        @BindView(R.id.cv_item)
        CardView cvItem;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}






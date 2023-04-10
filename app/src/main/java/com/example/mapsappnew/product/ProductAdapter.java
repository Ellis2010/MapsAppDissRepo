package com.example.mapsappnew.product;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mapsappnew.R;
import com.example.mapsappnew.reviews.ProductModel;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    // Interface for handling item click events
    public interface OnClickListenerAdapter {
        void onItemClicked(String product);
    }

    private OnClickListenerAdapter onClickListenerAdapter;
    private List<ProductModel> listProductModel;

    // Constructor for the adapter
    public ProductAdapter(List<ProductModel> listProductModel) {
        this.listProductModel = listProductModel;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        // Bind the data to the view elements in each item
        ProductModel model = listProductModel.get(position);
        holder.totalStarRating.setRating(Float.parseFloat(String.valueOf(model.getTotalRating())));
        holder.tvProductName.setText(((ProductModel) model).getProductName());

        // Set an OnClickListener for the item
        holder.cvItem.setOnClickListener(v -> {
            if (onClickListenerAdapter != null) {
                onClickListenerAdapter.onItemClicked(new Gson().toJson(model));
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return listProductModel.size();
    }

    public void setOnClickListenerAdapter(OnClickListenerAdapter onClickListenerAdapter) {
        // Set the OnClickListener for the adapter
        this.onClickListenerAdapter = onClickListenerAdapter;
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {

        // Declare the view elements for each item in the RecyclerView
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.review_venue_name)
        TextView tvProductName;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.total_star_rating)
        MaterialRatingBar totalStarRating;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.cv_item)
        CardView cvItem;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}






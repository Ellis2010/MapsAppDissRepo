package com.example.mapsappnew.product;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapsappnew.R;
import com.example.mapsappnew.reviews.ProductModel;
import com.example.mapsappnew.reviews.ReviewActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainReviewActivity extends AppCompatActivity implements ProductAdapter.OnClickListenerAdapter {

    // View bindings using ButterKnife library
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_product)
    RecyclerView rvProduct;

    // Instance of FirebaseFirestore
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // Progress dialog to show loading state
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout of activity
        setContentView(R.layout.activity_review_main);

        // Bind view with ButterKnife
        ButterKnife.bind(this);

        // Initialize view components
        initView();

        // Get product data from Firestore database
        getProduct();

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            // Go back to map activity
            onBackPressed();
        });


    }

    // Method to initialize view components
    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Load data product ...");
        progressDialog.show();
    }

    // Method to get product data from Firestore database
    private void getProduct() {
        CollectionReference collectionReference = firebaseFirestore.collection("product");
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressDialog.dismiss();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<ProductModel> list = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Convert each document to a ProductModel object
                            ProductModel productModel = document.toObject(ProductModel.class);
                            list.add(productModel);
                        }
                        // Initialize RecyclerView with the product data list
                        initListProduct(list);
                    } else {
                        Toast.makeText(MainReviewActivity.this, "Product is empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainReviewActivity.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

    // Method to initialize RecyclerView with the product data list
    private void initListProduct(List<ProductModel> productModelList) {
        ProductAdapter adapter = new ProductAdapter(productModelList);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnClickListenerAdapter(this);
        rvProduct.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload product data when the activity is resumed
        getProduct();
    }

    @Override
    public void onItemClicked(String product) {
        // Start the ReviewActivity for the selected product
        ReviewActivity.start(this, product);
    }
}
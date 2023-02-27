package com.example.mapsappnew.product;

import android.app.ProgressDialog;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.example.mapsappnew.R;
import com.example.mapsappnew.reviews.ReviewActivity;
import com.example.mapsappnew.reviews.reviewsClass;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
//import com.putuguna.ratinggoogleplaystore.R;
//import com.putuguna.ratinggoogleplaystore.reviews.ProductModel;
//import com.putuguna.ratinggoogleplaystore.reviews.ReviewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainReviewActivity extends AppCompatActivity implements reviewClassAdapter.OnClickListenerAdapter {



        @BindView(R.id.rv_product)
        RecyclerView rvProduct;
        private reviewClassAdapter adapter;
        private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog;

        @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        getProduct();
    }

        private void initView () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Load data product ...");
        progressDialog.show();
    }

        private void getProduct () {
        CollectionReference collectionReference = firebaseFirestore.collection("com/example/mapsappnew/product");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressDialog.dismiss();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<reviewsClass> list = new ArrayList<>();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                reviewsClass productModel = document.toObject(reviewsClass.class);
                                list.add(productModel);
                            }
                            initListProduct(list);
                        } else {
                            Toast.makeText(MainReviewActivity.this, "Product is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainReviewActivity.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

        private void initListProduct (List<reviewsClass> productModelList) {
        adapter = new reviewClassAdapter(productModelList);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnClickListenerAdapter(this);
        rvProduct.setAdapter(adapter);
    }

        @Override
        protected void onResume () {
        super.onResume();
        getProduct();
    }

        @Override
        public void onItemClicked (String product){
        ReviewActivity.start(this, product);
    }


}


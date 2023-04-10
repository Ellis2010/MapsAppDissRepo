package com.example.mapsappnew.reviews;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapsappnew.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class ReviewActivity extends AppCompatActivity {

    // Constant string for the extra information about the product model passed between activities.
    public static String EXTRA_PRODUCT_MODEL = "EXTRA_PRODUCT_MODEL";

    // Binding the view elements with their respective ids from the xml layout using ButterKnife library.
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_total_number_rating)
    TextView venueTotalNumberRating;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.total_star_rating)
    MaterialRatingBar totalStarRating;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.totalAllVoters)
    TextView venueTotalVoters;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_percentage_5)
    LinearLayout llPercentage5;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.constrain_layout_5)
    ConstraintLayout constrainLayout5;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_percentage_4)
    LinearLayout llPercentage4;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.constrain_layout_4)
    ConstraintLayout constrainLayout4;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_percentage_3)
    LinearLayout llPercentage3;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.constrain_layout_3)
    ConstraintLayout constrainLayout3;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_percentage_2)
    LinearLayout llPercentage2;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.constrain_layout_2)
    ConstraintLayout constrainLayout2;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_percentage_1)
    LinearLayout llPercentage1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.constrain_layout_1)
    ConstraintLayout constrainLayout1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_review)
    RecyclerView rvReview;

    // Instance variables for ProgressDialog, Handler, ProductModel, and FirebaseFirestore.
    private ProgressDialog progressDialog;
    private final Handler handler = new Handler();
    private ProductModel productModelGlobal;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // This is a static method which is used to start the ReviewActivity class by passing the product model information
// using an Intent to start a new activity.
    public static void start(Context context, String productModel) {
        // Create an Intent to start the ReviewActivity class.
        Intent starter = new Intent(context, ReviewActivity.class);
        // Put the product model information as an extra to the Intent.
        starter.putExtra(EXTRA_PRODUCT_MODEL, productModel);
        // Start the ReviewActivity with the Intent.
        context.startActivity(starter);
    }

    // This method is called when the ReviewActivity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Bind the view elements with their respective ids from the xml layout using ButterKnife library.
        ButterKnife.bind(this);
        // Initialize the view elements.
        initView();
    }

    // This method is used to initialize the view elements and set the click listener for the "btn" button.
    private void initView() {
        progressDialog = new ProgressDialog(this);
        // Retrieve the product model information from the Intent and convert it to a ProductModel object.
        productModelGlobal = new Gson().fromJson(getIntent().getStringExtra(EXTRA_PRODUCT_MODEL), ProductModel.class);

        // Get a reference to the "btn" button and set the click listener to open the review dialog.
        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(v -> openDialogReview());

        // This code shows a progress dialog with a message while the width of the view is being counted.
        progressDialog.setMessage("Count Width Of View");
        progressDialog.show();

// This code is used to dismiss the progress dialog after a delay of 3 seconds and then calls the
// setRatingByColor and getAllReview methods.
        handler.postDelayed(() -> {
            progressDialog.dismiss();
            setRatingByColor(productModelGlobal);
            getAllReview(productModelGlobal.getIdProduct());
        }, 3000);
    }

    private void insertDataReview(ReviewModel review) {
        // Create a new ReviewModel object to avoid any potential reference issues
        ReviewModel reviewModel = new ReviewModel(review.getName(), review.getReview(), review.getTimeStamp(), review.getTotalStarGiven());

        // Get a reference to the product collection in Firestore
        CollectionReference collectionReference = firebaseFirestore.collection("product");

        // Get a reference to the specific document of the product being reviewed
        DocumentReference documentReference = collectionReference.document(productModelGlobal.getIdProduct());

        // Add the new review document to the product's "review" sub collection
        documentReference.collection("review")
                .add(reviewModel)
                .addOnSuccessListener(documentReference1 -> {
                    progressDialog.dismiss();
                    // After the review is added successfully, update the product's rating
                    updateRating(review, productModelGlobal);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ReviewActivity.this, "Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void updateRating(ReviewModel reviewModel, ProductModel productModel) {

        // Create a new ProductModel object to avoid any potential reference issues
        ProductModel rate = new ProductModel();

        // Copy the product's existing information
        rate.setIdProduct(productModel.getIdProduct());
        rate.setProductName(productModel.getProductName());

        // Initialize totalStars and totalVoters
        double totalStars;
        int totalVoters = 0;

// Check if the review gave 1 star
        if (reviewModel.getTotalStarGiven() == 1.0) {

            // Add 1 star to the product's current star1 count
            totalStars = 1.0 + (double) productModel.getStar1();
            rate.setStar1((int) totalStars);

            // Copy the product's existing counts for stars 2-5
            rate.setStar2(productModel.getStar2());
            rate.setStar3(productModel.getStar3());
            rate.setStar4(productModel.getStar4());
            rate.setStar5(productModel.getStar5());

            // Calculate the total number of voters for this product
            totalVoters = (int) (totalStars + productModel.getStar2() + productModel.getStar3() + productModel.getStar4() + productModel.getStar5());

            // If the product has no voters yet, set totalVoters to 1
            if (productModel.getTotalVoters() == 0) {
                rate.setTotalVoters(1);
            }
            // Otherwise, set totalVoters to the calculated total
            else {
                rate.setTotalVoters(totalVoters);
            }
            // Check if the review gave 2 stars
        } else if (reviewModel.getTotalStarGiven() == 2.0) {
            totalStars = 1.0 + (double) productModel.getStar2();
            rate.setStar1(productModel.getStar1());
            rate.setStar2((int) totalStars);
            rate.setStar3(productModel.getStar3());
            rate.setStar4(productModel.getStar4());
            rate.setStar5(productModel.getStar5());

            totalVoters = (int) (totalStars + productModel.getStar1() + productModel.getStar3() + productModel.getStar4() + productModel.getStar5());
            if (productModel.getTotalVoters() == 0) {
                rate.setTotalVoters(1);
            } else {
                rate.setTotalVoters(totalVoters);
            }
            // Check if the review gave 3 stars
        } else if (reviewModel.getTotalStarGiven() == 3.0) {
            totalStars = 1.0 + (double) productModel.getStar3();
            rate.setStar1(productModel.getStar1());
            rate.setStar2(productModel.getStar2());
            rate.setStar3((int) totalStars);
            rate.setStar4(productModel.getStar4());
            rate.setStar5(productModel.getStar5());

            totalVoters = (int) (totalStars + productModel.getStar1() + productModel.getStar2() + productModel.getStar4() + productModel.getStar5());
            if (productModel.getTotalVoters() == 0) {
                rate.setTotalVoters(1);
            } else {
                rate.setTotalVoters(totalVoters);
            }

            // Check if the review gave 4 stars
        } else if (reviewModel.getTotalStarGiven() == 4.0) {
            totalStars = 1.0 + (double) productModel.getStar4();
            rate.setStar1(productModel.getStar1());
            rate.setStar2(productModel.getStar2());
            rate.setStar3(productModel.getStar3());
            rate.setStar4((int) totalStars);
            rate.setStar5(productModel.getStar5());

            totalVoters = (int) (totalStars + productModel.getStar1() + productModel.getStar2() + productModel.getStar3() + productModel.getStar5());
            if (productModel.getTotalVoters() == 0) {
                rate.setTotalVoters(1);
            } else {
                rate.setTotalVoters(totalVoters);
            }
            // Check if the review gave 5 stars
        } else if (reviewModel.getTotalStarGiven() == 5.0) {
            totalStars = 1.0 + (double) productModel.getStar5();
            rate.setStar1(productModel.getStar1());
            rate.setStar2(productModel.getStar2());
            rate.setStar3(productModel.getStar3());
            rate.setStar4(productModel.getStar4());
            rate.setStar5((int) totalStars);

            totalVoters = (int) (totalStars + productModel.getStar1() + productModel.getStar2() + productModel.getStar3() + productModel.getStar4());
            if (productModel.getTotalVoters() == 0) {
                rate.setTotalVoters(1);
            } else {
                rate.setTotalVoters(totalVoters);
            }
        }

        //update rate
        int totalStar1 = rate.getStar1();
        int totalStar2 = rate.getStar2() * 2;
        int totalStar3 = rate.getStar3() * 3;
        int totalStar4 = rate.getStar4() * 4;
        int totalStar5 = rate.getStar5() * 5;

        // Calculate the total number of stars for this product
        double sumOfStars = totalStar1 + totalStar2 + totalStar3 + totalStar4 + totalStar5;
        double totalRating = sumOfStars / (double) totalVoters;
        DecimalFormat format = new DecimalFormat(".#");
        rate.setTotalRating(Double.parseDouble(format.format(totalRating)));

        //update product rating and total voters in firebase
        CollectionReference collectionReference = firebaseFirestore.collection("product");
        collectionReference.document(productModel.getIdProduct())
                .set(rate)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(ReviewActivity.this, "Successfully update Rating", Toast.LENGTH_SHORT).show();
                    productModelGlobal = rate;
                    setRatingByColor(rate);
                    getAllReview(productModel.getIdProduct());
                })
                // If the update fails, display a message to the user.
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ReviewActivity.this, "Failed Update Rating : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SetTextI18n")
    private void setRatingByColor(ProductModel productModel) {

        //get width of constraint layout
        int widthView = constrainLayout1.getWidth();

        //get total voters
        int totalAllVoters = productModel.getTotalVoters();
        int totalRateStar1 = productModel.getStar1();
        int totalRateStar2 = productModel.getStar2();
        int totalRateStar3 = productModel.getStar3();
        int totalRateStar4 = productModel.getStar4();
        int totalRateStar5 = productModel.getStar5();

        //convert to double


        //RATING STAR 1
        double sum1 = ((double) totalRateStar1 / (double) totalAllVoters);
        int rating1 = (int) (sum1 * widthView);
        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(rating1, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams1.setMargins(0, 5, 0, 5);
        llPercentage1.setBackgroundColor(Color.parseColor("#ff6f31"));
        llPercentage1.setLayoutParams(layoutParams1);

        //RATING STAR 2
        double sum2 = ((double) totalRateStar2 / (double) totalAllVoters);
        int rating2 = (int) (sum2 * widthView);
        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(rating2, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams2.setMargins(0, 5, 0, 5);
        llPercentage2.setBackgroundColor(Color.parseColor("#ff9f02"));
        llPercentage2.setLayoutParams(layoutParams2);

        //RATING STAR 3
        double sum3 = ((double) totalRateStar3 / (double) totalAllVoters);
        int rating3 = (int) (sum3 * widthView);
        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(rating3, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams3.setMargins(0, 5, 0, 5);
        llPercentage3.setBackgroundColor(Color.parseColor("#ffcf02"));
        llPercentage3.setLayoutParams(layoutParams3);

        //RATING STAR 4
        double sum4 = ((double) totalRateStar4 / (double) totalAllVoters);
        int rating4 = (int) (sum4 * widthView);
        ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(rating4, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams4.setMargins(0, 5, 0, 5);
        llPercentage4.setBackgroundColor(Color.parseColor("#9ace6a"));
        llPercentage4.setLayoutParams(layoutParams4);

        //RATING STAR 5
        double sum5 = ((double) totalRateStar5 / (double) totalAllVoters);
        int rating5 = (int) (sum5 * widthView);
        ConstraintLayout.LayoutParams layoutParams5 = new ConstraintLayout.LayoutParams(rating5, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams5.setMargins(0, 5, 0, 5);
        llPercentage5.setBackgroundColor(Color.parseColor("#57bb8a"));
        llPercentage5.setLayoutParams(layoutParams5);

        // Calculate the total number of stars for this product
        int intTotalStar2 = totalRateStar2 * 2;
        int intTotalStar3 = totalRateStar3 * 3;
        int intTotalStar4 = totalRateStar4 * 5;
        int intTotalStar5 = totalRateStar5 * 5;

        double sumTotal = totalRateStar1 +
                intTotalStar2 +
                intTotalStar3 +
                intTotalStar4 +
                intTotalStar5;

        // Calculate the average rating for this product
        double rating = (sumTotal / (double) totalAllVoters);
        DecimalFormat format = new DecimalFormat(".#");

        //set text total rating and total voters in text view
        venueTotalNumberRating.setText(format.format(rating));
        totalStarRating.setRating(Float.parseFloat(String.valueOf(rating)));
        totalStarRating.clearFocus();
        venueTotalVoters.setText(totalAllVoters + " total");


    }

    //open dialog review
    private void openDialogReview() {

        //create dialog
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review);

        //init view in dialog review layout
        EditText etReview = dialog.findViewById(R.id.et_review); // create these in dialog_review;
        EditText etName = dialog.findViewById(R.id.et_name);
        MaterialRatingBar rate = dialog.findViewById(R.id.rate_star);
        Button btnSubmitReview = dialog.findViewById(R.id.btn_send_review);

        //set on click listener for button submit review
        btnSubmitReview.setOnClickListener(v -> {
            dialog.dismiss();
            if (TextUtils.isEmpty(etReview.getText().toString())) {
                etReview.setError("Required field");
            } else {
                //show progress dialog
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                //create review model
                ReviewModel reviewModel = new ReviewModel();
                reviewModel.setName(etName.getText().toString());
                reviewModel.setReview(etReview.getText().toString());
                reviewModel.setTimeStamp(new Date());
                reviewModel.setTotalStarGiven(Math.round(rate.getRating()));
                insertDataReview(reviewModel);
            }
        });

        //show dialog
        dialog.show();
    }

    //insert data review to firestore
    private void getAllReview(String idProduct) {
        //show progress bar
        progressBar.setVisibility(View.VISIBLE);
        //hide recycler view
        rvReview.setVisibility(View.GONE);

        //get collection reference
        CollectionReference collectionReference = firebaseFirestore.collection("product");

        //get document reference
        DocumentReference documentReference = collectionReference.document(idProduct);


        documentReference.collection("review")
                .get()
                .addOnCompleteListener(task -> {
                    // When the data has been retrieved, hide the progress bar and show the review list
                    progressBar.setVisibility(View.GONE);
                    rvReview.setVisibility(View.VISIBLE);

                    if (task.getResult().isEmpty()) {
                        // If there are no reviews, do nothing
                    } else if (task.isSuccessful()) {
                        // If there are reviews, retrieve the data and populate the review list
                        List<ReviewModel> listReview = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            ReviewModel reviewModel = new ReviewModel();
                            try {
                                // Retrieve the data from the document snapshot and create a ReviewModel object
                                Timestamp timestamp = documentSnapshot.getTimestamp("timeStamp");
                                reviewModel.setName(Objects.requireNonNull(documentSnapshot.get("name")).toString());
                                reviewModel.setReview(Objects.requireNonNull(documentSnapshot.get("review")).toString());
                                assert timestamp != null;
                                reviewModel.setTimeStamp(timestamp.toDate());
                                reviewModel.setTotalStarGiven(Double.parseDouble(Objects.requireNonNull(documentSnapshot.get("totalStarGiven")).toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // Add the ReviewModel object to the list and populate the review list
                            listReview.add(reviewModel);
                            initListReview(listReview);
                        }
                    }
                })

                //
                .addOnFailureListener(e -> {

                });
    }

    //this method used to populating the recyclerview with data of reviews
    private void initListReview(List<ReviewModel> reviewModels) {
        ReviewAdapter adapter = new ReviewAdapter(reviewModels);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setAdapter(adapter);
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu from the XML resource file
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawer_menu, menu);

        // Return the boolean value indicating whether the menu was displayed
        // successfully or not
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* int id = item.getItemId();*/
       /* if (id == R.id.action_add_review) {
            openDialogReview();
        }*/
        return super.onOptionsItemSelected(item);
    }




}

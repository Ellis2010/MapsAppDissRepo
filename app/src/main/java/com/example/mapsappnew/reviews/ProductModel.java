package com.example.mapsappnew.reviews;

/**
 * The `ProductModel` class represents a product that can be reviewed by users.
 */
public class ProductModel {

    // Private member variables to store the product's properties
    private String idProduct;
    private int totalVoters;
    private double totalRating;
    private int star1;
    private int star2;
    private int star3;
    private int star4;
    private int star5;
    private String productName;

    /**
     * Constructs a `ProductModel` object with the specified properties.
     *
     * @param totalVoters the total number of users who have reviewed the product
     * @param totalRating the average rating of the product based on user reviews
     * @param star1 the number of one-star ratings the product has received
     * @param star2 the number of two-star ratings the product has received
     * @param star3 the number of three-star ratings the product has received
     * @param star4 the number of four-star ratings the product has received
     * @param star5 the number of five-star ratings the product has received
     * @param productName the name of the product
     */
    public ProductModel(int totalVoters, double totalRating, int star1, int star2, int star3, int star4, int star5, String productName) {
        this.totalVoters = totalVoters;
        this.totalRating = totalRating;
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.star5 = star5;
        this.productName = productName;
    }

    /**
     * Constructs a `ProductModel` object with default values for all properties.
     */
    public ProductModel() {
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getTotalVoters() {
        return totalVoters;
    }

    public void setTotalVoters(int totalVoters) {
        this.totalVoters = totalVoters;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public int getStar1() {
        return star1;
    }

    public void setStar1(int star1) {
        this.star1 = star1;
    }

    public int getStar2() {
        return star2;
    }

    public void setStar2(int star2) {
        this.star2 = star2;
    }

    public int getStar3() {
        return star3;
    }

    public void setStar3(int star3) {
        this.star3 = star3;
    }

    public int getStar4() {
        return star4;
    }

    public void setStar4(int star4) {
        this.star4 = star4;
    }

    public int getStar5() {
        return star5;
    }

    public void setStar5(int star5) {
        this.star5 = star5;
    }



}

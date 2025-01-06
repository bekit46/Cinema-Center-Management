package com.group15;

public class Movie {
    private int movieId;
    private String poster;
    private String title;
    private String genre;
    private String summary;
    private int price;
    private int discount;
    private int tax;

    // No-argument constructor (required)
    public Movie() {}

    // Parameterized constructor
    public Movie(int movieId, String poster, String title, String genre, String summary,int price, int discount, int tax) {
        this.movieId = movieId;
        this.poster = poster;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.price = price;
        this.discount = discount;
        this.tax = tax;
    }

    // Getters Setters
    public int getMovieId() { return movieId;}
    public void setMovieId(int movieId) {this.movieId = movieId;}
    public String getTitle() { return title;}
    public void setTitle(String title) {this.title = title;}
    public String getGenre() { return genre;}
    public void setGenre(String genre) {this.genre = genre;}
    public String getSummary() { return summary;}
    public void setSummary(String summary) {this.summary = summary;}
    public int getPrice() { return price;}
    public void setPrice(int price) {
        if(price >= 0)
            this.price = price;
    }
    public int getDiscount() { return discount;}
    public void setDiscount(int discount) {
        if(discount >= 0)
            this.discount = discount;
    }
    public int getTax() { return tax;}
    public void setTax(int tax) {
        if(tax >= 0)
            this.tax = tax;}

    public String getPoster() {return poster;}
    public void setPoster(String poster){this.poster = poster;}
}

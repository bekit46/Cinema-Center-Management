package com.group15;

/**
 * The {@code Movie} class represents a movie entity with details such as ID, title, genre, summary,
 * price, discount, tax, and poster image.
 */
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

    /**
     * Parameterized constructor for creating a {@code Movie} instance with specific details.
     *
     * @param movieId  The unique identifier for the movie.
     * @param poster   The URL or file path of the movie's poster image.
     * @param title    The title of the movie.
     * @param genre    The genre of the movie.
     * @param summary  A brief summary or description of the movie.
     * @param price    The price of the movie.
     * @param discount The discount amount for the movie.
     * @param tax      The tax amount applicable to the movie.
     */
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
    /**
     * Gets the unique identifier for the movie.
     *
     * @return The movie ID.
     */
    public int getMovieId() { return movieId;}
    /**
     * Sets the unique identifier for the movie.
     *
     * @param movieId The movie ID.
     */
    public void setMovieId(int movieId) {this.movieId = movieId;}
    /**
     * Gets the title of the movie.
     *
     * @return The movie title.
     */
    public String getTitle() { return title;}
    /**
     * Sets the title of the movie.
     *
     * @param title The movie title.
     */
    public void setTitle(String title) {this.title = title;}
    /**
     * Gets the genre of the movie.
     *
     * @return The movie genre.
     */
    public String getGenre() { return genre;}
    /**
     * Sets the genre of the movie.
     *
     * @param genre The movie genre.
     */
    public void setGenre(String genre) {this.genre = genre;}
    /**
     * Gets a brief summary of the movie.
     *
     * @return The movie summary.
     */
    public String getSummary() { return summary;}
    /**
     * Sets a brief summary of the movie.
     *
     * @param summary The movie summary.
     */
    public void setSummary(String summary) {this.summary = summary;}
    /**
     * Gets the price of the movie.
     *
     * @return The movie price.
     */
    public int getPrice() { return price;}
    /**
     * Sets the price of the movie.
     *
     * @param price The movie price.
     */
    public void setPrice(int price) {this.price = price;}
    /**
     * Gets the discount amount for the movie.
     *
     * @return The movie discount.
     */
    public int getDiscount() { return discount;}
    /**
     * Sets the discount amount for the movie.
     *
     * @param discount The movie discount.
     */
    public void setDiscount(int discount) {this.discount = discount;}
    /**
     * Gets the tax amount applicable to the movie.
     *
     * @return The movie tax.
     */
    public int getTax() { return tax;}
    /**
     * Sets the tax amount applicable to the movie.
     *
     * @param tax The movie tax.
     */
    public void setTax(int tax) {this.tax = tax;}
    /**
     * Gets the URL or file path of the movie's poster image.
     *
     * @return The movie poster.
     */
    public String getPoster() {return poster;}
    /**
     * Sets the URL or file path of the movie's poster image.
     *
     * @param poster The movie poster.
     */
    public void setPoster(String poster){this.poster = poster;}
}

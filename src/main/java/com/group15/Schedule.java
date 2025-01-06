package com.group15;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class Schedule {
    private int scheduleId;
    private int movieId;
    private Date date;
    private Time sessionTime;
    private String hall;
    private int takenSeats;
    private String seating;
    private Movie movie;

    // No-argument constructor (required)
    public Schedule() {}

    // Parameterized constructor
    public Schedule(int scheduleId, int movieId, Date date, Time sessionTime, String hall, int takenSeats, String seating, Movie movie) {
        this.scheduleId = scheduleId;
        this.movieId = movieId;
        this.date = date;
        this.sessionTime = sessionTime;
        this.hall = hall;
        this.takenSeats = takenSeats;
        this.seating = seating;
        this.movie = movie;
    }

    // Getters Setters
    public int getMovieId() { return movieId;}
    public void setMovieId(int movieId) {this.movieId = movieId;}
    public int getScheduleId() { return scheduleId;}
    public void setScheduleId(int scheduleId) {this.scheduleId = scheduleId;}
    public Date getDate() { return date;}
    public void setDate(Date date) {this.date = date;}
    public Time getSessionTime() { return sessionTime;}
    public void setSessionTime(Time sessionTime) {this.sessionTime = sessionTime;}
    public String getHall() { return hall;}
    public void setHall(String hall) {
        if(Objects.equals(hall, "Hall A") || Objects.equals(hall, "Hall B"))
            this.hall = hall;
    }
    public int getTakenSeats() { return takenSeats;}
    public void setTakenSeats(int takenSeats) {this.takenSeats = takenSeats;}
    public String getSeating() { return seating;}
    public void setSeating(String seating) {this.seating = seating;}
    public Movie getSeatMovie() { return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}
    public String getTitle(){return movie.getTitle();}
}

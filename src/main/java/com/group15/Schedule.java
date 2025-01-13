/**
 * The Schedule class represents a schedule for a movie, including its ID,
 * associated movie, date, time, hall, seating arrangement, and other relevant details.
 */
package com.group15;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

/**
 * Represents a schedule for a movie showing in a cinema.
 */
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
    /**
     * Constructs a Schedule with all its properties.
     *
     * @param scheduleId  The unique ID of the schedule.
     * @param movieId     The unique ID of the associated movie.
     * @param date        The date of the movie showing.
     * @param sessionTime The time of the movie session.
     * @param hall        The hall where the movie is being shown.
     * @param takenSeats  The number of taken seats for the session.
     * @param seating     The seating arrangement.
     * @param movie       The associated Movie object.
     */
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
    /**
     * Gets the unique ID of the movie associated with this schedule.
     *
     * @return The movie ID.
     */
    public int getMovieId() { return movieId;}
    /**
     * Sets the unique ID of the movie associated with this schedule.
     *
     * @param movieId The movie ID to set.
     */
    public void setMovieId(int movieId) {this.movieId = movieId;}
    /**
     * Gets the unique ID of the schedule.
     *
     * @return The schedule ID.
     */
    public int getScheduleId() { return scheduleId;}
    /**
     * Sets the unique ID of the schedule.
     *
     * @param scheduleId The schedule ID to set.
     */
    public void setScheduleId(int scheduleId) {this.scheduleId = scheduleId;}
    /**
     * Gets the date of the movie showing.
     *
     * @return The date of the session.
     */
    public Date getDate() { return date;}
    /**
     * Sets the date of the movie showing.
     *
     * @param date The date to set.
     */
    public void setDate(Date date) {this.date = date;}
    /**
     * Gets the time of the movie session.
     *
     * @return The session time.
     */
    public Time getSessionTime() { return sessionTime;}

    /**
     * Sets the time of the movie session.
     *
     * @param sessionTime The session time to set.
     */
    public void setSessionTime(Time sessionTime) {this.sessionTime = sessionTime;}
    /**
     * Gets the hall where the movie is being shown.
     *
     * @return The hall name.
     */
    public String getHall() { return hall;}
    /**
     * Sets the hall where the movie is being shown.
     * Only accepts "Hall A" or "Hall B".
     *
     * @param hall The hall name to set.
     */
    public void setHall(String hall) {
        if(Objects.equals(hall, "Hall A") || Objects.equals(hall, "Hall B"))
            this.hall = hall;
    }
    /**
     * Gets the number of taken seats for the session.
     *
     * @return The number of taken seats.
     */
    public int getTakenSeats() { return takenSeats;}
    /**
     * Sets the number of taken seats for the session.
     *
     * @param takenSeats The number of taken seats to set.
     */
    public void setTakenSeats(int takenSeats) {this.takenSeats = takenSeats;}
    /**
     * Gets the seating arrangement for the session.
     *
     * @return The seating arrangement.
     */
    public String getSeating() { return seating;}
    /**
     * Sets the seating arrangement for the session.
     *
     * @param seating The seating arrangement to set.
     */
    public void setSeating(String seating) {this.seating = seating;}
    /**
     * Gets the Movie object associated with this schedule.
     *
     * @return The associated Movie object.
     */
    public Movie getSeatMovie() { return movie;}
    /**
     * Sets the Movie object associated with this schedule.
     *
     * @param movie The Movie object to associate.
     */
    public void setMovie(Movie movie) {this.movie = movie;}
    /**
     * Gets the title of the movie associated with this schedule.
     *
     * @return The movie title.
     */
    public String getTitle(){return movie.getTitle();}
}

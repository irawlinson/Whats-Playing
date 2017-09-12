package com.example.isaiahrawlinson.whatsplaying;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaiahrawlinson on 5/5/17.
 */

class Movie implements Serializable {
    private static final String API_KEY = "ehazbrzyrqeg5zfpx93jfdkt";

    private String tmsId;
    private String rootId;
    private String title;
    private ArrayList<String> genres;
    private String longDescription, shortDescription;
    private ArrayList<String> topCast, directors;
    private ArrayList<Showtime> showtimes;
    private String preferredImage;

    Movie() {
        this.tmsId = "";
        this.rootId = "";
        this.title = "";
        this.genres = new ArrayList<>();
        this.longDescription = "";
        this.shortDescription = "";
        this.topCast = new ArrayList<>();
        this.directors = new ArrayList<>();
        this.showtimes = new ArrayList<>();
    }

    Movie(String tmsId, String rootId, String title, ArrayList<String> genres,
          String longDescription, String shortDescription, ArrayList<String> topCast,
          ArrayList<String> directors, ArrayList<Showtime> showtimes) {
        this.tmsId = tmsId;
        this.rootId = rootId;
        this.title = title;
        this.genres = genres;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.topCast = topCast;
        this.directors = directors;
        this.showtimes = showtimes;
    }

    String getTmsId() {
        return tmsId;
    }

    void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    String getRootId() {
        return rootId;
    }

    void setRootId(String rootId) {
        this.rootId = rootId;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    ArrayList<String> getGenres() {
        return genres;
    }

    void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    String getLongDescription() {
        return longDescription;
    }

    void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    String getShortDescription() {
        return shortDescription;
    }

    void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    ArrayList<String> getTopCast() {
        return topCast;
    }

    void setTopCast(ArrayList<String> topCast) {
        this.topCast = topCast;
    }

    ArrayList<String> getDirectors() {
        return directors;
    }

    void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    ArrayList<Showtime> getShowtimes() {
        return showtimes;
    }

    void setShowtimes(ArrayList<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    String getPreferredImage() {
        return this.preferredImage;
    }

    void setPreferredImage(String uri) {
        this.preferredImage = "http://developer.tmsimg.com/" + uri + "?api_key=" + API_KEY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (getTmsId() != null ? !getTmsId().equals(movie.getTmsId()) : movie.getTmsId() != null)
            return false;
        if (getRootId() != null ? !getRootId().equals(movie.getRootId()) : movie.getRootId() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(movie.getTitle()) : movie.getTitle() != null)
            return false;
        if (getGenres() != null ? !getGenres().equals(movie.getGenres()) : movie.getGenres() != null)
            return false;
        if (getLongDescription() != null ? !getLongDescription().equals(movie.getLongDescription()) : movie.getLongDescription() != null)
            return false;
        if (getShortDescription() != null ? !getShortDescription().equals(movie.getShortDescription()) : movie.getShortDescription() != null)
            return false;
        if (getTopCast() != null ? !getTopCast().equals(movie.getTopCast()) : movie.getTopCast() != null)
            return false;
        if (getDirectors() != null ? !getDirectors().equals(movie.getDirectors()) : movie.getDirectors() != null)
            return false;
        return getShowtimes() != null ? getShowtimes().equals(movie.getShowtimes()) : movie.getShowtimes() == null;

    }

    @Override
    public String toString(){
       return title + "\n"
               + shortDescription + "\n";
    }
}

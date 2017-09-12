package com.example.isaiahrawlinson.whatsplaying;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by isaiahrawlinson on 5/5/17.
 */

class Showtime implements Serializable {
    private static final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    //private static final DateFormat outputFormat = new SimpleDateFormat("EEE, MMM d, HH:mm");
    private static final DateFormat outputFormat = new SimpleDateFormat("h:mm a");
    private Theatre theatre;
    //private String dateTime;
    private Date dateTime;

    Showtime() {
        this.theatre = new Theatre();
        this.dateTime = null;
    }

    Showtime(Theatre theatre, String dateTime) {
        this.theatre = theatre;
        try {
            Date parsedDate = inputFormat.parse(dateTime);
            this.dateTime = outputFormat.parse(outputFormat.format(parsedDate));
        } catch (ParseException e) {
            Log.e("Showtime Constructor", e.getMessage());
        }

    }

    Theatre getTheatre() {
        return theatre;
    }

    void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    String getDateTime() {
        return outputFormat.format(dateTime);
    }

    void setDateTime(String dateTime) {
        try {
            Date parsedDate = inputFormat.parse(dateTime);
            this.dateTime = outputFormat.parse(outputFormat.format(parsedDate));
        } catch (ParseException e) {
            Log.e("setDateTime", e.getMessage());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Showtime)) return false;

        Showtime showtime = (Showtime) o;

        if (getTheatre() != null ? !getTheatre().equals(showtime.getTheatre()) : showtime.getTheatre() != null)
            return false;
        return getDateTime() != null ? getDateTime().equals(showtime.getDateTime()) : showtime.getDateTime() == null;

    }

    @Override
    public String toString(){
        return outputFormat.format(dateTime) + " - " + theatre;
    }
}

package com.example.isaiahrawlinson.whatsplaying;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovieView extends DialogFragment {

    TextView title, genres, directors, stars, description;
    Movie movie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_view, container, false);

        title = (TextView) v.findViewById(R.id.movie_view_title);
        genres = (TextView) v.findViewById(R.id.movie_view_genres);
        directors = (TextView) v.findViewById(R.id.movie_view_director);
        stars = (TextView) v.findViewById(R.id.movie_view_stars);
        description = (TextView) v.findViewById(R.id.movie_view_description);

        movie = new Movie();
        movie = (Movie) getArguments().getSerializable("movieSelected");

        try {
            title.setText(movie.getTitle());
            genres.setText(movie.getGenres().toString()
                    .substring(1, movie.getGenres().toString().length() - 1));
            directors.setText(movie.getDirectors().toString()
                    .substring(1, movie.getDirectors().toString().length() - 1));
            stars.setText(movie.getTopCast().toString()
                    .substring(1, movie.getTopCast().toString().length() - 1));
            description.setText(movie.getLongDescription());
        } catch (Exception e) {
            Log.e("MovieView", e.getMessage());
        }

        return v;
    }

}

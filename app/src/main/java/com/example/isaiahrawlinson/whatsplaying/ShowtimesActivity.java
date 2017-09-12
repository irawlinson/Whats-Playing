package com.example.isaiahrawlinson.whatsplaying;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowtimesActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ShowtimesActivity";
    private static final String defaultZip = "02135";
    ListView showtimeList;
    Movie movieSelected;
    ArrayList<Showtime> showtimes;
    String lat, lng;
    String theatreQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);
        showtimeList = (ListView) findViewById(R.id.showtime_list);
        movieSelected = new Movie();
        showtimes = new ArrayList<>();
        lat = "";
        lng = "";

        try {
            movieSelected = (Movie) getIntent().getSerializableExtra("movieSelected");
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            showtimes = movieSelected.getShowtimes();

            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Showtimes");

            ShowtimeAdapter adapter = new ShowtimeAdapter(getApplicationContext(), showtimes);
            showtimeList.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting extras");
        }

        setListViewHeader(showtimeList);

        showtimeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri mapIntentUri;

                theatreQuery = "?q=" + showtimes.get(position - 1)
                        .getTheatre().getName().replaceAll("\\s+", "+").toLowerCase();


                if (lat != null && lng != null) {
                    if (!lat.isEmpty() && !lng.isEmpty()) {
                        mapIntentUri = Uri.parse("geo:" + lat + "," + lng + theatreQuery);
                    } else
                        mapIntentUri = Uri.parse("geo:0,0" + theatreQuery + "+" + defaultZip);
                } else {
                    mapIntentUri = Uri.parse("geo:0,0" + theatreQuery + "+" + defaultZip);
                }

                Log.e("mapIntentUri", mapIntentUri.toString());

                Intent mapActivity = new Intent(Intent.ACTION_VIEW, mapIntentUri);
                mapActivity.setPackage("com.google.android.apps.maps");

                startActivity(mapActivity);
            }
        });
    }

    private void setListViewHeader(final ListView listView){
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(
                R.layout.showtime_header, listView, false);

        final TextView title = (TextView) header.findViewById(R.id.st_header_title);
        final TextView genres = (TextView) header.findViewById(R.id.st_header_genres);
        final TextView directors = (TextView) header.findViewById(R.id.st_header_director);
        final TextView stars = (TextView) header.findViewById(R.id.st_header_stars);
        final TextView description = (TextView) header.findViewById(R.id.st_header_description);

        title.setText(movieSelected.getTitle());
        genres.setText(movieSelected.getGenres().toString()
                .substring(1, movieSelected.getGenres().toString().length() - 1));
        directors.setText(movieSelected.getDirectors().toString()
                .substring(1, movieSelected.getDirectors().toString().length() - 1));
        stars.setText(movieSelected.getTopCast().toString()
                .substring(1, movieSelected.getTopCast().toString().length() - 1));
        description.setText(movieSelected.getLongDescription());

        listView.addHeaderView(header, null, false);
    }
}

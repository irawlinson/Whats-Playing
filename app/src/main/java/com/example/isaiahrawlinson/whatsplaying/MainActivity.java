package com.example.isaiahrawlinson.whatsplaying;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String GRACENOTE_API_KEY = "ehazbrzyrqeg5zfpx93jfdkt";

    private static final String githubSampleData =
            "https://gist.githubusercontent.com/irawlinson/7c6f19c9a44bd9a27edc644eedbe3319/raw/ff7bfcae5869ef3796d4966c93522a5ddcdb4070/sampleData.json";

    private static final String githubSampleDataBrighton =
            "https://gist.githubusercontent.com/irawlinson/ae4f6b7de460fdaf8a01c23af5cba0d0/raw/184e9851ead09abaacb71fe712e980b611cd0c72/graceNoteMay9Data.json";
            //"https://gist.githubusercontent.com/irawlinson/6f99243d7f7a99312aab49742ef59724/raw/d2690e1a53e3fe6965ede61922c117d4e90cadc2/sampleDataBrighton.json";

    private static final String GracenoteTestUrl =
            "http://data.tmsapi.com/v1.1/movies/showings?startDate=2017-05-06&zip=02135&api_key="
                    + GRACENOTE_API_KEY;
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101;

    private static final String gnUrlStart = "http://data.tmsapi.com/v1.1/movies/showings?startDate=";
    private static final String latParam = "&lat=";
    private static final String lngParam = "&lng=";
    private static final String apiParam = "&api_key=" + GRACENOTE_API_KEY;

    String gracenoteUrl;

    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<Movie> movies;
    ListView moviesNearbyList;
    GoogleApiClient googleApiClient;
    String today;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ProgressBar progressBar;
    DatabaseHelper db;

    Location myLocation;
    Boolean locSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviesNearbyList = (ListView) findViewById(R.id.movies_nearby_list);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_local_movies);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        locSet = false;
        today = sdf.format(new Date());

        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        requestMapsPermissions();

        moviesNearbyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayShowtimes(position);
            }
        });

        moviesNearbyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.main_activity_long_click);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_main_add_to_wl:
                                db.addToWatchList(movies.get(position));
                                break;
                            case R.id.menu_main_add_to_favs:
                                db.addToFavs(movies.get(position));
                                break;
                        }

                        return false;
                    }
                });

                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
        db = new DatabaseHelper(getApplicationContext());
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_local_movies) {

        } else if (id == R.id.nav_watchlist) {
            Intent watchList = new Intent(MainActivity.this, WatchListActivity.class);
            startActivity(watchList);
        } else if (id == R.id.nav_favorites) {
            Intent favorites = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(favorites);
        } else if (id == R.id.nav_games) {
            Intent gamesActivity = new Intent(MainActivity.this, Games.class);
            startActivity(gamesActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayShowtimes(int position){
        Intent showtimesActivity = new Intent(MainActivity.this, ShowtimesActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("movieSelected", movies.get(position));
        if (locSet){
            data.putString("lat", String.valueOf(myLocation.getLatitude()));
            data.putString("lng", String.valueOf(myLocation.getLongitude()));
        }
        showtimesActivity.putExtras(data);
        startActivity(showtimesActivity);
    }

    private class DownloadJSON extends AsyncTask<Void, Integer, Void> {
        ProgressBar bar;
        DownloadJSON(ProgressBar bar) { this.bar = bar; }

        @Override
        protected Void doInBackground(Void... params) {
            movies = new ArrayList<>();
            try {
                // TODO change this back for presentation
                //jsonArray = JSONGracenote.getJSONfromURL(gracenoteUrl);// out of api calls
                jsonArray = JSONGracenote.getJSONfromURL(githubSampleDataBrighton);
                //jsonArray = JSONGracenote.getJSONfromURL(githubSampleData);

                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();
                    try {
                        movie.setTmsId(jsonObject.optString("tmsId"));
                    } catch (Exception e) {
                        Log.e("tmsId", e.getMessage());
                    }
                    try {
                        movie.setRootId(jsonObject.optString("rootId"));
                    } catch (Exception e) {
                        Log.e("rootId", e.getMessage());
                    }
                    try {
                        movie.setTitle(jsonObject.optString("title"));
                    } catch (Exception e) {
                        Log.e("title", e.getMessage());
                    }

                    try {
                        JSONArray jsonGenres = jsonObject.getJSONArray("genres");
                        ArrayList<String> genres = new ArrayList<>();
                        for (int j = 0; j < jsonGenres.length(); j++)
                            genres.add(jsonGenres.getString(j));
                        movie.setGenres(genres);
                    } catch (Exception e) {
                        Log.e("Parsing genres", "movie: " + movie.getTitle() + "\n" + e.getMessage());
                    }

                    try {
                        movie.setLongDescription(jsonObject.optString("longDescription"));
                    } catch (Exception e) {
                        Log.e("longDescription", e.getMessage());
                    }
                    try {
                        movie.setShortDescription(jsonObject.optString("shortDescription"));
                    } catch (Exception e) {
                        Log.e("shortDescription", e.getMessage());
                    }

                    try {
                        JSONArray jsonTopCast = jsonObject.getJSONArray("topCast");
                        ArrayList<String> topCast = new ArrayList<>();
                        for (int j = 0; j < jsonTopCast.length(); j++)
                            topCast.add(jsonTopCast.getString(j));
                        movie.setTopCast(topCast);
                    } catch (Exception e) {
                        Log.e("Parsing topCast", "movie: " + movie.getTitle() + "\n" + e.getMessage());
                    }

                    try {
                        JSONArray jsonDirectors = jsonObject.getJSONArray("directors");
                        ArrayList<String> directors = new ArrayList<>();
                        for (int j = 0; j < jsonDirectors.length(); j++)
                            directors.add(jsonDirectors.getString(j));
                        movie.setDirectors(directors);
                    } catch (Exception e) {
                        Log.e("Parsing directors", "movie: " + movie.getTitle() + "\n" + e.getMessage());
                    }

                    JSONArray jsonShowtimes = jsonObject.getJSONArray("showtimes");
                    ArrayList<Showtime> showtimes = new ArrayList<>();
                    for (int j = 0; j < jsonShowtimes.length(); j++){

                        JSONObject jsonShowtime = jsonShowtimes.getJSONObject(j);
                        Showtime showtime = new Showtime();

                        JSONObject jsonTheatre = jsonShowtime.getJSONObject("theatre");
                        Theatre theatre = new Theatre();
                        theatre.setId(jsonTheatre.optString("id"));
                        theatre.setName(jsonTheatre.optString("name"));
                        showtime.setTheatre(theatre);
                        showtime.setDateTime(jsonShowtime.optString("dateTime"));

                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        Date today = sdf.parse(sdf.format(new Date()));

                        if (today.compareTo(sdf.parse(showtime.getDateTime())) <= 0)
                            showtimes.add(showtime);
                    }

                    try {
                        String preferredImage = jsonObject.getJSONObject("preferredImage").optString("uri");
                        movie.setPreferredImage(preferredImage);
                        //Log.e(movie.getTitle(), movie.getPreferredImage());
                    } catch (Exception e) {
                        Log.e("Getting Poster", e.getMessage());
                    }

                    if (!showtimes.isEmpty()) {
                        movie.setShowtimes(showtimes);
                        movies.add(movie);
                    }
                }
            } catch (Exception e) {
                Log.e("DownloadJSON", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args){
            bar.setVisibility(View.INVISIBLE);

            moviesNearbyList = (ListView) findViewById(R.id.movies_nearby_list);

            if (!movies.isEmpty()) {
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movies);
                moviesNearbyList.setAdapter(adapter);
            } else
                moviesNearbyList.setAdapter(new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        new ArrayList<String>()));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.incrementProgressBy(20);
        }
    }

    private void requestMapsPermissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // do maps stuff
                    // Set user location
                } else {
                    // show theatres in boston
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Context context = getApplicationContext();
        PackageManager pm = context.getPackageManager();

        int hasPerm = pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.getPackageName());
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            String lat, lng;

            if (myLocation != null) {
                lat = String.valueOf(myLocation.getLatitude());
                lng = String.valueOf(myLocation.getLongitude());
                gracenoteUrl = gnUrlStart + today + latParam + lat + lngParam + lng + apiParam;
                locSet = true;
            } else gracenoteUrl = gnUrlStart + today + "&zip=02135" + apiParam;
        } else
            gracenoteUrl = gnUrlStart + today + "&zip=02135" + apiParam;

        ///debug(gracenoteUrl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new DownloadJSON(progressBar).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            new DownloadJSON(progressBar).execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    private void debug(String message){
        Toast toast = Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_LONG
        );
        toast.show();
    }
}

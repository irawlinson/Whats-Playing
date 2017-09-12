package com.example.isaiahrawlinson.whatsplaying;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    DatabaseHelper db;

    ListView favsList;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Favorites");

        favsList = (ListView) findViewById(R.id.favoritesList);
        favsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayInfo(movies.get(position));
            }});

        favsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(FavoritesActivity.this, view);
                popupMenu.inflate(R.menu.favs_long_click);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_favs_add_to_wl:
                                db.addToWatchList(movies.get(position));
                                break;
                            case R.id.menu_favs_delete:
                                db.removeFromFavorites(movies.get(position));
                                refreshList();
                                break;
                        }

                        return false;
                    }});

                return false;
            }});
    }

    @Override
    protected void onStart() {
        super.onStart();

        db = new DatabaseHelper(getApplicationContext());

        refreshList();
    }

    void displayInfo(Movie movie){
        Bundle data = new Bundle();
        data.putSerializable("movieSelected", movie);

        MovieView movieView = new MovieView();
        movieView.setRetainInstance(true);
        movieView.setArguments(data);

        movieView.show(getFragmentManager(), "movieView");
    }

    void refreshList(){
        movies = db.getFavorites();
        if (movies.size() > 0) {
            MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movies);
            favsList.setAdapter(adapter);
        } else {
            favsList.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<String>()
            ));
        }
    }
}

package com.example.isaiahrawlinson.whatsplaying;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by isaiahrawlinson on 5/7/17.
 */

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "whatsPlaying";

    //------------------------------------ Table Names ---------------------------------------------
    private static final String TABLE_LIST = "watch_list";
    private static final String TABLE_FAVS = "favorites";

    //------------------------------------ Column Names --------------------------------------------

    // Watch List column names
    private static final String KEY_LIST_ID = "wl_id";
    private static final String KEY_LIST_TITLE = "wl_title";
    private static final String KEY_LIST_GENRES = "wl_genres";
    private static final String KEY_LIST_SHORT_DESC = "wl_short_desc";
    private static final String KEY_LIST_LONG_DESC = "wl_long_desc";
    private static final String KEY_LIST_DIRECTORS = "wl_dirs";
    private static final String KEY_LIST_TOP_CAST = "wl_top_cast";

    // Favorites column names
    private static final String KEY_FAVS_ID = "favs_id";
    private static final String KEY_FAVS_TITLE = "favs_title";
    private static final String KEY_FAVS_GENRES = "favs_genres";
    private static final String KEY_FAVS_SHORT_DESC = "favs_short_desc";
    private static final String KEY_FAVS_LONG_DESC = "favs_long_desc";
    private static final String KEY_FAVS_DIRECTORS = "favs_dirs";
    private static final String KEY_FAVS_TOP_CAST = "favs_top_cast";

    private static final String CREATE_TABLE_LIST =
            "CREATE TABLE " + TABLE_LIST + " ( "
                    + KEY_LIST_ID + " TEXT PRIMARY KEY, "
                    + KEY_LIST_TITLE + " TEXT, "
                    + KEY_LIST_GENRES + " TEXT, "
                    + KEY_LIST_SHORT_DESC + " TEXT, "
                    + KEY_LIST_LONG_DESC + " TEXT, "
                    + KEY_LIST_DIRECTORS + " TEXT, "
                    + KEY_LIST_TOP_CAST + " TEXT "
                    + ")";

    private static final String CREATE_TABLE_FAVS =
            "CREATE TABLE " + TABLE_FAVS + " ( "
                    + KEY_FAVS_ID + " TEXT PRIMARY KEY, "
                    + KEY_FAVS_TITLE + " TEXT, "
                    + KEY_FAVS_GENRES + " TEXT, "
                    + KEY_FAVS_SHORT_DESC + " TEXT, "
                    + KEY_FAVS_LONG_DESC + " TEXT, "
                    + KEY_FAVS_DIRECTORS + " TEXT, "
                    + KEY_FAVS_TOP_CAST + " TEXT "
                    + ")";

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // create my tables
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_FAVS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_FAVS);

        // create new tables
        onCreate(db);
    }

    //-------------------------------------- Inserts -----------------------------------------------

    void addToWatchList(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ID, movie.getRootId());
        values.put(KEY_LIST_TITLE, movie.getTitle());
        values.put(KEY_LIST_GENRES, movie.getGenres().toString()
                .substring(1, movie.getGenres().toString().length() - 1));
        values.put(KEY_LIST_SHORT_DESC, movie.getShortDescription());
        values.put(KEY_LIST_LONG_DESC, movie.getLongDescription());
        values.put(KEY_LIST_DIRECTORS, movie.getDirectors().toString()
                .substring(1, movie.getDirectors().toString().length() - 1));
        values.put(KEY_LIST_TOP_CAST, movie.getTopCast().toString()
                .substring(1, movie.getTopCast().toString().length() - 1));

        db.insert(TABLE_LIST, null, values);
    }

    void addToFavs(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FAVS_ID, movie.getRootId());
        values.put(KEY_FAVS_TITLE, movie.getTitle());
        values.put(KEY_FAVS_GENRES, movie.getGenres().toString()
                .substring(1, movie.getGenres().toString().length() - 1));
        values.put(KEY_FAVS_SHORT_DESC, movie.getShortDescription());
        values.put(KEY_FAVS_LONG_DESC, movie.getLongDescription());
        values.put(KEY_FAVS_DIRECTORS, movie.getDirectors().toString()
                .substring(1, movie.getDirectors().toString().length() - 1));
        values.put(KEY_FAVS_TOP_CAST, movie.getTopCast().toString()
                .substring(1, movie.getTopCast().toString().length() - 1));

        db.insert(TABLE_FAVS, null, values);
    }

    //-------------------------------------- Queries -----------------------------------------------

    ArrayList<Movie> getWatchList(){
        ArrayList<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_LIST
                + " ORDER BY " + KEY_LIST_TITLE;
        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Movie movie = new Movie();

                movie.setRootId(c.getString(c.getColumnIndex(KEY_LIST_ID)));
                movie.setTitle(c.getString(c.getColumnIndex(KEY_LIST_TITLE)));

                ArrayList<String> genres = new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_LIST_GENRES))
                        .split("\\s*,\\s*")));
                movie.setGenres(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_LIST_GENRES))
                                .split("\\s*,\\s*"))));

                movie.setShortDescription(c.getString(c.getColumnIndex(KEY_LIST_SHORT_DESC)));
                movie.setLongDescription(c.getString(c.getColumnIndex(KEY_LIST_LONG_DESC)));

                movie.setDirectors(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_LIST_DIRECTORS))
                                .split("\\s*,\\s*"))));

                movie.setTopCast(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_LIST_TOP_CAST))
                                .split("\\s*,\\s*"))));

                movies.add(movie);
            } while (c.moveToNext());
        } c.close();

        return movies;
    }

    ArrayList<Movie> getFavorites(){
        ArrayList<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_FAVS
                + " ORDER BY " + KEY_FAVS_TITLE;
        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Movie movie = new Movie();

                movie.setRootId(c.getString(c.getColumnIndex(KEY_FAVS_ID)));
                movie.setTitle(c.getString(c.getColumnIndex(KEY_FAVS_TITLE)));

                ArrayList<String> genres = new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_FAVS_GENRES))
                                .split("\\s*,\\s*")));
                movie.setGenres(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_FAVS_GENRES))
                                .split("\\s*,\\s*"))));

                movie.setShortDescription(c.getString(c.getColumnIndex(KEY_FAVS_SHORT_DESC)));
                movie.setLongDescription(c.getString(c.getColumnIndex(KEY_FAVS_LONG_DESC)));

                movie.setDirectors(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_FAVS_DIRECTORS))
                                .split("\\s*,\\s*"))));

                movie.setTopCast(new ArrayList<>(Arrays.asList(
                        c.getString(c.getColumnIndex(KEY_FAVS_TOP_CAST))
                                .split("\\s*,\\s*"))));

                movies.add(movie);
            } while (c.moveToNext());
        } c.close();

        return movies;
    }

    //-------------------------------------- Deletes -----------------------------------------------

    void removeFromWatchList(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIST, KEY_LIST_ID + " = ?",
                new String[] {movie.getRootId()});
    }

    void removeFromFavorites(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVS, KEY_FAVS_ID + " = ?",
                new String[] {movie.getRootId()});
    }

    void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}

package com.example.isaiahrawlinson.whatsplaying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by isaiahrawlinson on 5/7/17.
 */

public class MovieAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_listview_item, null, true);

            holder.title = (TextView) convertView.findViewById(R.id.movie_listview_item_title);
            holder.description = (TextView) convertView.findViewById(R.id.movie_listview_item_description);
            holder.image = (ImageView) convertView.findViewById(R.id.movie_listview_item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(movies.get(position).getTitle());
        holder.description.setText(movies.get(position).getShortDescription());
        /*Log.e("MovieAdapter", "About to launch thread " + holder.image.getId() + " " + movies.get(position).getPreferredImage());
        new DownloadImageTask(holder.image, movies.get(position)).execute();
        Log.e("MovieAdapter", "Launched thread");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e("Sleeping main thread", e.getMessage());
        }*/

        return convertView;
    }

    private class ViewHolder {
        private TextView title, description;
        private ImageView image;
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode;

        try {
            URL url = new URL(urlStr);
            URLConnection urlCon = url.openConnection();

            if (!(urlCon instanceof HttpURLConnection)) {
                throw new IOException("URL is not an HTTP URL");
            }

            HttpURLConnection httpConn = (HttpURLConnection) urlCon;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
            Log.e("Creating url", e.getMessage());
        }
        return in;
    }

    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        ImageView bmImage;
        String imageUri;

        public DownloadImageTask(ImageView bmImage, Movie movie) {
            this.bmImage = bmImage;
            imageUri = movie.getPreferredImage();
            Log.e("DIT contstructor", "initialized");
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = imageUri;
            Bitmap mIcon1 = null;
            Log.e("doInBackground", "starting");
            try {
                //InputStream in = new java.net.URL(urlDisplay).openStream();
                InputStream in = null;
                try {
                    Log.e("doInBackground", "opening connection");
                    in = openHttpConnection(imageUri);
                    /*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("Sleeping thread", e.getMessage());
                    }*/
                    Log.e("doInBackground", "decoding input stream");
                    mIcon1 = BitmapFactory.decodeStream(in);

                    /*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("Sleeping thread", e.getMessage());
                    }*/
                    in.close();
                } catch (IOException e) {
                    Log.e("Opening Connection", e.getMessage());
                }
                mIcon1 = BitmapFactory.decodeStream(in);
                Log.e("download image", urlDisplay);
            } catch (Exception e) {
                Log.e("Downloading Image", e.getMessage());
            }
            return mIcon1;
        }

        protected void onPostExecute(Bitmap result) {
            //bmImage.setImageResource(result);
            Log.e("OnPostExecute", "Setting image");
            bmImage.setImageBitmap(result);

        }
    }
}

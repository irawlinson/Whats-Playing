package com.example.isaiahrawlinson.whatsplaying;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by isaiahrawlinson on 5/7/17.
 */

public class ShowtimeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Showtime> showtimes;

    public ShowtimeAdapter(Context context, ArrayList<Showtime> showtimes) {
        this.context = context;
        this.showtimes = showtimes;
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
        return showtimes.size();
    }

    @Override
    public Object getItem(int position) {
        return showtimes.get(position);
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
            convertView = inflater.inflate(R.layout.showtime_listview_item, null, true);

            holder.time = (TextView) convertView.findViewById(R.id.showtime_listview_item_time);
            holder.theatre = (TextView) convertView.findViewById(R.id.showtime_listview_item_theatre);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(showtimes.get(position).getDateTime());
        holder.theatre.setText(showtimes.get(position).getTheatre().getName());

        return convertView;
    }

    private class ViewHolder { private TextView time, theatre; }
}

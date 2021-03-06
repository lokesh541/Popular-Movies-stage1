package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lokesh on 22/7/16.
 */

public class MovieAdapter extends ArrayAdapter<Movie.MovieItem> {
    private List<Movie.MovieItem> items;


    public MovieAdapter(Context context, List<Movie.MovieItem> items) {
        super(context, 0, items);
    }

    @BindView(R.id.detail_activity_poster)
    ImageView imageView;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View gridItemView = convertView;

        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);

            holder = new ViewHolder(gridItemView);
            gridItemView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();


        }

        Movie.MovieItem currentMovie = getItem(position);
        String url = "http://image.tmdb.org/t/p/w500" + currentMovie.getPosterPath();
        Picasso.with(getContext()).load(url).into(holder.imageView);
        return gridItemView;
    }

    public int getCount() {
        if (items == null) {
            return -1;
        }
        return items.size();
    }

    public void swapList(List<Movie.MovieItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static final class ViewHolder {
        @BindView(R.id.movie_poster)
        ImageView imageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}


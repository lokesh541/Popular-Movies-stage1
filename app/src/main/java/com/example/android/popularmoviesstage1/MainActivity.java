package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.popularmoviesstage1.MovieService.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private static final String API_BASE_URL = "http://api.themoviedb.org/";
    private Call<Movie> call;
    private Movie movie;
    private List<Movie.MovieItem> items;
    private MovieAdapter movieAdapter;
    private String version = "3";
    private final String API_KEY = "36c9aef2c07c58b2a228ec4be25dadf8";
    private String sort_by = "popular";
    private String imageUrl = "http://image.tmdb.org/t/p/w500";
    private final static String MENU_SELECTED = "selected";
    private int selected = -1;
    private Retrofit retrofit;

    @BindView(R.id.grid_view_movies)
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            sort_by = savedInstanceState.getString(MENU_SELECTED);
        }
        ButterKnife.bind(this);
        updateScreen(sort_by);

    }

    public void updateScreen(String sort_by) {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieApi movieApi = retrofit.create(MovieApi.class);
        call = movieApi.getMovies(version, sort_by, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                try {
                    movie = response.body();
                    items = movie.getResults();
                    movieAdapter = new MovieAdapter(MainActivity.this, items);
                    gridView.setAdapter(movieAdapter);
                    movieAdapter.swapList(items);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("getQuestions threw: ", t.getMessage());
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Context context = getApplicationContext();
                Movie.MovieItem currentMovie = movieAdapter.getItem(position);
                String title = currentMovie.getTitle();
                String avgRating = currentMovie.getVoteAverage().toString();
                String releaseDate = currentMovie.getReleaseDate();
                String plotSummary = currentMovie.getOverview();
                String id = currentMovie.getId().toString();
                String message = title + "\nAverage Rating :" + avgRating + "\n\nRelease date : " + releaseDate;
                String url = imageUrl + currentMovie.getPosterPath();
                Intent intent = new Intent(context, DetailActivity.class).
                        putExtra("message", message)
                        .putExtra("plotsummary",plotSummary)
                        .putExtra("url", url)
                        .putExtra("id", id);
                startActivity(intent);
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        switch (sort_by) {
            case "popular":
                menu.findItem(R.id.popular).setChecked(true);
                break;
            case "top_rated":
                menu.findItem(R.id.top_rated).setChecked(true);
                break;
        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        switch (id) {
            case R.id.popular:
                sort_by = "popular";
                updateScreen(sort_by);
                item.setChecked(true);
                return true;
            case R.id.top_rated:
                sort_by = "top_rated";
                updateScreen(sort_by);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(MENU_SELECTED, sort_by);
        super.onSaveInstanceState(savedInstanceState);
    }
}




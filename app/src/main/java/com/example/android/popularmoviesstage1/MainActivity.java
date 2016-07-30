package com.example.android.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.popularmoviesstage1.MovieService.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    private static final String API_BASE_URL = "http://api.themoviedb.org/";
    private Call<Movie> call;
    private Movie movie;
    private List<Movie.MovieItem> items;
    private GridView gridView;
    private  MovieAdapter movieAdapter;
    private String version = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        gridView = (GridView) findViewById(R.id.grid_view_movies);
        gridView.setAdapter(movieAdapter);

        MovieApi movieApi = retrofit.create(MovieApi.class);
        call = movieApi.getMovies(version);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                try{
                    movie = response.body();
                    Log.v(TAG,"response v"+response.body());

                    items = movie.getItems();
                    Log.w(TAG, "items=" +items);

                    movieAdapter.swapList(items);
                } catch (NullPointerException e) {
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(MainActivity.this, "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(MainActivity.this, "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("getQuestions threw: ", t.getMessage());
            }
        });
    }
}



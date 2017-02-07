package com.example.bench.movie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;
import static com.example.bench.movie.MovieDBContract.COL_OVERVIEW;
import static com.example.bench.movie.MovieDBContract.COL_POSTER;
import static com.example.bench.movie.MovieDBContract.COL_RELEASE_DATE;
import static com.example.bench.movie.MovieDBContract.COL_TITLE;
import static com.example.bench.movie.MovieDBContract.COL_VOTE_AVG;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;

public class FilmActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FilmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        final ImageView toolbarPoster = (ImageView) findViewById(R.id.poster);

        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = width;

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        Intent i = getIntent();
        int tag = i.getIntExtra("filmid", -1);

        Log.d(LOG_TAG, Integer.toString(tag));

        SQLiteDatabase db = new MovieDB(FilmActivity.this).getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE _ID = '"+tag+"'", null);

        cursor.moveToPosition(0);
        String uri = MovieItemAdapter.imageBaseUrl + cursor.getString(cursor.getColumnIndex(COL_BACKDROP));
        getSupportActionBar().setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));

        Picasso.with(FilmActivity.this).load(uri).transform(transformation).into(toolbarPoster);


        RecyclerView movie_data = (RecyclerView) findViewById(R.id.movie_data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        movie_data.setLayoutManager(mLayoutManager);
        movie_data.setItemAnimator(new DefaultItemAnimator());
        movie_data.setAdapter(new FilmDetailAdapter(tag, FilmActivity.this));
    }


}

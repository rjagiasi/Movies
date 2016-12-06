package com.example.bench.movie;


import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.bench.movie.FetchMovieData.image_paths;
import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;
import static com.example.bench.movie.MovieDBContract.COL_DATEINS;
import static com.example.bench.movie.MovieDBContract.COL_POPULARITY;
import static com.example.bench.movie.MovieDBContract.COL_TITLE;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;


class MovieItemAdapter extends BaseAdapter {
    ArrayList<String> movie_names = FetchMovieData.movies_names;
    public Context context;
    int page;
    public static final String LOG_TAG = "MovieItemAdapter";
    final String imageBaseUrl = "http://image.tmdb.org/t/p/w185/";

    static int height;
    static int width;
    static int colwidth;
    static int colheight;

    SQLiteDatabase db;
    Cursor cursor;

    public MovieItemAdapter(Context c) throws ExecutionException, InterruptedException {
        page = 1;
        context = c;

        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        colwidth = (width / 2) - 40;
        colheight = (colwidth / 5) * 4;

        db = new MovieDB(context).getWritableDatabase();
        cursor = db.rawQuery("SELECT "+COL_TITLE + ", "+COL_BACKDROP+" FROM "+TABLE_NAME +" ORDER BY "+COL_POPULARITY +" ASC" , null);
    }

    @Override
    public int getCount() {

        return movie_names.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;

//        Log.d(LOG_TAG, Integer.toString(i));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor.moveToPosition(i);
        if (view == null) {
            grid = inflater.inflate(R.layout.movie_list_item, null);

        } else {
            grid = view;
        }
        TextView textView = (TextView) grid.findViewById(R.id.movie_name);
        ImageView imageView = (ImageView) grid.findViewById(R.id.movie_img);
        textView.setText(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
        String builtUrl = imageBaseUrl + cursor.getString(cursor.getColumnIndex(COL_BACKDROP));
        Picasso.with(context).load(builtUrl).into(imageView);

        grid.setLayoutParams(new GridView.LayoutParams(colwidth, colheight));
//        Log.d(LOG_TAG, i + " : " + image_paths.get(i).toString());
        grid.setPadding(8, 8, 8, 8);
        return grid;
    }


}
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

import static android.provider.BaseColumns._ID;
//import static com.example.bench.movie.FetchMovieData.image_paths;
import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;
import static com.example.bench.movie.MovieDBContract.COL_DATEINS;
import static com.example.bench.movie.MovieDBContract.COL_POPULARITY;
import static com.example.bench.movie.MovieDBContract.COL_TITLE;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;


class MovieItemAdapter extends BaseAdapter {
    //    ArrayList<String> movie_names = FetchMovieData.movies_names;
    public Context context;
    int page;
    private static final String LOG_TAG = "MovieItemAdapter";

    public static final String imageBaseUrl = "http://image.tmdb.org/t/p/w342/";

    static int height, width, colwidth, colheight;

    SQLiteDatabase db;
    Cursor cursor;

    public MovieItemAdapter(Context c, int page) throws ExecutionException, InterruptedException {
        this.page = page;
        context = c;

        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        colwidth = (width / 2) - 40;
        colheight = (colwidth / 5) * 4;

        db = new MovieDB(context).getReadableDatabase();
        cursor = db.rawQuery("SELECT _ID, " + COL_TITLE + ", " + COL_BACKDROP + " FROM " + TABLE_NAME, null);

    }

    @Override
    public int getCount() {
//        Log.d(LOG_TAG, Integer.toString(cursor.getCount()));
        return cursor.getCount();
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
        grid.setTag(cursor.getInt(cursor.getColumnIndex(_ID)));

        if (i == getCount())
            db.close();

        return grid;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(LOG_TAG, "Hello");
        cursor = db.rawQuery("SELECT _ID, " + COL_TITLE + ", " + COL_BACKDROP + " FROM " + TABLE_NAME, null);
//        super.notifyDataSetChanged();
    }
}
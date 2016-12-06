package com.example.bench.movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.bench.movie.MovieDBContract.*;

/**
 * Created by bench on 11/15/2016.
 */

public class MovieDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Movie.db";
    public static final int DATABASE_VERSION = 2;
    private static final String LOG_TAG = "MovieDB";

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public MovieDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE_MOVIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_BACKDROP + " TEXT, " +
                        COL_OVERVIEW + " TEXT NOT NULL, " +
                        COL_POSTER + " TEXT, " +
                        COL_VOTE_AVG + " REAL NOT NULL, " +
                        COL_RELEASE_DATE + " TEXT NOT NULL, " +
                        COL_DATEINS + " TEXT NOT NULL, "+
                        COL_POPULARITY + " REAL NOT NULL );";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIES);
        Log.d(LOG_TAG, CREATE_TABLE_MOVIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

package com.example.bench.movie;

import android.provider.BaseColumns;

/**
 * Created by bench on 11/16/2016.
 */

public class MovieDBContract implements BaseColumns{

    public static final String TABLE_NAME = "Movies";

    public static final String COL_TITLE = "Title";
    public static final String COL_BACKDROP = "Backdrop";
    public static final String COL_POSTER = "Poster";
    public static final String COL_VOTE_AVG = "Rating";
    public static final String COL_OVERVIEW = "Overview";
    public static final String COL_RELEASE_DATE = "Date_Released";
    public static final String COL_DATEINS = "Date_Ins";
    public static final String COL_POPULARITY = "Popularity";
}

package com.example.bench.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bench.movie.FilmActivity;
import com.example.bench.movie.MovieDB;
import com.example.bench.movie.R;

import static com.example.bench.movie.MovieDBContract.COL_OVERVIEW;
import static com.example.bench.movie.MovieDBContract.COL_RELEASE_DATE;
import static com.example.bench.movie.MovieDBContract.COL_TITLE;
import static com.example.bench.movie.MovieDBContract.COL_VOTE_AVG;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;

/**
 * Created by rohan on 22-12-2016.
 */

public class FilmDetailAdapter extends RecyclerView.Adapter<FilmDetailAdapter.MovieViewHolder> {

    private static final String LOG_TAG ="FilmDetailAdapter";
    Cursor cursor;
    Context c;

    public FilmDetailAdapter(int tag, Context context) {
        c = context;
        SQLiteDatabase db = new MovieDB(context).getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE _ID = '"+tag+"'", null);
        cursor.moveToPosition(0);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public TextView name_tv, desc;
        public MovieViewHolder(View itemView) {
            super(itemView);
            name_tv  = (TextView) itemView.findViewById(R.id.title);
            desc  = (TextView) itemView.findViewById(R.id.desc);

        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_card, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        int n = 0;

        if(position == 0)
            n = cursor.getColumnIndex(COL_TITLE);
        else if(position == 1)
            n = cursor.getColumnIndex(COL_OVERVIEW);
        else if(position == 2)
            n = cursor.getColumnIndex(COL_RELEASE_DATE);
        else if(position == 3)
            n = cursor.getColumnIndex(COL_VOTE_AVG);


        holder.name_tv.setText(cursor.getColumnName(n));
        holder.desc.setText(cursor.getString(n));
        Log.d(LOG_TAG, cursor.getColumnName(n));
//        data_field.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
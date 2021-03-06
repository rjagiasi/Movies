package com.example.bench.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;
import static com.example.bench.movie.MovieDBContract.COL_DATEINS;
import static com.example.bench.movie.MovieDBContract.COL_TITLE;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;
import static java.security.AccessController.getContext;

/**
 * Created by bench on 11/13/2016.
 */

public class FetchMovieData extends AsyncTask {

    public static final String LOG_TAG = "FetchMovieData";
    private int pageno;
    //    public static ArrayList<String> movies_names = new ArrayList<>();
//    public static ArrayList<String> image_paths = new ArrayList<>();
    String JSONString = "";
    private static Context context;
    Boolean updated_today = false;
    final String baseUrl = "https://api.themoviedb.org/3/discover/movie";
    GridView movies_list;
    MovieItemAdapter movieItemAdapter;
    String API_KEY = "";
    int count;

    public FetchMovieData(int pageno, Context context, View grid, MovieItemAdapter movieItemAdapter) {
        this.pageno = pageno;
        this.context = context;
        this.movies_list = (GridView) grid;
        this.movieItemAdapter = movieItemAdapter;
        API_KEY = context.getResources().getString(R.string.key);
        Log.d(LOG_TAG, String.valueOf(pageno));
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    @Override
    protected void onPreExecute() {

        SQLiteDatabase db = new MovieDB(context).getWritableDatabase();
//        db.delete(TABLE_NAME, null, null);
        Cursor cursor = db.rawQuery("SELECT " + COL_TITLE + ", " + COL_BACKDROP + " from " + TABLE_NAME, null);

        count = cursor.getCount();
        Cursor c2 = db.rawQuery("SELECT EXISTS(SELECT 1 from " + TABLE_NAME + " WHERE " + COL_DATEINS + " <= date('now','0 day'))", null);
        c2.moveToPosition(0);
        int i = c2.getInt(0);
        if (i == 1) {
            updated_today = true;
        }
        cursor.close();
        c2.close();
        db.close();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if ((updated_today && count > (pageno * 20)) || !(activeNetworkInfo.isConnected())) {
//            Log.d(LOG_TAG, String.valueOf(updated_today));
//            Toast.makeText(context, "Check Network!", Toast.LENGTH_SHORT).show();
            return null;
        }

        Uri dataUri = Uri.parse(baseUrl).buildUpon().
                appendQueryParameter("api_key", API_KEY).
                appendQueryParameter("sort_by", "popularity.desc").
                appendQueryParameter("page", Integer.toString(pageno)).
                build();

        Log.d(LOG_TAG, dataUri.toString());

        try {
            URL dataUrl = new URL(dataUri.toString());

            HttpsURLConnection urlConnection = (HttpsURLConnection) dataUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream dataFetched = urlConnection.getInputStream();
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataFetched));

            String line;

            while ((line = dataReader.readLine()) != null) {
                JSONString += line;
            }
            dataReader.close();
            urlConnection.disconnect();

//            Log.d(LOG_TAG, JSONString);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {

        try {
            JSONObject jsonParser = new JSONObject(JSONString);
            JSONArray results = jsonParser.getJSONArray("results");
            SQLiteDatabase db = new MovieDB(context).getWritableDatabase();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            movies_names.clear();

            for (int i = 0, n = results.length(); i < n; i++) {
//                Log.d(LOG_TAG, results.toString());

                JSONObject movie = results.getJSONObject(i);

//                image_paths.add(movie.getString("backdrop_path"));
//                movies_names.add(movie.getString("original_title"));

                String insert_stmt = "Insert or replace into " + TABLE_NAME + " VALUES ("
                        + movie.getString("id") + ", '"
                        + movie.getString("original_title").replace("'", "''") + "', '"
                        + movie.getString("backdrop_path") + "', '"
                        + movie.getString("overview").replace("'", "''") + "' , '"
                        + movie.getString("poster_path") + "', '"
                        + movie.getString("vote_average") + "' , '"
                        + movie.getString("release_date") + "', '"
                        + date + "', '"
                        + movie.getString("popularity")
                        + "');";

//                Log.d(LOG_TAG, insert_stmt);
                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_DATEINS + " <= date('now','-3 day')");
                db.execSQL(insert_stmt);
            }

            db.close();

        } catch (JSONException e) {
//            e.printStackTrace();
        } finally {

                movies_list.deferNotifyDataSetChanged();
        }

    }
}

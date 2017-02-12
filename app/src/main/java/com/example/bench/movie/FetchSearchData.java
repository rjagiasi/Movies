package com.example.bench.movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.example.bench.movie.MovieDBContract.COL_DATEINS;
import static com.example.bench.movie.MovieDBContract.TABLE_NAME;

/**
 * Created by rohan on 07-02-2017.
 */

public class FetchSearchData extends AsyncTask<Object, Void, JSONArray> {

    private static final String searchBaseURL = "https://api.themoviedb.org/3/search/movie";
    private static final String LOG_TAG = "FetchSearchData";
    static String search_query = "";
    Context context;
    private static ListView search_list;

    public FetchSearchData(ListView search_list, String search_query, Context context) {
        this.search_query = search_query;
//        Log.d(LOG_TAG, search_query);
        this.context = context;
        this.search_list = search_list;
    }

    @Override
    protected JSONArray doInBackground(Object[] params) {

        Uri searchUri = Uri.parse(searchBaseURL).buildUpon()
                .appendQueryParameter("api_key", context.getResources().getString(R.string.key))
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("query", search_query)
                .appendQueryParameter("page", String.valueOf(1))
                .build();

        Log.d(LOG_TAG, searchUri.toString());
        JSONArray arr = null;
        try {
            URL dataUrl = new URL(searchUri.toString());

            HttpsURLConnection urlConnection = (HttpsURLConnection) dataUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream dataFetched = urlConnection.getInputStream();
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataFetched));

            JSONObject obj = new JSONObject(dataReader.readLine());
            arr = obj.getJSONArray("results");

            dataReader.close();
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    @Override
    protected void onPostExecute(JSONArray arr) {

        try {

            JSONArray results = arr;
            SQLiteDatabase db = new MovieDB(context).getWritableDatabase();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            for (int i = 0, n = results.length(); i < n; i++) {

                JSONObject movie = results.getJSONObject(i);

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

                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_DATEINS + " <= date('now','-3 day')");
                db.execSQL(insert_stmt);
            }

            db.close();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        search_list.setAdapter(new SearchListAdapter(arr, context));


    }
}

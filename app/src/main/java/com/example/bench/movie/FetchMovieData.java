package com.example.bench.movie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static java.security.AccessController.getContext;

/**
 * Created by bench on 11/13/2016.
 */

public class FetchMovieData extends AsyncTask {

    public static final String LOG_TAG = "FetchMovieData";
    private int pageno;
    public static ArrayList<String> movies_names = new ArrayList<>();
    public static ArrayList<String> image_paths = new ArrayList<>();
    String JSONString = "";
    private static Context context;
    final String baseUrl = "https://api.themoviedb.org/3/discover/movie";

    String API_KEY = "";

    public FetchMovieData(int pageno, Context context) {
        this.pageno = pageno;
        this.context = context;
        API_KEY = context.getResources().getString(R.string.key);
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        Uri dataUri = Uri.parse(baseUrl).buildUpon().
                appendQueryParameter("api_key", API_KEY).
                appendQueryParameter("sort_by", "popularity.desc").
                appendQueryParameter("page", Integer.toString(pageno)).build();

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

            Log.d(LOG_TAG, JSONString);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try
        {
            JSONObject jsonParser = new JSONObject(JSONString);
            JSONArray results = jsonParser.getJSONArray("results");

            movies_names.clear();

            for (int i=0, n=results.length() ; i<n ; i++)
            {
//                Log.d(LOG_TAG, results.toString());

                JSONObject movie = results.getJSONObject(i);

                image_paths.add(movie.getString("backdrop_path"));
                movies_names.add(movie.getString("original_title"));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

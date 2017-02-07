package com.example.bench.movie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.bench.movie.MovieDBContract.COL_BACKDROP;


/**
 * Created by rohan on 07-02-2017.
 */

public class SearchListAdapter extends BaseAdapter {

    private static final String LOG_TAG = "SearchListAdapter";
    private final Context context;
    JSONArray results;

    public static final String searchImageBaseUrl = "http://image.tmdb.org/t/p/w154/";

    public SearchListAdapter(JSONArray arr, Context context) {
        results = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return results.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_list_element, null);
        }

        try {

            JSONObject result_item = results.getJSONObject(position);
            String title = result_item.getString("original_title");
            String release_date = result_item.getString("release_date");

            ((TextView) convertView.findViewById(R.id.search_title)).setText(title);
            ((TextView) convertView.findViewById(R.id.search_release_date)).setText("Release Date : " + release_date);

            String builtUrl = searchImageBaseUrl + result_item.getString("poster_path");
            Picasso.with(context).load(builtUrl).into((ImageView) convertView.findViewById(R.id.search_image_view));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}

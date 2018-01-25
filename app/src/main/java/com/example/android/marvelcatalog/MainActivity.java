package com.example.android.marvelcatalog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.android.marvelcatalog.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mSearchEditText;
    private ProgressBar mLoadingIndicator;
    private View mEmptyView;
    public String jsonString;
    public URL marvelQueryUrl;
    private ListView listView;
    private ArrayList<HashMap<String, String>> comicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchEditText = (EditText) findViewById(R.id.editText_query);
        comicList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.action_bar_search) {
            ConnectivityManager cm = (ConnectivityManager)
                    MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                comicList.clear();
                new MarvelQueryTask().execute();
            } else { // not connected to the internet
                Toast.makeText(getBaseContext(), R.string.check_connection,
                        Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class MarvelQueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            String characterQuery = mSearchEditText.getText().toString();

            if (characterQuery.equals(null) || characterQuery.equals("")) {
                listView.setVisibility(View.INVISIBLE);

            } else {
                listView.setVisibility(View.VISIBLE);
                marvelQueryUrl = NetworkUtils.marvelBuildURL(characterQuery);
            }
        }

        @Override
        protected Void doInBackground(Void... urls) {
            if (marvelQueryUrl != null) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(marvelQueryUrl);
                    JSONObject jsonRootObject = new JSONObject(jsonString);
                    JSONObject jsonDataObject = jsonRootObject.optJSONObject("data");
                    JSONArray jsonResultsArray = jsonDataObject.optJSONArray("results");

                    for (int i = 0; i < jsonResultsArray.length(); i++) {
                        JSONObject jsonFirstResult = jsonResultsArray.getJSONObject(i);
                        String name = jsonFirstResult.getString("name");
                        HashMap<String, String> character = new HashMap<>();
                        character.put(getString(R.string.name), name);
                        comicList.add(character);
                    }

                } catch (IOException e) {
                    Log.e(TAG, "IOException at " + e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException at " + e);
                }

            } else {
                Log.e(TAG, getString(R.string.json_server_error));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (!comicList.equals(null)) {
                Log.v(TAG, "comicList is: " + comicList);
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, comicList,
                        R.layout.list_item, new String[]{getString(R.string.name)},
                        new int[]{R.id.name});
                Log.v(TAG, "listview is: " + listView);
                listView.setAdapter(adapter);
            } else {
                listView.setEmptyView(mEmptyView);
            }
        }
    }
}
package com.example.android.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<Movie> movieArray;
    private MovieAdapter movieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
        Log.v("MainActivity","started");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("MainActivity","paused");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivity","created");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("MainActivity","resumed");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("MainActivity","stopped");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity","destroyed");
    }

    private void updateMovies() {
        // then you use
        new FetchMoviesTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        movieArray = new ArrayList<Movie>();

        movieAdapter = new MovieAdapter(getActivity(), movieArray);
        GridView lv = (GridView) rootview.findViewById(R.id.GridViewMovies);
        lv.setAdapter(movieAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movieItem = movieAdapter.getItem(i);
                Intent movieIntent = new Intent(getActivity(), MovieActivity.class)
                        .putExtra("sampleObject", movieItem);
                              /*getApplicationContext()*/
                startActivity(movieIntent);

            }
        });

        return rootview;

    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        protected ArrayList<Movie> doInBackground(Void... voids) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            ArrayList<Movie> moviesArray = null;

            try {

                //   https://api.themoviedb.org/3/movie/popular?api_key=7e6dc060dbdcb4dd9e311101d8b4dc47&language=en-US


                String lang = "en-US";
                String appId = "7e6dc060dbdcb4dd9e311101d8b4dc47";

                final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
                final String LANG_PARAM = "language";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, appId)
                        .appendQueryParameter(LANG_PARAM, lang)
                        .build();
                URL url = new URL(builtUri.toString());


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                moviesArray = getMoviesDataFromJson(moviesJsonStr);


                Log.v("ForecastJsonStr", moviesJsonStr);

            } catch (JSONException e) {
                Log.e("MainActivityFragment", "Error JSON string ", e);
            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }
            }
            return moviesArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesArray) {
            if (moviesArray != null) {
                movieAdapter.clear();
                for (int i = 0; i < moviesArray.size(); i++) {
                    movieAdapter.add(moviesArray.get(i));
                }

            }
        }

        private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_TITLE = "original_title";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWN_RELEASE_DATE = "release_date";
            final String OWN_PLOT_SYNOPSIS = "overview";
            final String OWN_VOTE_AVERGAE = "vote_average";

            ArrayList<Movie> resultMovieArray = new ArrayList<Movie>();

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);


            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                String title = movieObject.getString(OWM_TITLE);
                String posterPath = movieObject.getString(OWM_POSTER_PATH);
                String releaseDate = movieObject.getString(OWN_RELEASE_DATE);
                String plotSynopsis = movieObject.getString(OWN_PLOT_SYNOPSIS);
                String voteAverage = movieObject.getString(OWN_VOTE_AVERGAE);

                Log.v("json_movie_and_path", title + "" + posterPath);

                Movie mMovie = new Movie(title, posterPath, releaseDate, plotSynopsis, voteAverage);

                resultMovieArray.add(mMovie);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.

            }
            for (Movie s : resultMovieArray) {
                Log.v("AsyncTask", "Forecast entry: " + s.getmMovieName() + s.getmPosterPath());
            }
            return resultMovieArray;

        }

    }


}

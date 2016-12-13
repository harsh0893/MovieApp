package com.example.android.movieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment {

    public MovieActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        Movie movieObject = (Movie)getActivity().getIntent().getSerializableExtra("sampleObject");
        String title = movieObject.getmMovieName();
        String posterPath = movieObject.getmPosterPath();
        String releaseDate = movieObject.getmReleaseDate();
        String plotSynopsis = movieObject.getmPlotSynopsis();
        String voteAverage = movieObject.getmVoteAverage();

        TextView titleTextView = (TextView)rootView.findViewById(R.id.title_text_view);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.poster_image_view);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.rating_text_view);
        TextView relDatTextView = (TextView)rootView.findViewById(R.id.release_date_text_view);
        TextView synopsisTextView = (TextView)rootView.findViewById(R.id.synopsis_text_view);

       titleTextView.setText(title);

        if (posterPath!=null) {
            new ImageLoadTask("https://image.tmdb.org/t/p/w500/"+posterPath,posterView).execute();
            posterView.setVisibility(View.VISIBLE);  //We wanna make sure the view is visible again coz the views will be reused

        } else {
            posterView.setVisibility(View.GONE);
        }
          ratingTextView.setText(voteAverage+"/10");
        relDatTextView.setText("Released on "+releaseDate);


        synopsisTextView.setText(plotSynopsis);

        return rootView;

    }
//    private String formatDate(String releaseDate){
//
//        String dtStart = "2016-11-16";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = format.parse(dtStart);
//            System.out.println(date);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}

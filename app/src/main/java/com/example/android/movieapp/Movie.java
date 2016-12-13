package com.example.android.movieapp;

import java.io.Serializable;

/**
 * Created by sainih on 11/27/2016.
 */
public class Movie implements Serializable {
    private String mMovieName;
    private String mPosterPath;
    private String mReleaseDate;
    private String mPlotSynopsis;
    private String mVoteAverage;

    Movie(String movieName, String posterPath,String releaseDate,String plotSynopsis,String voteAverage) {
        mMovieName = movieName;
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mPlotSynopsis = plotSynopsis;
        mVoteAverage = voteAverage;

    }

    public boolean hasImage() {
        if (mPosterPath != null)
            return true;
        else
            return false;
    }


    public String getmMovieName() {
        return mMovieName;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }
}

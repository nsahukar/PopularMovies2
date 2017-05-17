package android.nsahukar.com.popularmovies2.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.utilities.MoviesJsonUtils;
import android.nsahukar.com.popularmovies2.utilities.MoviesNetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikhil on 04/05/17.
 */

public class MoviesSyncTask {

    private static void syncPopularMovies(Context context) {

        try {
            // get popular movies url
            URL popularMoviesURL = new URL(MoviesNetworkUtils.getPopularMoviesUrl());

            // use the url to retrieve the JSON
            final String popularMoviesJSONResponse = MoviesNetworkUtils.
                    getResponseFromHttpUrl(popularMoviesURL);

            // parse the json into a list of movies content values
            ContentValues[] popularMoviesContentValues = MoviesJsonUtils.
                    getPopularMoviesContentValuesFromJson(popularMoviesJSONResponse);

            if (popularMoviesContentValues != null && popularMoviesContentValues.length != 0) {

                // get a handle on the content resolver to delete and insert data
                ContentResolver moviesContentResolver = context.getContentResolver();

                // delete old popular movies data
                Uri popularMoviesUri = MoviesContract.MoviesEntry.getPopularMoviesContentUri();
                moviesContentResolver.delete(popularMoviesUri, null, null);

                // insert our new popular movies data into movies content provider
                moviesContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, popularMoviesContentValues);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private static void syncTopRatedMovies(Context context) {
        try {
            // get top rated movies url
            URL topRatedMoviesURL = new URL(MoviesNetworkUtils.getTopRatedMoviesUrl());

            // use the url to retrieve the JSON
            final String topRatedMoviesJSONResponse = MoviesNetworkUtils.getResponseFromHttpUrl(topRatedMoviesURL);

            // parse the json into a list of movies content values
            ContentValues[] topRatedMoviesContentValues = MoviesJsonUtils.getTopRatedMoviesContentValuesFromJson(topRatedMoviesJSONResponse);

            if (topRatedMoviesContentValues != null && topRatedMoviesContentValues.length != 0) {

                // get a handle on the content resolver to delete and insert data
                ContentResolver moviesContentResolver = context.getContentResolver();

                // delete old top rated movies data
                Uri topRatedMoviesUri = MoviesContract.MoviesEntry.getTopRatedMoviesContentUri();
                moviesContentResolver.delete(topRatedMoviesUri, null, null);

                // insert our new top rated movies data into movies content provider
                moviesContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, topRatedMoviesContentValues);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        performs the network request for movies, parses the JSON from that request, and
        inserts the new movies information into the ContentProvider.
     */
    synchronized public static void syncMovies(Context context) {
        syncPopularMovies(context);
        syncTopRatedMovies(context);
    }

}

package android.nsahukar.com.popularmovies2.extra;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract.*;
import android.nsahukar.com.popularmovies2.utilities.MoviesJsonUtils;
import android.nsahukar.com.popularmovies2.utilities.MoviesNetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Nikhil on 10/05/17.
 */

public class MoviesExtraTask {

    /*
        performs the network request for movie videos, parses the JSON from that request, and
        inserts the new movie videos information into the ContentProvider.
     */
    synchronized public static void syncMovieVideos(Context context, long movieId) {

        try {
            // get movie videos url
            URL movieVideosURL = new URL(MoviesNetworkUtils.getMovieVideosUrl(movieId));

            // use the url to retrieve the JSON
            final String movieVideosJSONResponse = MoviesNetworkUtils.getResponseFromHttpUrl(movieVideosURL);

            // parse the json into a list of movies content values
            ContentValues[] movieVideosContentValues = MoviesJsonUtils.
                    getMovieVideosContentValuesFromJson(movieVideosJSONResponse, movieId);

            if (movieVideosContentValues != null && movieVideosContentValues.length != 0) {

                // get a handle on the content resolver to delete and insert data
                ContentResolver moviesContentResolver = context.getContentResolver();

                // insert our new top rated movies data into movies content provider
                moviesContentResolver.bulkInsert(VideosEntry.CONTENT_URI, movieVideosContentValues);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    /*
        performs the network request for movie reviews, parses the JSON from that request, and
        inserts the new movie reviews information into the ContentProvider.
     */
    synchronized public static void syncMovieReviews(Context context, long movieId) {

        try {
            // get movie reviews url
            URL movieReviewsURL = new URL(MoviesNetworkUtils.getMovieReviewsUrl(movieId));

            // use the url to retrieve the JSON
            final String movieReviewsJSONResponse = MoviesNetworkUtils.getResponseFromHttpUrl(movieReviewsURL);

            // parse the json into a list of movies content values
            ContentValues[] movieReviewsContentValues = MoviesJsonUtils.
                    getMovieReviewsContentValuesFromJson(movieReviewsJSONResponse, movieId);

            if (movieReviewsContentValues != null && movieReviewsContentValues.length != 0) {

                // get a handle on the content resolver to delete and insert data
                ContentResolver moviesContentResolver = context.getContentResolver();

                // insert our new top rated movies data into movies content provider
                moviesContentResolver.bulkInsert(ReviewsEntry.CONTENT_URI, movieReviewsContentValues);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    /*
        marks movie as favourite by inserting and updating information into the ContentProvider.
     */
    synchronized public static void markMovieAsFavourite(Context context, long movieId) {

        final Uri markMovieAsFavUri = MoviesEntry.
                getMarkMovieAsFavouriteContentUri(movieId);
        ContentValues values = new ContentValues();
        values.put(MoviesEntry.COLUMN_FAVOURITE, MoviesEntry.MARK_AS_FAVOURITE);
        int rowsUpdated = context.getContentResolver().update(markMovieAsFavUri, values, null, null);

        if (rowsUpdated == 1) {

            // get movie details from movies table
            Uri movieWithIdUri = MoviesEntry.getMovieWithIdContentUri(movieId);
            Cursor cursor = context.getContentResolver().query(movieWithIdUri, null, null, null, null);
            if (cursor != null && cursor.getCount() == 1) {

                cursor.moveToFirst();
                ContentValues favMovieValues = new ContentValues();
                favMovieValues.put(MoviesEntry.COLUMN_MOVIE_ID, cursor.getLong(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID)));
                favMovieValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_ORIGINAL_TITLE)));
                favMovieValues.put(MoviesEntry.COLUMN_POSTER_PATH, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH)));
                favMovieValues.put(MoviesEntry.COLUMN_OVERVIEW, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_OVERVIEW)));
                favMovieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, cursor.getFloat(cursor.getColumnIndex(MoviesEntry.COLUMN_VOTE_AVERAGE)));
                favMovieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_RELEASE_DATE)));
                favMovieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_BACKDROP_PATH)));
                favMovieValues.put(MoviesEntry.COLUMN_TYPE, FavouriteMoviesEntry.TYPE_FAVOURITE);

                Uri favMoviesUri = FavouriteMoviesEntry.CONTENT_URI;
                context.getContentResolver().bulkInsert(favMoviesUri, new ContentValues[]{favMovieValues});

            }
            cursor.close();

        }

    }


    /*
        un-marks movie as favourite by deleting and updating information into the ContentProvider.
     */
    synchronized public static void unmarkMovieAsFavourite(Context context, long movieId) {

        final Uri markMovieAsFavUri = MoviesEntry.
                getMarkMovieAsFavouriteContentUri(movieId);
        ContentValues values = new ContentValues();
        values.put(MoviesEntry.COLUMN_FAVOURITE, MoviesEntry.UNMARK_AS_FAVOURITE);
        int rowsUpdated = context.getContentResolver().update(markMovieAsFavUri, values, null, null);

        if (rowsUpdated == 1) {
            Uri favMovieWithIdUri = FavouriteMoviesEntry.
                    getFavouriteMovieWithIdContentUri(movieId);
            context.getContentResolver().delete(favMovieWithIdUri, null, null);
        }

    }

}

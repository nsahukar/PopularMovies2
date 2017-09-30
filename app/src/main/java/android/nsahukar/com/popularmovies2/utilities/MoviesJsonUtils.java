package android.nsahukar.com.popularmovies2.utilities;


import android.content.ContentValues;
import android.nsahukar.com.popularmovies2.data.MoviesContract.MoviesEntry;
import android.nsahukar.com.popularmovies2.data.MoviesContract.ReviewsEntry;
import android.nsahukar.com.popularmovies2.data.MoviesContract.VideosEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nikhil on 02/05/17.
 */

public final class MoviesJsonUtils {

    private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    private static ContentValues[] getMoviesContentValuesFromJson(String moviesJsonStr, String movieType) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String BACKDROP_PATH = "backdrop_path";

        JSONObject moviesJsonObj = new JSONObject(moviesJsonStr);
        JSONArray moviesJsonArr = moviesJsonObj.getJSONArray(RESULTS);

        ContentValues[] moviesContentValues = new ContentValues[moviesJsonArr.length()];
        for (int i=0; i<moviesJsonArr.length(); i++) {
            JSONObject movieJsonObj = moviesJsonArr.getJSONObject(i);

            ContentValues movieValues = new ContentValues();
            movieValues.put(MoviesEntry.COLUMN_MOVIE_ID, movieJsonObj.getLong(MOVIE_ID));
            movieValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, movieJsonObj.getString(ORIGINAL_TITLE));
            movieValues.put(MoviesEntry.COLUMN_POSTER_PATH, movieJsonObj.getString(POSTER_PATH));
            movieValues.put(MoviesEntry.COLUMN_OVERVIEW, movieJsonObj.getString(OVERVIEW));
            movieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, movieJsonObj.getDouble(VOTE_AVERAGE));
            movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, MoviesDateUtils.
                    getFriendlyDateString(movieJsonObj.getString(RELEASE_DATE)));
            movieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, movieJsonObj.getString(BACKDROP_PATH));
            movieValues.put(MoviesEntry.COLUMN_TYPE, movieType);

            moviesContentValues[i] = movieValues;
        }

        return moviesContentValues;
    }

    public static ContentValues[] getPopularMoviesContentValuesFromJson(String popularMoviesJsonStr) throws JSONException {
        return getMoviesContentValuesFromJson(popularMoviesJsonStr, MoviesEntry.TYPE_POPULAR);
    }

    public static ContentValues[] getTopRatedMoviesContentValuesFromJson(String topRatedMoviesJsonStr) throws JSONException {
        return getMoviesContentValuesFromJson(topRatedMoviesJsonStr, MoviesEntry.TYPE_TOP_RATED);
    }

    public static ContentValues[] getMovieVideosContentValuesFromJson(String movieVideosJsonStr, long movieId) throws JSONException {

        final String RESULTS = "results";
        final String VIDEO_ID = "id";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";

        final String VAL_TYPE_TRAILER = "Trailer";

        JSONObject movieVideosJsonObj = new JSONObject(movieVideosJsonStr);
        JSONArray movieVideosJsonArr = movieVideosJsonObj.getJSONArray(RESULTS);

        ArrayList<ContentValues> movieVideosContentValues = new ArrayList<>();
        for (int i=0; i<movieVideosJsonArr.length(); i++) {
            JSONObject videoJsonObj = movieVideosJsonArr.getJSONObject(i);

            final String type = videoJsonObj.getString(TYPE);
            if (type.equals(VAL_TYPE_TRAILER)) {

                ContentValues movieVideoValues = new ContentValues();
                movieVideoValues.put(VideosEntry.COLUMN_VIDEO_ID, videoJsonObj.getString(VIDEO_ID));
                movieVideoValues.put(VideosEntry.COLUMN_KEY, videoJsonObj.getString(KEY));
                movieVideoValues.put(VideosEntry.COLUMN_NAME, videoJsonObj.getString(NAME));
                movieVideoValues.put(VideosEntry.COLUMN_SITE, videoJsonObj.getString(SITE));
                movieVideoValues.put(VideosEntry.COLUMN_SIZE, videoJsonObj.getInt(SIZE));
                movieVideoValues.put(VideosEntry.COLUMN_TYPE, type);
                movieVideoValues.put(VideosEntry.COLUMN_MOVIE_ID, movieId);

                movieVideosContentValues.add(movieVideoValues);
            }

        }

        return movieVideosContentValues.toArray(new ContentValues[movieVideosContentValues.size()]);
    }

    public static ContentValues[] getMovieReviewsContentValuesFromJson(String movieReviewsJsonStr, long movieId) throws JSONException {

        final String RESULTS = "results";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject movieReviewsJsonObj = new JSONObject(movieReviewsJsonStr);
        JSONArray movieReviewsJsonArr = movieReviewsJsonObj.getJSONArray(RESULTS);

        ContentValues[] movieReviewsContentValues = new ContentValues[movieReviewsJsonArr.length()];
        for (int i=0; i<movieReviewsJsonArr.length(); i++) {
            JSONObject reviewJsonObj = movieReviewsJsonArr.getJSONObject(i);

            ContentValues movieVideoValues = new ContentValues();
            movieVideoValues.put(ReviewsEntry.COLUMN_REVIEW_ID, reviewJsonObj.getString(REVIEW_ID));
            movieVideoValues.put(ReviewsEntry.COLUMN_AUTHOR, reviewJsonObj.getString(AUTHOR));
            movieVideoValues.put(ReviewsEntry.COLUMN_CONTENT, reviewJsonObj.getString(CONTENT));
            movieVideoValues.put(ReviewsEntry.COLUMN_MOVIE_ID, movieId);

            movieReviewsContentValues[i] = movieVideoValues;
        }

        return movieReviewsContentValues;
    }

}

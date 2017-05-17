package android.nsahukar.com.popularmovies2.extra;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Nikhil on 10/05/17.
 */

public class MoviesExtraIntentService extends IntentService {


    interface IntentExtra {
        String KEY_ACTION = "action";
        String ACTION_SYNC_MOVIE_VIDEOS = "movie_videos";
        String ACTION_SYNC_MOVIE_REVIEWS = "movie_reviews";
        String ACTION_MARK_MOVIE_AS_FAV = "mark_movie_as_fav";
        String ACTION_UNMARK_MOVIE_AS_FAV = "unmark_movie_as_fav";

        String KEY_MOVIE_ID = "movie_id";
    }

    public MoviesExtraIntentService() {
        super("MoviesExtraIntentService");
    }

    private static Intent getIntentServiceWithAction(Context context, String action, long movieId) {
        Intent intentToSyncMovieVideos = new Intent(context, MoviesExtraIntentService.class);
        intentToSyncMovieVideos.putExtra(IntentExtra.KEY_ACTION, action);
        intentToSyncMovieVideos.putExtra(IntentExtra.KEY_MOVIE_ID, movieId);
        return intentToSyncMovieVideos;
    }

    public static Intent getIntentServiceToSyncMovieVideos(Context context, long movieId) {
        return getIntentServiceWithAction(context, IntentExtra.ACTION_SYNC_MOVIE_VIDEOS, movieId);
    }

    public static Intent getIntentServiceToSyncMovieReviews(Context context, long movieId) {
        return getIntentServiceWithAction(context, IntentExtra.ACTION_SYNC_MOVIE_REVIEWS, movieId);
    }
    
    public static Intent getIntentServiceToMarkMovieAsFavourite(Context context, long movieId) {
        return getIntentServiceWithAction(context, IntentExtra.ACTION_MARK_MOVIE_AS_FAV, movieId);
    }

    public static Intent getIntentServiceToUnmarkMovieAsFavourite(Context context, long movieId) {
        return getIntentServiceWithAction(context, IntentExtra.ACTION_UNMARK_MOVIE_AS_FAV, movieId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle == null || !bundle.containsKey(IntentExtra.KEY_ACTION)) {
            throw new UnsupportedOperationException("Action not specified in the intent");
        }

        final String action = bundle.getString(IntentExtra.KEY_ACTION);
        // sync movie videos
        if (action.equals(IntentExtra.ACTION_SYNC_MOVIE_VIDEOS)) {
            long movieId = bundle.getLong(IntentExtra.KEY_MOVIE_ID);
            if (movieId > 0) {
                MoviesExtraTask.syncMovieVideos(this, movieId);
            } else {
                throw new UnsupportedOperationException("Sync Movie Videos - Invalid movie id : " + movieId);
            }
        }

        // sync movie reviews
        else if (action.equals(IntentExtra.ACTION_SYNC_MOVIE_REVIEWS)) {
            long movieId = bundle.getLong(IntentExtra.KEY_MOVIE_ID);
            if (movieId > 0) {
                MoviesExtraTask.syncMovieReviews(this, movieId);
            } else {
                throw new UnsupportedOperationException("Sync Movie Reviews - Invalid movie id : " + movieId);
            }
        }

        // mark movie as favourite
        else if (action.equals(IntentExtra.ACTION_MARK_MOVIE_AS_FAV)) {
            long movieId = bundle.getLong(IntentExtra.KEY_MOVIE_ID);
            if (movieId > 0) {
                MoviesExtraTask.markMovieAsFavourite(this, movieId);
            } else {
                throw new UnsupportedOperationException("Mark Movie As Favourite - Invalid movie id : " + movieId);
            }
        }

        // mark movie as favourite
        else if (action.equals(IntentExtra.ACTION_UNMARK_MOVIE_AS_FAV)) {
            long movieId = bundle.getLong(IntentExtra.KEY_MOVIE_ID);
            if (movieId > 0) {
                MoviesExtraTask.unmarkMovieAsFavourite(this, movieId);
            } else {
                throw new UnsupportedOperationException("Unmark Movie As Favourite - Invalid movie id : " + movieId);
            }
        }

    }

}

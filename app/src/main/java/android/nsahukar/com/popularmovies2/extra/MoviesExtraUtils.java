package android.nsahukar.com.popularmovies2.extra;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.support.annotation.NonNull;

/**
 * Created by Nikhil on 10/05/17.
 */

public class MoviesExtraUtils {

    public static void syncMovieVideos(@NonNull final Context context, long movieId) {

        Cursor cursor = null;
        try {
            // check if videos of movie empty
            Uri videosWithIdUri = MoviesContract.VideosEntry.getVideoWithIdContentUri(movieId);
            cursor = context.getContentResolver().query(videosWithIdUri, null, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                // start service to get movie videos
                context.startService(MoviesExtraIntentService.getIntentServiceToSyncMovieVideos(
                        context, movieId
                ));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public static void syncMovieReviews(@NonNull final Context context, long movieId) {

        Cursor cursor = null;
        try {
            // check if reviews of movie empty
            Uri reviewsWithIdUri = MoviesContract.ReviewsEntry.getReviewsWithIdContentUri(movieId);
            cursor = context.getContentResolver().query(reviewsWithIdUri, null, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                // start service to get movie reviews
                context.startService(MoviesExtraIntentService.getIntentServiceToSyncMovieReviews(
                        context, movieId
                ));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public static void markMovieAsFavourite(Context context, long movieId) {
        // start service to mark movie as favourite
        context.startService(MoviesExtraIntentService.getIntentServiceToMarkMovieAsFavourite(
                context, movieId
        ));
    }

    public static void unmarkMovieAsFavourite(Context context, long movieId) {
        // start service to mark movie as favourite
        context.startService(MoviesExtraIntentService.getIntentServiceToUnmarkMovieAsFavourite(
                context, movieId
        ));
    }

}

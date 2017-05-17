package android.nsahukar.com.popularmovies2.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikhil on 04/05/17.
 */

public class MoviesSyncUtils {

    /*
        Interval at which to sync movies
     */
    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS;

    private static final String MOVIES_SYNC_TAG = "movies_sync";

    private static boolean sInitialized;


    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // create the job to periodically sync movies
        Job syncSunshineJob = dispatcher.newJobBuilder()
                // The Service that will be used to sync movies
                .setService(MoviesFirebaseJobService.class)
                // Set the UNIQUE tag used to identify this Job
                .setTag(MOVIES_SYNC_TAG)
                // Network constraints on which this Job should run
                .setConstraints(Constraint.ON_ANY_NETWORK)
                // setLifetime sets how long this job should persist
                .setLifetime(Lifetime.FOREVER)
                // to keep local database up to date
                .setRecurring(true)
                // execution window
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // replace the existing job with a new one (if exists)
                .setReplaceCurrent(true)
                // build the job
                .build();

        // schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
    }

    synchronized public static void initialize(@NonNull final Context context) {

        // only perform initialization once per app lifetime
        if (sInitialized) return;

        sInitialized = true;

        // This method call triggers Sunshine to create its task to synchronize weather data
        // periodically.
        scheduleFirebaseJobDispatcherSync(context);


        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = null;
                boolean syncStarted = false;

                try {
                    // check if popular movies empty
                    Uri popularMoviesUri = MoviesContract.MoviesEntry.getPopularMoviesContentUri();
                    cursor = context.getContentResolver().query(popularMoviesUri, null, null, null, null);
                    if (cursor == null || cursor.getCount() == 0) {
                        startImmediateSync(context);
                        syncStarted = true;
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                if (!syncStarted) {
                    try {
                        // check if top rated movies empty
                        Uri topRatedMoviesUri = MoviesContract.MoviesEntry.getTopRatedMoviesContentUri();
                        cursor = context.getContentResolver().query(topRatedMoviesUri, null, null, null, null);
                        if (cursor == null || cursor.getCount() == 0) {
                            startImmediateSync(context);
//                            syncStarted = true;
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }

            }
        });

        // finally, once the thread is prepared, fire it off to perform checks
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        // start service to initiate syncing
        Intent intentToStartSyncImmediately = new Intent(context, MoviesSyncIntentService.class);
        context.startService(intentToStartSyncImmediately);
    }

}

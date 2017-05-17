package android.nsahukar.com.popularmovies2.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Nikhil on 17/05/17.
 */

public class MoviesFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mSyncMoviesTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mSyncMoviesTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                MoviesSyncTask.syncMovies(context);
                jobFinished(job, false);
                return null;
            }
        };

        mSyncMoviesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mSyncMoviesTask != null) {
            mSyncMoviesTask.cancel(true);
        }
        return true;
    }

}

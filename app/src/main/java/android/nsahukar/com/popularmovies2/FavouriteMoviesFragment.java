package android.nsahukar.com.popularmovies2;


import android.database.Cursor;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.sync.MoviesSyncUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteMoviesFragment extends MoviesFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = FavouriteMoviesFragment.class.getSimpleName();


    // constructor
    public FavouriteMoviesFragment() {
        // Required empty public constructor
    }

    // static method to return fragment instance
    public static FavouriteMoviesFragment getInstance() {
        return new FavouriteMoviesFragment();
    }



    /*
        lifecycle methods
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoaderIndicator();
        getLoaderManager().initLoader(ID_FAVOURITE_MOVIES_LOADER, null, this);
    }



    /*
        LoaderManager.LoaderCallbacks methods
     */

    private static final int ID_FAVOURITE_MOVIES_LOADER = 101;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {

        switch (loaderId) {

            case ID_FAVOURITE_MOVIES_LOADER:
                // popular movies content uri
                final Uri favouriteMoviesContentUri = MoviesContract.FavouriteMoviesEntry.CONTENT_URI;
                // sort order
                final String sortOrder = MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " DESC";

                return new CursorLoader(getContext(), favouriteMoviesContentUri, null, null, null, sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            // swap cursor of the movie adapter
            mMoviesAdapter.swapCursor(data);

            if (data.getCount() != 0) {
                // showMoviesGridView() will hide all views except grid view
                // including loading indicator
                showMoviesGridView();
            } else {
                // show err msg - no movies marked as favourite yet
                showErrorMessage(getString(R.string.err_no_fav_movies));
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

}

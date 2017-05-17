package android.nsahukar.com.popularmovies2;


import android.database.Cursor;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.sync.MoviesSyncUtils;
import android.nsahukar.com.popularmovies2.utilities.MoviesNetworkUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends MoviesFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = PopularMoviesFragment.class.getSimpleName();


    // constructor
    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    // static method to return fragment instance
    public static PopularMoviesFragment getInstance() {
        PopularMoviesFragment fragment = new PopularMoviesFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, MoviesNetworkUtils.getPopularMoviesUrl());
        fragment.setArguments(args);
        return fragment;
    }



    /*
        lifecycle methods
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesBinding.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoaderManager().restartLoader(ID_POPULAR_MOVIES_LOADER, null, PopularMoviesFragment.this);
            }
        });
        showLoaderIndicator();
        getLoaderManager().initLoader(ID_POPULAR_MOVIES_LOADER, null, this);
        MoviesSyncUtils.initialize(getContext());
    }



    /*
        LoaderManager.LoaderCallbacks methods
     */

    private static final int ID_POPULAR_MOVIES_LOADER = 101;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {

        switch (loaderId) {

            case ID_POPULAR_MOVIES_LOADER:
                // popular movies content uri
                final Uri popularMoviesContentUri = MoviesContract.MoviesEntry.getPopularMoviesContentUri();
                // sort order
                final String sortOrder = MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " DESC";

                return new CursorLoader(getContext(), popularMoviesContentUri, null, null, null, sortOrder);

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
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

}

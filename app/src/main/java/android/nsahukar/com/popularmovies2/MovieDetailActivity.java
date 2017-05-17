package android.nsahukar.com.popularmovies2;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.nsahukar.com.popularmovies2.adapter.MovieDetailsAdapter;
import android.nsahukar.com.popularmovies2.adapter.VideosAdapter;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.databinding.ActivityMovieDetailBinding;
import android.nsahukar.com.popularmovies2.extra.MoviesExtraUtils;
import android.nsahukar.com.popularmovies2.utilities.MovieVideoUtils;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        VideosAdapter.OnItemClickListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    interface IntentExtra {
        String KEY_MOVIE_ID = "movie_id";
        String KEY_MOVIE_TYPE = "movie_type";
        String KEY_FAVOURITE = "is_movie_favourite";
    }

    private ActivityMovieDetailBinding mMovieDetailBinding;
    private long mMovieId;
    private String mMovieType;
    private boolean mIsMovieFavourite;
    private MovieDetailsAdapter mMovieDetailsAdapter;

    private Menu mMenu;


    private void initLoadersAndSyncData() {
        // init loader to get movie details
        getSupportLoaderManager().initLoader(ID_MOVIE_DETAILS_LOADER, null, this);
        // init loader to get movie videos
        getSupportLoaderManager().initLoader(ID_MOVIE_VIDEOS_LOADER, null, this);
        // init loader to get movie reviews
        getSupportLoaderManager().initLoader(ID_MOVIE_REVIEWS_LOADER, null, this);

        // get movie videos
        MoviesExtraUtils.syncMovieVideos(this, mMovieId);
        // get movie reviews
        MoviesExtraUtils.syncMovieReviews(this, mMovieId);
    }


    /*
        life cycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        mMovieId = getIntent().getLongExtra(IntentExtra.KEY_MOVIE_ID, 0);
        mMovieType = getIntent().getStringExtra(IntentExtra.KEY_MOVIE_TYPE);
        mIsMovieFavourite = getIntent().getBooleanExtra(IntentExtra.KEY_FAVOURITE, false);
        mMovieDetailsAdapter = new MovieDetailsAdapter(this);

        // set up toolbar
        setSupportActionBar(mMovieDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up movie details recycler view
        RecyclerView movieDetailsRecyclerView = mMovieDetailBinding.recyclerViewMovieDetail;
        LinearLayoutManager movieDetailsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        movieDetailsRecyclerView.setLayoutManager(movieDetailsLayoutManager);
        movieDetailsRecyclerView.setAdapter(mMovieDetailsAdapter);

        initLoadersAndSyncData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();
        // Use the inflater's inflate method to inflate our menu layout to this menu
        inflater.inflate(R.menu.menu_movie_detail, menu);
        // keep a reference to menu object
        mMenu = menu;
        // Return true so that the menu is displayed in the Toolbar
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mIsMovieFavourite) {
            menu.findItem(R.id.favourite).setIcon(R.drawable.ic_favorite);
        } else {
            menu.findItem(R.id.favourite).setIcon(R.drawable.ic_favorite_border_white);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: {
                onBackPressed();
                break;
            }

            case R.id.favourite: {

                if (mIsMovieFavourite) {
                    // un-mark movie as favourite
                    MoviesExtraUtils.unmarkMovieAsFavourite(this, mMovieId);
                    mIsMovieFavourite = false;
                    mMenu.findItem(R.id.favourite).setIcon(R.drawable.ic_favorite_border_white);
                } else {
                    // mark movie as favourite
                    MoviesExtraUtils.markMovieAsFavourite(this, mMovieId);
                    mIsMovieFavourite = true;
                    mMenu.findItem(R.id.favourite).setIcon(R.drawable.ic_favorite);
                }

                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }



    /*
        LoaderManager.LoaderCallbacks methods
     */

    private static final int ID_MOVIE_DETAILS_LOADER = 201;
    private static final int ID_MOVIE_VIDEOS_LOADER = 202;
    private static final int ID_MOVIE_REVIEWS_LOADER = 203;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_MOVIE_DETAILS_LOADER:
                // movie detail content uri
                final Uri movieDetailsContentUri;
                if (mMovieType.equals(MoviesContract.FavouriteMoviesEntry.TYPE_FAVOURITE)) {
                    movieDetailsContentUri = MoviesContract.FavouriteMoviesEntry.getFavouriteMovieWithIdContentUri(mMovieId);
                } else {
                    movieDetailsContentUri = MoviesContract.MoviesEntry.getMovieWithIdContentUri(mMovieId);
                }
                return new CursorLoader(this, movieDetailsContentUri, null, null, null, null);

            case ID_MOVIE_VIDEOS_LOADER:
                // movie videos content uri
                final Uri movieVideosContentUri = MoviesContract.VideosEntry.getVideoWithIdContentUri(mMovieId);
                return new CursorLoader(this, movieVideosContentUri, null, null, null, null);

            case ID_MOVIE_REVIEWS_LOADER:
                // movie reviews content uri
                final Uri movieReviewsContentUri = MoviesContract.ReviewsEntry.getReviewsWithIdContentUri(mMovieId);
                return new CursorLoader(this, movieReviewsContentUri, null, null, null, null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            if (data.getCount() != 0) {

                switch (loader.getId()) {
                    case ID_MOVIE_DETAILS_LOADER:
                        // set toolbar title as movie title
                        data.moveToFirst();
                        final String originalTitle = data.
                                getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE));
                        getSupportActionBar().setTitle(originalTitle);
                        // swap basic info cursor
                        mMovieDetailsAdapter.swapBasicInfoCursor(data);
                        break;

                    case ID_MOVIE_VIDEOS_LOADER:
                        // swap video adapter cursor
                        mMovieDetailsAdapter.swapVideosCursor(data);
                        break;

                    case ID_MOVIE_REVIEWS_LOADER:
                        // swap review adapter cursor
                        mMovieDetailsAdapter.swapReviewsCursor(data);
                        break;
                }

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        switch (loader.getId()) {
            case ID_MOVIE_DETAILS_LOADER:
                mMovieDetailsAdapter.swapBasicInfoCursor(null);
                break;

            case ID_MOVIE_VIDEOS_LOADER:
                // swap cursor of the video adapter
                mMovieDetailsAdapter.swapVideosCursor(null);
                break;

            case ID_MOVIE_REVIEWS_LOADER:
                // swap cursor of the video adapter
                mMovieDetailsAdapter.swapReviewsCursor(null);
                break;
        }

    }


    /*
        VideosAdapter.OnItemClickListener methods
     */

    @Override
    public void onClick(String videoKey, View view) {
        final String videoUrl = MovieVideoUtils.getVideoUrl(videoKey);
        Intent viewVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(viewVideoIntent);
    }

}

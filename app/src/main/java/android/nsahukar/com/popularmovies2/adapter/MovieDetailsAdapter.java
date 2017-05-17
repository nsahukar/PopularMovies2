package android.nsahukar.com.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.nsahukar.com.popularmovies2.R;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.utilities.MoviesImageUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil on 02/05/17.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.MovieDetailsViewHolder> {

    private static final String TAG = MovieDetailsAdapter.class.getSimpleName();

    private Context mContext;
    private Cursor mBasicInfoCursor;
    private VideosAdapter mMovieTrailersAdapter;
    private ReviewsAdapter mMovieReviewsAdapter;


    // constructor
    public MovieDetailsAdapter(Context context) {
        mContext = context;
        mMovieTrailersAdapter = new VideosAdapter(mContext);
        mMovieReviewsAdapter = new ReviewsAdapter(mContext);
    }

    @Override
    public MovieDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_detail_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDetailsViewHolder holder, int position) {
        if (mBasicInfoCursor != null) {
            mBasicInfoCursor.moveToFirst();

            // backdrop
            final String movieBackdropPath = mBasicInfoCursor.getString(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH));
            final String movieBackdropUrl = MoviesImageUtils.
                    getMovieImageUrl(MoviesImageUtils.Size.WIDTH_500, movieBackdropPath);
            Picasso.with(mContext)
                    .load(movieBackdropUrl)
                    .into(holder.movieBackdrop);

            // poster
            final String moviePosterPath = mBasicInfoCursor.getString(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH));
            final String moviePosterUrl = MoviesImageUtils.
                    getMovieImageUrl(MoviesImageUtils.Size.WIDTH_DEFAULT, moviePosterPath);
            Picasso.with(mContext)
                    .load(moviePosterUrl)
                    .into(holder.moviePoster);

            // original title
            final String originalTitle = mBasicInfoCursor.getString(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE));
            holder.originalTitle.setText(originalTitle);

            // release date
            final String releaseDate = mBasicInfoCursor.getString(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));
            holder.releaseDate.setText(releaseDate);

            // rating
            final float voteAverage = mBasicInfoCursor.getFloat(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE));
            holder.rating.setRating(voteAverage / 2);

            // plot
            final String plot = mBasicInfoCursor.getString(mBasicInfoCursor.
                    getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW));
            holder.plot.setText(plot);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    // View Holder
    public class MovieDetailsViewHolder extends RecyclerView.ViewHolder {

        public final ImageView movieBackdrop;
        public final ImageView moviePoster;
        public final TextView originalTitle;
        public final TextView releaseDate;
        public final RatingBar rating;
        public final TextView plot;

        public final RecyclerView movieTrailers;
        public final RecyclerView movieReviews;

        public MovieDetailsViewHolder(View itemView) {
            super(itemView);
            movieBackdrop = (ImageView) itemView.findViewById(R.id.movie_backdrop);
            moviePoster = (ImageView) itemView.findViewById(R.id.poster);
            originalTitle = (TextView) itemView.findViewById(R.id.original_title);
            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            plot = (TextView) itemView.findViewById(R.id.plot);

            movieTrailers = (RecyclerView) itemView.findViewById(R.id.recycler_view_trailers);
            LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            movieTrailers.setLayoutManager(trailersLayoutManager);
            movieTrailers.setAdapter(mMovieTrailersAdapter);

            movieReviews = (RecyclerView) itemView.findViewById(R.id.recycler_view_reviews);
            LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            movieReviews.setLayoutManager(reviewsLayoutManager);
            movieReviews.setAdapter(mMovieReviewsAdapter);
        }

    }

    // swap basic info cursor
    public void swapBasicInfoCursor(Cursor newCursor) {
        mBasicInfoCursor = newCursor;
        notifyDataSetChanged();
    }

    // swap videos cursor
    public void swapVideosCursor(Cursor newCursor) {
        mMovieTrailersAdapter.swapCursor(newCursor);
    }

    // swap reviews cursor
    public void swapReviewsCursor(Cursor newCursor) {
        mMovieReviewsAdapter.swapCursor(newCursor);
    }

}

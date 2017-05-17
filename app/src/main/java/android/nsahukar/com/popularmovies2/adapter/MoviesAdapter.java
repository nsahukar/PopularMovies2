package android.nsahukar.com.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.nsahukar.com.popularmovies2.R;
import android.nsahukar.com.popularmovies2.data.MoviesContract.MoviesEntry;
import android.nsahukar.com.popularmovies2.utilities.MoviesImageUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil on 02/05/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private final OnItemClickListener mItemClickListener;

    // indexes
    private int mColumnPosterPathIndex = -1;

    // constructor
    public MoviesAdapter(Context context) {
        if (context instanceof OnItemClickListener) {
            mContext = context;
            mItemClickListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MoviesAdapter.OnItemClickListener");
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        if (mColumnPosterPathIndex == -1) {
            mColumnPosterPathIndex = mCursor.getColumnIndex(MoviesEntry.COLUMN_POSTER_PATH);
        }
        final String moviePosterPath = mCursor.getString(mColumnPosterPathIndex);
        final String moviePosterUrl = MoviesImageUtils.
                getMovieImageUrl(MoviesImageUtils.Size.WIDTH_DEFAULT, moviePosterPath);
        Picasso.with(mContext).load(moviePosterUrl).into(holder.moviePosterImageButton);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    // View Holder
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageButton moviePosterImageButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePosterImageButton = (ImageButton) itemView.findViewById(R.id.poster);
            moviePosterImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());

            long movieId = mCursor.getLong(mCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID));
            String movieType = mCursor.getString(mCursor.getColumnIndex(MoviesEntry.COLUMN_TYPE));
            boolean favourite = mCursor.getInt(mCursor.getColumnIndex(MoviesEntry.COLUMN_FAVOURITE)) == 1;
            mItemClickListener.onClick(movieId, movieType, favourite, view);
        }
    }

    // click listener interface
    public interface OnItemClickListener {
        void onClick(long movieId, String movieType, boolean favourite, View view);
    }

    // swap movies cursor
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}

package android.nsahukar.com.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.nsahukar.com.popularmovies2.R;
import android.nsahukar.com.popularmovies2.data.MoviesContract;
import android.nsahukar.com.popularmovies2.data.MoviesContract.VideosEntry;
import android.nsahukar.com.popularmovies2.utilities.MovieVideoUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil on 02/05/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.VideoViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    // indexes
    private int mColumnAuthorIndex = -1;
    private int mColumnContentIndex = -1;

    // constructor
    public ReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        if (mColumnAuthorIndex == -1) {
            mColumnAuthorIndex = mCursor.getColumnIndex(MoviesContract.ReviewsEntry.COLUMN_AUTHOR);
        }
        if (mColumnContentIndex == -1) {
            mColumnContentIndex = mCursor.getColumnIndex(MoviesContract.ReviewsEntry.COLUMN_CONTENT);
        }

        final String author = mCursor.getString(mColumnAuthorIndex);
        holder.author.setText(author);

        final String content = mCursor.getString(mColumnContentIndex);
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    // View Holder
    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public final TextView author;
        public final TextView content;

        public VideoViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    // swap videos cursor
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}

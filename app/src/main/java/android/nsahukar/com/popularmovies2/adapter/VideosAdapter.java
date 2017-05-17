package android.nsahukar.com.popularmovies2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.nsahukar.com.popularmovies2.R;
import android.nsahukar.com.popularmovies2.data.MoviesContract.VideosEntry;
import android.nsahukar.com.popularmovies2.utilities.MovieVideoUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

/**
 * Created by Nikhil on 02/05/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private final OnItemClickListener mItemClickListener;

    // indexes
    private int mColumnKeyIndex = -1;

    // constructor
    public VideosAdapter(Context context) {
        if (context instanceof OnItemClickListener) {
            mContext = context;
            mItemClickListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MoviesAdapter.OnItemClickListener");
        }
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        if (mColumnKeyIndex == -1) {
            mColumnKeyIndex = mCursor.getColumnIndex(VideosEntry.COLUMN_KEY);
        }
        final String videoKey = mCursor.getString(mColumnKeyIndex);
        final String videoThumbnailUrl = MovieVideoUtils.
                getVideoThumbnailUrl(videoKey);
        Picasso.with(mContext).load(videoThumbnailUrl).into(holder.videoThumbnailImageButton);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    // View Holder
    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageButton videoThumbnailImageButton;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoThumbnailImageButton = (ImageButton) itemView.findViewById(R.id.thumbnail);
            videoThumbnailImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());

            String videoKey = mCursor.getString(mCursor.getColumnIndex(VideosEntry.COLUMN_KEY));
            mItemClickListener.onClick(videoKey, view);
        }
    }

    // click listener interface
    public interface OnItemClickListener {
        void onClick(String videoKey, View view);
    }

    // swap videos cursor
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}

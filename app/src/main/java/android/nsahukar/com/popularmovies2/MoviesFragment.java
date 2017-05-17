package android.nsahukar.com.popularmovies2;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.nsahukar.com.popularmovies2.adapter.MoviesAdapter;
import android.nsahukar.com.popularmovies2.databinding.FragmentMoviesBinding;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();
    protected static final String KEY_URL = "key_url";

    // url to fetch movies data from
    protected URL mMoviesURL;

    // binding
    protected FragmentMoviesBinding mMoviesBinding;

    // adapter
    protected MoviesAdapter mMoviesAdapter;

    // item decoration
    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    // constructor
    public MoviesFragment() {
        // Required empty public constructor
    }


    // show loader indicator
    protected void showLoaderIndicator() {
        mMoviesBinding.layoutError.setVisibility(ConstraintLayout.INVISIBLE);
        mMoviesBinding.recyclerViewMovies.setVisibility(RecyclerView.INVISIBLE);
        mMoviesBinding.progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    // show error message
    public void showErrorMessage(String errorMessage) {
        mMoviesBinding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        mMoviesBinding.recyclerViewMovies.setVisibility(RecyclerView.INVISIBLE);
        mMoviesBinding.layoutError.setVisibility(ConstraintLayout.VISIBLE);
        mMoviesBinding.errorMsgTextView.setText(errorMessage);
    }

    // show grid view
    protected void showMoviesGridView() {
        mMoviesBinding.progressBar.setVisibility(ProgressBar.INVISIBLE);
        mMoviesBinding.layoutError.setVisibility(ConstraintLayout.INVISIBLE);
        mMoviesBinding.recyclerViewMovies.setVisibility(RecyclerView.VISIBLE);
    }

    // Wrapper class that serves as a union of a result value and an exception.
    protected class Result<T> {
        public T value;
        public Exception exception;
        public Result(T resultValue) {
            value = resultValue;
        }
        public Result(Exception e) {
            exception = e;
        }
    }


    /*
        life cycle methods
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(KEY_URL)) {
            try {
                mMoviesURL = new URL(args.getString(KEY_URL));
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);

        // set up recycler view
        final int spanCount = getResources().getInteger(R.integer.grid_span_count);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount, GridLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = mMoviesBinding.recyclerViewMovies;
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.grid_item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        // set adapter to recycler view
        mMoviesAdapter = new MoviesAdapter(getContext());
        recyclerView.setAdapter(mMoviesAdapter);

        return mMoviesBinding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMoviesAdapter = null;
        mMoviesBinding = null;
        mMoviesURL = null;
    }

}

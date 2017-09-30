package android.nsahukar.com.popularmovies2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.nsahukar.com.popularmovies2.adapter.MoviesAdapter;
import android.nsahukar.com.popularmovies2.databinding.ActivityMainBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    // movie tab adapter
    private class MovieTabsPagerAdapter extends FragmentPagerAdapter {

        // movie tabs to be shown in tab layout
        private String[] tabs;

        public MovieTabsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            final String title = tabs[position];
            if (title.equals(getString(R.string.tab_popular))) {
                return PopularMoviesFragment.getInstance();
            }
            else if (title.equals(getString(R.string.tab_top_rated))) {
                return TopRatedMoviesFragment.getInstance();
            }
            else if (title.equals(getString(R.string.tab_favourite))) {
                return FavouriteMoviesFragment.getInstance();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    // activity components binding variable
    private ActivityMainBinding mActivityMainBinding;


    /*
        life cycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // setup toolbar, view pager and tab layout
        setSupportActionBar(mActivityMainBinding.toolbar);
        mActivityMainBinding.viewPager.setAdapter(new MovieTabsPagerAdapter(getSupportFragmentManager()));
        mActivityMainBinding.tabLayout.setupWithViewPager(mActivityMainBinding.viewPager);
    }


    /*
        MoviesAdapter.OnItemClickListener methods
     */

    @Override
    public void onClick(long movieId, String movieType, boolean favourite, View view) {
        Intent intentToStartMovieDetailActivity = new Intent(this, MovieDetailActivity.class);
        intentToStartMovieDetailActivity.putExtra(MovieDetailActivity.IntentExtra.KEY_MOVIE_ID, movieId);
        intentToStartMovieDetailActivity.putExtra(MovieDetailActivity.IntentExtra.KEY_MOVIE_TYPE, movieType);
        intentToStartMovieDetailActivity.putExtra(MovieDetailActivity.IntentExtra.KEY_FAVOURITE, favourite);
        startActivity(intentToStartMovieDetailActivity);
    }
}

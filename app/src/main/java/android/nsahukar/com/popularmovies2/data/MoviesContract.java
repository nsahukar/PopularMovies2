package android.nsahukar.com.popularmovies2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nikhil on 03/05/17.
 */

public class MoviesContract {


    /* content authority - name for the entire content provider */
    public static final String CONTENT_AUTHORITY = "android.nsahukar.com.popularmovies2";

    /* using content authority to create base of all URIs which app will use to contact content
        provider */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    /* movies path */
    public static final String PATH_MOVIES = "/movies";
    public static final String PATH_POPULAR_MOVIES = PATH_MOVIES + "/" + MoviesEntry.TYPE_POPULAR;
    public static final String PATH_TOP_RATED_MOVIES = PATH_MOVIES + "/" + MoviesEntry.TYPE_TOP_RATED;
    public static final String PATH_MOVIE_WITH_ID  = PATH_MOVIES + "/#";
    public static final String PATH_MARK_MOVIE_AS_FAVOURITE = PATH_MOVIES + "/fav/#";


    /* favourite movies path */
    public static final String PATH_FAVOURITE_MOVIES = "/favourite_movies";
    public static final String PATH_FAVOURITE_MOVIES_WITH_ID = PATH_FAVOURITE_MOVIES + "/#";


    /* videos path */
    public static final String PATH_VIDEOS = "/videos";
    public static final String PATH_VIDEOS_WITH_ID = PATH_VIDEOS + "/#";


    /* reviews path */
    public static final String PATH_REVIEWS = "/reviews";
    public static final String PATH_REVIEWS_WITH_ID = PATH_REVIEWS + "/#";



    /* Abstract base entry inner class */
    private static abstract class BaseEntry implements BaseColumns {

        protected static Uri getContentUriWithPath(String path) {
            return BASE_CONTENT_URI.buildUpon().appendEncodedPath(path).build();
        }

    }


    /* Interface defining movies columns */
    private interface MoviesColumns {
        String COLUMN_MOVIE_ID = "movie_id";
        String COLUMN_ORIGINAL_TITLE = "original_title";
        String COLUMN_POSTER_PATH = "poster_path";
        String COLUMN_OVERVIEW = "overview";
        String COLUMN_VOTE_AVERAGE = "vote_average";
        String COLUMN_RELEASE_DATE = "release_date";
        String COLUMN_BACKDROP_PATH = "backdrop_path";
        String COLUMN_TYPE = "type";
        String COLUMN_FAVOURITE = "favourite";
    }


    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry extends BaseEntry implements MoviesColumns {

        /*
            table name
        */

        public static final String TABLE_NAME = "movies";


        /*
            values
        */

        public static final String TYPE_POPULAR = "popular";
        public static final String TYPE_TOP_RATED = "top_rated";
        public static final int MARK_AS_FAVOURITE = 1;
        public static final int UNMARK_AS_FAVOURITE = 0;


        /*
            content uri(s)
        */

        // the base content uri used to query the movies table from content provider
        public static final Uri CONTENT_URI = getContentUriWithPath(PATH_MOVIES);

        public static Uri getPopularMoviesContentUri() {
            return getContentUriWithPath(PATH_POPULAR_MOVIES);
        }

        public static Uri getTopRatedMoviesContentUri() {
            return getContentUriWithPath(PATH_TOP_RATED_MOVIES);
        }

        public static Uri getMovieWithIdContentUri(long movieId) {
            final String path = PATH_MOVIE_WITH_ID.replace("#", Long.toString(movieId));
            return getContentUriWithPath(path);
        }

        public static Uri getMarkMovieAsFavouriteContentUri(long movieId) {
            final String path = PATH_MARK_MOVIE_AS_FAVOURITE.replace("#", Long.toString(movieId));
            return getContentUriWithPath(path);
        }

    }


    /* Inner class that defines the table contents of the favourite movies table */
    public static final class FavouriteMoviesEntry extends BaseEntry implements MoviesColumns {

        /*
            table name
         */

        public static final String TABLE_NAME = "favourite_movies";


        /*
            values
        */

        public static final String TYPE_FAVOURITE = "favourite";


        /*
            content uri(s)
         */

        // the base content uri used to query the movies table from content provider
        public static final Uri CONTENT_URI = getContentUriWithPath(PATH_FAVOURITE_MOVIES);

        public static final Uri getFavouriteMovieWithIdContentUri(long movieId) {
            final String path = PATH_FAVOURITE_MOVIES_WITH_ID.replace("#", Long.toString(movieId));
            return getContentUriWithPath(path);
        }

    }


    /* Inner class that defines the table contents of the videos table */
    public static final class VideosEntry extends BaseEntry {

        /*
            table name
         */

        public static final String TABLE_NAME = "videos";


        /*
            columns
         */

        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MOVIE_ID = "movie_id";        // foreign key, movies table


        /*
            content uri(s)
         */

        // the base content uri used to query the videos table from the content provider
        public static final Uri CONTENT_URI = getContentUriWithPath(PATH_VIDEOS);

        public static final Uri getVideoWithIdContentUri(long movieId) {
            final String path = PATH_VIDEOS_WITH_ID.replace("#", Long.toString(movieId));
            return getContentUriWithPath(path);
        }

    }


    /* Inner class that defines the table contents of the reviews table */
    public static final class ReviewsEntry extends BaseEntry {

        /*
            table name
         */

        public static final String TABLE_NAME = "reviews";


        /*
            columns
         */

        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "key";
        public static final String COLUMN_CONTENT = "name";
        public static final String COLUMN_MOVIE_ID = "movie_id";        // foreign key, movies table


        /*
            content uri(s)
         */

        // the base content uri used to query the videos table from the content provider
        public static final Uri CONTENT_URI = getContentUriWithPath(PATH_REVIEWS);

        public static final Uri getReviewsWithIdContentUri(long movieId) {
            final String path = PATH_REVIEWS_WITH_ID.replace("#", Long.toString(movieId));
            return getContentUriWithPath(path);
        }

    }


}

package android.nsahukar.com.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.nsahukar.com.popularmovies2.data.MoviesContract.*;

/**
 * Created by Nikhil on 04/05/17.
 */

public class MoviesProvider extends ContentProvider {


    /* these constants will be used to match URIs with the data they are looking for */
    public static final int CODE_MOVIES = 100;
    public static final int CODE_POPULAR_MOVIES = 101;
    public static final int CODE_TOP_RATED_MOVIES = 102;
    public static final int CODE_MOVIE_WITH_ID = 103;
    public static final int CODE_MARK_MOVIE_AS_FAVOURITE = 104;

    public static final int CODE_FAVOURITE_MOVIES = 200;
    public static final int CODE_FAVOURITE_MOVIES_WITH_ID = 201;

    public static final int CODE_VIDEOS = 300;
    public static final int CODE_VIDEOS_WITH_MOVIE_ID = 301;

    public static final int CODE_REVIEWS = 400;
    public static final int CODE_REVIEWS_WITH_MOVIE_ID = 401;


    /* creates the URIMatcher that will match each URI to the constants defined above */
    public static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        /* Movies - URI is content://android.nsahukar.com.popularmovies2/movies */
        uriMatcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);

        /* Popular movies - URI is content://android.nsahukar.com.popularmovies2/movies/popular */
        uriMatcher.addURI(authority, MoviesContract.PATH_POPULAR_MOVIES, CODE_POPULAR_MOVIES);

        /* Top rated movies - URI is content://android.nsahukar.com.popularmovies2/movies/top_rated */
        uriMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED_MOVIES, CODE_TOP_RATED_MOVIES);

        /* Specific movie - URI is content://android.nsahukar.com.popularmovies2/movies/# */
        uriMatcher.addURI(authority, MoviesContract.PATH_MOVIE_WITH_ID, CODE_MOVIE_WITH_ID);

        /* Mark/Unmark movie as favourite - URI is content://android.nsahukar.com.popularmovies2/movies/#/fav/# */
        uriMatcher.addURI(authority, MoviesContract.PATH_MARK_MOVIE_AS_FAVOURITE, CODE_MARK_MOVIE_AS_FAVOURITE);



        /* Favourite movies - URI is content://android.nsahukar.com.popularmovies2/favourite_movies */
        uriMatcher.addURI(authority, MoviesContract.PATH_FAVOURITE_MOVIES, CODE_FAVOURITE_MOVIES);

        /* Specific favourite movie - URI is content://android.nsahukar.com.popularmovies2/favourite_movies/# */
        uriMatcher.addURI(authority, MoviesContract.PATH_FAVOURITE_MOVIES_WITH_ID, CODE_FAVOURITE_MOVIES_WITH_ID);



        /* Videos - URI is content://android.nsahukar.com.popularmovies2/videos */
        uriMatcher.addURI(authority, MoviesContract.PATH_VIDEOS, CODE_VIDEOS);

        /* Videos with movie id - URI is content://android.nsahukar.com.popularmovies2/videos/# */
        uriMatcher.addURI(authority, MoviesContract.PATH_VIDEOS_WITH_ID, CODE_VIDEOS_WITH_MOVIE_ID);



        /* Reviews - URI is content://android.nsahukar.com.popularmovies2/reviews */
        uriMatcher.addURI(authority, MoviesContract.PATH_REVIEWS, CODE_REVIEWS);

        /* Reviews with movie id - URI is content://android.nsahukar.com.popularmovies2/reviews/# */
        uriMatcher.addURI(authority, MoviesContract.PATH_REVIEWS_WITH_ID, CODE_REVIEWS_WITH_MOVIE_ID);


        return uriMatcher;
    }


    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mMoviesDbHelper;


    /*
        Content Provider Methods
     */

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String tableName;
        switch (sUriMatcher.match(uri)) {

            case CODE_POPULAR_MOVIES:
            case CODE_TOP_RATED_MOVIES: {
                tableName = MoviesEntry.TABLE_NAME;
                selection = MoviesEntry.COLUMN_TYPE + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            case CODE_MOVIE_WITH_ID: {
                tableName = MoviesEntry.TABLE_NAME;
                selection = MoviesEntry.COLUMN_MOVIE_ID + "=?";
                final String movieId = uri.getLastPathSegment();
                selectionArgs = new String[] {movieId};
                break;
            }

            case CODE_FAVOURITE_MOVIES: {
                tableName = FavouriteMoviesEntry.TABLE_NAME;
                break;
            }

            case CODE_FAVOURITE_MOVIES_WITH_ID: {
                tableName = FavouriteMoviesEntry.TABLE_NAME;
                selection = FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            case CODE_VIDEOS_WITH_MOVIE_ID: {
                tableName = VideosEntry.TABLE_NAME;
                selection = VideosEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            case CODE_REVIEWS_WITH_MOVIE_ID: {
                tableName = ReviewsEntry.TABLE_NAME;
                selection = ReviewsEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        Cursor cursor;
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        String tableName;
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                tableName = MoviesEntry.TABLE_NAME;
                break;

            case CODE_FAVOURITE_MOVIES:
                tableName = FavouriteMoviesEntry.TABLE_NAME;
                break;

            case CODE_VIDEOS:
                tableName = VideosEntry.TABLE_NAME;
                break;

            case CODE_REVIEWS:
                tableName = ReviewsEntry.TABLE_NAME;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsInserted;
        }

        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        String tableName;
        switch (sUriMatcher.match(uri)) {

            case CODE_POPULAR_MOVIES:
            case CODE_TOP_RATED_MOVIES: {
                tableName = MoviesEntry.TABLE_NAME;
                selection = MoviesEntry.COLUMN_TYPE + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            case CODE_FAVOURITE_MOVIES_WITH_ID: {
                tableName = FavouriteMoviesEntry.TABLE_NAME;
                selection = FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        int numRowsDeleted;
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        if (null == selection) selection = "1";

        numRowsDeleted =  db.delete(tableName, selection, selectionArgs);
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        String tableName;
        switch (sUriMatcher.match(uri)) {

            case CODE_MARK_MOVIE_AS_FAVOURITE: {
                tableName = MoviesEntry.TABLE_NAME;
                selection = FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        int numRowsUpdated;
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        numRowsUpdated =  db.update(tableName, contentValues, selection, selectionArgs);
        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(MoviesEntry.getPopularMoviesContentUri(), null);
            getContext().getContentResolver().notifyChange(MoviesEntry.getTopRatedMoviesContentUri(), null);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

}

package android.nsahukar.com.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.nsahukar.com.popularmovies2.data.MoviesContract.*;

/**
 * Created by Nikhil on 04/05/17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    /* database name */
    private static final String DATABASE_NAME = "movies.db";

    /* database version */
    private static final int DATABASE_VERSION = 1;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* database methods */

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /* sql statement that will create the movies table */
        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +

                        MoviesEntry._ID +                       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesEntry.COLUMN_MOVIE_ID +           " INTEGER NOT NULL, " +
                        MoviesEntry.COLUMN_ORIGINAL_TITLE +     " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_POSTER_PATH +        " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_OVERVIEW +           " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_VOTE_AVERAGE +       " REAL NOT NULL, " +
                        MoviesEntry.COLUMN_RELEASE_DATE +       " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_BACKDROP_PATH +      " TEXT NOT NULL, " +
                        MoviesEntry.COLUMN_TYPE +               " TEXT NOT NULL," +
                        MoviesEntry.COLUMN_FAVOURITE +          " INTEGER NOT NULL DEFAULT 0, " +

                        " UNIQUE (" + MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +

                ");";


        /* sql statement that will create the favourite movies table */
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE =

                "CREATE TABLE " + FavouriteMoviesEntry.TABLE_NAME + " (" +

                        FavouriteMoviesEntry._ID +                       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouriteMoviesEntry.COLUMN_MOVIE_ID +           " INTEGER NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_ORIGINAL_TITLE +     " TEXT NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_POSTER_PATH +        " TEXT NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_OVERVIEW +           " TEXT NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_VOTE_AVERAGE +       " REAL NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_RELEASE_DATE +       " TEXT NOT NULL, " +
                        FavouriteMoviesEntry.COLUMN_BACKDROP_PATH +      " TEXT NOT NULL, " +

                        FavouriteMoviesEntry.COLUMN_TYPE +               " TEXT NOT NULL DEFAULT '" +
                        FavouriteMoviesEntry.TYPE_FAVOURITE + "'," +

                        FavouriteMoviesEntry.COLUMN_FAVOURITE +          " INTEGER NOT NULL DEFAULT 1, " +

                        " UNIQUE (" + FavouriteMoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +

                ");";


        /* sql statement that will create the videos table */
        final String SQL_CREATE_VIDEOS_TABLE =

                "CREATE TABLE " + VideosEntry.TABLE_NAME + " (" +

                        VideosEntry._ID +                       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        VideosEntry.COLUMN_VIDEO_ID +           " TEXT NOT NULL, " +
                        VideosEntry.COLUMN_KEY +                " TEXT NOT NULL, " +
                        VideosEntry.COLUMN_NAME +               " TEXT NOT NULL, " +
                        VideosEntry.COLUMN_SITE +               " TEXT NOT NULL, " +
                        VideosEntry.COLUMN_SIZE +               " INTEGER NOT NULL, " +
                        VideosEntry.COLUMN_TYPE +               " TEXT NOT NULL, " +

                        VideosEntry.COLUMN_MOVIE_ID +           " INTEGER REFERENCES " +
                        MoviesEntry.TABLE_NAME +                "(" + MoviesEntry.COLUMN_MOVIE_ID + ") ON DELETE CASCADE, " +

                        " UNIQUE (" + VideosEntry.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE" +

                ");";


        /* sql statement that will create the reviews table */
        final String SQL_CREATE_REVIEWS_TABLE =

                "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +

                        ReviewsEntry._ID +                       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewsEntry.COLUMN_REVIEW_ID +          " TEXT NOT NULL, " +
                        ReviewsEntry.COLUMN_AUTHOR +             " TEXT NOT NULL, " +
                        ReviewsEntry.COLUMN_CONTENT +            " TEXT NOT NULL, " +

                        ReviewsEntry.COLUMN_MOVIE_ID +           " INTEGER REFERENCES " +
                        MoviesEntry.TABLE_NAME +                "(" + MoviesEntry.COLUMN_MOVIE_ID + ") ON DELETE CASCADE, " +

                        " UNIQUE (" + ReviewsEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE" +

                ");";


        /* execute the sql statement with the execSQL method of our SQLite database object */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}

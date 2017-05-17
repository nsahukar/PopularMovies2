package android.nsahukar.com.popularmovies2.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Nikhil on 02/05/17.
 */

public class MoviesNetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String POPULAR_MOVIES_EXTENSION = "/movie/popular";
    private static final String TOP_RATED_MOVIES_EXTENSION = "/movie/top_rated";

    private static final String MOVIE_ID_PLACEHOLDER = "<id>";
    private static final String MOVIE_VIDEOS_EXTENSION = "/movie/" + MOVIE_ID_PLACEHOLDER + "/videos";
    private static final String MOVIE_REVIEWS_EXTENSION = "/movie/" + MOVIE_ID_PLACEHOLDER + "/reviews";

    private final static String API_KEY_PARAM = "api_key";
    private final static String LANGUAGE_PARAM = "language";

    /*
        Enter your API key from the themoviedb.org site
     */
    private static final String apiKey = "<ENTER YOUR API KEY HERE>";
    private static final String language = "en-US";


    private static String buildUrlWithExtension(String extension) {
        String extendedUrl = BASE_URL + extension;
        Uri builtUri = Uri.parse(extendedUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();
        return builtUri.toString();
    }

    public static String getPopularMoviesUrl() {
        return buildUrlWithExtension(POPULAR_MOVIES_EXTENSION);
    }

    public static String getTopRatedMoviesUrl() {
        return buildUrlWithExtension(TOP_RATED_MOVIES_EXTENSION);
    }

    public static String getMovieVideosUrl(long movieId) {
        String movieIdStr = Long.toString(movieId);
        String movieVideosExtensionWithMovieId = MOVIE_VIDEOS_EXTENSION.replace(MOVIE_ID_PLACEHOLDER, movieIdStr);
        return buildUrlWithExtension(movieVideosExtensionWithMovieId);
    }

    public static String getMovieReviewsUrl(long movieId) {
        String movieIdStr = Long.toString(movieId);
        String movieReviewsExtensionWithMovieId = MOVIE_REVIEWS_EXTENSION.replace(MOVIE_ID_PLACEHOLDER, movieIdStr);
        return buildUrlWithExtension(movieReviewsExtensionWithMovieId);
    }


    public static boolean isActiveNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        } else {
            return true;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).

            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                Scanner scanner = new Scanner(stream);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    result = scanner.next();
                }
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

}

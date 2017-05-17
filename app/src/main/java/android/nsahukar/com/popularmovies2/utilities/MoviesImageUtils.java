package android.nsahukar.com.popularmovies2.utilities;

import android.net.Uri;

/**
 * Created by Nikhil on 02/05/17.
 */

public class MoviesImageUtils {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static String buildImageUrlWithExtension(String extension) {
        String extendedUrl = IMAGE_BASE_URL + extension;
        Uri builtUri = Uri.parse(extendedUrl).buildUpon()
                .build();
        return builtUri.toString();
    }

    public interface Size {
        String WIDTH_92 = "w92";
        String WIDTH_154 = "w154";
        String WIDTH_185 = "w185";
        String WIDTH_342 = "w342";
        String WIDTH_500 = "w500";
        String WIDTH_780 = "w780";
        String WIDTH_ORIGINAL = "original";
        String WIDTH_DEFAULT = WIDTH_185;
    }

    public static String getMovieImageUrl(String imageSize, String imagePath) {
        final String extension = imageSize + imagePath;
        return buildImageUrlWithExtension(extension);
    }

}

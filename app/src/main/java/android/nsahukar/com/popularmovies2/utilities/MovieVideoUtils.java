package android.nsahukar.com.popularmovies2.utilities;

import android.net.Uri;

/**
 * Created by Nikhil on 10/05/17.
 */

public class MovieVideoUtils {

    private static final String VIDEO_KEY_PLACEHOLDER = "<video_id>";
    private static final String VIDEO_THUMBNAIL_URL = "http://img.youtube.com/vi/" +
            VIDEO_KEY_PLACEHOLDER + "/mqdefault.jpg";
    private static final String VIDEO_URL = "http://www.youtube.com/watch?v=" + VIDEO_KEY_PLACEHOLDER;


    public static String getVideoThumbnailUrl(final String videoKey) {
        final String videoThumbnailUrl = VIDEO_THUMBNAIL_URL.replace(VIDEO_KEY_PLACEHOLDER, videoKey);
        Uri builtUri = Uri.parse(videoThumbnailUrl).buildUpon()
                .build();
        return builtUri.toString();
    }

    public static String getVideoUrl(final String videoKey) {
        final String videoUrl = VIDEO_URL.replace(VIDEO_KEY_PLACEHOLDER, videoKey);
        Uri builtUri = Uri.parse(videoUrl).buildUpon()
                .build();
        return builtUri.toString();
    }

}

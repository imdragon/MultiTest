package android.example.com.multitest;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovDBContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_MOVIES = "movie";

        public static final String _ID = "_id";
        public static final String COLUMN_MOVIEID = "apiID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "synopsis";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_FAVORITE = "isFavorite";

        // Content Uri section

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_MOVIES).build();
        // cursor
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        public static Uri buildMovieUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}

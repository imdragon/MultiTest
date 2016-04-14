package android.example.com.multitest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "popmovies.db";
    public static final int DATABASE_VERSION = 1;


    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MovDBContract.MovieEntry.TABLE_MOVIES + "(" +
                MovDBContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovDBContract.MovieEntry.COLUMN_MOVIEID + " TEXT NOT NULL UNIQUE, " +
                MovDBContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL, " +
                MovDBContract.MovieEntry.COLUMN_FAVORITE +
                " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// copied from gist

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MovDBContract.MovieEntry.TABLE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovDBContract.MovieEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(db);
    }
}

package mrh5493.edu.psu.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class DoodleProvider extends ContentProvider {

    static final String PROVIDER_NAME = "mrh5493.psu.edu.DoodleProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/doodle";
    static final Uri CONTENT_URI = Uri.parse(URL);
    DoodleOpenHelper doodleOpenHelper;

    private static final UriMatcher uriMatcher;

    private static final int DOODLE = 1;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "doodle", DOODLE);
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        doodleOpenHelper = DoodleOpenHelper.getInstance(context);

        SQLiteDatabase db = doodleOpenHelper.getWritableDatabase();
        return db == null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = doodleOpenHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case DOODLE:
                cursor = db.query(DoodleContract.DoodleTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

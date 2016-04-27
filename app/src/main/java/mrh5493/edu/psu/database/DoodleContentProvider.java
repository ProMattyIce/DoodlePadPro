package mrh5493.edu.psu.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class DoodleContentProvider extends ContentProvider {

    private DoodleOpenHelper doodleOpenHelper;

    private static final String AUTHORITY = "mrh5493.edu.psu";
    private static final String BASE_PATH = "doodle";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int DOODLE = 1;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, DOODLE);
    }

    @Override
    public boolean onCreate() {
        doodleOpenHelper = DoodleOpenHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = doodleOpenHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case DOODLE:
                cursor = db.query(DoodleContract.DoodleTable.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    private String appendIdToSelection(String selection, String sId) {
        int id = Integer.valueOf(sId);

        if (selection == null || selection.trim().equals(""))
            return "_ID = " + id;
        else
            return selection + " AND _ID = " + id;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //This hopefully will work
        SQLiteDatabase database = doodleOpenHelper.getWritableDatabase();
        long id;

        switch (uriMatcher.match(uri)) {
            case DOODLE:
                id = database.insert(DoodleContract.DoodleTable.TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
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

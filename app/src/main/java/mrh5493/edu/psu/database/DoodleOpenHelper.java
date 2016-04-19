package mrh5493.edu.psu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DoodleOpenHelper extends SQLiteOpenHelper {

    private static DoodleOpenHelper sInstance;

    public static synchronized DoodleOpenHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DoodleOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DoodleOpenHelper(Context context) {
        super(context, DoodleContract.DATABASE_NAME, null, DoodleContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DoodleContract.DoodleTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

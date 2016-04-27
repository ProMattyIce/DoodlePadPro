package mrh5493.edu.psu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DoodleOpenHelper extends SQLiteOpenHelper {

    private static DoodleOpenHelper Instance;

    public static synchronized DoodleOpenHelper getInstance(Context context) {
        if (Instance == null)
            Instance = new DoodleOpenHelper(context.getApplicationContext());
        return Instance;
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

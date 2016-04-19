package mrh5493.edu.psu.database;


import android.provider.BaseColumns;

public class DoodleContract {

    //DB Types
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "doodlePadDataBase.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private DoodleContract() {
    }

    ;

    public static abstract class DoodleTable implements BaseColumns {

        //Columns
        public static final String TABLE_NAME = "doodlePad";
        public static final String DOODLETITLE = "title";
        public static final String DOODLEDESCRIPTION = "description";
        public static final String DOODLEFILEPATH = "filepath";

        //Table Statements
        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                DOODLETITLE + TEXT_TYPE + COMMA_SEP +
                DOODLEDESCRIPTION + TEXT_TYPE + COMMA_SEP +
                DOODLEFILEPATH + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}

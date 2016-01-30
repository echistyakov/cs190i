package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import android.provider.BaseColumns;

public final class DbContract {

    // Database version
    public static final int DATABASE_VERSION = 1;
    // Database name
    public static final String DATABASE_NAME = "camera_roll.db";
    //DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static abstract class ImageSchema implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "images";

        // Columns
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        // Default projection and ordering
        public static final String[] PROJECTION = new String[]{COLUMN_URI, COLUMN_TIMESTAMP};
        public static final String ORDER_BY = COLUMN_TIMESTAMP + " DESC";

        // Queries
        public static final String COUNT = "SELECT count(*) FROM " + TABLE_NAME;
        public static final String TABLE_CREATE = String.format("CREATE TABLE %s (%s TEXT NOT NULL, %s INTEGER PRIMARY KEY)",
                                                                 TABLE_NAME,
                                                                 COLUMN_URI,
                                                                 COLUMN_TIMESTAMP);

        public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
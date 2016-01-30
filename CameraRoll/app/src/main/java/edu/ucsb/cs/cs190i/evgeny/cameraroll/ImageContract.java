package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import android.provider.BaseColumns;

public final class ImageContract {

    public ImageContract() {}

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_URI = "uri";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

        public static final String[] PROJECTION = new String[]{COLUMN_NAME_URI, COLUMN_NAME_TIMESTAMP};
        public static final String ORDER_BY = COLUMN_NAME_TIMESTAMP + " DESC";
        public static final String COUNT = "SELECT count(*) FROM " + TABLE_NAME;
    }
}
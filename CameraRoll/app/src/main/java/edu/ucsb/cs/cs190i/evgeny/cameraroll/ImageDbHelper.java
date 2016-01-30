package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageDbHelper extends SQLiteOpenHelper {

    //DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "camera_roll.db";
    private static final String IMAGE_TABLE_CREATE = String.format("CREATE TABLE %s (%s TEXT, %s INTEGER PRIMARY KEY)",
                                                                    ImageContract.ImageEntry.TABLE_NAME,
                                                                    ImageContract.ImageEntry.COLUMN_NAME_URI,
                                                                    ImageContract.ImageEntry.COLUMN_NAME_TIMESTAMP);

    private static final String IMAGE_TABLE_DROP = "DROP TABLE IF EXISTS " + ImageContract.ImageEntry.TABLE_NAME;

    public ImageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(IMAGE_TABLE_DROP);
        onCreate(db);
    }

    public void insertImage(Image image) {
        ContentValues values = new ContentValues();
        values.put(ImageContract.ImageEntry.COLUMN_NAME_URI, image.uri.toString());
        values.put(ImageContract.ImageEntry.COLUMN_NAME_TIMESTAMP, image.timestamp);
        this.getWritableDatabase().insert(ImageContract.ImageEntry.TABLE_NAME, "null", values);
    }

    public void deleteImage(long timestamp) {
        this.getWritableDatabase().delete(ImageContract.ImageEntry.TABLE_NAME,
                                          ImageContract.ImageEntry.COLUMN_NAME_TIMESTAMP + " == ?",
                                          new String[]{Long.toString(timestamp)});
    }

    public void deleteImage(Image i) {
        deleteImage(i.timestamp);
    }

    public List<Image> getAllImages() {
        Cursor c = this.getReadableDatabase().query(ImageContract.ImageEntry.TABLE_NAME,  // Table name
                                                    ImageContract.ImageEntry.PROJECTION,  // Projection
                                                    null,                                 // Selection
                                                    null,                                 // Selection args
                                                    null,                                 // Group by
                                                    null,                                 // Having
                                                    ImageContract.ImageEntry.ORDER_BY,    // Order by
                                                    null);                                // Limit

        List<Image> images = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Image image = new Image();
                image.uri = Uri.parse(c.getString(c.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_URI)));
                image.timestamp = c.getLong(c.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_TIMESTAMP));
                images.add(image);
            } while (c.moveToNext());
        }
        return images;
    }

    public int getImageCount() {
        Cursor c = this.getReadableDatabase().rawQuery(ImageContract.ImageEntry.COUNT, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public boolean isEmpty() {
        return getImageCount() == 0;
    }
}

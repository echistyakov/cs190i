package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ImageDbHelper extends SQLiteOpenHelper {

    //DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ImageDbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.ImageSchema.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.ImageSchema.TABLE_DROP);
        onCreate(db);
    }

    public void insertImage(Image image) {
        ContentValues values = new ContentValues();
        values.put(DbContract.ImageSchema.COLUMN_URI, image.uri.toString());
        values.put(DbContract.ImageSchema.COLUMN_TIMESTAMP, image.timestamp);
        this.getWritableDatabase().insert(DbContract.ImageSchema.TABLE_NAME, "null", values);
    }

    public void deleteImage(long timestamp) {
        this.getWritableDatabase().delete(DbContract.ImageSchema.TABLE_NAME,
                                          DbContract.ImageSchema.COLUMN_TIMESTAMP + " == ?",
                                          new String[]{Long.toString(timestamp)});
    }

    public void deleteImage(Image i) {
        deleteImage(i.timestamp);
    }

    public List<Image> getAllImages() {
        Cursor c = this.getReadableDatabase().query(DbContract.ImageSchema.TABLE_NAME,  // Table name
                                                    DbContract.ImageSchema.PROJECTION,  // Projection
                                                    null,                               // Selection
                                                    null,                               // Selection args
                                                    null,                               // Group by
                                                    null,                               // Having
                                                    DbContract.ImageSchema.ORDER_BY,    // Order by
                                                    null);                              // Limit

        List<Image> images = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Image image = new Image();
                image.uri = Uri.parse(c.getString(c.getColumnIndexOrThrow(DbContract.ImageSchema.COLUMN_URI)));
                image.timestamp = c.getLong(c.getColumnIndexOrThrow(DbContract.ImageSchema.COLUMN_TIMESTAMP));
                images.add(image);
            } while (c.moveToNext());
        }
        c.close();
        return images;
    }

    public int getImageCount() {
        Cursor c = this.getReadableDatabase().rawQuery(DbContract.ImageSchema.COUNT, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public boolean isEmpty() {
        return getImageCount() == 0;
    }
}

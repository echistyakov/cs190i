package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImageIO {

    private final static String APP_FOLDER = "CS190IPics";
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private final static File imageFolder = new File(Environment.getExternalStorageDirectory(), APP_FOLDER);

    public static void deleteImages(List<Image> images) {
        for (Image image : images) {
            deleteImage(image.uri);
        }
    }

    public static void deleteImage(Uri imageUri) {
        File imageFile = new File(imageUri.getPath());
        imageFile.delete();
    }

    public static Uri getOutputImageFileUri() {
        return Uri.fromFile(getOutputImageFile());
    }

    public static File getOutputImageFile() {
        String timeStamp = dateFormat.format(new Date());
        return new File(imageFolder, "IMG_"+ timeStamp + ".jpg");
    }

    public static Bitmap getBitmap(Uri bitmapUri) {
        try {
            return BitmapFactory.decodeFile(bitmapUri.getPath());
        } catch (Exception e) {
            return null;
        }
    }
}

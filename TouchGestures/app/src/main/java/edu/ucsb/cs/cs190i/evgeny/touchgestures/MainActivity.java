package edu.ucsb.cs.cs190i.evgeny.touchgestures;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;

    public static final String ucsbmap = "ucsbmap.png";
    public static final String ucsbmapSmall = "ucsbmap_small.png";
    public static final String ucsbmapAerial = "ucsbmapaerial.png";
    public static final String defaultImage = ucsbmap;
    public static final File downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (hasReadStoragePermission()) {
            setDefaultImage();
        } else {
            requestPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_select_image) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
        } else if (itemId == R.id.action_settings) {
            new AlertDialog.Builder(this)
                    .setTitle("Settings")
                    .setMessage("Hello Settings!")
                    .setIcon(R.drawable.ic_settings)
                    .show();

        } else if (itemId == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle("Help")
                    .setMessage("Hello Help!")
                    .setIcon(R.drawable.ic_help)
                    .show();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImage = getBitmapFromUri(selectedImageUri);
                this.setImage(selectedImage);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setDefaultImage();
            } else {
                Toast.makeText(this, "Read permission denied - can't open default image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setImage(Bitmap image) {
        TouchImageView touchImageVIew = (TouchImageView) findViewById(R.id.touch_image_view);
        touchImageVIew.setImageBitmap(image);
    }

    private void setDefaultImage() {
        File imageFile = new File(downloadsPath, defaultImage);
        if (imageFile.exists()) {
            Uri imageUri = Uri.parse(imageFile.toURI().toString());
            Bitmap bitmap = getBitmapFromUri(imageUri);
            this.setImage(bitmap);
        } else {
            Toast.makeText(this, "Default image could not be found.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasReadStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
    }

    private Bitmap getBitmapFromUri(Uri bitmapUri) {
        InputStream stream = null;
        Bitmap bitmap = null;
        try {
            stream = getContentResolver().openInputStream(bitmapUri);
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException e) {
            // Do nothing
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
        return bitmap;
    }
}

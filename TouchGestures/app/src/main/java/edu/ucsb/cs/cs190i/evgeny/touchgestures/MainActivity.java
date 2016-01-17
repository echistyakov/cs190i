package edu.ucsb.cs.cs190i.evgeny.touchgestures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;

    public static final String ucsbmap = "ucsbmap.png";
    public static final String ucsbmapSmall = "ucsbmap_small.png";
    public static final String ucsbmapAerial = "ucsbmapaerial.png";
    public static final File downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show ucsbmap.png by default
        File imageFile = new File(downloadsPath, ucsbmap);
        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        this.setImage(image);
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
        if (itemId == R.id.select_image) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Could not open image file!",  Toast.LENGTH_SHORT).show();
                }
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                this.setImage(selectedImage);
            }
        }
    }

    private void setImage(Bitmap image) {
        TouchImageView touchImageVIew = (TouchImageView) findViewById(R.id.touch_image_view);
        touchImageVIew.setImageBitmap(image);
    }
}

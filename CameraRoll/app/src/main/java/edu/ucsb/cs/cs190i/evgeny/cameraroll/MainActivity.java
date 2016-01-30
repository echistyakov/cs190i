package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String APP_FOLDER = "CS190IPics";

    private final static int IMAGE_CAPTURE_CODE = 1;
    private final static int PERMISSION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action Button (Camera)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = getOutputImageFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, IMAGE_CAPTURE_CODE);

                // TODO: open camera
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Permissions
        String[] missingPermissions = listMissingPermissions();
        if (missingPermissions.length > 0) {
            requestPermissions(missingPermissions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all) {
            // Show confirmation dialogue
            AlertDialog confirmationDialog = new AlertDialog.Builder(this)
                    .setTitle("Delete all?")
                    .setMessage("Photos will be permanently deleted.")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: delete all
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .create();
            confirmationDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                // TODO: add to list
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" + imageUri, Toast.LENGTH_LONG).show();
            }
        }
    }

    private static Uri getOutputImageFileUri() {
        File imageFile = getOutputImageFile();
        return (imageFile == null) ? null : Uri.fromFile(getOutputImageFile());
    }

    private static File getOutputImageFile() {
        // Image folder
        File imageFolder = new File(Environment.getExternalStorageDirectory(), APP_FOLDER);

        // If storage is inaccessible right now
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        // If folder does not exist and cannot be created
        if (!imageFolder.exists() && !imageFolder.mkdirs()) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        String timeStamp = dateFormat.format(now);

        return new File(imageFolder, "IMG_"+ timeStamp + ".jpg");
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
    }

    private String[] listRequiredPermissions() {
        String[] permissions = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissions = Arrays.copyOf(info.requestedPermissions, info.requestedPermissions.length);
            }
        } catch (Exception e) {
            // Do nothing
        }
        return permissions;
    }

    private String[] listMissingPermissions() {
        List<String> missingPermissions = new ArrayList<>();
        String[] permissions = listRequiredPermissions();
        if (permissions != null) {
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    missingPermissions.add(permission);
                }
            }
        }
        return missingPermissions.toArray(new String[missingPermissions.size()]);
    }
}

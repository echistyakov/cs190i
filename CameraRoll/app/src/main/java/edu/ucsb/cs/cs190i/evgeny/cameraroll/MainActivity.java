package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static int IMAGE_CAPTURE_CODE = 1;
    private final static int PERMISSION_REQUEST = 2;

    private PermissionManager pm = null;
    private ImageDb db = null;
    private Picasso picasso = null;
    private ImageAdapter imageAdapter = null;
    private Image image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialization
        SugarContext.init(this);
        this.pm = new PermissionManager();
        this.db = new ImageDb();
        this.picasso = Picasso.with(this);
        this.imageAdapter = new ImageAdapter(this.db, this.picasso);

        // Floating Action Button (Camera)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If we have the necessary permissions
                if (pm.hasPermission(Manifest.permission.CAMERA) && pm.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Launch camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    image = new Image(ImageIO.getOutputImageFileUri(), new Date().getTime());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, image.uri());
                    startActivityForResult(intent, IMAGE_CAPTURE_CODE);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.missing_permissions, Toast.LENGTH_SHORT);
                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });

        // Set up Recycle View
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.imageAdapter);
        this.displayCorrectView();

        // Permissions
        this.pm.requestPermissionsIfMissing();
    }

    @Override
    protected void onDestroy() {
        SugarContext.terminate();
        super.onDestroy();
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
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Image i : db.getAllImages()) {
                                db.deleteImage(i);
                                ImageIO.deleteImage(i.uri());
                                imageAdapter.notifyItemRemoved(0);
                                picasso.invalidate(i.uri());
                            }
                            hideRecyclerView();
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
                // Insert image into the DB
                this.db.insertImage(image);
                // Add image to the adapter
                this.imageAdapter.notifyItemInserted(0);
                // Show RecyclerView if it's not already shown
                this.showRecyclerView();
            }
        }
    }

    private void showRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView noPhotosText = (TextView) findViewById(R.id.no_photos_text);

        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
            noPhotosText.setVisibility(View.GONE);
        }

        // Scroll RecyclerView to the top
        recyclerView.scrollToPosition(0);
    }

    private void hideRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView noPhotosText = (TextView) findViewById(R.id.no_photos_text);

        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            noPhotosText.setVisibility(View.VISIBLE);
        }

        // Remove view cache from RecyclerView
        recyclerView.removeAllViews();
    }

    private void displayCorrectView() {
        if(this.db.isEmpty()) {
            this.hideRecyclerView();
        } else {
            this.showRecyclerView();
        }
    }

    private class PermissionManager {

        private boolean hasPermission(String permission) {
            return ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
        }

        private void requestPermissions(String[] permissions) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
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

        private void requestPermissionsIfMissing() {
            String[] missingPermissions = this.listMissingPermissions();
            if (missingPermissions.length > 0) {
                requestPermissions(missingPermissions);
            }
        }
    }
}

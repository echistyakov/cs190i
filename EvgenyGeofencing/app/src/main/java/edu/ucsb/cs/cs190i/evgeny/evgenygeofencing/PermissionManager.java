package edu.ucsb.cs.cs190i.evgeny.evgenygeofencing;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionManager {

    public static final int PERMISSION_REQUEST = 1;

    private Activity context;

    public PermissionManager(Activity activity) {
        this.context = activity;
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.context.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this.context, permissions, PERMISSION_REQUEST);
    }

    public String[] listRequiredPermissions() {
        String[] permissions = null;
        try {
            PackageInfo info = this.context.getPackageManager().getPackageInfo(this.context.getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissions = Arrays.copyOf(info.requestedPermissions, info.requestedPermissions.length);
            }
        } catch (Exception e) {
            // Do nothing
        }
        return permissions;
    }

    public String[] listMissingPermissions() {
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

    public void requestPermissionsIfMissing() {
        String[] missingPermissions = this.listMissingPermissions();
        if (missingPermissions.length > 0) {
            requestPermissions(missingPermissions);
        }
    }
}

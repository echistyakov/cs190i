package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.net.Uri;

import com.orm.SugarRecord;

public class Image extends SugarRecord {

    public String uriString;
    public long timestamp;

    public Image() {}

    public Image(Uri uri, long timestamp) {
        this.uriString = uri.toString();
        this.timestamp = timestamp;
    }

    public Uri uri() {
        return Uri.parse(this.uriString);
    }
}

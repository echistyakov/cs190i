package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.net.Uri;

public class Image {
    public Uri uri;
    public long timestamp;

    public Image() {}

    public Image(Uri uri, long timestamp) {
        this.uri = uri;
        this.timestamp = timestamp;
    }
}

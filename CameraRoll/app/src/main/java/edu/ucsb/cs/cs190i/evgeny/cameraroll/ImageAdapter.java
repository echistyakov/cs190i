package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ImageDbHelper db;

    public ImageAdapter(ImageDbHelper db) {
        this.db = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.image_view);
        Image image = db.getNthImage(position);
        Bitmap bitmap = ImageIO.getBitmap(image.uri);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return db.getImageCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

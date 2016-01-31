package edu.ucsb.cs.cs190i.evgeny.cameraroll;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ImageDb db;
    private Picasso picasso;

    public ImageAdapter(ImageDb db, Picasso picasso) {
        this.db = db;
        this.picasso = picasso;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.image_view);
        CardView cardView = (CardView) holder.itemView.findViewById(R.id.card_view);
        if (position != this.getItemCount() - 1) {
            LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) cardView.getLayoutParams();
            int dp = 6;
            float density = imageView.getContext().getResources().getDisplayMetrics().density;
            int margin = (int) (dp * density);
            params.setMargins(0, 0, 0, margin);
            cardView.setLayoutParams(params);
        }
        Image image = this.db.getNthImage(position);
        this.picasso.load(image.uri()).fit().centerCrop().into(imageView);
    }

    @Override
    public int getItemCount() {
        return (int) this.db.getImageCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

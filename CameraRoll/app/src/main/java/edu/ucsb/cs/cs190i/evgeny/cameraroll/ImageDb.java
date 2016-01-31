package edu.ucsb.cs.cs190i.evgeny.cameraroll;


import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class ImageDb {

    public void insertImage(Image image) {
        image.save();
    }

    public void deleteImage(long timestamp) {
        List<Image> images = Select.from(Image.class).where(Condition.prop("timestamp").eq(timestamp)).list();
        if (images.size() == 1) {
            images.get(0).delete();
        }
    }

    public void deleteImage(Image i) {
        this.deleteImage(i.timestamp);
    }

    public Image getNthImage(int n) {
        Image image = null;
        List<Image> images = Select.from(Image.class).orderBy("timestamp DESC").limit(n + ", " + 1).list();
        if (images.size() == 1) {
            image = images.get(0);
        }

        return image;
    }

    public List<Image> getAllImages() {
        return Select.from(Image.class).orderBy("timestamp DESC").list();
    }

    public long getImageCount() {
        return Image.count(Image.class);
    }

    public boolean isEmpty() {
        return this.getImageCount() == 0;
    }
}

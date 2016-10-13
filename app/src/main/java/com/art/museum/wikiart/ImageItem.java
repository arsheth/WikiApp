package com.art.museum.wikiart;

import android.graphics.Bitmap;
/**
 * Created by arpita on 20/09/16.
 */
public class ImageItem {
    private String name;
    private String link;
    private Bitmap image;

    public ImageItem(String name, String link) {
        super();
        this.name = name;
        this.link = link;
    }

    public ImageItem(String name, String link, Bitmap image) {
        super();
        this.name = name;
        this.link = link;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

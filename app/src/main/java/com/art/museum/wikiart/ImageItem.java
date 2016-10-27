package com.art.museum.wikiart;

import android.graphics.Bitmap;
/**
 * Created by arpita on 20/09/16.
 */
public class ImageItem {
    private String name;
    private String link;
    private int idx;

    public ImageItem(String name, String link, int idx) {
        super();
        this.name = name;
        this.link = link;
        this.idx = idx;
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

    public int getIndex() {
        return idx;
    }

    public void setImage(int idx) {
        this.idx = idx;
    }
}

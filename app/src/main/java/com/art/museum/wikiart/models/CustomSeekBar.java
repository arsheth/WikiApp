package com.art.museum.wikiart.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by arpita on 09/11/16.
 */
public class CustomSeekBar extends SeekBar {

    private int mThumbSize;
    private TextPaint mTextPaint;

    public CustomSeekBar(Context context) {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    Drawable mThumb;

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }
}
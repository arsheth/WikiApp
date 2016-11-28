package com.art.museum.wikiart.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.art.museum.wikiart.models.ImageItem;

import java.util.ArrayList;

/**
 * Created by arpita on 16/11/16.
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;
    private int offset;
    private ArrayList<ImageItem> data;
    private boolean isChecked;

    public CustomItemDecoration(ArrayList<ImageItem> data, boolean isChecked){
        offset = 5;
        this.data = data;
        this.isChecked = isChecked;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(offset, offset, offset, offset);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);


        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            for (int i = 0; i < parent.getChildCount(); i++) {
                final View child = parent.getChildAt(i);
                int tag = (Integer) child.getTag();
                ImageItem item = data.get(tag);

                if(isChecked) paint.setColor(Color.parseColor(item.getColor()));


                else paint.setColor(Color.WHITE);
                c.drawRect(
                        layoutManager.getDecoratedLeft(child) + offset,
                        layoutManager.getDecoratedTop(child) + offset,
                        layoutManager.getDecoratedRight(child) - offset,
                        layoutManager.getDecoratedBottom(child) - offset, paint);

            }
    }
}

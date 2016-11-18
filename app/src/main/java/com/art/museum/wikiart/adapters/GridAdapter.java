package com.art.museum.wikiart.adapters;

/**
 * Created by arpita on 12/10/16.
 */

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.art.museum.data.DataStore;
import com.art.museum.wikiart.GridRecyclerView;
import com.art.museum.wikiart.ImageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    public interface OnLongClickListener {
        void onLongClick(final String url);
    }

    public interface OnClickListener {
        void onClick(final ImageItem item);
    }

    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private Context context;
    private OnLongClickListener longClickListener;
    private OnClickListener listener;
    public static final int totalSpan = 100;
    private int currentSpan;
    private int spanConstant = 25;
    private GridRecyclerView grv;
    DataStore dataStore;
    private int focusedItem = 0;

    public GridAdapter(Context context,ArrayList<ImageItem> data,GridRecyclerView gv, DataStore dataStore, OnLongClickListener longClickListener, OnClickListener listener) {
        this.data = data;
        this.context = context;
        this.longClickListener = longClickListener;
        this.listener = listener;
        this.grv = gv;
        currentSpan = spanConstant;
        this.dataStore = dataStore;
    }

    public void addData(ArrayList<ImageItem> dataToAdd) {
        data.clear();
        data.addAll(dataToAdd);
        notifyDataSetChanged();
    }

    public GridLayoutManager.SpanSizeLookup getScalableSpanSizeLookUp() {
        return scalableSpanSizeLookUp;
    }

  /*  public int calculateRange() {
        int start = ((GridLayoutManager) grv.getLayoutManager()).findFirstVisibleItemPosition();
        int end = ((GridLayoutManager) grv.getLayoutManager()).findLastVisibleItemPosition();
        if (start < 0)
            start = 0;
        if (end < 0)
            end = 0;
        return end - start;
    } */


    private GridLayoutManager.SpanSizeLookup scalableSpanSizeLookUp = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getCurrentSpan();
        }
    };


    public int getCurrentSpan() {
        return currentSpan;
    }

  /*  public void setCurrentSpan(int span) {
        this.currentSpan = span;

    }

    public void delayedNotify(final int pos, final int range) {
        grv.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(pos - range > 0 ? pos - range : 0, range * 2 < getItemCount() ? range * 2 : range);
            }
        }, 100);
    }*/

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder viewHolder, int i) {
        //String url = data.get(i).getLink().toString();
        //int index = data.get(i).getIndex();
        String rid = "image_" + data.get(i).getIndex();
        viewHolder.bind(context,data.get(i),rid);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }

        public void bind(final Context context, final ImageItem item, final String rid) {
            int resource_id = context.getResources().getIdentifier(rid,"drawable", context.getPackageName());
            Picasso.with(context).load(resource_id).resize(100,100).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusedItem = item.getPosition();
                    listener.onClick(item);
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    longClickListener.onLongClick(item.getLink());
                    return true;
                }
            });

        }

    }


}

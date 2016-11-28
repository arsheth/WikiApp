package com.art.museum.wikiart.adapters;

/**
 * Created by arpita on 12/10/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.art.museum.data.DataStore;
import com.art.museum.wikiart.R;
import com.art.museum.wikiart.models.ImageItem;
import com.art.museum.wikiart.views.GridRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    public interface OnLongClickListener {
        void onLongClick(final ImageItem item);
    }

    public interface OnClickListener {
        void onClick(final ImageItem item);
    }

    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private Context context;
    private OnLongClickListener longClickListener;
    private OnClickListener listener;
    public static final int totalSpan = 10;
    private int currentSpan;
    private int spanConstant = 1;
    private GridRecyclerView grv;
    DataStore dataStore;
    private int focusedItem = 0;
    private boolean artist_mode = false;
    private boolean date_mode = false;
    private boolean style_mode = false;
    private boolean genre_mode = false;

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

    public void setFilter(int mode) {
        clear_modes();
        switch(mode){
            case 1:
                artist_mode = true;
                break;
            case 2:
                date_mode = true;
                break;
            case 3:
                style_mode = true;
                break;
            case 4:
                genre_mode = true;
                break;
            default:
                clear_modes();

        }
        notifyDataSetChanged();
    }

    public void clear_modes(){
        artist_mode = false;
        date_mode = false;
        style_mode = false;
        genre_mode = false;
    }

    public GridLayoutManager.SpanSizeLookup getScalableSpanSizeLookUp() {
        return scalableSpanSizeLookUp;
    }

    private GridLayoutManager.SpanSizeLookup scalableSpanSizeLookUp = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return getCurrentSpan();
        }
    };


    public int getCurrentSpan() {
        return currentSpan;
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
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
                    longClickListener.onLongClick(item);
                    return true;
                }
            });

            if(artist_mode) image.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.MULTIPLY);
            else if(date_mode) image.setColorFilter(Color.parseColor(item.getdColor()), PorterDuff.Mode.MULTIPLY);
            else if(style_mode) image.setColorFilter(Color.parseColor(item.getsColor()), PorterDuff.Mode.MULTIPLY);
            else if(genre_mode) image.setColorFilter(Color.parseColor(item.getgColor()), PorterDuff.Mode.MULTIPLY);
            else image.clearColorFilter();
           /* GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.grid_items_border);
            drawable.setColor(Color.parseColor(item.getColor()));
            drawable.setAlpha(100);
            image.setForeground(drawable); */

            //image.setBackgroundResource(R.drawable.grid_items_border);
            //GradientDrawable drawable = (GradientDrawable) image.getBackground();
            //drawable.setColor(Color.parseColor(item.getColor()));
        }

    }


}

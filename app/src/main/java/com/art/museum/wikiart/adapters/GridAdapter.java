package com.art.museum.wikiart.adapters;

/**
 * Created by arpita on 12/10/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.museum.data.DataStore;
import com.art.museum.wikiart.R;
import com.art.museum.wikiart.models.ImageItem;
import com.art.museum.wikiart.views.GridRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;


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

    public void setFilter(HashSet<Integer> filters) {
        clear_modes();
        for(int mode : filters){
            if(mode == 1) artist_mode = true;
            if(mode == 2) date_mode = true;
            if(mode == 3) style_mode = true;
            if(mode == 4) genre_mode = true;
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
        private TextView text_artist;
        private TextView text_date;
        private TextView text_style;
        private TextView text_genre;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            text_artist = (TextView) view.findViewById(R.id.text_artist);
            text_date = (TextView) view.findViewById(R.id.text_date);
            text_style = (TextView) view.findViewById(R.id.text_style);
            text_genre = (TextView) view.findViewById(R.id.text_genre);

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
            text_artist.setVisibility(View.GONE);
            text_date.setVisibility(View.GONE);
            text_style.setVisibility(View.GONE);
            text_genre.setVisibility(View.GONE);
            if(artist_mode){
                //image.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.MULTIPLY);
                int bg = Color.parseColor(item.getColor());
                double darkness = 1-(0.299*Color.red(bg) + 0.587*Color.green(bg) + 0.114*Color.blue(bg))/255;
                if(darkness<0.5){
                    text_artist.setTextColor(Color.BLACK);
                }else{
                    text_artist.setTextColor(Color.WHITE);
                }

                text_artist.setBackgroundColor(bg);
                String text = item.getArtist();
                if(text.contains(" ")) text = text.substring(text.indexOf(" "),text.length());
                if(text.length() >= 11) text = text.substring(0,11);
                text_artist.setText(text);
                text_artist.setTextSize(10);
                text_artist.setVisibility(View.VISIBLE);
            }
            if(date_mode){
                //image.setColorFilter(Color.parseColor(item.getdColor()), PorterDuff.Mode.MULTIPLY);
                text_date.setBackgroundColor(Color.parseColor(item.getdColor()));
                int bg = Color.parseColor(item.getdColor());
                double darkness = 1-(0.299*Color.red(bg) + 0.587*Color.green(bg) + 0.114*Color.blue(bg))/255;
                if(darkness<0.5){
                    text_date.setTextColor(Color.BLACK);
                }else{
                    text_date.setTextColor(Color.WHITE);
                }
                text_date.setText(item.getDate());
                text_date.setTextSize(10);
                text_date.setVisibility(View.VISIBLE);
            }
            if(style_mode){

                //image.setColorFilter(Color.parseColor(item.getsColor()), PorterDuff.Mode.MULTIPLY);
                text_style.setBackgroundColor(Color.parseColor(item.getsColor()));
                int bg = Color.parseColor(item.getsColor());
                double darkness = 1-(0.299*Color.red(bg) + 0.587*Color.green(bg) + 0.114*Color.blue(bg))/255;
                if(darkness<0.5){
                    text_style.setTextColor(Color.BLACK);
                }else{
                    text_style.setTextColor(Color.WHITE);
                }
                String style = item.getStyle();
                if(style.length() > 12) style = style.substring(0,12);
                text_style.setText(style);
                text_style.setTextSize(10);
                text_style.setVisibility(View.VISIBLE);
            }
            if(genre_mode){
                //image.setColorFilter(Color.parseColor(item.getgColor()), PorterDuff.Mode.MULTIPLY);
                text_genre.setBackgroundColor(Color.parseColor(item.getgColor()));
                int bg = Color.parseColor(item.getgColor());
                double darkness = 1-(0.299*Color.red(bg) + 0.587*Color.green(bg) + 0.114*Color.blue(bg))/255;
                if(darkness<0.5){
                    text_genre.setTextColor(Color.BLACK);
                }else{
                    text_genre.setTextColor(Color.WHITE);
                }
                String genre = item.getGenre();
                if(genre.length() >= 11) genre = genre.substring(0,11);
                text_style.setText(genre);
                text_genre.setTextSize(10);
                text_genre.setVisibility(View.VISIBLE);
            }
        }

    }


}

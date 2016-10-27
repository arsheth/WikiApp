package com.art.museum.wikiart;

/**
 * Created by arpita on 12/10/16.
 */

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Context;
import com.squareup.picasso.Picasso;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(final String url);
    }

    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private Context context;
    private OnItemClickListener listener;

    public GridAdapter(Context context,ArrayList<ImageItem> data, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridAdapter.ViewHolder viewHolder, int i) {
        String url = data.get(i).getLink().toString();
        //url = url.substring(0,14) + url.substring(15);
        String rid = "image_" + data.get(i).getIndex();
        viewHolder.bind(context,url,rid,listener);

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

        public void bind(Context context, final String url, final String rid, final OnItemClickListener listener) {
            //Picasso.with(context).load(url).resize(100, 100).into(image);
            //Log.d("rid:",rid);
            //Log.d("url:",url);
            int resource_id = context.getResources().getIdentifier(rid,"drawable", context.getPackageName());
            Picasso.with(context).load(resource_id).resize(100,100).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(url);
                }
            });
        }

    }

}

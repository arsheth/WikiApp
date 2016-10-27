package com.art.museum.wikiart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by arpita on 20/09/16.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        String url = getIntent().getStringExtra("link");

        //Log.d("URL in intent:",""+url);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(getApplicationContext()).load(url).into(imageView);

    }
}

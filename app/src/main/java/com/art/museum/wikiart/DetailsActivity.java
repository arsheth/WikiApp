package com.art.museum.wikiart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
        String name = getIntent().getStringExtra("name");
        String artist = getIntent().getStringExtra("artist");
        int rank = getIntent().getIntExtra("rank",0);
        String date = getIntent().getStringExtra("year");
        String style = getIntent().getStringExtra("style");
        String genre = getIntent().getStringExtra("genre");

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(getApplicationContext()).load(url).into(imageView);

        TextView imagename = (TextView) findViewById(R.id.title);
        imagename.setText(" Title:  " + name
                + "\n Artist: " + artist
                + "\n Year: " + date
                + "\n Style: " + style
                + "\n Genre: " + genre
                + "\n Rank: " + rank
        );

    }
}

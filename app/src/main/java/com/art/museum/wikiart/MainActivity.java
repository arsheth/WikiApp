package com.art.museum.wikiart;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;

import java.util.ArrayList;
import android.content.Intent;
import com.art.museum.data.DataStore;
import android.util.Log;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;

public class MainActivity extends AppCompatActivity {

    private DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataStore = new DataStore(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(Constants.FIRST_TIME, true);

       if (isFirstTime) {
            try {
                dataStore.createDataBase();
                //Log.d("first time database","created");
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.FIRST_TIME, false);
            editor.commit();
        }
        try {
            dataStore.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ImageItem> data = getData();
        GridAdapter adapter = new GridAdapter(getApplicationContext(), data, new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String url) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("link",url);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        imageItems = dataStore.getImages();
        return imageItems;
    }
}

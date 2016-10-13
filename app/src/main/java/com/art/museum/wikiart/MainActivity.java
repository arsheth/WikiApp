package com.art.museum.wikiart;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.art.museum.data.DataStore;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataStore = new DataStore(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(Constants.FIRST_TIME, true);

       //if (isFirstTime) {
            try {
                dataStore.createDataBase();
                Log.d("first time database","created");
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.FIRST_TIME, false);
            editor.commit();
       // }
        try {
            dataStore.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                try {
                    String url = item.getLink().toString();
                    url = url.substring(0,14) + url.substring(15);
                    new DownloadImage(MainActivity.this).execute(url);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.e("Image Fetch Exception:",""+item.getLink());
                }

            }
        });
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        imageItems = dataStore.getImages();
        return imageItems;
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        AppCompatActivity activity;

        public DownloadImage(AppCompatActivity activity){
            this.activity = activity;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Intent intent = new Intent(activity, DetailsActivity.class);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();

            intent.putExtra("title", "");
            intent.putExtra("image", bytes);

            //Start details activity
            activity.startActivity(intent);
        }

        private Bitmap downloadUrl(String myurl) throws IOException {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                Bitmap image = BitmapFactory.decodeStream(is);
                return image;

            }
            catch(Exception e){
                e.printStackTrace();
                Log.e("Image Fetch Exception: ",""+myurl);
                return null;
            }
            finally {
                if(conn != null){
                    conn.disconnect();
                }
            }
        }
    }
}

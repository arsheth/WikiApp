package com.art.museum.wikiart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.art.museum.data.DataStore;
import com.art.museum.wikiart.adapters.GridAdapter;
import com.art.museum.wikiart.models.Constants;
import com.art.museum.wikiart.models.CustomDialog;
import com.art.museum.wikiart.models.CustomSeekBar;
import com.art.museum.wikiart.models.ImageItem;
import com.art.museum.wikiart.views.GridRecyclerView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements OnSeekBarChangeListener,
        OnShowcaseEventListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private DataStore dataStore;
    private CustomSeekBar seekBar;
    private static ImageItem item;
    private TextView position,progressText;
    private static GridAdapter adapter;
    private static GridLayoutManager layoutManager;
    private int helperCount;
    private ShowcaseView firstSV;
    private ShowcaseView secondSV, thirdSV, fourthSV;
    private int currEnd, rankCount;
    private GridRecyclerView recyclerView;
    private SearchView searchView;
    private NumberPicker numberPicker;
    private ArrayList<ImageItem> data;
    private int gridSize = 4;
    boolean tsp = true;
    boolean prime = false;
    boolean pop = false;
    private HashSet<Integer> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataStore = new DataStore(this);
        filters = new HashSet<Integer>();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(Constants.FIRST_TIME, true);
        seekBar = (CustomSeekBar) findViewById(R.id.seek_bar);
        progressText = (TextView) findViewById(R.id.progress);
        position = (TextView) findViewById(R.id.position);
        this.helperCount = 1;
        this.rankCount = 100;

        seekBar.setOnSeekBarChangeListener(this);

        if (isFirstTime) {
            try {
                dataStore.createDataBase();
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

        recyclerView = (GridRecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),gridSize);
        data = dataStore.getImages(rankCount,0);
        position.setText("Position: 0");

        adapter = new GridAdapter(getApplicationContext(), data,recyclerView,dataStore,new GridAdapter.OnLongClickListener(){
            @Override
            public void onLongClick(ImageItem item) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("link", item.getLink());
                intent.putExtra("name",item.getName());
                intent.putExtra("artist",item.getArtist());
                intent.putExtra("rank",item.getRank());
                intent.putExtra("year",item.getDate());
                intent.putExtra("style",item.getStyle());
                intent.putExtra("genre",item.getGenre());
                startActivity(intent);
            }
        },new GridAdapter.OnClickListener(){
            @Override
            public void onClick(ImageItem it){
                item = it;
                position.setText("Position: " + it.getPosition());
                final CustomDialog dialog = new CustomDialog(MainActivity.this, R.style.myCoolDialog);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Image Information");
                TextView name = (TextView) dialog.findViewById(R.id.name);
                name.setText(" Title: " + item.getName());
                TextView artist = (TextView) dialog.findViewById(R.id.artist);
                artist.setText(" Artist: " + item.getArtist());
                TextView style = (TextView) dialog.findViewById(R.id.style);
                style.setText(" Style: " + item.getStyle());
                TextView genre = (TextView) dialog.findViewById(R.id.genre);
                genre.setText(" Genre: " + item.getGenre());
                TextView year = (TextView) dialog.findViewById(R.id.year);
                year.setText(" Year: " + item.getDate());
                TextView rank = (TextView) dialog.findViewById(R.id.rank);
                rank.setText(" Rank: " + item.getRank());

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        layoutManager.setSpanSizeLookup(adapter.getScalableSpanSizeLookUp());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress == 0) progress = 1;
        String dynamicText = String.valueOf(progress*100);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ABOVE, seekBar.getId());
        Rect thumbRect = ((CustomSeekBar) seekBar).getSeekBarThumb().getBounds();
        p.setMargins(
                thumbRect.centerX(),0, 0, 0);
        progressText.setLayoutParams(p);
        progressText.setText(String.valueOf(dynamicText) + " ");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        changeData(progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        filters.add(1);
        adapter.setFilter(filters);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about_app) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.help) {
            showHelpScreens();
        } else if (id == R.id.artist_color) {
            if(!item.isChecked()) filters.add(1);
            else{
                if(filters.contains(1)) filters.remove(1);
            }
            adapter.setFilter(filters);
            item.setChecked(!item.isChecked());
            return true;
        }else if (id == R.id.date_color) {
            if(!item.isChecked()) filters.add(2);
            else{
                if(filters.contains(2)) filters.remove(2);
            }
            item.setChecked(!item.isChecked());
            adapter.setFilter(filters);
            return true;
        }else if (id == R.id.style_color) {
            if(!item.isChecked()) filters.add(3);
            else{
                if(filters.contains(3)) filters.remove(3);
            }
            item.setChecked(!item.isChecked());
            adapter.setFilter(filters);
            return true;
        }else if (id == R.id.genre_color) {
            if(!item.isChecked()) filters.add(4);
            else{
                if(filters.contains(4)) filters.remove(4);
            }
            item.setChecked(!item.isChecked());
            adapter.setFilter(filters);
            return true;
        } else if(id == R.id.tsp){
            tsp = true;
            prime = false;
            pop = false;
            data = dataStore.getImages(rankCount,0);
            adapter.addData(data);
        }else if(id == R.id.prime) {
            tsp = false;
            prime = true;
            pop = false;
            data = dataStore.getImagesPrime(rankCount, 0, 2);
            adapter.addData(data);
        }else if(id == R.id.popular){
            tsp = false;
            prime = false;
            pop = true;
            data = dataStore.getPopularImages(rankCount,0);
            adapter.addData(data);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpScreens() {
        if (firstSV == null) {
            TextPaint title = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            title.setColor(getResources().getColor(R.color.help));
            ViewTarget target = new ViewTarget(findViewById(R.id.image));
            firstSV = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle("Long Click on the thumbnail to open the image.\n" +
                            " Short click displays information about the image.")
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitlePaint(title)
                    .hideOnTouchOutside()
                    .build();
            firstSV.setOnShowcaseEventListener(this);
            secondSV = new ShowcaseView.Builder(MainActivity.this)
                    .setTarget(new ViewTarget(findViewById(R.id.sliderStart)))
                    .setContentTitle("The lower the rank number the greater the significance of the paintings.")
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitlePaint(title)
                    .hideOnTouchOutside().build();
            secondSV.hide();
            secondSV.setOnShowcaseEventListener(this);
             thirdSV = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(R.id.seek_bar,this))
                    .setContentTitle("Slider sets the significance of the items displayed.\n Your universe enlarges as you move to the right.")
                    .setContentTitlePaint(title)
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .hideOnTouchOutside()
                    .build();
            thirdSV.hide();
            thirdSV.setOnShowcaseEventListener(this);
            fourthSV = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(findViewById(R.id.image)))
                    .setContentTitle("Short click on an image to center the expansion around it before using the slider.")
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitlePaint(title)
                    .hideOnTouchOutside()
                    .build();
            fourthSV.hide();
            fourthSV.setOnShowcaseEventListener(this);
        } else {
            firstSV.show();
        }
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        switch (helperCount) {
            case 1:
                secondSV.show();
                helperCount++;
                break;
            case 2:
                thirdSV.show();
                helperCount++;
                break;
            case 3:
                fourthSV.show();
                helperCount++;
                break;
            case 4:
                helperCount = 0;
                break;
        }
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query = query.toLowerCase();
        ArrayList<ImageItem> filteredList = dataStore.search(query);
        adapter.addData(filteredList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    private void changeData(int progress) {
        if(progress == 0) progress = 1;
        progress = progress * 100;
        int pos = item == null ? 0 : item.getPosition();
        ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        if(tsp){
            imageItems = dataStore.getImages(progress,pos);
            int first = imageItems.get(0).getPosition();
            int last = imageItems.get(imageItems.size()-1).getPosition();
            int newf = imageItems.get(0).getPosition();
            int newl = imageItems.get(imageItems.size()-1).getPosition();
            int count = 0;
            while(imageItems.size() < progress){
                if(imageItems.size() >= progress) break;
                if(count == 0 || newf != first ) imageItems.addAll(0,dataStore.getImagesAbove(progress,newf));
                if(count == 0 || newl != last ) imageItems.addAll(dataStore.getImagesBelow(progress,newl));
                if(first == newf && last == newl && count > 0) break;
                first = newf;
                last = newl;
                newf = imageItems.get(0).getPosition();
                newl = imageItems.get(imageItems.size()-1).getPosition();
                count++;
            }
        }

        else if(prime) imageItems = dataStore.getImagesPrime(progress,pos,progress/100);
        else if(pop) imageItems = dataStore.getPopularImages(progress,pos);
        int current = imageItems.indexOf(item);
        adapter.addData(imageItems);
        layoutManager.scrollToPosition(current);

    }


}

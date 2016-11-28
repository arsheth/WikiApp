package com.art.museum.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.art.museum.wikiart.R;
import com.art.museum.wikiart.models.Constants;
import com.art.museum.wikiart.models.ImageItem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DataStore extends SQLiteOpenHelper {

    private Context context;
    private static String DB_PATH;
    private static String DATABASE_NAME;
    private static String TABLE_NAME;
    private SQLiteDatabase myDataBase;

    public DataStore(Context context) {
        super(context,
                context.getString(R.string.db_name),
                null,
                Integer.parseInt(context.getString(R.string.db_version)));
        this.context = context;
        this.DB_PATH = context.getString(R.string.db_path);
        this.DATABASE_NAME = context.getString(R.string.db_name);
        this.TABLE_NAME = context.getString(R.string.table_name);
    }


    public void createDataBase() throws IOException {
        this.getReadableDatabase();
        try {
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);

            String outFileName = context.getDatabasePath(DATABASE_NAME) + DATABASE_NAME;

            OutputStream myOutput = new FileOutputStream(outFileName);
            Log.d("output:",""+myOutput);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Error copying database");
        }

    }

    public void openDataBase() throws SQLException {
        String myPath = context.getDatabasePath(DATABASE_NAME) + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<ImageItem> search(String searchTerm) {
        Log.d("SEARCH ", "search");
        ArrayList<ImageItem> itemList = new ArrayList<ImageItem>();
        String selectQuery = "SELECT position, name, rank, link, image_id, artist_name, color, date, date_color, style, style_color, genre, genre_color from "+TABLE_NAME+" WHERE artist_name like '%" +
                searchTerm.toLowerCase() + "%' COLLATE NOCASE ORDER BY position ASC LIMIT 100";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ImageItem image = new ImageItem();
                image.setIndex(Integer.parseInt(cursor.getString(4)));
                image.setName(cursor.getString(1));
                image.setRank(Integer.parseInt(cursor.getString(2)));
                image.setLink(cursor.getString(3));
                image.setPosition(cursor.getInt(0));
                image.setArtist(cursor.getString(5));
                image.setColor(cursor.getString(6));
                image.setDate(cursor.getString(7));
                image.setdColor(cursor.getString(8));
                image.setStyle(cursor.getString(9));
                image.setsColor(cursor.getString(10));
                image.setGente(cursor.getString(11));
                image.setgColor(cursor.getString(12));
                itemList.add(image);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return itemList;
    }

    public ArrayList<ImageItem> getImages(int rank, int position) {
        ArrayList<ImageItem> itemList = new ArrayList<ImageItem>();
        int count;
        if (rank < 1000) {
            count = 1000;
        } else {
            count = 50;
        }
        int result = 0, i=0;
        while (true) {
            if (i == Constants.MAX_ITER * 5) {
                break;
            }
            String selectQuery = "SELECT position, image_name, rank, link, image_id, artist_name, color, date, date_color, style, style_color, genre, genre_color FROM " + TABLE_NAME + " WHERE rank <= " + rank
                    + " AND position >=  " + (position-count/(i+1)) + " AND position <" + (position + count) + " ORDER BY position ASC LIMIT 100";
            Cursor cursor = myDataBase.rawQuery(selectQuery, null);
            result = cursor.getCount();
            if ((result < 100)) {
                count += count;
                i++;
                cursor.close();
                continue;
            }
            if (cursor.moveToFirst()) {
                do {
                    ImageItem image = new ImageItem();
                    image.setIndex(Integer.parseInt(cursor.getString(4)));
                    image.setName(cursor.getString(1));
                    image.setRank(Integer.parseInt(cursor.getString(2)));
                    image.setLink(cursor.getString(3));
                    image.setPosition(cursor.getInt(0));
                    image.setArtist(cursor.getString(5));
                    image.setColor(cursor.getString(6));
                    image.setDate(cursor.getString(7));
                    image.setdColor(cursor.getString(8));
                    image.setStyle(cursor.getString(9));
                    image.setsColor(cursor.getString(10));
                    image.setGente(cursor.getString(11));
                    image.setgColor(cursor.getString(12));
                    itemList.add(image);
                } while (cursor.moveToNext());
                cursor.close();
                break;
            }
        }
        Log.d("RESULT SIZE",""+ itemList.size());
        return itemList;
    }

}

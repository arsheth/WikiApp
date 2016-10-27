package com.art.museum.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.res.Resources;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.art.museum.wikiart.R;
import com.art.museum.wikiart.ImageItem;
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


    public ArrayList<ImageItem> getImages() {
        ArrayList<ImageItem> itemList = new ArrayList<ImageItem>();
        int result = 0;
        int i=0;
        while (i < 10000) {
            String selectQuery = "SELECT idx, name, link, rank  FROM "+TABLE_NAME+" WHERE rank >=  " + i + " ORDER BY rank ASC LIMIT 100";
            Cursor cursor = myDataBase.rawQuery(selectQuery, null);
            result = cursor.getCount();
            i += result;
            if (cursor.moveToFirst()) {
                do {
                    ImageItem item = new ImageItem(cursor.getString(1),cursor.getString(2),cursor.getInt(0));
                    itemList.add(item);
                } while (cursor.moveToNext());
                cursor.close();
            }
            if(result < 100) break;
        }
        Log.d("RESULT SIZE",""+ itemList.size());
        return itemList;
    }
}

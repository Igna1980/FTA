package com.bamobile.fdtks.databases;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import com.bamobile.fdtks.util.Tools;


public class ImageDatabase extends SQLiteOpenHelper implements BaseColumns {
    private static final String DATABASE_NAME = "BAMImageDB_";
    private static final String DIR_NAME = "BAMCONTENT_";

    private static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "contenidos_imagenes";
    static final String COLUMN_IMAGE_ID = "_id";
    static final String COLUMN_IMAGE_NAME = "name";
    static final String COLUMN_LAST_UPDATE = "last_update";

    private Context mContext;
    private static Context mCteStatic;
    
    public ImageDatabase(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context.getApplicationContext();
        ImageDatabase.mCteStatic = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_IMAGE_NAME + " TEXT, "
                + COLUMN_LAST_UPDATE + " INTEGER);");
    }

    public void addImage(String imageName, Bitmap image) {
        String hashImageName = Tools.hash(imageName);
        final ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, hashImageName);
        values.put(COLUMN_LAST_UPDATE, System.currentTimeMillis());

        getWritableDatabase().insert(TABLE_NAME, COLUMN_LAST_UPDATE, values);

        saveFile(hashImageName, image);
        try {
            getWritableDatabase().close();
        } catch (Exception ex) {
            Log.e("ImageDatabase", ex.getMessage(), ex);
        }
    }

    public void addImage(String imageName, byte[] image) {
        String hashImageName = Tools.hash(imageName);
        final ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, hashImageName);
        values.put(COLUMN_LAST_UPDATE, System.currentTimeMillis());

        getWritableDatabase().insert(TABLE_NAME, COLUMN_LAST_UPDATE, values);

        saveFile(hashImageName, image);
        try {
            getWritableDatabase().close();
        } catch (Exception ex) {
            Log.e("ImageDatabase", ex.getMessage(), ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("INFO", "Upgrading database from version " + oldVersion + " to " +
                newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private static boolean saveFile(String fileName, byte[] data) {
        try {
            String path = mCteStatic.getApplicationContext().getFilesDir().getAbsolutePath();
            File dir = new File(path + "/" + DIR_NAME);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File outputFile = new File(dir, fileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean saveFile(String fileName, Bitmap image) {
        try {
            String path = mCteStatic.getApplicationContext().getFilesDir().getAbsolutePath();
            File dir = new File(path + "/" + DIR_NAME);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File outputFile = new File(dir, fileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Bitmap getImage(String imageName) {
        try {
            String imageHashName = Tools.hash(imageName);
            String path = mContext.getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + DIR_NAME, imageHashName);
            Bitmap image = null;
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                // TODO; check if decodeStream cause the same problem as decodeByteArray with memory leaks
                image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return image;
            }
        } catch (Exception e) {
            Log.e("getImage", e.getMessage());
        }

        return null;
    }

    public Bitmap getImage(String imageName, int width, int height) {
        try {
            String imageHashName = Tools.hash(imageName);
            String path = mContext.getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + DIR_NAME, imageHashName);
            Bitmap image = null;
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                // TODO; check if decodeStream cause the same problem as decodeByteArray with memory leaks
                image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                image = Tools.resizeBitmap(image, width, height);
                return image;
            }
        } catch (Exception e) {
            Log.e("getImage", e.getMessage());
        }

        return null;
    }

    public byte[] getImageRawData(String imageName) {
        try {
            String imageHashName = Tools.hash(imageName);
            String path = mContext.getFilesDir().getAbsolutePath();
            File file = new File(path + "/" + DIR_NAME, imageHashName);
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                byte fileContent[] = new byte[(int) file.length()];
                inputStream.read(fileContent);
                inputStream.close();
                return fileContent;
            }
        } catch (Exception e) {
            Log.e("getImageRawData", e.getMessage());
        }

        return null;
    }

    public boolean exists(String imageName) {
        String imageHashName = Tools.hash(imageName);
        String columnas[] = {COLUMN_IMAGE_ID, COLUMN_IMAGE_NAME, COLUMN_LAST_UPDATE};
        Cursor c = this.getReadableDatabase().query(TABLE_NAME, columnas,
                COLUMN_IMAGE_NAME + "=?", new String[]{imageHashName}, null, null, null);

        int iname = c.getColumnIndex(COLUMN_IMAGE_NAME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iname).equalsIgnoreCase(imageHashName)) {
                String path = mContext.getFilesDir().getAbsolutePath();
                File file = new File(path + "/" + DIR_NAME, imageHashName);
                if (file.exists()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        c.close();
        try {
            getWritableDatabase().close();
        } catch (Exception ex) {
            Log.e("ImageDatabase", ex.getMessage(), ex);
        }
        return false;
    }
}
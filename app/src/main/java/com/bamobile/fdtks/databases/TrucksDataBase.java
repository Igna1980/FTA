package com.bamobile.fdtks.databases;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.entities.Camion;

public class TrucksDataBase extends SQLiteOpenHelper implements BaseColumns {

	private static final String DATABASE_NAME = "BAMTruckDB_";

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "trucks";

	private static final String COLUMN_TRUCK_ID = "_id";
	private static final String COLUMN_TRUCK_NAME = "name";
//	private static final String COLUMN_STARRED = "starred";
//	private static final String COLUMN_FOTO1 = "foto1";
//	private static final String COLUMN_FOTO2 = "foto2";
//	private static final String COLUMN_LOGO = "logo";
//	private static final String COLUMN_REPORTS = "reports";
//	private static final String COLUMN_TIPO = "tipo";
//	private static final String COLUMN_USER = "user";

	public TrucksDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_TRUCK_ID
				+ " INTEGER PRIMARY KEY, " + COLUMN_TRUCK_NAME + " TEXT);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("INFO", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}
	
	public List<Camion> findAll(){
		List<Camion> fav_cam = new ArrayList<Camion>();

	    Cursor c = getWritableDatabase().rawQuery("select * from " + TABLE_NAME, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			for(Camion cam : FdTksApplication.getCamiones()){
				if(cam.getCamionPK().getIdcamion().equals(c.getString(c.getColumnIndex(COLUMN_TRUCK_ID)))){
					fav_cam.add(cam);
				}
			}
		}
		return fav_cam;
	}

	public boolean exists(Camion camion) {
		String columnas[] = { COLUMN_TRUCK_ID, COLUMN_TRUCK_NAME};
		Cursor c = this.getReadableDatabase().query(TABLE_NAME, columnas,
				COLUMN_TRUCK_NAME + "=?", new String[] { camion.getNombre() },
				null, null, null);

		int name = c.getColumnIndex(COLUMN_TRUCK_NAME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(name).equalsIgnoreCase(camion.getNombre())) {
				return true;
			} else {
				return false;
			}
		}
		c.close();
		try {
			close();
		} catch (Exception ex) {
			Log.e("TruckDataBase", ex.getMessage(), ex);
		}
		return false;
	}

	public Camion getCamion(Camion camion) {
		String columnas[] = { COLUMN_TRUCK_ID, COLUMN_TRUCK_NAME};
		Cursor c = this.getReadableDatabase().query(TABLE_NAME, columnas,
				COLUMN_TRUCK_NAME + "=?", new String[] { camion.getNombre() },
				null, null, null);

		int name = c.getColumnIndex(COLUMN_TRUCK_NAME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(name).equalsIgnoreCase(camion.getNombre())) {
				return camion;
			}
		}
		c.close();
		return null;
	}

	public void addRenoveFavCam(Camion camion) {
		if (exists(camion)) {
			getWritableDatabase().delete(TABLE_NAME, COLUMN_TRUCK_ID + "=" + camion.getCamionPK().getIdcamion(), null);
		} else {
			final ContentValues values = new ContentValues();
			values.put(COLUMN_TRUCK_ID, camion.getCamionPK().getIdcamion());
			values.put(COLUMN_TRUCK_NAME, camion.getNombre());
			getWritableDatabase().insert(TABLE_NAME, null, values);
			try {
				close();
			} catch (Exception ex) {
				Log.e("TruckDataBase", ex.getMessage(), ex);
			}
		}
	}
}

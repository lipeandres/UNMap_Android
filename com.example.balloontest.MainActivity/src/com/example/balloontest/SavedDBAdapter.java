package com.example.balloontest;

//------------------------------------------------------------------------------------
//
//Librerias
//
//------------------------------------------------------------------------------------

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Declaracion de la clase
public class SavedDBAdapter {

	// ------------------------------------------------------------------------------
	//
	// ATRIBUTOS
	//
	// -----------------------------------------------------------------------------

	private static final String DATABASE_NAME = "un_buildings.db";
	private static final String DATABASE_TABLE = "buildings";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase db;
	private final Context context;

	public static final String KEY_ID = "_id"; // Variables Columnas
	public static final String KEY_NAME = "name";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_LONGITUD = "longitud";
	public static final String KEY_LATITUD = "latitud";

	private SavedDBOpenHelper dbHelper; // Instancia Helper
	
	// -------------------------------------------------------------------------------
	//
	// CONTRUCTOR CLASE (INCLUYE CONTRUCTOR DE LA CLASE INTERNA)
	//
	// -------------------------------------------------------------------------------

	public SavedDBAdapter(Context _context) {
		this.context = _context;
		// crear clase intener en el contructor de la clase principal
		dbHelper = new SavedDBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);

	}

	// ------------------------------------------------------------------------------
	//
	// METODOS
	//
	// ------------------------------------------------------------------------------

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	// Insert a new task
	public long insertItem(BuildingItem _saved) {
		// Create a new row of values to insert.
		ContentValues newSavedValues = new ContentValues();
		// Assign values for each row.
		newSavedValues.put(KEY_NAME, _saved.getName());
		newSavedValues.put(KEY_NUMBER, _saved.getNumber());
		newSavedValues.put(KEY_LONGITUD, _saved.getLongitud());
		newSavedValues.put(KEY_LATITUD, _saved.getLatitud());
		// Insert the row.
		return db.insert(DATABASE_TABLE, null, newSavedValues);
	}

	// Remove a task based on its index
	public boolean removeSaved(long _rowIndex) {
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}

	// Update a task
	public boolean updateSaved(long _rowIndex, String _saved) {
		ContentValues newValue = new ContentValues();
		newValue.put(KEY_NAME, _saved);
		
		return db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex,
				null) > 0;
	}

	// Retorna todas las tareas
	public Cursor getAllSavedItemCursor() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID,KEY_NAME, KEY_NUMBER, KEY_LONGITUD}, null, null, null, null, null);
				//KEY_CREATION_DATE }, null, null, null, null, null);
	}

	// Retorna un registro particular
	public Cursor setCursorToSavedItem(long _rowIndex) throws SQLException {
		Cursor result = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,KEY_NAME,
				KEY_NUMBER, KEY_LONGITUD }, KEY_ID + "=" + _rowIndex, null, null, null, null,
				null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No saved items found for row: " + _rowIndex);
		}
		return result;
	}

	//Retorna un SavedItem
	public BuildingItem getSavedItem(long _rowIndex) throws SQLException {
		Cursor cursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,KEY_NAME,
				KEY_NUMBER, KEY_LONGITUD }, KEY_ID + "=" + _rowIndex, null, null, null, null,
				null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _rowIndex);
		}
		String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
 		String number = cursor.getString(cursor.getColumnIndex(KEY_NUMBER));
		int longitud = cursor.getInt(cursor.getColumnIndex(KEY_LONGITUD));
		int latitud = cursor.getInt(cursor.getColumnIndex(KEY_LATITUD));
		//long created = cursor.getLong(cursor.getColumnIndex(KEY_CREATION_DATE));
		BuildingItem result = new BuildingItem(name,number, longitud, latitud);
		return result;
	}
	
	
	/////////////////
	public String getKeyItemId(long _rowIndex) {
		Cursor result = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,KEY_NAME,
				KEY_NUMBER, KEY_LONGITUD }, KEY_ID + "=" + _rowIndex, null, null, null, null,
				null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No to do item found for row: " + _rowIndex);
		}
		String key_id = result.getString(result.getColumnIndex(KEY_ID));
		return key_id;
	}

	// -----------------------------------------------------------------------------
	//
	// Declaracion de la clase interna
	//
	// -----------------------------------------------------------------------------

	private static class SavedDBOpenHelper extends SQLiteOpenHelper {
		public SavedDBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// SQL Statement to create a new database.
		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + KEY_NAME
				+ " text not null, " + KEY_NUMBER
				+ " text not null, "+ KEY_LONGITUD
				+ " text not null );";

		//+ " text not null, " + KEY_CREATION_DATE + " long);";
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("SavedDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");
			// Drop the old table.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(_db);
		}
	}

}
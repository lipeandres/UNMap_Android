package com.example.balloontest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	 
	//Ruta por defecto de las bases de datos en el sistema Android
	private static String DB_PATH = "/data/data/com.example.balloontest/databases/";
	private static String DB_NAME = "un_buildings.db";
	private static String DATABASE_TABLE = "buildings";
	private SQLiteDatabase db;
	private final Context context;
	
	//Establecemos los nombres de las columnas
	public static final String KEY_ID = "_id";
	public final static String KEY_NAME = "name";
	public final static String KEY_NUMBER = "number";
	public final static String KEY_LONGITUD = "longitud";
	public final static String KEY_LATITUD = "latitud";
	
	 
	//Array de strings para su uso en los diferentes métodos
	private static final String[] cols = new String[] { KEY_ID, KEY_NAME, KEY_NUMBER, KEY_LONGITUD, KEY_LATITUD };
	 
	/**
	* Constructor
	* Toma referencia hacia el contexto de la aplicación que lo invoca para poder acceder a los 'assets' 
	* y 'resources' de la aplicación.
	* Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de la base de datos.
	* @param context
	*/
	public DBHelper(Context context) {
	 
		super(context, DB_NAME, null, 1);
		this.context = context;
	 
	}
	 
	/**
	* Crea una base de datos vacía en el sistema y la reescribe con nuestro fichero de base de datos.
	* */
	public void createDataBase() throws IOException{
	 
		boolean dbExist = checkDataBase();
	 
		if(dbExist){
		//la base de datos existe y no hacemos nada.
		}else{
				//Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
				//de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
				this.getReadableDatabase();
			 
			try {
			 
				copyDataBase();
			 
			} catch (IOException e) {
				throw new Error("Error copiando Base de Datos");
			}
		}
	 
	}
	 
	/**
	* Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicación.
	* @return true si existe, false si no existe
	*/
	private boolean checkDataBase(){
	 
		SQLiteDatabase checkDB = null;
		 
		try{
		 
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			 
		}catch(SQLiteException e){
		 
		//si llegamos aqui es porque la base de datos no existe todavía.
		 
		}
		if(checkDB != null){
		 
			checkDB.close();
		 
		}
		return checkDB != null ? true : false;
	}
	 
	/**
	* Copia nuestra base de datos desde la carpeta assets a la recién creada
	* base de datos en la carpeta de sistema, desde dónde podremos acceder a ella.
	* Esto se hace con bytestream.
	* */
	private void copyDataBase() throws IOException{
	 
	//Abrimos el fichero de base de datos como entrada
	InputStream myInput = context.getAssets().open(DB_NAME);
	 
	//Ruta a la base de datos vacía recién creada
	String outFileName = DB_PATH + DB_NAME;
	 
	//Abrimos la base de datos vacía como salida
	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	//Transferimos los bytes desde el fichero de entrada al de salida
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
	}
	 
	//Liberamos los streams
	myOutput.flush();
	myOutput.close();
	myInput.close();
	 
	}
	 
	public void open() throws SQLException{
	 
	//Abre la base de datos
	try {
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}
		 
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	}
	 
	@Override
	public synchronized void close() {
		if(db != null)
		db.close();
		super.close();
	}
	 
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/*Estas funcionalidades no aplican para el alcance del actual proyecto, pero 
	en la escabilidad del mismo pueden ser útiles
	
	//INSERTAR NUEVO EDIFICIO
	public long insertBuilding(Integer id, String name, String number, Integer longitud, Integer latitud) {
	ContentValues newValues = new ContentValues();
	newValues.put(KEY_ID, id);
	
	newValues.put(KEY_NAME, name);
	newValues.put(KEY_NUMBER, number);
	newValues.put(KEY_LONGITUD, longitud);
	newValues.put(KEY_LATITUD, latitud);
	return db.insert(DATABASE_TABLE, null, newValues);
	} 
	
	//BORRAR EDIFICIO CON _id = _rowIndex
	public boolean removeAlarma(long _rowIndex) {
	return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}*/
	 
	/**
	* ACTUALIZAR EDIFICIO _id = _rowIndex
	* */
	public boolean updateAlarma(Integer _rowIndex, String name, String number, Integer longitud, Integer latitud) {
	ContentValues newValues = new ContentValues();
	newValues.put(KEY_NAME, name);
	newValues.put(KEY_NUMBER, number);
	newValues.put(KEY_LONGITUD, longitud);
	newValues.put(KEY_LATITUD, latitud);
	return db.update(DATABASE_TABLE, newValues, KEY_ID + "=" + _rowIndex, null) > 0;
	}
	
	public Building getBuilding(long _rowIndex) {
			Building building = new Building();
			Cursor result = db.query(true, DATABASE_TABLE, cols, KEY_ID + "=" + _rowIndex, null, null, null,
			null, null);
			if ((result.getCount() == 0) || !result.moveToFirst()) {
				//Si el edificio no existe, devuelve un edificio con valores a, a, -1 y -1
				building = new Building("a","a",-1,-1);
			} else {
				if (result.moveToFirst()) {
					building = new Building(
				    result.getString(result.getColumnIndex(KEY_NAME)),
				    result.getString(result.getColumnIndex(KEY_NUMBER)),
					result.getInt(result.getColumnIndex(KEY_LONGITUD)),
					result.getInt(result.getColumnIndex(KEY_LATITUD))
					);
				}
			}
		return building;
		}
		 
	public List<Building> getBuildings() {
		ArrayList<Building> buildings = new ArrayList<Building>();
		Cursor result = db.query(DATABASE_TABLE,
		cols, null, null, null, null, KEY_ID);
		if (result.moveToFirst())
		do {
			buildings.add(new Building(
					result.getString(result.getColumnIndex(KEY_NAME)),
				    result.getString(result.getColumnIndex(KEY_NUMBER)),
					result.getInt(result.getColumnIndex(KEY_LONGITUD)),
					result.getInt(result.getColumnIndex(KEY_LATITUD))
					)
			);
		} while(result.moveToNext());
		return buildings;
		}
	
		
}

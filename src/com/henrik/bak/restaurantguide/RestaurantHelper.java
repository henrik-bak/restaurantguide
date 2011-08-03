package com.henrik.bak.restaurantguide;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;

class RestaurantHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.henrik.bak.restaurantguide/databases/";
	private static String DB_NAME = "lunchlist.db";
	private static String REST_TABLE = "restaurants";
	private static String FAV_TABLE = "favourites";
	private static final int SCHEMA_VERSION=1;

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public RestaurantHelper(Context context) {
		super(context, DB_NAME, null, SCHEMA_VERSION);
		this.myContext = context;
	}

	/*
	 * @Override public void onCreate(SQLiteDatabase db) { //db.execSQL(
	 * "CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT, feed TEXT, lat REAL, lon REAL);"
	 * ); //db.execSQL(
	 * "CREATE TABLE reviews (_rid INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, score INTEGER, review TEXT, resid INTEGER, FOREIGN KEY(resid) REFERENCES restaurants(_id));"
	 * ); }
	 * 
	 * @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int
	 * newVersion) { if (oldVersion < 2) {
	 * db.execSQL("ALTER TABLE restaurants ADD COLUMN feed TEXT"); } if
	 * (oldVersion < 3) {
	 * db.execSQL("ALTER TABLE restaurants ADD COLUMN lat REAL");
	 * db.execSQL("ALTER TABLE restaurants ADD COLUMN lon REAL"); }
	 * 
	 * }
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {
				Log.d("MAÚÚNIKA", e.getMessage());
				throw new Error("Error copying database" + e.getMessage());

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			Log.d("Meret: ", String.valueOf(length));
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

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

	public void insertRestaurant(String name, String address, String phone,
			String web, String details) {
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("address", address);
		cv.put("phone", phone);
		cv.put("web", web);
		cv.put("details", details);
		getWritableDatabase().insert(REST_TABLE, "name", cv);

	}

	public Cursor getAllRestaurants(String orderBy) {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, name, address, phone, web, details, lat, lon FROM "+REST_TABLE+" ORDER BY "
								+ orderBy, null));
	}

	public Cursor getRestaurantById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, name, address, phone, web, details, lat, lon FROM "+REST_TABLE+" WHERE _ID=?",
						args));
	}

	public void updateRestaurant(String id, String name, String address,
			String phone, String web, String details) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("name", name);
		cv.put("address", address);
		cv.put("phone", phone);
		cv.put("web", web);
		cv.put("details", details);
		getWritableDatabase().update(REST_TABLE, cv, "_ID=?", args);
	}

	public void updateLocation(String id, double lat, double lon) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		cv.put("lat", lat);
		cv.put("lon", lon);
		getWritableDatabase().update(REST_TABLE, cv, "_ID=?", args);
	}

	public void deleteRestaurantById(String id) {
		String[] args = { id };
		getWritableDatabase().delete(REST_TABLE, "_ID=?", args);
	}
	
	public Cursor getSearchResults(String name, String address, String phone,
			String web, String details) {

		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, name, address, phone, web, details, lat, lon FROM "+REST_TABLE+" WHERE name LIKE '%"+name+"%' OR address LIKE '%"+address+"%' OR phone LIKE '%"+phone+"%' OR web LIKE '%"+web+"%' OR details LIKE '%"+details+"%'",null));
		
	}
	
	
	public void insertToFavourites(int resid) {
		ContentValues cv = new ContentValues();
		cv.put("resid", resid);
		getWritableDatabase().insert(FAV_TABLE, "resid", cv);
	}
	
	public Cursor getAllFavourites() {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, resid FROM "+FAV_TABLE, null));
	}

	public Cursor getFavouriteById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, resid FROM "+FAV_TABLE+" WHERE _ID=?",
						args));
	}
	
	public void deleteFavouriteById(String id) {
		String[] args = { id };
		getWritableDatabase().delete(FAV_TABLE, "_ID=?", args);
	}


	public void insertReview(String user, int score, String review, int resid) {
		ContentValues cv = new ContentValues();
		cv.put("user", user);
		cv.put("score", score);
		cv.put("review", review);
		cv.put("resid", resid);
		getWritableDatabase().insert("reviews", "user", cv);

	}

	public Cursor getAllReviews(String orderBy) {
		return (getReadableDatabase().rawQuery(
				"SELECT _rid, user, score, review, resid FROM reviews ORDER BY "
						+ orderBy, null));
	}

	public Cursor getReviewById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _rid, user, score, review, resid FROM reviews WHERE _ID=?",
						args));
	}

	public void updateReview(String rid, String user, int score, String review,
			int resid) {
		ContentValues cv = new ContentValues();
		String[] args = { rid };
		cv.put("user", user);
		cv.put("score", score);
		cv.put("review", review);
		cv.put("resid", resid);
		getWritableDatabase().update("reviews", cv, "_RID=?", args);
	}

	public void deleteReviewById(String id) {
		String[] args = { id };
		getWritableDatabase().delete("reviews", "_RID=?", args);
	}

	public String getResName(Cursor c) {
		return (c.getString(1));
	}

	public String getResAddress(Cursor c) {
		return (c.getString(2));
	}

	public String getResPhone(Cursor c) {
		return (c.getString(3));
	}

	public String getResWeb(Cursor c) {
		return (c.getString(4));
	}

	public String getResDetails(Cursor c) {
		return (c.getString(5));
	}

	public double getLatitude(Cursor c) {
		return (c.getDouble(6));
	}

	public double getLongitude(Cursor c) {
		return (c.getDouble(7));
	}

	public String getRevUser(Cursor c) {
		return (c.getString(1));
	}

	public String getRevScore(Cursor c) {
		return (c.getString(2));
	}

	public String getRevReview(Cursor c) {
		return (c.getString(3));
	}

	public String getRevResid(Cursor c) {
		return (c.getString(4));
	}

}

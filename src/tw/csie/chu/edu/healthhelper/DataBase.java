package tw.csie.chu.edu.healthhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "health.db";
	private static final int DATABASE_VERSION = 1;
	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE user (UID int(255) not null,ID varchar(255) not null ,"
				+ "Passwd varchar(255) not null, Name varchar(255) not null, Disease varchar(255),"
				+ "Date date not null)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS titles");
        onCreate(db);
	}
}
package kz.shymkent.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "aibolscorpion";
    private static DatabaseWrapper sInstance;
    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
        public static synchronized DatabaseWrapper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseWrapper(context.getApplicationContext());
        }
        return sInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");
        sqLiteDatabase.execSQL(CityORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(LastRequestORM.SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");
        sqLiteDatabase.execSQL(CityORM.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(LastRequestORM.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
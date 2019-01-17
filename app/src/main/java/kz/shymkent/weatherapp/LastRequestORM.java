package kz.shymkent.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import kz.shymkent.weatherapp.DatabaseWrapper;

public class LastRequestORM {

    private static final String TAG = "LastRequestORM";

    private static final String TABLE_NAME = "requests";


    private static final String COLUMN_LAST_REQUEST_TEXT_TYPE = "TEXT";
    private static final String COLUMN_LAST_REQUEST_TEXT = "last_request_text";

    ;



    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_LAST_REQUEST_TEXT  + " " + COLUMN_LAST_REQUEST_TEXT_TYPE+
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertRequest(Context context,String request){
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();
        ContentValues values = requestsToContentValues(request);
        long requestId = database.insert(LastRequestORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new text request with ID: " + requestId);

        database.close();
    }

    public  ArrayList<String> getRequests(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + LastRequestORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " text requests...");
        ArrayList<String> requestsList = new ArrayList<String>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String request = cursorToPost(cursor);
                requestsList.add(request);
                cursor.moveToNext();
            }
            Log.i(TAG, "Requests loaded successfully.");
        }

        database.close();

        return requestsList;
    }
    private static String cursorToPost(Cursor cursor) {
        String request = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_REQUEST_TEXT));
        return request;
    }
    private static ContentValues requestsToContentValues(String request) {
        ContentValues values = new ContentValues();
        values.put(LastRequestORM.COLUMN_LAST_REQUEST_TEXT, request);
        return values;
    }
}


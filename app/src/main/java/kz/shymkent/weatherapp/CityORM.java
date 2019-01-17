package kz.shymkent.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kz.shymkent.weatherapp.model.Main;
import kz.shymkent.weatherapp.model.Weathers;
public class CityORM {

    private static final String TAG = "CityORM";

    private static final String TABLE_NAME = "city";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_CITY_TYPE = "TEXT";
    private static final String COLUMN_CITY = "city";

    private static final String COLUMN_TEMPERATURE_TYPE = "DOUBLE";
    private static final String COLUMN_TEMPERATURE = "temperature";
    ;



    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CITY  + " " + COLUMN_CITY_TYPE + COMMA_SEP +
                    COLUMN_TEMPERATURE + " " + COLUMN_TEMPERATURE_TYPE +
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertCity(Context context,Weathers weathers){
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();
        ContentValues values = postToContentValues(weathers);
        long postId = database.insert(CityORM.TABLE_NAME, "null", values);

        database.close();
    }

    public static ArrayList<Weathers> getCities(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        ArrayList<Weathers> weatherList = new ArrayList<Weathers>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + CityORM.TABLE_NAME, null);


        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Weathers weathers = cursorToPost(cursor);
                weatherList.add(weathers);
                cursor.moveToNext();
            }
        }

        database.close();

        return weatherList;
    }
    private static Weathers cursorToPost(Cursor cursor) {
        Weathers weathers = new Weathers();
        weathers.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
        weathers.setMain(new Main());
        weathers.getMain().setTemp(cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE)));
        return weathers;
    }
    private static ContentValues postToContentValues(Weathers weathers) {
        ContentValues values = new ContentValues();
        values.put(CityORM.COLUMN_CITY, weathers.getName());
        values.put(CityORM.COLUMN_TEMPERATURE, weathers.getMain().getTemp());

        return values;
    }
}


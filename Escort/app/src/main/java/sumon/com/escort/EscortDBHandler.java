package sumon.com.escort;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EscortDBHandler extends SQLiteOpenHelper {

    private String TAG = EscortDBHandler.class.getSimpleName();

    private static final String DATABASE_NAME = "EscortDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_UPDATE_TIME = "update_time";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAST_UPDATED_TIME = "last_updated_time";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_UPDATE_TIME + " (" +
                    COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAST_UPDATED_TIME + " INTEGER " +
                    ")";

    public EscortDBHandler(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_UPDATE_TIME);
        db.execSQL(CREATE_TABLE);
    }

    public void insertIntoUpdatedTimeTable(long updatedTime) {

        if (isExistTime()) {
            updateLatestTime(updatedTime);
        } else {
            addLatestTime(updatedTime);
        }
    }

    public boolean isExistTime(){
        boolean isExist = false;

        try {

            String selectQuery = "SELECT  * FROM " + TABLE_UPDATE_TIME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() == 1) {
                isExist = true;
                Log.d(TAG, "updated time exists");
            }else{
                isExist = false;
                Log.d(TAG, "no updated time exists");
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

        return isExist;
    }

    public void updateLatestTime(long updatedTime){

        try {
            if(updatedTime != 0){
                int id = 1;
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_LAST_UPDATED_TIME, updatedTime);

                db.update(TABLE_UPDATE_TIME, values, COLUMN_ID + " = ?", new String[] { String.valueOf(id)});
                db.close();
            }else {
                Log.d(TAG, "0 inserted on app launch time. No need to insert 0 again. "+ updatedTime);
            }
        }catch (Exception e){
            Log.e(TAG, "" + e);
        }

        Log.d(TAG, "update latest time : "+ updatedTime);
    }

    public void addLatestTime(long updatedTime){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_LAST_UPDATED_TIME, updatedTime);

            db.insert(TABLE_UPDATE_TIME, null, values);
            db.close();

        }catch (Exception e){
            Log.e(TAG, "" + e);
        }

        Log.d(TAG, "add latest time : "+ updatedTime);
    }

    public long getLastUpdatedTime() {

        long lastUpdatedTime = 0;

        try {

            String selectQuery = "SELECT  * FROM " + TABLE_UPDATE_TIME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(COLUMN_LAST_UPDATED_TIME);

                if(cursor.getCount() == 1){
                    lastUpdatedTime = cursor.getLong(index);
                }else {
                    Log.d(TAG, "multiple updated time exist");
                }
            }else{
                Log.d(TAG, "cursor is null");
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

        Log.d(TAG, "get last updated time : "+ lastUpdatedTime);

        return lastUpdatedTime;
    }
}

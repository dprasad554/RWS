package tatwa.example.com.recyclerview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBOHelper {


    public long insert(Context context, String tableName, ContentValues contentValues) {

        DatabaseHelper databaseHelper;
        SQLiteDatabase sqLiteDatabase;

        long row_id = -1;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            sqLiteDatabase = databaseHelper.getWritableDatabase();
            if (sqLiteDatabase != null) {
                row_id = sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues,SQLiteDatabase.CONFLICT_ABORT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row_id;
    }

    public long update(Context context, String tablename, ContentValues contentValues, String _id) {
        DatabaseHelper databaseHelper;
        SQLiteDatabase sqLiteDatabase;
        long row_id = 0;

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            sqLiteDatabase = databaseHelper.getWritableDatabase();

            row_id = sqLiteDatabase.update(tablename, contentValues, "id =" + _id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (row_id > 0) {
            Log.d("No of updated row is", "1");
        }
        return row_id;
    }

    public int delete(Context context, String tablename, String whereClause) {

        DatabaseHelper databaseHelper;
        SQLiteDatabase sqLiteDatabase;
        int row = 0;

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            sqLiteDatabase = databaseHelper.getWritableDatabase();

            row = sqLiteDatabase.delete(tablename, whereClause,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (row > 0) {
            Log.d("No of deleted row is", row+"");
        }
        return row;
    }

    /*public ArrayList<HashMap<String, String>> getSearchResult(Context context, String tableName, String columnName, String key) {

        DatabaseHelper databaseHelper;
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor;
        ArrayList<HashMap<String, String>> searchValue = new ArrayList<HashMap<String, String>>();

        try {
            databaseHelper = new DatabaseHelper(context);
            sqLiteDatabase = databaseHelper.getReadableDatabase();

            cursor = sqLiteDatabase.rawQuery("SELECT " + columnName + " FROM "
                    + tableName + " WHERE " + columnName + " like \'%" + key + "%\'", null);

            while (cursor.moveToNext()) {
                HashMap map = new HashMap<String, String>();
                map.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                map.put(columnName, cursor.getString(cursor.getColumnIndex("_id")));

                searchValue.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchValue;
    }*/

}

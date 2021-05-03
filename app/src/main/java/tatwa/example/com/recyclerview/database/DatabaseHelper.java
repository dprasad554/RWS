package tatwa.example.com.recyclerview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import tatwa.example.com.recyclerview.adapter.dataholder.OfflineData;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineModel;

/*
 * Created by biswajit on 14-07-15 10:42 AM.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "sanjog.db";
    //private static String DATABASE_NAME = "rwss.db";
    private static int VERSION_CODE = 2;
    private static DatabaseHelper databaseHelper;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Tables.UpdateStatus.CREATE_TABLE);
        db.execSQL(Tables.OfflineTable.CREATE_TABLE);
    }

    public static DatabaseHelper getInstance(Context context){
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Tables.UpdateStatus.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.OfflineTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    // Adding new contact
    public void addContact(OfflineModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Tables.UpdateStatus.USERID,contact.getUserId());
        values.put(Tables.UpdateStatus.COMPLAINTID,contact.getComplainId());
        values.put(Tables.UpdateStatus.STATUS_TEXT,contact.getStatus());
        values.put(Tables.UpdateStatus.LATITUDE,contact.getLatitude());
        values.put(Tables.UpdateStatus.LONGITUDE,contact.getLongitude());
        values.put(Tables.UpdateStatus.IMAGE_URL,contact.getImageFile());
        values.put(Tables.UpdateStatus.ACTION_DATE,contact.getActionDate());
        values.put(Tables.UpdateStatus.REMARKS,contact.getRemarks());
        values.put(Tables.UpdateStatus.REMARKS,contact.getRemarks());
        values.put(Tables.UpdateStatus.REMARKS,contact.getRemarks());
        values.put(Tables.UpdateStatus.LANDMARK,contact.getLandmark());
        values.put(Tables.UpdateStatus.TUBEWELLID,contact.getTubewellid());
        values.put(Tables.UpdateStatus.COMPLAINTCATEGORY,contact.getCategory());
        values.put(Tables.UpdateStatus.REQUIREDDAYS,contact.getDays());
        values.put(Tables.UpdateStatus.TICKETNUMBER,contact.getTicketNo());

        db.insert(Tables.UpdateStatus.TABLE_NAME, null, values);
        Log.d("Data inserted", "Data inserted");
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addOfflineData(OfflineData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Tables.OfflineTable.BLOCKID,data.getBlockId());
        values.put(Tables.OfflineTable.DISTRICTID,data.getDistrictId());
        values.put(Tables.OfflineTable.GPID,data.getGpId());
        values.put(Tables.OfflineTable.VILLAGEID,data.getVillageId());
        values.put(Tables.OfflineTable.COMPLAINTID,data.getComplaintId());
        values.put(Tables.OfflineTable.COMPLAINTRECEIVEDBY,data.getComplaintReceivedBy());
        values.put(Tables.OfflineTable.COMPLAINTNAME,data.getComplaintName());
        values.put(Tables.OfflineTable.PHONENUMBER,data.getPhoneNumber());
        values.put(Tables.OfflineTable.EMAILID,data.getEmailId());
        values.put(Tables.OfflineTable.GENDER,data.getGender());
        values.put(Tables.OfflineTable.ADDRESS,data.getAddress());
        values.put(Tables.OfflineTable.VILLAGENAME,data.getVillageName());
        values.put(Tables.OfflineTable.GPNAME,data.getGpName());
        values.put(Tables.OfflineTable.BLOCKNAME,data.getBlockName());
        values.put(Tables.OfflineTable.DISTRICTNAME,data.getDistrictName());
        values.put(Tables.OfflineTable.HABITATION,data.getHabitation());
        values.put(Tables.OfflineTable.LOCATION,data.getLocation());
        values.put(Tables.OfflineTable.TUBEWELL,data.getTubeWell());
        values.put(Tables.OfflineTable.SCHEME,data.getScheme());
        values.put(Tables.OfflineTable.SUBSCHEME,data.getSubScheme());
        values.put(Tables.OfflineTable.COMPLAINTYPE,data.getComplainType());
        values.put(Tables.OfflineTable.COMPLAINDETAILS,data.getComplainDetails());
        values.put(Tables.OfflineTable.TICKETNUMBER,data.getTicketNumber());
        values.put(Tables.OfflineTable.COMPLAINSTATUS,data.getComplainStatus());
        values.put(Tables.OfflineTable.COMPLAINREGISTEREDDATE,data.getComplainRegisteredDate());
        values.put(Tables.OfflineTable.LANDMARK,data.getLandmark());
        values.put(Tables.OfflineTable.TUBEWELLID,data.getTubewellid());



        db.insert(Tables.OfflineTable.TABLE_NAME, null, values);
        Log.d("Data inserted", "Data inserted");
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public ArrayList<OfflineModel> getAllKYC() {
        ArrayList<OfflineModel> contactList = new ArrayList<OfflineModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Tables.UpdateStatus.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    OfflineModel contact = new OfflineModel();
                    contact.setUserId(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.USERID)));
                    contact.setComplainId(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.COMPLAINTID)));
                    contact.setStatus(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.STATUS_TEXT)));
                    contact.setLatitude(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.LATITUDE)));
                    contact.setLongitude(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.LONGITUDE)));
                    contact.setImageFile(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.IMAGE_URL)));
                    contact.setActionDate(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.ACTION_DATE)));
                    contact.setRemarks(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.REMARKS)));
                    contact.setCategory(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.COMPLAINTCATEGORY)));
                    contact.setDays(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.REQUIREDDAYS)));
                    contact.setTicketNo(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.TICKETNUMBER)));
                    contact.setLandmark(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.LANDMARK)));
                    contact.setTubewellid(cursor.getString(cursor.getColumnIndex(Tables.UpdateStatus.TUBEWELLID)));

                    // Adding contact to list
                    contactList.add(contact);
                    Log.v("NAME", "DATABASE" + cursor.getString(4));
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.v("BIKASH", "Exception");
            }
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public void emptyKYCTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Tables.UpdateStatus.TABLE_NAME);
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Tables.UpdateStatus.TABLE_NAME + " where id='" + id + "'");
        db.close();
    }

    public void emptyOfflineData() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Tables.OfflineTable.TABLE_NAME);
        db.close();
    }


    public boolean checkIfTableIsEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + Tables.UpdateStatus.TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount == 0) {
            mcursor.close();
            db.close();
            return true;
        } else {
            mcursor.close();
            db.close();
            return false;
        }
    }

    // Getting All Contacts
    public ArrayList<OfflineData> getAllOfflineComplaint(String districtId) {
        ArrayList<OfflineData> contactList = new ArrayList<OfflineData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Tables.OfflineTable.TABLE_NAME /*+ " WHERE " + Tables.OfflineTable.DISTRICTID + " = " + districtId*/;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    OfflineData contact = new OfflineData();
                    contact.setBlockId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.BLOCKID)));
                    contact.setDistrictId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.DISTRICTID)));
                    contact.setGpId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.GPID)));
                    contact.setVillageId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.VILLAGEID)));
                    contact.setComplaintId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINTID)));
                    contact.setComplaintReceivedBy(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINTRECEIVEDBY)));
                    contact.setComplaintName(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINTNAME)));
                    contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.PHONENUMBER)));
                    contact.setEmailId(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.EMAILID)));
                    contact.setGender(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.GENDER)));
                    contact.setAddress(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.ADDRESS)));
                    contact.setVillageName(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.VILLAGENAME)));
                    contact.setGpName(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.GPNAME)));
                    contact.setBlockName(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.BLOCKNAME)));
                    contact.setDistrictName(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.DISTRICTNAME)));
                    contact.setHabitation(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.HABITATION)));
                    contact.setLocation(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.LOCATION)));
                    contact.setTubeWell(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.TUBEWELL)));
                    contact.setScheme(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.SCHEME)));
                    contact.setSubScheme(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.SUBSCHEME)));
                    contact.setComplainType(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINTYPE)));
                    contact.setComplainDetails(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINDETAILS)));
                    contact.setTicketNumber(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.TICKETNUMBER)));
                    contact.setComplainStatus(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINSTATUS)));
                    contact.setComplainRegisteredDate(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.COMPLAINREGISTEREDDATE)));
                    contact.setLandmark(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.LANDMARK)));
                    contact.setTubewellid(cursor.getString(cursor.getColumnIndex(Tables.OfflineTable.TUBEWELLID)));

                    // Adding contact to list
                    contactList.add(contact);
                    Log.v("NAME", "DATABASE" + cursor.getString(4));
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.v("BIKASH", "Exception");
            }
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }
}

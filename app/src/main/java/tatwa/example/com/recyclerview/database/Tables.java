package tatwa.example.com.recyclerview.database;

public class Tables {

    public static class UpdateStatus{

        public static String TABLE_NAME = "status";
        public static String ID = "id";
        public static String STATUS_TEXT = "status_text";
        public static String LATITUDE = "latitude";
        public static String LONGITUDE = "longitude";
        public static String ACTION_DATE = "action_date";
        public static String REMARKS = "remarks";
        public static String LANDMARK="landmark";
        public static String TUBEWELLID="tubewellid";
        public static String USERID = "userid";
        public static String COMPLAINTID = "complaintid";
        public static String IMAGE_URL = "image_url";
        public static String COMPLAINTCATEGORY = "complaintcategory";
        public static String REQUIREDDAYS = "requireddays";
        public static String TICKETNUMBER = "ticketnumber";



        public static String CREATE_TABLE = "create table " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STATUS_TEXT + "  TEXT,"
                + LATITUDE + " TEXT,"
                + LONGITUDE + " TEXT,"
                + ACTION_DATE + " DATETIME,"
                + REMARKS + " TEXT,"
                + LANDMARK + " TEXT,"
                + TUBEWELLID + " TEXT,"
                + USERID + " TEXT,"
                + COMPLAINTID + " TEXT,"
                + COMPLAINTCATEGORY + " TEXT,"
                + REQUIREDDAYS + " TEXT,"
                + TICKETNUMBER + " TEXT,"
                + IMAGE_URL + " TEXT)";
    }

    public static class OfflineTable{

        public static String TABLE_NAME = "offline";
        public static String ID = "id";
        public static String BLOCKID = "blockid";
        public static String DISTRICTID = "districtid";
        public static String GPID = "gpid";
        public static String VILLAGEID = "villageid";
        public static String COMPLAINTID = "complaintid";
        public static String COMPLAINTRECEIVEDBY = "complaintreceivedby";
        public static String COMPLAINTNAME = "complaintname";
        public static String PHONENUMBER = "phonenumber";
        public static String EMAILID = "emailid";
        public static String GENDER = "gender";
        public static String ADDRESS = "address";
        public static String VILLAGENAME = "villagename";
        public static String GPNAME = "gpname";
        public static String BLOCKNAME = "blockname";
        public static String DISTRICTNAME = "districtname";
        public static String HABITATION = "habitation";
        public static String LOCATION = "location";
        public static String TUBEWELL = "tubewell";
        public static String SCHEME = "scheme";
        public static String SUBSCHEME = "subscheme";
        public static String COMPLAINTYPE = "complaintype";
        public static String COMPLAINDETAILS = "complaindetails";
        public static String TICKETNUMBER = "ticketnumber";
        public static String COMPLAINSTATUS = "complainstatus";
        public static String COMPLAINREGISTEREDDATE = "complainRegisteredDate";
        public static String LANDMARK="landmark";
        public static String TUBEWELLID="tubewellid";

        public static String CREATE_TABLE = "create table " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BLOCKID + "  TEXT,"
                + DISTRICTID + " TEXT,"
                + GPID + " TEXT,"
                + VILLAGEID + " TEXT,"
                + COMPLAINTID + " TEXT,"
                + COMPLAINTRECEIVEDBY + " TEXT,"
                + COMPLAINTNAME + " TEXT,"
                + PHONENUMBER + " TEXT,"
                + EMAILID + " TEXT,"
                + GENDER + " TEXT,"
                + ADDRESS + " TEXT,"
                + VILLAGENAME + " TEXT,"
                + GPNAME + " TEXT,"
                + BLOCKNAME + " TEXT,"
                + DISTRICTNAME + " TEXT,"
                + HABITATION + " TEXT,"
                + LOCATION + " TEXT,"
                + TUBEWELL + " TEXT,"
                + SCHEME + " TEXT,"
                + SUBSCHEME + " TEXT,"
                + LANDMARK + " TEXT,"
                + TUBEWELLID + " TEXT,"
                + COMPLAINTYPE + " TEXT,"
                + COMPLAINDETAILS + " TEXT,"
                + TICKETNUMBER + " TEXT,"
                + COMPLAINSTATUS + " TEXT,"
                + COMPLAINREGISTEREDDATE + " TEXT)";
    }

}

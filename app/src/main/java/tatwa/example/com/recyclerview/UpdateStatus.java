package tatwa.example.com.recyclerview;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tatwa.example.com.recyclerview.Utils.SessionManager;
import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineModel;
import tatwa.example.com.recyclerview.database.DatabaseHelper;
import tatwa.example.com.recyclerview.webservices.VolleySingleton;

public class UpdateStatus extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private DatePicker datePicker;
    private boolean gps_enabled, network_enabled;
    private Calendar calendar;
    private TextView gpsLocation;
    private TextView dateView, image_name;
    private int year, month, day;
    private TextView header;
    private ImageButton back;
    private String headerText;
    private TextView Scheme, subScheme, complaintType, ticketno;
    private EditText remarks, req_days,tubewellid,landmark;
    private String scheme_value = "";
    private String subScheme_value = "";
    private String complain_type = "";
    private String ticket_no = "";
    private String complain_id = "";
    private String status = "";
    private BetterSpinner spinner_status, spinner_category;
    private Button update, cancel, upload_image;
    private LinearLayout image_layout;
    public static Handler locationHandler;
    private Location mCurrentLocation;
    double latitude = 0.0, longitude = 0.0;
    private ImageView image_file;
    private final int CAMERA_REQUEST = 1888;
    byte[] imageInByte;
    private String name;
    ProgressDialog progressDialog;
    private int i = 0;
    SharedPreference sharedPreference;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String role = "";
    File destination;
    String imagepath;
    Bundle firstPagebundle;
    String userid;
    String encodedImage;
    String text_remarks, text_status, text_date,text_landmark,text_tubewellid;
    String spinselectedItem = " ", spinSelectedCategory = " ";
    Bitmap thumbnail;
    String data = "";
    private Button btn_update;
    private LinearLayout complaintCategory, days;
    private String category_value = "", days_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_status);
        progressDialog = new ProgressDialog(UpdateStatus.this);
        progressDialog.setMessage("Loading please wait");
        sharedPreference = new SharedPreference(getApplicationContext());
        btn_update = (Button)findViewById(R.id.btn_update);
        userid = sharedPreference.getValue_string("UserId");
        role = sharedPreference.getValue_string("UserRole");
        // userid = sharedPreference.getValue_string("UserId");

        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        header = (TextView) findViewById(R.id.header_text);
        headerText = "Update Status";
        header.setText(headerText);

        gpsLocation = (TextView) findViewById(R.id.txt_location);
        image_name = (TextView) findViewById(R.id.tv_imageName);
        upload_image = (Button) findViewById(R.id.btn_upload);
        update = (Button) findViewById(R.id.btn_update);
        cancel = (Button) findViewById(R.id.btn_cancel);
        image_layout = (LinearLayout) findViewById(R.id.linear_layout_image_camera_icon);
        image_file = (ImageView) findViewById(R.id.image_user_icon);
        complaintCategory = (LinearLayout) findViewById(R.id.ll_category);
        days = (LinearLayout) findViewById(R.id.ll_days);

        if(role.equals("1")){
            btn_update.setVisibility(View.GONE);
        }else if (role.equals("2")){
            btn_update.setVisibility(View.VISIBLE);
        }

        image_file.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStatus.this);
                LayoutInflater layoutInflater = (LayoutInflater) UpdateStatus.this.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                View userReviewLayout = layoutInflater.inflate(R.layout.alert_dialouge, null);
                builder.setView(userReviewLayout);
                final ImageView bookingRefNum;

                bookingRefNum = (ImageView) userReviewLayout.findViewById(R.id.dialog_imageview);
                bookingRefNum.setImageBitmap(thumbnail);


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }

        });

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        //Toast.makeText(activity, "Permission already granted.", Toast.LENGTH_LONG).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        File mydir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "RWSSApp" + "/" + "Images" + "/");
        if (!mydir.exists())
            mydir.mkdirs();
        else
            Log.d("error", "dir. already exists");

        if (getIntent().getBundleExtra("FirstPageBundle") != null) {
            firstPagebundle = getIntent().getBundleExtra("FirstPageBundle");

            name = firstPagebundle.getString("name");

        }

        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateView = (TextView) findViewById(R.id.selectedDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        Scheme = (TextView) findViewById(R.id.Scheme);
        subScheme = (TextView) findViewById(R.id.subScheme);
        complaintType = (TextView) findViewById(R.id.complaintType);
        ticketno = (TextView) findViewById(R.id.ticketno);
        remarks = (EditText) findViewById(R.id.remarks);
        tubewellid = (EditText) findViewById(R.id.tubewell_id);
        landmark = (EditText)findViewById(R.id.landmark);
        req_days = (EditText) findViewById(R.id.requiredDay);
        spinner_status = (BetterSpinner) findViewById(R.id.spinner_status_drop);
        spinner_category = (BetterSpinner) findViewById(R.id.spinner_complaint_category);

        scheme_value = getIntent().getStringExtra("Scheme");
        subScheme_value = getIntent().getStringExtra("SubScheme");
        //subScheme_value = "Pipe Water Supply";
        complain_type = getIntent().getStringExtra("ComplainType");
        complain_id = getIntent().getStringExtra("ComplaintId");
        ticket_no = getIntent().getStringExtra("TicketNumber");

        Scheme.setText(scheme_value);
        subScheme.setText(subScheme_value);
        //subScheme.setText("Pipe Water Supply");
        complaintType.setText(complain_type);
        ticketno.setText(ticket_no);


        if (subScheme_value.equals("Pipe Water Supply")) {
            complaintCategory.setVisibility(View.VISIBLE);
            spinner_category.setVisibility(View.VISIBLE);
        } else {
            complaintCategory.setVisibility(View.GONE);
            spinner_category.setVisibility(View.GONE);
        }




        String[] arrSpinStates = UpdateStatus.this.getResources().getStringArray(R.array.status);
        String[] arrSpinCategories = UpdateStatus.this.getResources().getStringArray(R.array.complaintCategory);

        ArrayAdapter<String> spinStateAdptr = new ArrayAdapter<>(UpdateStatus.this, R.layout.spinner_list_item, R.id.spinner_item, arrSpinStates);
        spinner_status.setAdapter(spinStateAdptr);

        ArrayAdapter<String> spinCategoryAdptr = new ArrayAdapter<>(UpdateStatus.this, R.layout.spinner_list_item, R.id.spinner_item, arrSpinCategories);
        spinner_category.setAdapter(spinCategoryAdptr);

        spinner_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinselectedItem = parent.getItemAtPosition(position).toString();

            }
        });

        spinner_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinSelectedCategory = parent.getItemAtPosition(position).toString();

                if (spinSelectedCategory.equals("Major")) {
                    days.setVisibility(View.VISIBLE);
                    req_days.setVisibility(View.VISIBLE);
                } else {
                    days.setVisibility(View.GONE);
                    req_days.setVisibility(View.GONE);
                }

            }
        });

        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_date = dateView.getText().toString();
                text_remarks =remarks.getText().toString();
                text_landmark=landmark.getText().toString();
                text_tubewellid=tubewellid.getText().toString();
                data = spinner_status.getText().toString();

                if (subScheme_value.equals("Pipe Water Supply")) {

                    category_value = spinner_category.getText().toString();
                    days_value = req_days.getText().toString();

                    if (data.isEmpty()) {
                        Toast.makeText(UpdateStatus.this, "Please Select Status", Toast.LENGTH_SHORT).show();
                    } else if (text_date.length() == 0) {
                        dateView.setError("Please choose date");
                    } else if (category_value.isEmpty()) {
                        Toast.makeText(UpdateStatus.this, "Please Select Complaint Category", Toast.LENGTH_SHORT).show();

                    } else if (category_value.equals("Major") && days_value.equals("")) {
                        req_days.setError("Please enter required days");
                    } else {
                        //Toast.makeText(UpdateStatus.this, "Else part", Toast.LENGTH_SHORT).show();
                        if (SessionManager.isNetworkAvailable(UpdateStatus.this)) {
                            syncUpdateStatusForPWS();
                        } else {
                            DatabaseHelper db = new DatabaseHelper(UpdateStatus.this);
                            OfflineModel offline_data = new OfflineModel();

                            offline_data.setUserId(userid);
                            offline_data.setComplainId(complain_id);
                            offline_data.setStatus(spinselectedItem);
                            offline_data.setLatitude(String.valueOf(currentLatitude));
                            offline_data.setLongitude(String.valueOf(currentLongitude));
                            if (encodedImage == null) {
                                offline_data.setImageFile("");

                            } else {
                                offline_data.setImageFile(encodedImage);
                            }
                            offline_data.setActionDate(text_date);
                            offline_data.setRemarks(text_remarks);
                            offline_data.setLandmark(text_landmark);
                            offline_data.setTubewellid(text_tubewellid);
                            offline_data.setCategory(spinSelectedCategory);
                            if (days_value.equals("")) {
                                offline_data.setDays("");
                            } else {
                                offline_data.setDays(days_value);
                            }
                            offline_data.setTicketNo(ticket_no);
                            db.addContact(offline_data);

                            new android.support.v7.app.AlertDialog.Builder(UpdateStatus.this, R.style.MyAlertDialogStyle)
                                    .setTitle(getResources().getString(R.string.success))
                                    .setCancelable(false)
                                    .setMessage(getResources().getString(R.string.offline_update_success))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            //startActivity(new Intent(UpdateStatus.this, ComplaintListActivity.class));
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    }
                } else {
                    //Log.d("Error", spinselectedItem);

                    if (data.isEmpty()) {
                        Toast.makeText(UpdateStatus.this, "Please Select Status", Toast.LENGTH_SHORT).show();
                    } else if (text_date.length() == 0) {
                        dateView.setError("Please choose date");
                    }/*else if (text_remarks.length() == 0) {
                    remarks.setError("Please give remarks");
                }*/

               /* String name= null;
                if(spinnerName != null && spinnerName.getSelectedItem() !=null ) {
                    name = (String)spinnerName.getSelectedItem();
                } else  {

                }*/
                    else {

                        if (SessionManager.isNetworkAvailable(UpdateStatus.this)) {
                            syncUpdateStatus();
                        } else {
                            //Toast.makeText(UpdateStatus.this, "OFFLINE WORKS", Toast.LENGTH_SHORT).show();
                            DatabaseHelper db = new DatabaseHelper(UpdateStatus.this);
                            OfflineModel offline_data = new OfflineModel();

                            offline_data.setUserId(userid);
                            offline_data.setComplainId(complain_id);
                            offline_data.setStatus(spinselectedItem);
                            offline_data.setLatitude(String.valueOf(currentLatitude));
                            offline_data.setLongitude(String.valueOf(currentLongitude));
                            if (encodedImage == null) {
                                offline_data.setImageFile("");

                            } else {
                                offline_data.setImageFile(encodedImage);
                            }
                            offline_data.setActionDate(text_date);
                            offline_data.setRemarks(text_remarks);
                            offline_data.setLandmark(text_landmark);
                            offline_data.setTubewellid(text_tubewellid);
                            offline_data.setCategory("");
                            offline_data.setDays("");
                            offline_data.setTicketNo(ticket_no);
                            db.addContact(offline_data);

                            new android.support.v7.app.AlertDialog.Builder(UpdateStatus.this, R.style.MyAlertDialogStyle)
                                    .setTitle(getResources().getString(R.string.success))
                                    .setCancelable(false)
                                    .setMessage(getResources().getString(R.string.offline_update_success))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            //startActivity(new Intent(UpdateStatus.this, ComplaintListActivity.class));
                                        }
                                    })
                                    /*    .setNegativeButton("No", null)*/
                                    .show();
                        }


                        // startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    }

                }
                //Toast.makeText(UpdateStatus.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
                                  }
        );
    }


    private void syncUpdateStatus() {
        progressDialog.show();
        String tag_json_req = "update_status";

        StringRequest data = new StringRequest(Request.Method.POST,getResources().getString(R.string.main_url) + getResources().getString(R.string.update_status) + "UpdateComplainStatus", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            Log.d("login response is ", response);
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("Message");
                          /*  jsonObject.spinner_status = jsonObject.getString("ComplainId");
                            jsonObject.spinner_status = jsonObject.getString("Status");
                            jsonObject.spinner_status = jsonObject.getString("UploadFile");
                            jsonObject.spinner_status = jsonObject.getString("ActionDate");
                            jsonObject.spinner_status = jsonObject.getString("AuthorityRemark");
*/
                            //String status = jsonObject.getString("Status");
                            Log.d("JSON", status);
                            //Toast.makeText(MainActivity2.this, status, Toast.LENGTH_SHORT).show();
                            if (status.equals("Success")) {

                                new android.support.v7.app.AlertDialog.Builder(UpdateStatus.this, R.style.MyAlertDialogStyle)
                                        .setTitle(getResources().getString(R.string.success))
                                        .setMessage(getResources().getString(R.string.update_success))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                //startActivity(new Intent(UpdateStatus.this, ComplaintListActivity.class));
                                            }
                                        })
                                        /*    .setNegativeButton("No", null)*/
                                        .show();

                            } else {
                                Toast.makeText(UpdateStatus.this, "Data Updation Unsuccessful", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    if (i < 3) {
                        Log.e("Retry due to error ", "for time : " + i);
                        i++;
                        syncUpdateStatus();
                    } else {
                        Toast.makeText(UpdateStatus.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(UpdateStatus.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String latitude = String.valueOf(currentLatitude);
                String longitude = String.valueOf(currentLongitude);
                String addressDesp = gpsLocation.getText().toString().trim();

                Map<String, String> params = new HashMap<>();
                params.put("Userid", userid);

                params.put("Status", spinselectedItem);
                params.put("Latitude", latitude);
                params.put("Longitude", longitude);
                //params.put("Description", addressDesp);
                System.out.println("encodedImage=========" + encodedImage);
                if (encodedImage == null) {
                    params.put("UploadFile", " ");

                } else {
                    params.put("UploadFile", encodedImage);
                }
                params.put("ActionDate", text_date);
                params.put("ComplainId", complain_id);
                params.put("TubeWellId",text_tubewellid);
                params.put("LandMark",text_landmark);
                params.put("AuthorityRemark", text_remarks);
                params.put("TicketNo", ticket_no);
                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }

    private void syncUpdateStatusForPWS() {
        progressDialog.show();
        String tag_json_req = "update_status_pws";

        StringRequest data = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.update_status) + "UpdateComplainStatusForPWS",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            Log.d("login response is ", response);
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("Message");
                          /*  jsonObject.spinner_status = jsonObject.getString("ComplainId");
                            jsonObject.spinner_status = jsonObject.getString("Status");
                            jsonObject.spinner_status = jsonObject.getString("UploadFile");
                            jsonObject.spinner_status = jsonObject.getString("ActionDate");
                            jsonObject.spinner_status = jsonObject.getString("AuthorityRemark");
*/
                            //String status = jsonObject.getString("Status");
                            Log.d("JSON", status);
                            //Toast.makeText(MainActivity2.this, status, Toast.LENGTH_SHORT).show();
                            if (status.equals("Success")) {

                                new android.support.v7.app.AlertDialog.Builder(UpdateStatus.this, R.style.MyAlertDialogStyle)
                                        .setTitle(getResources().getString(R.string.success))
                                        .setMessage(getResources().getString(R.string.update_success))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                //startActivity(new Intent(UpdateStatus.this, ComplaintListActivity.class));
                                            }
                                        })
                                        /*    .setNegativeButton("No", null)*/
                                        .show();

                            } else {
                                Toast.makeText(UpdateStatus.this, "Data Updation Unsuccessful", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    if (i < 3) {
                        Log.e("Retry due to error ", "for time : " + i);
                        i++;
                        syncUpdateStatus();
                    } else {
                        Toast.makeText(UpdateStatus.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(UpdateStatus.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                String latitude = String.valueOf(currentLatitude);
                String longitude = String.valueOf(currentLongitude);
                String addressDesp = gpsLocation.getText().toString().trim();

                Map<String, String> params = new HashMap<>();
                params.put("Userid", userid);
                params.put("ComplainId", complain_id);
                params.put("Status", spinselectedItem);
                params.put("Latitude", latitude);
                params.put("Longitude", longitude);
                //params.put("Description", addressDesp);
                System.out.println("encodedImage=========" + encodedImage);
                if (encodedImage == null) {
                    params.put("UploadFile", " ");

                } else {
                    params.put("UploadFile", encodedImage);
                }
                params.put("ActionDate", text_date);
                params.put("TubeWellId",text_tubewellid);
                params.put("LandMark",text_landmark);
                params.put("AuthorityRemark", text_remarks);
                params.put("Requirdays", days_value);
                params.put("StsCategory", spinSelectedCategory);
                params.put("TicketNo", ticket_no);
                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            getCompleteAddressString(currentLatitude, currentLongitude);
            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        getCompleteAddressString(currentLatitude, currentLongitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services & GPS High Accuracy")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showFileChooser() {
        boolean result = SessionManager.checkPermission(UpdateStatus.this);
        if (result) {
            captureImage();
        }

    }

    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SessionManager.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage();
                } else {
//code for deny
                }
                break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        imagepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "FeedbackApp" + "/" + "Images" + "/" + name + "_photo" + ".png";
        destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "FeedbackApp" + "/" + "Images" + "/",
                name + "_photo" + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            imageInByte = bytes.toByteArray();
            encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            System.out.println("encodedImage=======" + encodedImage);

            Log.d("Image", encodedImage);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_file.setImageBitmap(thumbnail);
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i))/*.append("\n"*/;
                }
                strAdd = strReturnedAddress.toString();
                String lat = String.valueOf(LATITUDE);
                String lan = String.valueOf(LONGITUDE);
                gpsLocation.setText(lat + "," + lan);
                Log.w("My Current address", strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
    }
}






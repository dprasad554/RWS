package tatwa.example.com.recyclerview;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tatwa.example.com.recyclerview.Utils.SessionManager;
import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineData;
import tatwa.example.com.recyclerview.database.DatabaseHelper;
import tatwa.example.com.recyclerview.webservices.MyApplication;
import tatwa.example.com.recyclerview.webservices.VolleySingleton;

public class LoginActivity extends AppCompatActivity {
    //private ImageButton back;
    private EditText mobile, password;
    private TextView register,subScheme;
    private Button submit;
    String mobile_text, password_text;
    ProgressDialog progressDialog;
    private  int i=0;
    private boolean isLoggedin = false;
    private String imeinum;
    SharedPreference sharedPreference;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @TargetApi(Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        activity = this;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading please wait");

        sharedPreference = new SharedPreference(getApplicationContext());
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        if(sharedPreference.getValue_boolean("LoggedIn")){
            this.finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        mobile = (EditText) findViewById(R.id.mobile_edit);
        password = (EditText) findViewById(R.id.password_edit);
        submit = (Button) findViewById(R.id.btn_submit);


        //register = (TextView) findViewById(R.id.text_register);
       // register.setMovementMethod(LinkMovementMethod.getInstance());

        //setDeviceImei();
        if (!checkPermission()) {
            requestPermission();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_text = mobile.getText().toString();
                password_text= password.getText().toString();
                if (mobile_text.length() == 0) {
                    mobile.setError("Please enter username");
                } else if (password_text.length() == 0) {
                    password.setError("Please enter password ");
                }else{

                    if(SessionManager.isNetworkAvailable(LoginActivity.this)){
                        syncLoginDetails(mobile_text, password_text);
                    }
                   // startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setDeviceImei();
        } else {
            if (isPermissionAllowed()) {
                setDeviceImei();
                return;
            }
            requestLocationPermission();

            if (!checkPermission()) {
                requestPermission();
            }
        }
        if(sharedPreference.getValue_boolean("LoggedIn")){
            this.finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 11);
    }

    private boolean isPermissionAllowed() {
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result1 == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setDeviceImei();
            }
        }else if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(LoginActivity.this, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
                Log.d("Location Msg", "Permission Granted, Now you can access location data.");

            } else {
                // buildAlertMessageNoGps();
                //Toast.makeText(LoginActivity.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                Log.d("Location Msg", "Permission Denied, You cannot access location data.");
            }

        }
    }

    private void syncLoginDetails(final String username, final String password) {
        progressDialog.show();
        String tag_json_req = "upload_details";

        StringRequest data = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_url) + "GetLoginDetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            Log.d("login response is ", response);
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("Message");
                            sharedPreference.setValue_string("UserId",jsonObject.getString("UserId"));
                            sharedPreference.setValue_string("UserRole",jsonObject.getString("UserRole"));
                            sharedPreference.setValue_string("StateId",jsonObject.getString("StateId"));
                            sharedPreference.setValue_string("UserName",jsonObject.getString("UserName"));
                            sharedPreference.setValue_string("StateName",jsonObject.getString("StateName"));


                            //String status = jsonObject.getString("Status");
                            Log.d("JSON", status);
                            //Toast.makeText(MainActivity2.this, status, Toast.LENGTH_SHORT).show();
                            if (!status.equals("") && status.equals("Success")) {
                                syncOfflineLists(jsonObject.getString("UserRole"), jsonObject.getString("UserId"));
                                sharedPreference.setValue_boolean("LoggedIn",true);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                            } else {
                                showAlertMessage("Invalid Credentials");
                                sharedPreference.setValue_boolean("LoggedIn",false);
                                Toast.makeText(LoginActivity.this, "Offline data has not been synced", Toast.LENGTH_LONG).show();
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
                        syncLoginDetails(username, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("Password", password);
                params.put("ImeiNumber", imeinum);

                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }

    private void showAlertMessage(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("RWSS");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setDeviceImei() {
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeinum = mngr.getDeviceId();
        Log.v("IEMI", " iemi num " + imeinum);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(LoginActivity.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            //buildAlertMessageNoGps();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void syncOfflineLists(final String role, final String userid) {

        //progressDialog.show();
        String tag_json_req = "sync_district_data";
        StringRequest data = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_complaint) + "GetAllComplain?" + "DistrictId=" + "" + "&BlockId=" + "" + "&GPId=" + "" + "&villageId=" + "" + "&role=" + role + "&userid=" + userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {
                            Log.d("district list response", response);
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0 ) {
                                getCompaintData(jsonArray);

                            } else {
                                Log.d("Error inserting data", "Error inserting data");
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
                        syncOfflineLists(role, userid);

                    } else {
                        Toast.makeText(LoginActivity.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        //progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(LoginActivity.this, error.getMessage(),
                            Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                Log.d("params are :", "" + params);
                return params;
            }
        };

        data.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                0,
                0));
        Volley.newRequestQueue(MyApplication.getAppContext()).add(data).addMarker(tag_json_req);
    }

    private void getCompaintData(JSONArray jsonArray) {
        DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    OfflineData rowData = new OfflineData();
                    JSONObject ltData = jsonArray.getJSONObject(i);

                    rowData.complaintId = ltData.getString("ComplaintId");
                    rowData.complaintReceivedBy = ltData.getString("ComplaintReceivedBy");
                    rowData.complaintName = ltData.getString("ComplaintName");
                    rowData.phoneNumber = ltData.getString("PhoneNumber");
                    rowData.emailId = ltData.getString("EmailId");
                    rowData.gender = ltData.getString("Gender");
                    rowData.address = ltData.getString("Address");
                    rowData.villageName = ltData.getString("VillageName");
                    rowData.gpName = ltData.getString("GPName");
                    rowData.blockName = ltData.getString("BlockName");
                    rowData.districtName = ltData.getString("DistrictName");
                    rowData.habitation = ltData.getString("Habitation");
                    rowData.location = ltData.getString("Location");
                    rowData.tubeWell = ltData.getString("Tubewell");
                    rowData.scheme = ltData.getString("Scheme");
                    rowData.subScheme = ltData.getString("SubScheme");
                    rowData.complainType = ltData.getString("ComplainType");
                    rowData.complainDetails = ltData.getString("ComplainDetails");
                    rowData.ticketNumber = ltData.getString("TicketNumber");
                    rowData.complainStatus = ltData.getString("ComplainStatus");
                    rowData.complainRegisteredDate = ltData.getString("ComplainRegistredDate");
                    rowData.districtId = ltData.getString("Districtid");
                    rowData.blockId = ltData.getString("Blockid");
                    rowData.gpId = ltData.getString("gpid");
                    rowData.villageId = ltData.getString("villageid");

                    db.addOfflineData(rowData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

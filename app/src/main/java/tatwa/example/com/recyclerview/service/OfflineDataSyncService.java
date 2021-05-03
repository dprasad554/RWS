package tatwa.example.com.recyclerview.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tatwa.example.com.recyclerview.R;
import tatwa.example.com.recyclerview.UpdateStatus;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineModel;
import tatwa.example.com.recyclerview.database.DatabaseHelper;
import tatwa.example.com.recyclerview.receiver.ConnectivityReciever;
import tatwa.example.com.recyclerview.webservices.MyApplication;
import tatwa.example.com.recyclerview.webservices.VolleySingleton;

/**
 * Created by tatwa on 10/9/2018.
 */

public class OfflineDataSyncService extends Service implements ConnectivityReciever.ConnectivityReceiverListener{

   Context context = this;
    private Handler handler;
    boolean isConnected;
    int i = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
         isConnected = ConnectivityReciever.isConnected();
        if(isConnected){
            syncUpdatedDatas();
        }

        MyApplication.getmInstance().setConnectivityListener(this);
       /* handler = new Handler();
        handler.postDelayed(setData, 10000);*/

    }

    /*Runnable setData = new Runnable(){

        @Override
        public void run() {

            boolean isConnected = ConnectivityReciever.isConnected();
            if(isConnected){
                syncUpdatedDatas();
            }
            handler.postDelayed(setData, 10000);
        }
    };

    void startRepeatingTask()
    {
        setData.run();
    }

    void stopRepeatingTask()
    {
        handler.removeCallbacks(setData);
    }*/

    @Override
    public void onStart(Intent intent, int startid) {
       // Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


    }
    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();

    }

    private void syncUpdatedDatas() {
        Log.d("KYC", String.valueOf(new DatabaseHelper(context).checkIfTableIsEmpty()));
        if(new DatabaseHelper(context).checkIfTableIsEmpty()){
            Log.d("No Data to Sync", "No Data");
            //sendPayments();
        }else{
            ArrayList<OfflineModel> kyclist = new DatabaseHelper(context).getAllKYC();
            if (kyclist.size() > 0) {
                for (int i = 0; i < kyclist.size(); i++) {
                    OfflineModel data = kyclist.get(i);

                    String userid = data.getUserId();
                    String complainid = data.getComplainId();
                    String status = data.getStatus();
                    String latitude = data.getLatitude();
                    String longitude = data.getLongitude();
                    String image = data.getImageFile();
                    String date = data.getActionDate();
                    String remark = data.getRemarks();
                    String category = data.getCategory();
                    String day = data.getDays();
                    String ticket = data.getTicketNo();
                    String landmark=data.getLandmark();
                    String tubewellid=data.getTubewellid();

                    if(category.equals("")){
                        syncUpdateStatus(userid, complainid, status, latitude, longitude, image, date, remark,ticket,landmark,tubewellid);
                    }else{
                        syncUpdateStatusForPWS(userid, complainid, status, latitude, longitude, image, date, remark,category, day, ticket,landmark,tubewellid);
                    }

                }

                new DatabaseHelper(context).emptyKYCTable();
            }

        }
    }


    private void syncUpdateStatus(final String userid, final String complainid, final String status, final String latitude, final String longitude, final String image, final String date, final String remark,final String landmark,final String tubewellid, final String ticketNo) {
       // progressDialog.show();
        String tag_json_req = "update_status";

        StringRequest data = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.update_status) + "UpdateComplainStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //progressDialog.dismiss();
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

                                Toast.makeText(context, "Offline data synced successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Data Updation Unsuccessful", Toast.LENGTH_SHORT).show();

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
                        syncUpdateStatus(userid, complainid, status, latitude, longitude, image, date, remark, ticketNo,landmark,tubewellid);
                    } else {
                        Toast.makeText(context, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        //progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Userid", userid);
                params.put("ComplainId", complainid);
                params.put("Status", status);
                params.put("Latitude", latitude);
                params.put("Longitude", longitude);
                //params.put("Description", addressDesp);
                System.out.println("encodedImage=========" + image);
                params.put("UploadFile", image);
                params.put("ActionDate", date);
                params.put("AuthorityRemark", remark);
                params.put("TicketNo", ticketNo);
                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }

    private void syncUpdateStatusForPWS(final String userid, final String complainid, final String status, final String latitude, final String longitude, final String image, final String date, final String remark, final String category, final String days, final String ticketNo,final String landmark,final String tubelwellid) {
        //progressDialog.show();
        String tag_json_req = "update_status_pws";

        StringRequest data = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.update_status) + "UpdateComplainStatusForPWS",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                           // progressDialog.dismiss();
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

                                Toast.makeText(context, "Offline data synced successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Data sync unsuccessful", Toast.LENGTH_SHORT).show();

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
                        syncUpdateStatusForPWS(userid, complainid, status, latitude, longitude, image, date, remark, category, days, ticketNo,landmark,tubelwellid);
                    } else {
                        Toast.makeText(context, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        //progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Userid", userid);
                params.put("ComplainId", complainid);
                params.put("Status", status);
                params.put("Latitude", latitude);
                params.put("Longitude", longitude);
                params.put("UploadFile", image);
                params.put("ActionDate", date);
                params.put("AuthorityRemark", remark);
                params.put("Requirdays", days);
                params.put("StsCategory", category);
                params.put("TicketNo", ticketNo);
                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
         //isConnected = ConnectivityReciever.isConnected();
        if(isConnected){
            syncUpdatedDatas();
        }
    }
}

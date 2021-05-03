package tatwa.example.com.recyclerview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.DistrictListAdapter;
import tatwa.example.com.recyclerview.adapter.EscalationAdapter;
import tatwa.example.com.recyclerview.adapter.dataholder.DistrictData;
import tatwa.example.com.recyclerview.adapter.dataholder.EscalationData;
import tatwa.example.com.recyclerview.adapter.dataholder.MapData;
import tatwa.example.com.recyclerview.webservices.MyApplication;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected GoogleMap map;
    ProgressDialog progressDialog;
    String userid,role;
    Button btnred,btngreen;
    SharedPreference sharedPreference;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /*btnred = (Button) findViewById(R.id.btn_pending);
        btngreen = (Button) findViewById(R.id.btn_solved);


        btnred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btngreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        sharedPreference = new SharedPreference(MapActivity.this);

        progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("Loading please wait");

        userid = sharedPreference.getValue_string("UserId");
        role = sharedPreference.getValue_string("UserRole");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncMapData();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    private void syncMapData() {

        progressDialog.show();
        String tag_json_req = "sync_district_data";
        StringRequest data = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_district) + "AreaWiseComplaintData?" + "&role=" + role +  "&userid=" + userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("map list response", response);
                            JSONArray jsonArray = new JSONArray(response);
                            LatLng latLng = null;

                            if (jsonArray.length() > 0 ) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                                    String lat_s = jsonObj.getString("Latitude");
                                    String lng_s = jsonObj.getString("Longitude");

                                    /*if(lat_s.equals("null") || lng_s.equals("null")){
                                        Toast.makeText(MapActivity.this, "No Map Data Found for: " + jsonObj.getString("DistName"), Toast.LENGTH_LONG).show();
                                    }else{*/
                                        Double lat = Double.valueOf(jsonObj.getString("Latitude"));
                                        Double lng = Double.valueOf(jsonObj.getString("Longitude"));

                                        latLng = new LatLng(lat, lng);
                                    /*}*/

                                    //move CameraPosition on first result
                                    if (i == 0) {
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(latLng).zoom(8).build();

                                        map.animateCamera(CameraUpdateFactory
                                                .newCameraPosition(cameraPosition));
                                    }

                                   // String red_s = String.valueOf(Html.fromHtml(getResources().getString(R.string.registered)));

                                    /*String SnippetData = "Registered: " + jsonObj.getString("ComplaintResigstred") + " Solved: "
                                            + jsonObj.getString("ComplaintSolved") + " Pending: " + jsonObj.getString("ComplaintPending");*/
                                    String SnippetData = "Ticket No: " + jsonObj.getString("TicketNo");
                                    String complaintId = jsonObj.getString("ComplaintID");

                                    if(jsonObj.getString("ComplaintStatus").equals("Resolved")){

                                       Marker green =  map.addMarker(new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                .title(jsonObj.getString("DistrictName"))
                                                .snippet(SnippetData)
                                                .position(latLng));
                                       green.setTag(complaintId);
                                    }else {
                                        Marker red = map.addMarker(new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                .title(jsonObj.getString("DistrictName"))
                                                .snippet(SnippetData)
                                                .position(latLng));
                                        red.setTag(complaintId);
                                    }
                                }

                            } else {
                                    Toast.makeText(MapActivity.this, "No Map Data Found", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MapActivity.this, "No Map Data Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {
                    if (i < 3) {
                        Log.e("Retry due to error ", "for time : " + i);
                        i++;
                        syncMapData();

                    } else {
                        Toast.makeText(MapActivity.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(MapActivity.this, error.getMessage(),
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            syncMapData();
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // if marker  source is clicked
                String compId = (String) arg0.getTag();
                Toast.makeText(MapActivity.this, arg0.getTitle()+ " " + compId, Toast.LENGTH_SHORT).show();// display toast
                syncEscalationDetails(compId);
                return true;
            }
        });
    }



    private void syncEscalationDetails(final String complaintId) {

        progressDialog.show();
        String tag_json_req = "sync_escalation_data";
        StringRequest data = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_district) + "ComplaintByTicketNo?" + "Cid=" + complaintId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("escalation response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("EscalationDetails");

                            showEscalationDialog(jsonObject, jsonArray);

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
                        syncEscalationDetails(complaintId);

                    } else {
                        Toast.makeText(MapActivity.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(MapActivity.this, error.getMessage(),
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

    private void showEscalationDialog(JSONObject jObj, JSONArray jArray){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) MapActivity.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View escalationLayout = layoutInflater.inflate(R.layout.escalation_details, null);
        builder.setView(escalationLayout);
        final TextView compId, ticketNo, compStatus, distName, blockName, gpName, villageName, habitationName, locationName, no_data;
        final RecyclerView recyclerView;
        EscalationAdapter adapter;
        compId = (TextView) escalationLayout.findViewById(R.id.tv_complaint_id);
        ticketNo = (TextView) escalationLayout.findViewById(R.id.tv_ticket_number);
        compStatus = (TextView) escalationLayout.findViewById(R.id.tv_complaint_status);
        distName = (TextView) escalationLayout.findViewById(R.id.tv_dist_name);
        blockName = (TextView) escalationLayout.findViewById(R.id.tv_block_name);
        gpName = (TextView) escalationLayout.findViewById(R.id.tv_gp_name);
        villageName = (TextView) escalationLayout.findViewById(R.id.tv_village_name);
        habitationName = (TextView) escalationLayout.findViewById(R.id.tv_habitat_name);
        locationName = (TextView) escalationLayout.findViewById(R.id.tv_location_name);
        no_data = (TextView) escalationLayout.findViewById(R.id.tv_noData);
        recyclerView = (RecyclerView) escalationLayout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MapActivity.this));
        adapter = new EscalationAdapter(MapActivity.this);
        recyclerView.setAdapter(adapter);

        try {
            compId.setText(jObj.getString("ComplaintID"));
            ticketNo.setText(jObj.getString("TicketNo"));
            compStatus.setText(jObj.getString("ComplaintStatus"));
            distName.setText(jObj.getString("DistrictName"));
            blockName.setText(jObj.getString("BlockName"));
            gpName.setText(jObj.getString("GPName"));
            villageName.setText(jObj.getString("VillageName"));
            habitationName.setText(jObj.getString("HabitationName"));
            locationName.setText(jObj.getString("LocationName"));

            if(jArray.length() > 0){
                no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setEscalationList(getEscalationData(jArray));
            }else{
                no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private List<EscalationData> getEscalationData(JSONArray jsonArray) {
        List<EscalationData> gpData = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    EscalationData rowData = new EscalationData();
                    JSONObject ltData = jsonArray.getJSONObject(i);

                    rowData.intimatedDate = ltData.getString("IntimatedDate");
                    rowData.nextEscalationDate = ltData.getString("NextEscalationDate");
                    rowData.functionaryName = ltData.getString("FunctionaryName");
                    rowData.designationName = ltData.getString("DesignationName");
                    rowData.mobile = ltData.getString("Mobile");
                    rowData.email = ltData.getString("Email");

                    gpData.add(rowData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gpData;
    }


}

package tatwa.example.com.recyclerview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tatwa.example.com.recyclerview.Utils.SessionManager;
import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.VillageListAdapter;
import tatwa.example.com.recyclerview.adapter.dataholder.DistrictData;
import tatwa.example.com.recyclerview.webservices.MyApplication;

public class VillageListActivity extends AppCompatActivity{

    private TextView header, no_data;
    private String value = "Pending Complaints", gp_id = "";
    private ImageButton back;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SharedPreference sharedPreference;
    VillageListAdapter adapter;
    String userid,role;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villagelist);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        sharedPreference = new SharedPreference(VillageListActivity.this);

        userid = sharedPreference.getValue_string("UserId");
        role = sharedPreference.getValue_string("UserRole");

        header = (TextView) findViewById(R.id.header_text);
        no_data = (TextView) findViewById(R.id.tv_noData);
         value = getIntent().getStringExtra("Header");
        header.setText(value + " GramPanchayat");

        gp_id = getIntent().getStringExtra("gp_id");
        progressDialog = new ProgressDialog(VillageListActivity.this);
        progressDialog.setMessage("Loading please wait");

        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VillageListAdapter(VillageListActivity.this);
        recyclerView.setAdapter(adapter);

        if(SessionManager.isNetworkAvailable(VillageListActivity.this)){
            syncVillageComplaintsLists();
        }

      /*  Fragment home = new SubFragment();
        FragmentManager FM = getSupportFragmentManager();
        FM.beginTransaction()
                .replace(R.id.sublayout, home)
                .addToBackStack(home.getClass().getName())
                .commit();*/
    }

    private void syncVillageComplaintsLists() {

        progressDialog.show();
        String tag_json_req = "sync_district_data";
        StringRequest data = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_village) + "GetAllVillage?" + "gramPanchayatId="+ gp_id + "&role=" + role +  "&userid=" + userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("district list response", response);
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0 ) {
                                no_data.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter.setVillageList(getVillageData(jsonArray));

                            } else {
                                no_data.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
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
                        syncVillageComplaintsLists();

                    } else {
                        Toast.makeText(VillageListActivity.this, "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(VillageListActivity.this, error.getMessage(),
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

    private List<DistrictData> getVillageData(JSONArray jsonArray) {
        List<DistrictData> villageData = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    DistrictData rowData = new DistrictData();
                    JSONObject ltData = jsonArray.getJSONObject(i);

                    rowData.slNo = String.valueOf(i+1);
                    rowData.districtId = ltData.getString("VillageId");
                    rowData.districtName = ltData.getString("VillageName");
                    rowData.complaintRegistered = ltData.getString("ComplaintResigstred");
                    rowData.complaintSolved = ltData.getString("ComplaintSolved");
                    rowData.complaintPending = ltData.getString("ComplaintPending");
                    rowData.inProgress = ltData.getString("InProgress");

                    villageData.add(rowData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return villageData;
    }
}

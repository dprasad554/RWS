package tatwa.example.com.recyclerview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tatwa.example.com.recyclerview.adapter.ComplaintListAdapter;
import tatwa.example.com.recyclerview.adapter.SubcomplaintListAdapter;
import tatwa.example.com.recyclerview.adapter.dataholder.ComplaintData;
import tatwa.example.com.recyclerview.webservices.MyApplication;

public class SubFragment extends Fragment {
    private TextView header, no_data;
    private String value = "", village_id = "";
    private ImageButton back;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    String userid,role;

    ProgressDialog progressDialog;
    SubcomplaintListAdapter adapter;
    private int i = 0;
    SessionManager sessionManager;
    public static final String DATE_SEND_FORMAT = "yyyy-MM-dd";

    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.sub_complaint_list, container, false);

        sharedPreference = new SharedPreference(getActivity());

        userid = sharedPreference.getValue_string("UserId");
        role = sharedPreference.getValue_string("UserRole");


        no_data = (TextView) rootView.findViewById(R.id.tv_noData);
        sessionManager = new SessionManager(getActivity());

        village_id = getActivity().getIntent().getStringExtra("village_id");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait");

      /*  back = (ImageButton)rootView. findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        recyclerView = (RecyclerView)rootView. findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SubcomplaintListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        syncComplaintsLists();
        context = getActivity();
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void syncComplaintsLists() {

        progressDialog.show();
        String tag_json_req = "sync_district_data";
        StringRequest data = new StringRequest(Request.Method.GET,
                getResources().getString(R.string.main_url) + getResources()
                        .getString(R.string.index_complaint) + "GetAllComplain?" + "DistrictId=" + "D19" + "&BlockId=" + "" + "&GPId=" + "" + "&villageId=" + "" + "&role=" + role + "&userid=" + userid,
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
                                adapter.setComplaintList(getCompaintData(jsonArray));

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
                        syncComplaintsLists();

                    } else {
                        Toast.makeText(getActivity(), "Check your network connection.",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } else
                    Toast.makeText(getActivity(), error.getMessage(),
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

    private List<ComplaintData> getCompaintData(JSONArray jsonArray) {
        List<ComplaintData> complaintDataData = new ArrayList<>();
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    ComplaintData rowData = new ComplaintData();
                    JSONObject ltData = jsonArray.getJSONObject(i);

                    rowData.complaintId = ltData.getString("ComplaintId");
                    rowData.complaintReceivedBy = ltData.getString("ComplaintReceivedBy");
                    rowData.complaintName = ltData.getString("ComplaintName");
                    rowData.phoneNumber = ltData.getString("PhoneNumber");
                    rowData.remarks = ltData.getString("Remarks");
                    rowData.status = ltData.getString("Status");
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
                    String dateToSend = ltData.getString("ComplainRegistredDate");
                    String[] parts = dateToSend.split("T");
                    dateToSend = parts[0];
                    rowData.complainRegistredDate = sessionManager.parseDate(dateToSend, DATE_SEND_FORMAT, "dd-MMM-yy");

                    complaintDataData.add(rowData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return complaintDataData;
    }
}

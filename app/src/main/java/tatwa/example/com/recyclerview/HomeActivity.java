package tatwa.example.com.recyclerview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tatwa.example.com.recyclerview.Utils.SessionManager;
import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.dataholder.ComplaintData;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineData;
import tatwa.example.com.recyclerview.database.DatabaseHelper;
import tatwa.example.com.recyclerview.service.OfflineDataSyncService;
import tatwa.example.com.recyclerview.webservices.MyApplication;

public class HomeActivity extends AppCompatActivity{

    private Button admin_report_btn,logout_btn, map_btn, je_report_btn;
    /*private ImageButton logout_btn;*/
    SharedPreference sharedPreference;
    private BetterSpinner spinner_state;
    String spinner_value = "", role = "", userid = "";
    private int i = 0;
    private LinearLayout admin_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, OfflineDataSyncService.class));

        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        admin_report_btn = (Button) findViewById(R.id.btn_adminreport);
        je_report_btn = (Button) findViewById(R.id.btn_jereport);
        logout_btn = (Button) findViewById(R.id.btn_logout);
        map_btn = (Button) findViewById(R.id.btn_map);
        spinner_state = (BetterSpinner) findViewById(R.id.spinner_state);
        admin_layout = (LinearLayout) findViewById(R.id.ll_adminview);
        sharedPreference = new SharedPreference(getApplicationContext());
        role = sharedPreference.getValue_string("UserRole");
        userid = sharedPreference.getValue_string("UserId");

        if(role.equals("1")|| role.equals("4")){
            admin_layout.setVisibility(View.VISIBLE);
            je_report_btn.setVisibility(View.GONE);
        }else{
            admin_layout.setVisibility(View.GONE);
            je_report_btn.setVisibility(View.VISIBLE);
        }

        //String[] arrSpinStates = HomeActivity.this.getResources().getStringArray(R.array.status);
        String[] arrSpinStates = {"--Select--", sharedPreference.getValue_string("StateName")};

        ArrayAdapter<String> spinStateAdptr = new ArrayAdapter<>(HomeActivity.this, R.layout.spinner_list_item, R.id.spinner_item, arrSpinStates);
        spinner_state.setAdapter(spinStateAdptr);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(getResources().getString(R.string.logout))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper db = new DatabaseHelper(HomeActivity.this);
                                db.emptyOfflineData();
                                finishAffinity();
                                sharedPreference.setValue_boolean("LoggedIn",false);
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinner_value = parent.getItemAtPosition(position).toString();

            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });
        admin_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner_value.equals("") || spinner_value.equals("--Select--")){
                    Toast.makeText(HomeActivity.this, "Please select state",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(HomeActivity.this, DistrictListActivity.class)
                            .putExtra("Header", sharedPreference.getValue_string("StateName")));
                }
            }
        });

        je_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner_value.equals("") || spinner_value.equals("--Select--")){
                    Toast.makeText(HomeActivity.this, "Please select state",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(HomeActivity.this, DistrictListActivity.class)
                            .putExtra("Header", sharedPreference.getValue_string("StateName")));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();

        if (fragments == 0) {
            new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.want_to_exit))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}

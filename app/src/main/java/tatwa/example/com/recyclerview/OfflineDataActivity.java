package tatwa.example.com.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.BlockListAdapter;
import tatwa.example.com.recyclerview.adapter.OfflineDataAdapter;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineData;
import tatwa.example.com.recyclerview.database.DatabaseHelper;

public class OfflineDataActivity extends AppCompatActivity {

    private TextView header, no_data;
    private ImageButton back;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    private String districtId = "", header_val = "";
    OfflineDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint_list);

        sharedPreference = new SharedPreference(OfflineDataActivity.this);
        header = (TextView) findViewById(R.id.header_text);
        no_data = (TextView) findViewById(R.id.tv_noData);

        districtId = getIntent().getStringExtra("district_id");
        header_val = getIntent().getStringExtra("Header");
        header.setText(header_val + " District");

        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OfflineDataAdapter(  OfflineDataActivity.this);
        ArrayList<OfflineData> complaintArrayList = new DatabaseHelper(OfflineDataActivity.this).getAllOfflineComplaint(districtId);
        if (!complaintArrayList.isEmpty()) {
            adapter.setComplaintList(complaintArrayList);
            if(complaintArrayList.size() > 0){
                recyclerView.setAdapter(adapter);
            }else {
                no_data.setVisibility(View.VISIBLE);
            }
        }else{
            no_data.setVisibility(View.VISIBLE);
        }
    }
}

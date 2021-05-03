package tatwa.example.com.recyclerview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import tatwa.example.com.recyclerview.adapter.dataholder.ComplaintData;

public class MainActivity extends AppCompatActivity {
    private TextView header, receivedBy, complainantName, phoneNo, emailId, address,
                     villageName, gpName, blockName, districtName, habitation, location, tubeWell, scheme,
                     subScheme, complainType, complaintDetails,status,remarks;
    private ImageButton back;
    private Button update;

    private RadioButton male, female, transGender;
    String  received_by, complainant_name, phone_no, email_id, address_value, village_name, gp_name, block_name,
            district_name, habitation_value, location_value, tubeWell_value, scheme_value, subScheme_value,
            complain_type, complaint_details, complaint_id, gender_value, ticketNo,status_tv,remarks_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setReferences();
        getData();
        setData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateStatus.class)
                        .putExtra("Scheme", scheme_value)
                        .putExtra("SubScheme", subScheme_value)
                        .putExtra("ComplainType", complain_type)
                        .putExtra("TicketNumber", ticketNo));
            }
        });

    }

    public  void setReferences(){
        back = (ImageButton) findViewById(R.id.btn_back);
        header = (TextView) findViewById(R.id.header_text);
        header.setText("Complaint Details");
        update = (Button) findViewById(R.id.btn_update);
        receivedBy = (TextView) findViewById(R.id.tv_receivedBy);
        complainantName = (TextView) findViewById(R.id.tv_complainantName);
        phoneNo = (TextView) findViewById(R.id.tv_phoneNo);
        status = (TextView)findViewById(R.id.tv_status);
        remarks = (TextView)findViewById(R.id.tv_remarks);
        emailId = (TextView) findViewById(R.id.tv_emailId);
        address = (TextView) findViewById(R.id.tv_address);
        villageName = (TextView) findViewById(R.id.tv_villageName);
        gpName = (TextView) findViewById(R.id.tv_gpName);
        blockName = (TextView) findViewById(R.id.tv_blockName);
        districtName = (TextView) findViewById(R.id.tv_districtName);
        habitation = (TextView) findViewById(R.id.tv_habitation);
        location = (TextView) findViewById(R.id.tv_location);
        tubeWell = (TextView) findViewById(R.id.tv_tubeWell);
        scheme = (TextView) findViewById(R.id.tv_scheme);
        subScheme = (TextView) findViewById(R.id.tv_subScheme);
        complainType = (TextView) findViewById(R.id.tv_compalaintType);
        complaintDetails = (TextView) findViewById(R.id.tv_complaintDetails);
        male = (RadioButton) findViewById(R.id.rb_male);
        female = (RadioButton) findViewById(R.id.rb_female);
        transGender = (RadioButton) findViewById(R.id.rb_trans);
    }

    public void getData(){
        complaint_id = getIntent().getStringExtra("ComplaintId");
        received_by = getIntent().getStringExtra("ComplaintReceivedBy");
        complainant_name = getIntent().getStringExtra("ComplaintName");
        phone_no = getIntent().getStringExtra("PhoneNumber");
        status_tv = getIntent().getStringExtra("Status");
        remarks_tv = getIntent().getStringExtra("Remarks");
        email_id = getIntent().getStringExtra("EmailId");
        gender_value = getIntent().getStringExtra("Gender");
        address_value = getIntent().getStringExtra("Address");
        village_name = getIntent().getStringExtra("VillageName");
        gp_name = getIntent().getStringExtra("GPName");
        block_name = getIntent().getStringExtra("BlockName");
        district_name = getIntent().getStringExtra("DistrictName");
        habitation_value = getIntent().getStringExtra("Habitation");
        location_value = getIntent().getStringExtra("Location");
        tubeWell_value = getIntent().getStringExtra("Tubewell");
        scheme_value = getIntent().getStringExtra("Scheme");
        subScheme_value = getIntent().getStringExtra("SubScheme");
        complain_type = getIntent().getStringExtra("ComplainType");
        complaint_details = getIntent().getStringExtra("ComplainDetails");
        ticketNo = getIntent().getStringExtra("TicketNumber");

    }

    public void setData(){

        receivedBy.setText(received_by);
        complainantName.setText(complainant_name);
        phoneNo.setText(phone_no);
        status.setText(status_tv);
        remarks.setText(remarks_tv);
        emailId.setText(email_id);
        address.setText(address_value);
        villageName.setText(village_name);
        gpName.setText(gp_name);
        blockName.setText(block_name);
        districtName.setText(district_name);
        habitation.setText(habitation_value);
        location.setText(location_value);
        tubeWell.setText(tubeWell_value);
        scheme.setText(scheme_value);
        subScheme.setText(subScheme_value);
        complainType.setText(complain_type);
        complaintDetails.setText(complaint_details);

        if(gender_value.equals("Male")){
            male.setChecked(true);
        }else if(gender_value.equals("Female")){
            female.setChecked(true);
        }else if(gender_value.equals("Transgender")){
            transGender.setChecked(true);
        }else {
            male.setChecked(false);
            female.setChecked(false);
            transGender.setChecked(false);
        }

    }

}

package tatwa.example.com.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import tatwa.example.com.recyclerview.MainActivity;
import tatwa.example.com.recyclerview.R;
import tatwa.example.com.recyclerview.UpdateStatus;
import tatwa.example.com.recyclerview.Utils.SharedPreference;
import tatwa.example.com.recyclerview.adapter.dataholder.ComplaintData;
import tatwa.example.com.recyclerview.adapter.dataholder.OfflineData;

public class OfflineDataAdapter extends RecyclerView.Adapter<OfflineDataAdapter.NewProductViewHolder>{

    private Context mCtx;

    private List<OfflineData> newproductList = Collections.emptyList();
    String userid = "";
    SharedPreference sharedPreference;

    public OfflineDataAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void setComplaintList(List<OfflineData> newsList) {
        this.newproductList = newsList;
        sharedPreference = new SharedPreference(mCtx);
        Log.d("array size", newproductList.size() + "");
        notifyDataSetChanged();
        notifyItemRangeChanged(-1, newsList.size());
    }

    @Override
    public OfflineDataAdapter.NewProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.offline_complaint_list_row, null);
        return new OfflineDataAdapter.NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfflineDataAdapter.NewProductViewHolder holder, int position) {

        OfflineData product = newproductList.get(position);
        holder.complainantName.setText(product.complaintName);
        holder.complainType.setText(product.complainType);
        holder.date.setText(product.complainRegisteredDate);
        holder.status.setText(product.complainStatus);
        holder.block_name.setText("Block Name: " + product.blockName);
        holder.gp_name.setText("GP Name: " + product.gpName);
        holder.village_name.setText("Village Name: " + product.villageName);

        userid = sharedPreference.getValue_string("UserRole");

        if (product.complainStatus.equals("Resolved")){
            holder.edit.setVisibility(View.GONE);
        }else if(userid.equals("1")){
            holder.edit.setVisibility(View.GONE);
        }else if(userid.equals("4")){
            holder.edit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return newproductList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView complainantName, complainType, date, status, block_name, gp_name, village_name;
        ImageView view, edit;

        public NewProductViewHolder(View itemView) {
            super(itemView);

            complainantName = (TextView) itemView.findViewById(R.id.txt_complainantname);
            complainType = (TextView) itemView.findViewById(R.id.txt_complaintype);
            date = (TextView) itemView.findViewById(R.id.txt_date);
            status = (TextView) itemView.findViewById(R.id.txt_status);
            block_name = (TextView) itemView.findViewById(R.id.txt_blockName);
            gp_name = (TextView) itemView.findViewById(R.id.txt_gpName);
            village_name = (TextView) itemView.findViewById(R.id.txt_villageName);

            view = (ImageView) itemView.findViewById(R.id.view_complaint);
            edit = (ImageView) itemView.findViewById(R.id.edit_complaint);

            view.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.view_complaint) {
                mCtx.startActivity(new Intent(mCtx, MainActivity.class)
                        .putExtra("ComplaintId", newproductList.get(getAdapterPosition()).complaintId)
                        .putExtra("ComplaintReceivedBy", newproductList.get(getAdapterPosition()).complaintReceivedBy)
                        .putExtra("ComplaintName", newproductList.get(getAdapterPosition()).complaintName)
                        .putExtra("PhoneNumber", newproductList.get(getAdapterPosition()).phoneNumber)
                        .putExtra("EmailId", newproductList.get(getAdapterPosition()).emailId)
                        .putExtra("Gender", newproductList.get(getAdapterPosition()).gender)
                        .putExtra("Address", newproductList.get(getAdapterPosition()).address)
                        .putExtra("VillageName", newproductList.get(getAdapterPosition()).villageName)
                        .putExtra("GPName", newproductList.get(getAdapterPosition()).gpName)
                        .putExtra("BlockName", newproductList.get(getAdapterPosition()).blockName)
                        .putExtra("DistrictName", newproductList.get(getAdapterPosition()).districtName)
                        .putExtra("Habitation", newproductList.get(getAdapterPosition()).habitation)
                        .putExtra("Location", newproductList.get(getAdapterPosition()).location)
                        .putExtra("Tubewell", newproductList.get(getAdapterPosition()).tubeWell)
                        .putExtra("Scheme", newproductList.get(getAdapterPosition()).scheme)
                        .putExtra("SubScheme", newproductList.get(getAdapterPosition()).subScheme)
                        .putExtra("ComplainType", newproductList.get(getAdapterPosition()).complainType)
                        .putExtra("ComplainDetails", newproductList.get(getAdapterPosition()).complainDetails)
                        .putExtra("TicketNumber", newproductList.get(getAdapterPosition()).ticketNumber));
            }else if(v.getId() == R.id.edit_complaint){

                mCtx.startActivity(new Intent(mCtx, UpdateStatus.class)
                        .putExtra("ComplaintId", newproductList.get(getAdapterPosition()).complaintId)
                        .putExtra("Scheme", newproductList.get(getAdapterPosition()).scheme)
                        .putExtra("SubScheme", newproductList.get(getAdapterPosition()).subScheme)
                        .putExtra("ComplainType", newproductList.get(getAdapterPosition()).complainType)
                        .putExtra("TicketNumber", newproductList.get(getAdapterPosition()).ticketNumber));

            }
        }
    }
}

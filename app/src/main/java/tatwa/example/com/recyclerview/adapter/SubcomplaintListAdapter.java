package tatwa.example.com.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import tatwa.example.com.recyclerview.adapter.dataholder.ComplaintData;

public class SubcomplaintListAdapter extends RecyclerView.Adapter<SubcomplaintListAdapter.NewProductViewHolder> {

    private Context mCtx;

    private List<ComplaintData> newproductList = Collections.emptyList();
    ;

    public SubcomplaintListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void setComplaintList(List<ComplaintData> newsList) {
        this.newproductList = newsList;
        Log.d("array size", newproductList.size() + "");
        notifyDataSetChanged();
        notifyItemRangeChanged(-1, newsList.size());
    }

    @Override
    public SubcomplaintListAdapter.NewProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.sub_complaint_list_row, null);
        return new SubcomplaintListAdapter.NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubcomplaintListAdapter.NewProductViewHolder holder, int position) {

        ComplaintData product = newproductList.get(position);
        holder.complainantName.setText(product.complaintName);
        holder.complainType.setText(product.complainType);
        holder.VillageName.setText(product.villageName);
        holder.gpName.setText(product.gpName);

        //holder.remarks.setText(product.remarks);
        /*holder.date.setText(product.complainRegistredDate);*/
        holder.status.setText(product.complainStatus);
        if(product.complainStatus.equals("Resolved")){
            holder.edit.setVisibility(View.GONE);
        }else{
            holder.edit.setVisibility(View.VISIBLE);
        }
        if(product.subScheme.equals("Pipe Water Supply")){
            holder.complainType.setTextColor(Color.BLACK);
        }else if (product.subScheme.equals("Tube Well")) {
            holder.complainType.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {

        return newproductList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView VillageName, gpName, complainantName, complainType, date, status,remarks;
        ImageView view, edit ;

        public NewProductViewHolder(View itemView) {
            super(itemView);

            VillageName = (TextView) itemView.findViewById(R.id.txt_villagename);
            gpName = (TextView) itemView.findViewById(R.id.txt_gpname);
            complainantName = (TextView) itemView.findViewById(R.id.txt_complainantname);
            complainType = (TextView) itemView.findViewById(R.id.txt_complaintype);
            /*date = (TextView) itemView.findViewById(R.id.txt_date);*/
            status = (TextView) itemView.findViewById(R.id.txt_status);
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
                        .putExtra("Status",newproductList.get(getAdapterPosition()).complainStatus)
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
            } else if (v.getId() == R.id.edit_complaint) {

                mCtx.startActivity(new Intent(mCtx, UpdateStatus.class)
                        .putExtra("ComplaintId", newproductList.get(getAdapterPosition()).complaintId)
                        .putExtra("Scheme", newproductList.get(getAdapterPosition()).scheme)
                        .putExtra("TubeWellId",newproductList.get(getAdapterPosition()).tubewellid)
                        .putExtra("LandMark",newproductList.get(getAdapterPosition()).landmark)
                        .putExtra("SubScheme", newproductList.get(getAdapterPosition()).subScheme)
                        .putExtra("ComplainType", newproductList.get(getAdapterPosition()).complainType)
                        .putExtra("TicketNumber", newproductList.get(getAdapterPosition()).ticketNumber));

            }
        }

   }

}

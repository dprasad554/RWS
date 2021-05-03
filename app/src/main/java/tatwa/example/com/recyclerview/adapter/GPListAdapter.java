package tatwa.example.com.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import tatwa.example.com.recyclerview.PendingListActivity;
import tatwa.example.com.recyclerview.R;
import tatwa.example.com.recyclerview.RegisteredListActivity;
import tatwa.example.com.recyclerview.SolvedListActivity;
import tatwa.example.com.recyclerview.VillageListActivity;
import tatwa.example.com.recyclerview.adapter.dataholder.DistrictData;

public class GPListAdapter  extends RecyclerView.Adapter<GPListAdapter.NewProductViewHolder>{

    private Context mCtx;

    private List<DistrictData> newproductList = Collections.emptyList();

    public GPListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void setGPList(List<DistrictData> newsList) {
        this.newproductList = newsList;
        Log.d("array size", newproductList.size() + "");
        notifyDataSetChanged();
        notifyItemRangeChanged(-1, newsList.size());
    }

    @Override
    public GPListAdapter.NewProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.report_list_item_row, null);
        return new GPListAdapter.NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GPListAdapter.NewProductViewHolder holder, int position) {

        DistrictData product = newproductList.get(position);
        holder.slNo.setText(product.slNo);
        holder.districtName.setText(product.districtName);
        holder.registered.setText(product.complaintRegistered);
        holder.solved.setText(product.complaintSolved);
        int value = Integer.parseInt(product.complaintPending) /*+ Integer.parseInt(product.inProgress)*/;
        holder.pending.setText(String.valueOf(value));
    }

    @Override
    public int getItemCount() {

        return newproductList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView slNo, districtName, registered, solved, pending;
        RelativeLayout main_layout;

        public NewProductViewHolder(View itemView) {
            super(itemView);

            slNo = (TextView) itemView.findViewById(R.id.tv_slNo);
            districtName = (TextView) itemView.findViewById(R.id.tv_district);
            registered = (TextView) itemView.findViewById(R.id.tv_registered);
            solved = (TextView) itemView.findViewById(R.id.tv_solved);
            pending = (TextView) itemView.findViewById(R.id.tv_pending);
            main_layout = (RelativeLayout) itemView.findViewById(R.id.rl_main);
            main_layout.setOnClickListener(this);
            registered.setOnClickListener(this);
            solved.setOnClickListener(this);
            pending.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rl_main) {
                mCtx.startActivity(new Intent(mCtx, VillageListActivity.class)
                        .putExtra("Header", newproductList.get(getAdapterPosition()).districtName)
                        .putExtra("gp_id", newproductList.get(getAdapterPosition()).districtId)
                        .putExtra("village_id", "")
                        .putExtra("block_id", "")
                        .putExtra("district_id", ""));
            }else if(v.getId() == R.id.tv_registered) {
                mCtx.startActivity(new Intent(mCtx, RegisteredListActivity.class)
                        .putExtra("Header", newproductList.get(getAdapterPosition()).districtName)
                        .putExtra("gp_id", newproductList.get(getAdapterPosition()).districtId)
                        .putExtra("village_id", "")
                        .putExtra("block_id", "")
                        .putExtra("district_id", ""));
            }else if(v.getId() == R.id.tv_solved) {
                mCtx.startActivity(new Intent(mCtx, SolvedListActivity.class)
                        .putExtra("Header", newproductList.get(getAdapterPosition()).districtName)
                        .putExtra("gp_id", newproductList.get(getAdapterPosition()).districtId)
                        .putExtra("village_id", "")
                        .putExtra("block_id", "")
                        .putExtra("district_id", ""));
            }else if(v.getId() == R.id.tv_pending) {
                mCtx.startActivity(new Intent(mCtx, PendingListActivity.class)
                        .putExtra("Header", newproductList.get(getAdapterPosition()).districtName)
                        .putExtra("gp_id", newproductList.get(getAdapterPosition()).districtId)
                        .putExtra("village_id", "")
                        .putExtra("block_id", "")
                        .putExtra("district_id", ""));
            }
        }
    }
}

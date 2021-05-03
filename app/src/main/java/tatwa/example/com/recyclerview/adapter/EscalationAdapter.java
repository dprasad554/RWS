package tatwa.example.com.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import tatwa.example.com.recyclerview.R;
import tatwa.example.com.recyclerview.adapter.dataholder.EscalationData;

public class EscalationAdapter extends RecyclerView.Adapter<EscalationAdapter.NewProductViewHolder>{
    private Context mCtx;

    private List<EscalationData> newproductList = Collections.emptyList(); ;

    public EscalationAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void setEscalationList(List<EscalationData> newsList) {
        this.newproductList = newsList;
        Log.d("array size", newproductList.size() + "");
        notifyDataSetChanged();
        notifyItemRangeChanged(-1, newsList.size());
    }

    @Override
    public EscalationAdapter.NewProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.escalation_list_row, null);
        return new EscalationAdapter.NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EscalationAdapter.NewProductViewHolder holder, int position) {

        EscalationData product = newproductList.get(position);
        holder.intimatedDate.setText(product.intimatedDate);
        holder.nextEscalationDate.setText(product.nextEscalationDate);
        holder.functionaryName.setText(product.functionaryName);
        holder.designationName.setText(product.designationName);
        holder.mobile.setText(product.mobile);
        holder.email.setText(product.email);
    }

    @Override
    public int getItemCount() {
        return newproductList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView intimatedDate, nextEscalationDate, functionaryName, designationName, mobile, email;

        public NewProductViewHolder(View itemView) {
            super(itemView);

            intimatedDate = (TextView) itemView.findViewById(R.id.tv_intimationdate);
            nextEscalationDate = (TextView) itemView.findViewById(R.id.tv_escalationdate);
            functionaryName = (TextView) itemView.findViewById(R.id.tv_functionary);
            designationName = (TextView) itemView.findViewById(R.id.tv_designation);
            mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            email = (TextView) itemView.findViewById(R.id.tv_email);
        }

        @Override
        public void onClick(View v) {
        }
    }
}

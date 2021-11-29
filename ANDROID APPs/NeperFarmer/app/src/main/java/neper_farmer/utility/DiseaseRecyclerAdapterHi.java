package sih.cvrce.neper_farmer.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sih.cvrce.neper_farmer.DiseaseDetail;
import sih.cvrce.neper_farmer.R;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfo;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfoHi;

public class DiseaseRecyclerAdapterHi extends RecyclerView.Adapter<DiseaseRecyclerAdapterHi.ViewHolder>{

    private List<ApiAllDeseaseInfoHi> dList;
    private Context context;

    public DiseaseRecyclerAdapterHi(List<ApiAllDeseaseInfoHi> dList) {
        this.dList = dList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dView = inflater.inflate(R.layout.recycler_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(dView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ApiAllDeseaseInfoHi aadi = dList.get(i);
        viewHolder.txtDname.setText(aadi.name_hi);
        viewHolder.txtDname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseDetail.class);
                intent.putExtra(Tools.INTENT_EXTRA_DISEASE_ID, aadi.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDname;

        public ViewHolder(View itemView){
            super(itemView);

            txtDname = itemView.findViewById(R.id.disease_name);
        }
    }
}
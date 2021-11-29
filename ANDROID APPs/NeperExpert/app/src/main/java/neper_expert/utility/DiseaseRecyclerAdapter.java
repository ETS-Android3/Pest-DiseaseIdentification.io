package sih.cvrce.neper_expert.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import sih.cvrce.neper_expert.DiseaseClassify;
import sih.cvrce.neper_expert.R;
import sih.cvrce.neper_expert.api.Api;
import sih.cvrce.neper_expert.model.CropUncategorizedList;

public class DiseaseRecyclerAdapter extends RecyclerView.Adapter<DiseaseRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<CropUncategorizedList> dList;

    public DiseaseRecyclerAdapter(List<CropUncategorizedList> dList) {
        this.dList = dList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dView = inflater.inflate(R.layout.disease_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(dView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final CropUncategorizedList aadi = dList.get(i);
        viewHolder.txtDname.setText(aadi.crop);
        Glide.with(context)
                .load(Api.UPLOADED_IMAGE_FOLDER+aadi.image_id)
                .into(viewHolder.imageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseClassify.class);
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, aadi.image_id);
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
        public ImageView imageView;

        public ViewHolder(final View itemView){
            super(itemView);

            txtDname = itemView.findViewById(R.id.txt_crop_type);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
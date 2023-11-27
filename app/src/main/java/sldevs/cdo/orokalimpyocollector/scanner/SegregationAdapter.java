package sldevs.cdo.orokalimpyocollector.scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.GlideApp;
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.data_fetch.Segregated_Contributions;
import sldevs.cdo.orokalimpyocollector.records.ContributionAdapter;
import sldevs.cdo.orokalimpyocollector.records.update_contribution;

public class SegregationAdapter extends RecyclerView.Adapter<SegregationAdapter.SegregationHolder> {
    StorageReference storageReference;

    ImageView ivQR;
    Context context;
    Activity activity;
    ArrayList<Segregated_Contributions> segregated_contributionsArrayList;
    Segregated_Contributions segregated_contributions;

    public SegregationAdapter(Activity activity,Context context, ArrayList<Segregated_Contributions> segregated_contributionsArrayList) {
        this.activity = activity;
        this.context = context;
        this.segregated_contributionsArrayList = segregated_contributionsArrayList;
    }

    @NonNull
    @Override
    public SegregationAdapter.SegregationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.segregated_item_layout,parent,false);
        return new SegregationAdapter.SegregationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SegregationAdapter.SegregationHolder holder, int position) {

        segregated_contributions = segregated_contributionsArrayList.get(position);

        holder.tvSegregatedId.setText("Contribution ID: \n" +segregated_contributions.segregation_id);
        holder.tvDateTime.setText("Date and Time: \n" +segregated_contributions.date + " " + segregated_contributions.time);
        holder.tvWasteType.setText("Waste Type: " +segregated_contributions.waste_type);
        if(segregated_contributions.waste_type.equalsIgnoreCase("Plastic Waste")){
            holder.tvPlasticType.setVisibility(View.VISIBLE);
            holder.tvPlasticName.setVisibility(View.VISIBLE);
            holder.tvPlasticType.setText("Plastic Type: " + segregated_contributions.plastic_type);
            holder.tvPlasticName.setText("Plastic Name: " + segregated_contributions.plastic_name);
            holder.tvBrand.setText("Brand: " + segregated_contributions.brand);
            holder.tvTotalKilo.setText("Kilo: " + segregated_contributions.kilo);
        }else{
            holder.tvPlasticType.setVisibility(View.GONE);
            holder.tvPlasticName.setVisibility(View.GONE);
            holder.tvBrand.setText("Brand: " + segregated_contributions.brand);
            holder.tvTotalKilo.setText("Kilo: " + segregated_contributions.kilo);
        }


//
//        storageReference = FirebaseStorage.getInstance().getReference("Waste Contribution Proof/").child(scanned_contributions.contribution_id+".png");
//        GlideApp.with(context).load(storageReference).into(ivQR);


    }

    @Override
    public int getItemCount() {
        return segregated_contributionsArrayList.size();
    }

    public class SegregationHolder extends RecyclerView.ViewHolder {

        TextView tvSegregatedId;
        TextView tvConsolidatorId;
        TextView tvConsolidatorName;

        TextView tvWasteType;
        TextView tvPlasticType;
        TextView tvPlasticName;

        TextView tvBrand;

        TextView tvDateTime;

        TextView tvTotalKilo;

        CardView cardView;


        public SegregationHolder(@NonNull View itemView) {
            super(itemView);

            tvSegregatedId = itemView.findViewById(R.id.tvID);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvWasteType = itemView.findViewById(R.id.tvWasteType);
            tvPlasticType = itemView.findViewById(R.id.tvPlasticType);
            tvPlasticName = itemView.findViewById(R.id.tvPlasticName);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvTotalKilo = itemView.findViewById(R.id.tvKilo);
            ivQR = itemView.findViewById(R.id.ivQR);

            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, update_contribution.class);
                    intent.putExtra("segregated_id",segregated_contributions.segregation_id);
                    intent.putExtra("consolidator_id",segregated_contributions.consolidator_id);
                    intent.putExtra("brand",String.valueOf(segregated_contributions.brand));
                    intent.putExtra("plastic_type",segregated_contributions.waste_type);
                    intent.putExtra("plastic_name",segregated_contributions.waste_type);
                    intent.putExtra("waste",segregated_contributions.date);
                    intent.putExtra("kilo",segregated_contributions.kilo);
                    context.startActivity(intent);
                    activity.finish();

                }
            });

        }
    }


}
package sldevs.cdo.orokalimpyocollector.scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.GlideApp;
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.data_fetch.Segregated_Contributions;
import sldevs.cdo.orokalimpyocollector.records.ContributionAdapter;
import sldevs.cdo.orokalimpyocollector.records.update_contribution;

public class SegregationAdapter extends FirestoreRecyclerAdapter<Segregated_Contributions, SegregationAdapter.SegregationHolder> {
    StorageReference storageReference;

    ImageView ivQR;
    Context context;
    Activity activity;
    FirestoreRecyclerOptions<Segregated_Contributions> segregated_contributionsArrayList;
    Segregated_Contributions segregated_contributions;
    private String searchQuery = "";

    public SegregationAdapter(Activity activity,Context context, FirestoreRecyclerOptions<Segregated_Contributions> segregated_contributionsArrayList,String searchQuery) {
        super(segregated_contributionsArrayList);
        this.activity = activity;
        this.context = context;
        this.segregated_contributionsArrayList = segregated_contributionsArrayList;
        this.searchQuery = searchQuery;
    }
    public void updateOptions(@NonNull FirestoreRecyclerOptions<Segregated_Contributions> options) {
        this.searchQuery = "";  // Reset search query when updating options
        super.updateOptions(options);
    }
    public void updateSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SegregationAdapter.SegregationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.segregated_item_layout,parent,false);
        return new SegregationAdapter.SegregationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SegregationHolder holder, int position, @NonNull Segregated_Contributions model) {

        Segregated_Contributions segregatedContributions = segregated_contributionsArrayList.getSnapshots().get(position);

        if (model != null) {
            // Check if the model's field is not null
            String modelField = model.getWaste_type();
            if (modelField != null) {
                if (searchQuery == null || searchQuery.isEmpty() || modelField.toLowerCase().contains(searchQuery.toLowerCase())) {
                    holder.tvSegregatedId.setText("Contribution ID: \n" +segregatedContributions.getSegregation_id());
                    holder.tvDateTime.setText("Date and Time: \n" +segregatedContributions.getDate() + " " + segregatedContributions.getTime());
                    holder.tvWasteType.setText("Waste Type: " +segregatedContributions.getWaste_type());
                    if(segregatedContributions.getWaste_type().equalsIgnoreCase("Plastic Waste")){
                        holder.tvPlasticType.setVisibility(View.VISIBLE);
                        holder.tvPlasticName.setVisibility(View.VISIBLE);
                        holder.tvPlasticType.setText("Plastic Type: " + segregatedContributions.getPlastic_type());
                        holder.tvPlasticName.setText("Plastic Name: " + segregatedContributions.getPlastic_name());
                        holder.tvBrand.setText("Brand: " + segregatedContributions.getBrand());
                        holder.tvTotalKilo.setText("Kilo: " + segregatedContributions.getKilo());
                    }else{
                        holder.tvPlasticType.setVisibility(View.GONE);
                        holder.tvPlasticName.setVisibility(View.GONE);
                        holder.tvBrand.setText("Brand: " + segregatedContributions.getBrand());
                        holder.tvTotalKilo.setText("Kilo: " + segregatedContributions.getKilo());
                    }
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    return; // Exit the method after binding data
                }
            }
        }

        // If any condition fails or model is null, hide the ViewHolder
        holder.itemView.setVisibility(View.GONE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));





//
//        storageReference = FirebaseStorage.getInstance().getReference("Waste Contribution Proof/").child(scanned_contributions.contribution_id+".png");
//        GlideApp.with(context).load(storageReference).into(ivQR);


    }

    @Override
    public int getItemCount() {
        return segregated_contributionsArrayList.getSnapshots().size();
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

        LinearLayout cardView;


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

//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, update_contribution.class);
//                    intent.putExtra("segregated_id",segregated_contributions.segregation_id);
//                    intent.putExtra("consolidator_id",segregated_contributions.consolidator_id);
//                    intent.putExtra("brand",String.valueOf(segregated_contributions.brand));
//                    intent.putExtra("plastic_type",segregated_contributions.waste_type);
//                    intent.putExtra("plastic_name",segregated_contributions.waste_type);
//                    intent.putExtra("waste",segregated_contributions.date);
//                    intent.putExtra("kilo",segregated_contributions.kilo);
//                    context.startActivity(intent);
//                    activity.finish();
//
//                }
//            });

        }
    }


}
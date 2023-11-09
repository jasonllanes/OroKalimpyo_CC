package sldevs.cdo.orokalimpyocollector.records;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.GlideApp;
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;

public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.ContributionHolder> {
    StorageReference storageReference;

    ImageView ivQR;
    Context context;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;

    public ContributionAdapter(Context context, ArrayList<Scanned_Contributions> scanned_contributionsArrayList) {
        this.context = context;
        this.scanned_contributionsArrayList = scanned_contributionsArrayList;
    }

    @NonNull
    @Override
    public ContributionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ContributionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributionHolder holder, int position) {

        Scanned_Contributions scanned_contributions = scanned_contributionsArrayList.get(position);

        holder.tvContributionId.setText("Contribution ID: \n" +scanned_contributions.contribution_id);
        holder.tvDateTime.setText("Date and Time: \n" +scanned_contributions.date + " " + scanned_contributions.time);
        holder.tvName.setText("Name: " + scanned_contributions.name);
        holder.tvBarangay.setText("Barangay: " + scanned_contributions.barangay);
        holder.tvWasteType.setText("Waste Type: " +scanned_contributions.waste_type);
        holder.tvTotalKilo.setText("Kilo: " + scanned_contributions.kilo);


        storageReference = FirebaseStorage.getInstance().getReference("Waste Contribution Proof/").child(scanned_contributions.contribution_id+".png");
        GlideApp.with(context).load(storageReference).into(ivQR);


    }

    @Override
    public int getItemCount() {
        return scanned_contributionsArrayList.size();
    }

    public class ContributionHolder extends RecyclerView.ViewHolder {

        TextView tvContributionId;

        TextView tvDateTime;
        TextView tvName;

        TextView tvBarangay;

        TextView tvWasteType;

        TextView tvTotalKilo;


        public ContributionHolder(@NonNull View itemView) {
            super(itemView);

            tvContributionId = itemView.findViewById(R.id.tvID);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvBarangay = itemView.findViewById(R.id.tvBarangay);
            tvWasteType = itemView.findViewById(R.id.tvWasteType);
            tvTotalKilo = itemView.findViewById(R.id.tvKilo);
            ivQR = itemView.findViewById(R.id.ivQR);


        }
    }


}

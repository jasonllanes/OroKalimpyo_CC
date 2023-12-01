package sldevs.cdo.orokalimpyocollector.records;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    Activity activity;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;
    Scanned_Contributions scanned_contributions;

    public ContributionAdapter(Activity activity,Context context, ArrayList<Scanned_Contributions> scanned_contributionsArrayList) {
        this.activity = activity;
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

        scanned_contributions = scanned_contributionsArrayList.get(position);

        holder.tvContributionId.setText("Contribution ID: \n" + scanned_contributions.contribution_id);
        holder.tvGainedPoints.setText("Gained Points: " + String.valueOf(scanned_contributions.current_points));
        holder.tvUserId.setText("User ID: " + scanned_contributions.user_id);
        holder.tvDateTime.setText("Date and Time: \n" + scanned_contributions.date + " " + scanned_contributions.time);
        holder.tvName.setText("Name: " + scanned_contributions.name);
        holder.tvBarangay.setText("Barangay: " + scanned_contributions.barangay);
        holder.tvWasteType.setText("Waste Type: " + scanned_contributions.waste_type);
        holder.tvTotalKilo.setText("Total Kilo: " + scanned_contributions.kilo);


        storageReference = FirebaseStorage.getInstance().getReference("Waste Contribution Proof/").child(scanned_contributions.contribution_id+".png");
        GlideApp.with(context).load(storageReference).into(holder.ivQR);


        String kilo = scanned_contributions.kilo;
        String waste_type = scanned_contributions.waste_type;
        String user_id = scanned_contributions.user_id;
        String contribution_id = scanned_contributions.contribution_id;
        String gained_points = String.valueOf(scanned_contributions.current_points);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,update_contribution.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("contribution_id",contribution_id);
                intent.putExtra("gained_points",gained_points);
                intent.putExtra("waste",waste_type);
                intent.putExtra("kilo",kilo);
                context.startActivity(intent);
                activity.finish();

            }
        });
        holder.ivQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.image_pop);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                ImageView ivProof = dialog.findViewById(R.id.ivImage);
                ivProof.setImageDrawable(holder.ivQR.getDrawable());
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return scanned_contributionsArrayList.size();
    }

    public class ContributionHolder extends RecyclerView.ViewHolder {

        TextView tvContributionId;

        TextView tvDateTime;
        TextView tvName;
        TextView tvUserId;

        TextView tvBarangay;
        TextView tvGainedPoints;

        TextView tvWasteType;

        TextView tvTotalKilo;
        ImageView ivQR;

        CardView cardView;


        public ContributionHolder(@NonNull View itemView) {
            super(itemView);

            tvContributionId = itemView.findViewById(R.id.tvID);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvGainedPoints = itemView.findViewById(R.id.tvGainedPoints);
            tvBarangay = itemView.findViewById(R.id.tvBarangay);
            tvWasteType = itemView.findViewById(R.id.tvWasteType);
            tvTotalKilo = itemView.findViewById(R.id.tvKilo);
//            ivProof = itemView.findViewById(R.id.ivProof);
            ivQR = itemView.findViewById(R.id.ivQR);

            cardView = itemView.findViewById(R.id.cardView);



        }
    }


}

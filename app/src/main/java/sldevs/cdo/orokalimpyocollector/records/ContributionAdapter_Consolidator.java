package sldevs.cdo.orokalimpyocollector.records;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;
import sldevs.cdo.orokalimpyocollector.GlideApp;
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.scanner.view_collected_contributions;

public class ContributionAdapter_Consolidator extends RecyclerView.Adapter<ContributionAdapter_Consolidator.ContributionHolder> {
    StorageReference storageReference;

    ImageView ivQR;
    Context context;
    Activity activity;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;
    Scanned_Contributions scanned_contributions;

    String consolidator_name;

    public ContributionAdapter_Consolidator(Activity activity, Context context, String consolidator_name, ArrayList<Scanned_Contributions> scanned_contributionsArrayList) {
        this.activity = activity;
        this.context = context;
        this.scanned_contributionsArrayList = scanned_contributionsArrayList;
        this.consolidator_name = consolidator_name;
    }

    @NonNull
    @Override
    public ContributionAdapter_Consolidator.ContributionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ContributionAdapter_Consolidator.ContributionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributionAdapter_Consolidator.ContributionHolder holder, int position) {

        scanned_contributions = scanned_contributionsArrayList.get(position);

        holder.tvContributionId.setText("Contribution ID: \n" + scanned_contributions.contribution_id);
        holder.tvGainedPoints.setText("Gained Points: " + String.valueOf(scanned_contributions.current_points));
        holder.tvUserId.setText("User ID: " + scanned_contributions.user_id);
        holder.tvDateTime.setText("Date and Time: \n" + scanned_contributions.date + " " + scanned_contributions.time);
        holder.tvName.setText("Name: " + scanned_contributions.name);
        holder.tvBarangay.setText("Barangay: " + scanned_contributions.barangay);
        holder.tvWasteType.setText("Waste Type: " + scanned_contributions.waste_type);
        holder.tvTotalKilo.setText("Total Kilo: " + scanned_contributions.kilo);

        String contribution_id = scanned_contributions.contribution_id;

        storageReference = FirebaseStorage.getInstance().getReference("Waste Contribution Proof/").child(scanned_contributions.contribution_id + ".png");
        GlideApp.with(context).load(storageReference).into(ivQR);


        if(consolidator_name.equalsIgnoreCase(" ")){

        }else{
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new CuteDialog.withIcon(context)
                            .setIcon(R.drawable.edit)
                            .setTitle("Redeem Reward").setTitleTextColor(R.color.green)
                            .setDescription("Are you sure you want to redeem this?").setPositiveButtonColor(R.color.green)
                            .setPositiveButtonText("Yes", v2 -> {
                                db = FirebaseFirestore.getInstance();
                                mAuth = FirebaseAuth.getInstance();
                                DocumentReference contributionRef = db.collection("Waste Contribution").document(contribution_id);
                                contributionRef.update("status", "Waste Consolidated").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        contributionRef.update("consolidator_name", consolidator_name);
                                        contributionRef.update("consolidator_id", mAuth.getUid());
                                        Intent i = new Intent(context, view_collected_contributions.class);
                                        context.startActivity(i);
                                        activity.finish();
                                        Toast.makeText(context, "Successfully Consolidated", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            })
                            .setNegativeButtonText("No", v2 -> {
                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                    return false;
                }
            });
        }


        ivQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.image_pop);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                ImageView ivProof = dialog.findViewById(R.id.ivImage);
                ivProof.setImageDrawable(ivQR.getDrawable());
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
        ImageView ivProof;

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
            ivProof = itemView.findViewById(R.id.ivProof);
            ivQR = itemView.findViewById(R.id.ivQR);

            cardView = itemView.findViewById(R.id.cardView);


        }
    }


}

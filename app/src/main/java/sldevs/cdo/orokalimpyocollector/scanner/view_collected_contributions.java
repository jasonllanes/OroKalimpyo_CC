package sldevs.cdo.orokalimpyocollector.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.records.ContributionAdapter;
import sldevs.cdo.orokalimpyocollector.records.scanned_contributions;

public class view_collected_contributions extends AppCompatActivity implements View.OnClickListener {

    FirestoreRecyclerAdapter listAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public TextView tvID,tvDate;
    ImageView ivBack;
    RecyclerView recyclerView;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;
    firebase_crud fc;
    FirebaseAuth mAuth;
    StorageReference storageReference;

    LinearLayout llEmpty;
    Button btnUpdate;
    String collector_id,consolidator_name;
    ProgressBar pbLoading;
    TextView tvLoading;
    ContributionAdapter adapter;
    FirebaseFirestore db;

    LinearLayout linearLayout;

    int size ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collected_contributions);

        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();

        llEmpty = findViewById(R.id.llEmpty);
        linearLayout = findViewById(R.id.mainLayout);

        ivBack = findViewById(R.id.ivBack);

        pbLoading = findViewById(R.id.pbLoading);
        tvLoading = findViewById(R.id.tvLoading);
        btnUpdate = findViewById(R.id.btnUpdate);

        ivBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        //        ivBack = findViewById(R.id.ivBack);
//        ivBack.setOnClickListener(this);

        collector_id = getIntent().getStringExtra("collector_id");
        consolidator_name = getIntent().getStringExtra("consolidator_name");


        recyclerView = findViewById(R.id.lvContributions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        db = FirebaseFirestore.getInstance();
        scanned_contributionsArrayList = new ArrayList<Scanned_Contributions>();

        adapter = new ContributionAdapter(view_collected_contributions.this,scanned_contributionsArrayList);


        recyclerView.setAdapter(adapter);


        EventChangeListener();



    }

    public void EventChangeListener(){
        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        db.collection("Waste Contribution").whereEqualTo("status","Waste Collected").whereEqualTo("collector_id",collector_id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        size = value.size();
                        if(error != null){

                            Log.e("Firestore error!",error.getMessage());
                            return;


                        }

                        if(value.size() == 0){
                            llEmpty.setVisibility(View.VISIBLE);
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){

                                scanned_contributionsArrayList.add(dc.getDocument().toObject(Scanned_Contributions.class));
                            }else if(dc.getType() == DocumentChange.Type.MODIFIED){
                                scanned_contributionsArrayList.remove(dc.getDocument().toObject(Scanned_Contributions.class));
                            }

                            adapter.notifyDataSetChanged();

                        }
                        tvLoading.setVisibility(View.GONE);
                        pbLoading.setVisibility(View.GONE);
                        btnUpdate.setVisibility(View.VISIBLE);


                    }
                });

    }



    public void updateAll(){

        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        CollectionReference contributionsRef = db.collection("Waste Contribution");
        contributionsRef.whereEqualTo("status", "Waste Collected")
                .whereEqualTo("collector_id", collector_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    WriteBatch batch = db.batch();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String contributionId = document.getString("contribution_id");
                        DocumentReference documentRef = contributionsRef.document(contributionId);
                        batch.update(documentRef, "status", "Waste Consolidated");
                        batch.update(documentRef, "consolidator_name", consolidator_name);
                        batch.update(documentRef, "consolidator_id", mAuth.getUid());
                        scanned_contributionsArrayList.clear();

                    }

                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                // Handle successful update
                                tvLoading.setVisibility(View.GONE);
                                pbLoading.setVisibility(View.GONE);
                                btnUpdate.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Successfully Updated!", Snackbar.LENGTH_LONG)
                                        .setTextColor(getResources().getColor(R.color.white))
                                        .setBackgroundTint(getResources().getColor(R.color.green));
                                snackbar.show();
                                Intent i = new Intent(view_collected_contributions.this, consolidator_scanner.class);
                                startActivity(i);
                                finish();


                            })
                            .addOnFailureListener(e -> {
                                // Handle update failure
                            });

                })
                .addOnFailureListener(e -> {
                    // Handle query failure

                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Something went wrong!", Snackbar.LENGTH_LONG)
                            .setTextColor(getResources().getColor(R.color.white))
                            .setBackgroundTint(getResources().getColor(R.color.green));
                    snackbar.show();
                });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnUpdate){
            updateAll();
        } else if (id == R.id.ivBack) {
            finish();
        }
    }
}
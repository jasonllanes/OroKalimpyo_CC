package sldevs.cdo.orokalimpyocollector.scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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

public class view_segregated_contributions extends AppCompatActivity implements View.OnClickListener {

    FirestoreRecyclerAdapter listAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public TextView tvID,tvDate;
    RecyclerView recyclerView;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;
    ImageView ivBack;
    firebase_crud fc;
    FirebaseAuth mAuth;
    StorageReference storageReference;

    String collector_id;
    ProgressBar pbLoading;
    TextView tvLoading;
    ContributionAdapter adapter;
    FirebaseFirestore db;

    LinearLayout linearLayout;
    LinearLayout llEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_segregated_contributions);

        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();

        llEmpty = findViewById(R.id.llEmpty);
        linearLayout = findViewById(R.id.mainLayout);

        ivBack = findViewById(R.id.ivBack);
        pbLoading = findViewById(R.id.pbLoading);
        tvLoading = findViewById(R.id.tvLoading);



        //        ivBack = findViewById(R.id.ivBack);
//        ivBack.setOnClickListener(this);

        collector_id = getIntent().getStringExtra("collector_id");

        recyclerView = findViewById(R.id.lvContributions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        db = FirebaseFirestore.getInstance();
        scanned_contributionsArrayList = new ArrayList<Scanned_Contributions>();

        adapter = new ContributionAdapter(view_segregated_contributions.this,scanned_contributionsArrayList);


        recyclerView.setAdapter(adapter);


        EventChangeListener();


        ivBack.setOnClickListener(this);

//        Query query = waste_contribution_ref.orderBy("user_id",Query.Direction.DESCENDING);
//        FirestoreRecyclerOptions<Scanned_Contributions> options = new FirestoreRecyclerOptions.Builder<Scanned_Contributions>()
//                .setQuery(query, Scanned_Contributions.class)
//                .build();






    }

    public void EventChangeListener(){
        tvLoading.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        db.collection("Waste Contribution").whereEqualTo("status","Waste Segregated")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

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
                            }

                            adapter.notifyDataSetChanged();

                        }
                        tvLoading.setVisibility(View.GONE);
                        pbLoading.setVisibility(View.GONE);

                    }
                });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ivBack){
            finish();
        }
    }
}
package sldevs.cdo.orokalimpyocollector.scanner;

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
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.data_fetch.Segregated_Contributions;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.functions.other_functions;
import sldevs.cdo.orokalimpyocollector.home.consolidator_home;
import sldevs.cdo.orokalimpyocollector.records.ContributionAdapter;

public class view_segregated_contributions extends AppCompatActivity implements View.OnClickListener {

    FirestoreRecyclerAdapter listAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public TextView tvID,tvDate;
    RecyclerView recyclerView;
    ArrayList<Segregated_Contributions> segregationAdapterArrayList;
    ImageView ivBack;
    firebase_crud fc;
    FirebaseAuth mAuth;
    StorageReference storageReference;

    String collector_id,searchQuery;
    ProgressBar pbLoading;
    TextView tvLoading;
    SegregationAdapter adapter;
    FirebaseFirestore db;
    other_functions of;
    MaterialSpinner sSort;
    SearchView searchView;
    LinearLayout linearLayout,llEmpty;

    Query query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_segregated_contributions);

        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();
        of = new other_functions();


        ivBack = findViewById(R.id.ivBack);
        pbLoading = findViewById(R.id.pbLoading);
        tvLoading = findViewById(R.id.tvLoading);
        llEmpty = findViewById(R.id.llEmpty);
//        sSort = findViewById(R.id.sSort);
        searchView = findViewById(R.id.searchView);

//        sSort.setItems(of.sortSegregationList());


        collector_id = getIntent().getStringExtra("collector_id");


        db = FirebaseFirestore.getInstance();


        query = db.collection("Segregated Waste").whereEqualTo("consolidator_id",mAuth.getUid()).orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Segregated_Contributions> options = new FirestoreRecyclerOptions.Builder<Segregated_Contributions>()
                .setQuery(query, Segregated_Contributions.class)
                .build();

        adapter = new SegregationAdapter(view_segregated_contributions.this, view_segregated_contributions.this, options, searchQuery);
        segregationAdapterArrayList = new ArrayList<Segregated_Contributions>();



        recyclerView = findViewById(R.id.lvContributions);
        recyclerView.setLayoutManager(new LinearLayoutManager(view_segregated_contributions.this));
        recyclerView.setAdapter(adapter);


//        sSort.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                if(view.getText().toString().equalsIgnoreCase("Waste Type")){
//                    query = db.collection("Segregated Waste").whereEqualTo("consolidator_id",mAuth.getUid()).orderBy("waste_type", Query.Direction.DESCENDING).orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
//                    FirestoreRecyclerOptions<Segregated_Contributions> options = new FirestoreRecyclerOptions.Builder<Segregated_Contributions>()
//                            .setQuery(query, Segregated_Contributions.class)
//                            .build();
//
//                    adapter = new SegregationAdapter(view_segregated_contributions.this, view_segregated_contributions.this, options, searchQuery);
//                    segregationAdapterArrayList = new ArrayList<Segregated_Contributions>();
//
//
//                    recyclerView = findViewById(R.id.lvContributions);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(view_segregated_contributions.this));
//                    recyclerView.setAdapter(adapter);
//                } else if (view.getText().toString().equalsIgnoreCase("Plastic Type")) {
//                    query = db.collection("Segregated Waste").whereEqualTo("consolidator_id",mAuth.getUid()).orderBy("plastic_type", Query.Direction.DESCENDING).orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
//                    FirestoreRecyclerOptions<Segregated_Contributions> options = new FirestoreRecyclerOptions.Builder<Segregated_Contributions>()
//                            .setQuery(query, Segregated_Contributions.class)
//                            .build();
//
//                    adapter = new SegregationAdapter(view_segregated_contributions.this, view_segregated_contributions.this, options, searchQuery);
//                    segregationAdapterArrayList = new ArrayList<Segregated_Contributions>();
//
//
//                    recyclerView = findViewById(R.id.lvContributions);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(view_segregated_contributions.this));
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//        });








        ivBack.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                // Update the Firestore query based on the search input
                Query newQuery = db.collection("Segregated Waste").whereEqualTo("consolidator_id",mAuth.getUid()).orderBy("date", Query.Direction.DESCENDING).orderBy("time", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Segregated_Contributions> newOptions =
                        new FirestoreRecyclerOptions.Builder<Segregated_Contributions>()
                                .setQuery(newQuery, Segregated_Contributions.class)
                                .build();

                adapter.updateSearchQuery(newText);

                return true;
            }
        });




    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.ivBack){
            Intent i = new Intent(view_segregated_contributions.this, consolidator_home.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(view_segregated_contributions.this, consolidator_home.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}
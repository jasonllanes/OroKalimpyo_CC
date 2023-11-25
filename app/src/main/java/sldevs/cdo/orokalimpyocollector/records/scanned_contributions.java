package sldevs.cdo.orokalimpyocollector.records;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.data_fetch.Scanned_Contributions;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class scanned_contributions extends AppCompatActivity implements View.OnClickListener {

    FirestoreRecyclerAdapter listAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public TextView tvID,tvDate;
    RecyclerView recyclerView;
    ArrayList<Scanned_Contributions> scanned_contributionsArrayList;
    ImageView ivBack;
    firebase_crud fc;
    FirebaseAuth mAuth;

    LinearLayout llEmpty;

    StorageReference storageReference;

    String user_type,user_name,user_id;
    ContributionAdapter adapter;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_contributions);


        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();


        ivBack = findViewById(R.id.ivBack);
//        ivBack.setOnClickListener(this);
        llEmpty = findViewById(R.id.llEmpty);


        recyclerView = findViewById(R.id.lvContributions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        db = FirebaseFirestore.getInstance();
        scanned_contributionsArrayList = new ArrayList<Scanned_Contributions>();

        adapter = new ContributionAdapter(this,scanned_contributions.this,scanned_contributionsArrayList);


        recyclerView.setAdapter(adapter);


        EventChangeListener();

//        Query query = waste_contribution_ref.orderBy("user_id",Query.Direction.DESCENDING);
//        FirestoreRecyclerOptions<Scanned_Contributions> options = new FirestoreRecyclerOptions.Builder<Scanned_Contributions>()
//                .setQuery(query, Scanned_Contributions.class)
//                .build();



        ivBack.setOnClickListener(this);


    }

    public void EventChangeListener(){

        db.collection("Waste Contribution").whereEqualTo("status","Waste Collected").whereEqualTo("collector_id",mAuth.getUid())
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

                    }
                });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivBack) {
            finish();
        }
    }
}
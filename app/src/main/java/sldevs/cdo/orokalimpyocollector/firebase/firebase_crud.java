package sldevs.cdo.orokalimpyocollector.firebase;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


import sldevs.cdo.orokalimpyocollector.GlideApp;
import sldevs.cdo.orokalimpyocollector.authentication.log_in;
import sldevs.cdo.orokalimpyocollector.home.collector_home;
import sldevs.cdo.orokalimpyocollector.home.consolidator_home;
import sldevs.cdo.orokalimpyocollector.records.consolidator_add_record;

public class firebase_crud {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> waste_contribution;
    Map<String, Object> waste_segregation;
    String collector_name,collector_type;
    //Authentication Functions----------------------------

    //Log In Function
    public void logInCollector(Activity activity, Context context,String user_type, String email, String password, ProgressBar progressBar,Button btnLogIn){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Query query = db.collection(user_type).whereEqualTo("email",email);
                    AggregateQuery count = query.count();
                    count.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if(task.isSuccessful()){
                                AggregateQuerySnapshot snapshot = task.getResult();
                                if(snapshot.getCount() > 0){
                                    if(user_type.equalsIgnoreCase("Waste Collector")){
                                        Intent i = new Intent(context, collector_home.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                        activity.finish();
                                    }else if(user_type.equalsIgnoreCase("Waste Consolidator")){

                                        Intent i = new Intent(context, consolidator_home.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                        activity.finish();
                                    } else {
                                        if(user_type.equalsIgnoreCase("Waste Collector")){
                                            progressBar.setVisibility(View.GONE);
                                            btnLogIn.setVisibility(View.VISIBLE);
                                            Toast.makeText(context, "Make sure to use Waste Collector account.", Toast.LENGTH_SHORT).show();
                                        }else if(user_type.equalsIgnoreCase("Waste Consolidator")){
                                            progressBar.setVisibility(View.GONE);
                                            btnLogIn.setVisibility(View.VISIBLE);
                                            Toast.makeText(context, "Make sure to use Waste Consolidator account.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressBar.setVisibility(View.GONE);
                                            btnLogIn.setVisibility(View.VISIBLE);
                                            Toast.makeText(context, "Account doesn't exist.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else {
                                    Toast.makeText(context, "No result found!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnLogIn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

//                    Toast.makeText(context, "Maayong pag abot!", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(context, home.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
//                    context.startActivity(i);
//                    activity.finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    btnLogIn.setVisibility(View.VISIBLE);
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Sending Waste Contribution
    public void sendWasteContribution(Bitmap bitmap,Activity activity, Context context, String user_id, String user_type, String name, String household_type, String barangay,String establishment_type, String collector_name, String collector_type, String waste_type, String kilo, String month, String day, String year, String hour, String minutes, String seconds, String date,String time){
        String contribution_id = user_id.substring(0,5) +month+day+year+hour+minutes+seconds;
        waste_contribution = new HashMap<>();
        if(household_type.equalsIgnoreCase("Household")){
            waste_contribution.put("contribution_id",contribution_id);
            waste_contribution.put("user_id",user_id);
            waste_contribution.put("name", name);
            waste_contribution.put("user_type", user_type);
            waste_contribution.put("household_type", household_type);
            waste_contribution.put("barangay", barangay);
            waste_contribution.put("collector_id", mAuth.getUid());
            waste_contribution.put("collector_name", collector_name);
            waste_contribution.put("collector_type", collector_type);
            waste_contribution.put("status","Waste Collected");
            waste_contribution.put("waste_type",waste_type);
            waste_contribution.put("kilo",kilo);
            waste_contribution.put("date", date);
            waste_contribution.put("time", time);
            waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
        }else if(household_type.equalsIgnoreCase("Non-Household")){
            waste_contribution.put("contribution_id",contribution_id);
            waste_contribution.put("user_id",user_id);
            waste_contribution.put("name", name);
            waste_contribution.put("user_type", user_type);
            waste_contribution.put("household_type", household_type);
            waste_contribution.put("barangay", barangay);
            waste_contribution.put("establishment_type", establishment_type);
            waste_contribution.put("collector_id", mAuth.getUid());
            waste_contribution.put("collector_name", collector_name);
            waste_contribution.put("collector_type", collector_type);
            waste_contribution.put("status","Waste Collected");
            waste_contribution.put("waste_type",waste_type);
            waste_contribution.put("kilo",kilo);
            waste_contribution.put("date", date);
            waste_contribution.put("time", time);
            waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
        }
        db.collection("Waste Contribution").document(user_id.substring(0,5)+month+day+year+hour+minutes+seconds).set(waste_contribution).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte [] byteArray = stream.toByteArray();

                StorageReference ref = storageRef.child("Waste Contribution Proof/" +user_id.substring(0,5)+month+day+year+hour+minutes+seconds + ".png");

                ref.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        db.collection("Waste Generator").document(user_id).update("contributed_today","Yes");
                        Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, collector_home.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
                        context.startActivity(i);
                        activity.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Please try again...", Toast.LENGTH_SHORT).show();

//                Toast.makeText(final_sign_up.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //Sending Waste Segregation Data
    public void sendSegregatedWasteData(Activity activity,Context context,String segregation_id,String waste_type,String plastic_type,String plastic_name,String brand,String kilo){
        waste_segregation = new HashMap<>();

        if(waste_type.equalsIgnoreCase("Plastic Waste")){
            waste_segregation.put("segregation_id",segregation_id);
            waste_segregation.put("waste_type",waste_type);
            waste_segregation.put("plastic_type",plastic_type);
            waste_segregation.put("plastic_name",plastic_name);
            waste_segregation.put("brand",brand);
            waste_segregation.put("kilo",kilo);
        }else{
            waste_segregation.put("segregation_id",segregation_id);
            waste_segregation.put("waste_type",waste_type);
            waste_segregation.put("brand",brand);
            waste_segregation.put("kilo",kilo);
        }

        db.collection("Segregated Waste").document(segregation_id).set(waste_segregation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, consolidator_add_record.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                activity.finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });

    }

    //Fetch Waste Generator
    public void retrieveGeneratorProfile(Activity activity, Context context,)

    //Getting profile
    public void retrieveCollectorProfileAll(Activity activity, Context context,String id, TextView name, TextView collector_type,TextView contact_person,TextView number){
        DocumentReference docRef = db.collection("Waste Collector").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("name").toString());
                    collector_type.setText(document.get("collector_type").toString());
                    contact_person.setText(document.get("contact_person").toString());
                    number.setText(document.get("number").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void retrieveConsolidatorProfileAll(Activity activity, Context context,String id, TextView name, TextView collector_type,TextView contact_person,TextView number){
        DocumentReference docRef = db.collection("Waste Consolidator").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("name").toString());
                    collector_type.setText(document.get("consolidator_type").toString());
                    contact_person.setText(document.get("contact_person").toString());
                    number.setText(document.get("number").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void retrieveCollectorProfile(Activity activity, Context context,String id, TextView name, TextView collector_type){
        DocumentReference docRef = db.collection("Waste Collector").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                    } else {

                    }
                    name.setText(document.get("name").toString());
                    collector_type.setText(document.get("collector_type").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void retriveConsolidatorProfile(Activity activity, Context context,String id, TextView name, TextView consolidator_type){
        DocumentReference docRef = db.collection("Waste Consolidator").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("name").toString());
                    consolidator_type.setText(document.get("user_type").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public void retrieveQRCodeCollector(Activity activity, Context context, String id, ImageView qr_code){
        DocumentReference docRef = db.collection("Waste Collector").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    } else {

                    }
                    String name = document.get("name").toString();
                    storageRef = FirebaseStorage.getInstance().getReference("QR Codes/").child("Waste Collector"+"/"+ name +"_"+ id+".png");
                    GlideApp.with(context).load(storageRef).into(qr_code);

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void retrieveQRCodeConsolidator(Activity activity, Context context, String id, ImageView qr_code){
        DocumentReference docRef = db.collection("Waste Consolidator").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    } else {

                    }
                    String name = document.get("name").toString();
                    storageRef = FirebaseStorage.getInstance().getReference("QR Codes/").child("Waste Consolidator"+"/"+ name +"_"+ id+".png");
                    GlideApp.with(context).load(storageRef).into(qr_code);

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    //Log Out
    public void logOut(Activity activity, Context context){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(context, log_in.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
        context.startActivity(i);
        activity.finish();
    }

}

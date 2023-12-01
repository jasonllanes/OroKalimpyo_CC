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
import android.widget.LinearLayout;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.authentication.log_in;
import sldevs.cdo.orokalimpyocollector.home.collector_home;
import sldevs.cdo.orokalimpyocollector.home.consolidator_home;
import sldevs.cdo.orokalimpyocollector.records.consolidator_add_record;
import sldevs.cdo.orokalimpyocollector.records.scanned_contributions;
import sldevs.cdo.orokalimpyocollector.scanner.collector_scanner_result;
import sldevs.cdo.orokalimpyocollector.scanner.consolidator_scanner_result;
import sldevs.cdo.orokalimpyocollector.scanner.view_collected_contributions;

public class firebase_crud {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> waste_contribution;
    Map<String, Object> waste_segregation;
    double current_points = 0;
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
                                    if(user_type.equalsIgnoreCase("Waste Collectors")){
                                        Intent i = new Intent(context, collector_home.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                        activity.finish();
                                    }else if(user_type.equalsIgnoreCase("Waste Consolidators")){
                                        Intent i = new Intent(context, consolidator_home.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                        activity.finish();
                                    } else {
                                        if(user_type.equalsIgnoreCase("Waste Collectors")){
                                            progressBar.setVisibility(View.GONE);
                                            btnLogIn.setVisibility(View.VISIBLE);
                                            Toast.makeText(context, "Make sure to use Waste Collector account.", Toast.LENGTH_SHORT).show();
                                        }else if(user_type.equalsIgnoreCase("Waste Consolidators")){
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
                                    Toast.makeText(context, "Account cannot be logged in here.", Toast.LENGTH_SHORT).show();
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
    public void sendWasteContribution(Bitmap bitmap,Activity activity, Context context, String user_id, String user_type, String name, String household_type,
                                      String barangay,String establishment_type, String collector_name, String collector_type, String waste_type, String kilo,
                                      String month, String day, String year, String hour, String minutes, String seconds, String date,String time,
                                      ProgressBar pbLoading, Button btnCapture,Button btnBack,Button btnSend){
        String contribution_id = user_id.substring(0,5) +month+day+year+hour+minutes+seconds;
        waste_contribution = new HashMap<>();
        if(waste_type.equalsIgnoreCase("Recyclable")){
            current_points = Double.parseDouble(kilo) * 1;
            if(household_type.equalsIgnoreCase("Household")){
                waste_contribution.put("contribution_id",contribution_id);
                waste_contribution.put("user_id",user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);
                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
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
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);
                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
            }
        } else if (waste_type.equalsIgnoreCase("Biodegradable")) {
             current_points = Double.parseDouble(kilo) * 0.5;
            if(household_type.equalsIgnoreCase("Household")){
                waste_contribution.put("contribution_id",contribution_id);
                waste_contribution.put("user_id",user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
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
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
            }
        } else if (waste_type.equalsIgnoreCase("Residual")) {
             current_points = Double.parseDouble(kilo) * 0.2;
            if(household_type.equalsIgnoreCase("Household")){
                waste_contribution.put("contribution_id",contribution_id);
                waste_contribution.put("user_id",user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
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
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
            }
        } else if (waste_type.equalsIgnoreCase("Special Waste")) {
             current_points = Double.parseDouble(kilo) * 0.3;
            if(household_type.equalsIgnoreCase("Household")){
                waste_contribution.put("contribution_id",contribution_id);
                waste_contribution.put("user_id",user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
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
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status","Waste Collected");
                waste_contribution.put("waste_type",waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo",kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F"+user_id.substring(0,5)+month+day+year+hour+minutes+seconds+".png?alt=media");
            }
        }else {
             current_points = 0;
            if (household_type.equalsIgnoreCase("Household")) {
                waste_contribution.put("contribution_id", contribution_id);
                waste_contribution.put("user_id", user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status", "Waste Collected");
                waste_contribution.put("waste_type", waste_type);

                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo", kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F" + user_id.substring(0, 5) + month + day + year + hour + minutes + seconds + ".png?alt=media");
            } else if (household_type.equalsIgnoreCase("Non-Household")) {
                waste_contribution.put("contribution_id", contribution_id);
                waste_contribution.put("user_id", user_id);
                waste_contribution.put("name", name);
                waste_contribution.put("user_type", user_type);
                waste_contribution.put("household_type", household_type);
                waste_contribution.put("barangay", barangay);
                waste_contribution.put("establishment_type", establishment_type);
                waste_contribution.put("collector_id", mAuth.getUid());
                waste_contribution.put("collector_name", collector_name);
                waste_contribution.put("consolidator_id", "N/A");
                waste_contribution.put("consolidator_name", "N/A");
                waste_contribution.put("collector_type", collector_type);
                waste_contribution.put("status", "Waste Collected");
                waste_contribution.put("waste_type", waste_type);
                waste_contribution.put("current_points", FieldValue.increment(current_points));
                waste_contribution.put("kilo", kilo);
                waste_contribution.put("date_collected", date + " " + time);
                waste_contribution.put("date", date);
                waste_contribution.put("time", time);
                waste_contribution.put("contribution_proof_url", "https://firebasestorage.googleapis.com/v0/b/orokalimpyo-ok.appspot.com/o/Waste%20Contribution%20Proof%2F" + user_id.substring(0, 5) + month + day + year + hour + minutes + seconds + ".png?alt=media");
            }
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
                        if(waste_type.equalsIgnoreCase("Recyclable")){
                            db.collection("Waste Generator").document(user_id).update("recyclable",FieldValue.increment(Double.parseDouble(kilo)));
                            db.collection("Waste Generator").document(user_id).update("total_points",FieldValue.increment(current_points));

                        }else if(waste_type.equalsIgnoreCase("Biodegradable")){
                            db.collection("Waste Generator").document(user_id).update("biodegradable",FieldValue.increment(Double.parseDouble(kilo)));
                            db.collection("Waste Generator").document(user_id).update("total_points",FieldValue.increment(current_points));

                        } else if(waste_type.equalsIgnoreCase("Residual")){
                            db.collection("Waste Generator").document(user_id).update("residual",FieldValue.increment(Double.parseDouble(kilo)));
                            db.collection("Waste Generator").document(user_id).update("total_points",FieldValue.increment(current_points));

                        } else if(waste_type.equalsIgnoreCase("Special Waste")){
                            db.collection("Waste Generator").document(user_id).update("special_waste",FieldValue.increment(Double.parseDouble(kilo)));
                            db.collection("Waste Generator").document(user_id).update("total_points",FieldValue.increment(current_points));

                        }

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
                        btnCapture.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.VISIBLE);
                        btnSend.setVisibility(View.VISIBLE);
                        pbLoading.setVisibility(View.GONE);
                        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnCapture.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(context, "Please try again...", Toast.LENGTH_SHORT).show();

//                Toast.makeText(final_sign_up.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //Sending Waste Segregation Data
    public void sendSegregatedWasteData(Activity activity,Context context,ProgressBar progressBar, Button btnUpdate,Button btnEdit, String consolidator_name,String segregation_id,String waste_type,String plastic_type,String plastic_name,String brand,String kilo,String date,String time){
        waste_segregation = new HashMap<>();

        if(waste_type.equalsIgnoreCase("Plastic Waste")){
            waste_segregation.put("segregation_id",segregation_id);
            waste_segregation.put("consolidator_id",mAuth.getUid());
            waste_segregation.put("consolidator_name",consolidator_name);
            waste_segregation.put("waste_type",waste_type);
            waste_segregation.put("plastic_type",plastic_type);
            waste_segregation.put("plastic_name",plastic_name);
            waste_segregation.put("brand",brand);
            waste_segregation.put("kilo",kilo);
            waste_segregation.put("date",date);
            waste_segregation.put("time",time);
        }else{
            waste_segregation.put("segregation_id",segregation_id);
            waste_segregation.put("consolidator_id",mAuth.getUid());
            waste_segregation.put("consolidator_name",consolidator_name);
            waste_segregation.put("waste_type",waste_type);
            waste_segregation.put("brand",brand);
            waste_segregation.put("kilo",kilo);
            waste_segregation.put("date",date);
            waste_segregation.put("time",time);
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
                progressBar.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });

    }
    public void retrieveHouseholdType(Activity activity, Context context, String id, LinearLayout linearLayout){
//        DocumentReference docRef = db.collection("Waste Generator").document(id);
        db.collection("Waste Generator").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                    } else {

                    }
//                    household_type.setText(document.get("household_type").toString());

                    if(document.get("household_type").toString().equalsIgnoreCase("Household")){
                        Toast.makeText(activity, "Household", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, collector_scanner_result.class);
                        i.putExtra("user_id",id);
                        i.putExtra("name",document.get("name").toString());
                        i.putExtra("barangay",document.get("barangay").toString());
                        i.putExtra("user_type","Waste Generator");
                        i.putExtra("household_type","Household");
                        activity.startActivity(i);
                    }else if (document.get("household_type").toString().equalsIgnoreCase("Non-Household")){
                        Toast.makeText(activity, "Non-Household", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, collector_scanner_result.class);
                        i.putExtra("user_id",id);
                        i.putExtra("name",document.get("name").toString());
                        i.putExtra("user_type","Waste Generator");
                        i.putExtra("barangay",document.get("barangay").toString());
                        i.putExtra("household_type","Non-Household");
                        i.putExtra("establishment_type",document.get("establishment_type").toString());
                        activity.startActivity(i);
                    }else{
                        Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Please scan a Waste Generator QR Code.", Snackbar.LENGTH_LONG).setTextColor(activity.getResources().getColor(R.color.white)).setBackgroundTint(activity.getResources().getColor(R.color.green));
                                snackbar.show();
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                boolean isFound = false;
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        if(document.getId().equalsIgnoreCase(id)){
//                            isFound = true;
//                            if(document.get("household_type").toString().equalsIgnoreCase("Household")){
//                                Toast.makeText(activity, "Household", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(context, collector_scanner_result.class);
//                                i.putExtra("user_id",id);
//                                i.putExtra("household_type","Household");
//                                activity.startActivity(i);
//                            }else if (document.get("household_type").toString().equalsIgnoreCase("Non-Household")){
//                                Toast.makeText(activity, "Non-Household", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(context, collector_scanner_result.class);
//                                i.putExtra("user_id",id);
//                                i.putExtra("household_type","Non-Household");
//                                activity.startActivity(i);
//                            }else{
//                                Snackbar snackbar = Snackbar
//                                        .make(linearLayout, "Please scan a Waste Generator QR Code.", Snackbar.LENGTH_LONG).setTextColor(activity.getResources().getColor(R.color.white)).setBackgroundTint(activity.getResources().getColor(R.color.green));
//                                snackbar.show();
//                            }
//                        }
//                    }
//                    if(!isFound){
//                        Snackbar snackbar = Snackbar
//                                .make(linearLayout, "Please scan a Waste Generator QR Code.", Snackbar.LENGTH_LONG).setTextColor(activity.getResources().getColor(R.color.white)).setBackgroundTint(activity.getResources().getColor(R.color.green));
//                        snackbar.show();
//                    }
//                }
//            }
//        });



//
    }

    public void retrieveCollectorType(Activity activity, Context context, String id, LinearLayout linearLayout) {
//        DocumentReference docRef = db.collection("Waste Generator").document(id);
        db.collection("Waste Collectors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean isFound = false;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equalsIgnoreCase(id)) {
                            isFound = true;
                            if (document.get("role").toString().equalsIgnoreCase("Collector")) {
                                Toast.makeText(activity, "Collector", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, consolidator_scanner_result.class);
                                i.putExtra("user_id", id);
                                i.putExtra("user_type", "Waste Collector");
                                activity.startActivity(i);
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Please scan a Waste Collector QR Code.", Snackbar.LENGTH_LONG).setTextColor(activity.getResources().getColor(R.color.white)).setBackgroundTint(activity.getResources().getColor(R.color.green));
                                snackbar.show();
                            }
                        }
                    }
                    if (!isFound) {
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Please scan a Waste Collector QR Code.", Snackbar.LENGTH_LONG).setTextColor(activity.getResources().getColor(R.color.white)).setBackgroundTint(activity.getResources().getColor(R.color.green));
                        snackbar.show();
                    }
                }
            }
        });
    }

    //Updating contribution
    public void updateContribution(ProgressBar progressBar,Button btnUpdate,Activity activity,Context context,String user_id,String contribution_id,String curent_waste_type,String new_waste_type,String new_kilo,String current_kilo, double points_to_update,String current_points){
        DocumentReference contributionRef = db.collection("Waste Contribution").document(contribution_id);
        DocumentReference user_contributionRef = db.collection("Waste Generator").document(user_id);

        contributionRef.update("waste_type", new_waste_type).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                double toDeduct = Double.parseDouble(current_points);
                user_contributionRef.update("date", Timestamp.now().toDate());
                user_contributionRef.update("time", Timestamp.now().toDate().getTime());
                contributionRef.update("current_points",FieldValue.increment(-toDeduct)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        contributionRef.update("current_points", points_to_update);
                        contributionRef.update("kilo", FieldValue.increment(-Double.parseDouble(current_kilo))).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                contributionRef.update("kilo", new_kilo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        user_contributionRef.update("total_points",FieldValue.increment(-toDeduct));
                                        user_contributionRef.update(curent_waste_type.toLowerCase(),FieldValue.increment(-Double.parseDouble(current_kilo))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                user_contributionRef.update("total_points",FieldValue.increment(points_to_update));
                                                user_contributionRef.update(new_waste_type.toLowerCase(),FieldValue.increment(points_to_update)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Intent i = new Intent(context, scanned_contributions.class);
                                                        context.startActivity(i);
                                                        activity.finish();
                                                    }
                                                });

                                            }
                                        });

                                    }
                                });



                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });







    }

    //Getting profile

    public void retrieveName(Activity activity,Context context,String id,TextView name,TextView nameO,String waste_type){
        DocumentReference docRef = db.collection("Waste Consolidators").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                    } else {

                    }

                    if(waste_type.equalsIgnoreCase("Plastic Waste")){
                        name.setText(document.get("fullname").toString());;
                    }else{
                        nameO.setText(document.get("fullname").toString());;
                    }


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
    public void retrieveCollectorProfileAll(Activity activity, Context context,String id, TextView name, TextView collector_type,TextView contact_person,TextView number){
        DocumentReference docRef = db.collection("Waste Collectors").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("fullname").toString());
                    collector_type.setText(document.get("selectType").toString());
                    contact_person.setText(document.get("contactPersonsFullname").toString());
                    number.setText(document.get("contactNumber").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void retrieveConsolidatorProfileAll(Activity activity, Context context,String id, TextView name, TextView collector_type,TextView contact_person,TextView number){
        DocumentReference docRef = db.collection("Waste Consolidators").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("fullname").toString());
                    collector_type.setText(document.get("selectType").toString());
                    contact_person.setText(document.get("contactPersonsFullname").toString());
                    number.setText(document.get("contactNumber").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void retrieveCollectorProfile(Activity activity, Context context,String id, TextView name, TextView collector_type){
        DocumentReference docRef = db.collection("Waste Collectors").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                    } else {

                    }
                    name.setText(document.get("fullname").toString());
                    collector_type.setText(document.get("selectType").toString());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void retriveConsolidatorProfile(Activity activity, Context context,String id, TextView name, TextView consolidator_type){
        DocumentReference docRef = db.collection("Waste Consolidators").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {



                    } else {

                    }
                    name.setText(document.get("fullname").toString());
                    consolidator_type.setText(document.get("selectType").toString());
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

    public void retrieveUserDetails(collector_scanner_result collector_scanner_result, collector_scanner_result collector_scanner_result1, String user_id, String user_type, String household_type, TextView tvUserID, TextView tvName, TextView tvHouseholdType, TextView tvBarangay, TextView tvLocation, TextView tvNumber, TextView tvEmail, TextView tvUserIDN, TextView tvNameN, TextView tvHouseholdTypeN, TextView tvEstablishmentType, TextView tvBarangayN, TextView tvLocationN, TextView tvNumberN, TextView tvEmailN) {
        DocumentReference docRef = db.collection("Waste Generator").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if(household_type.equalsIgnoreCase("Household")){
                                tvUserID.setText(document.get("user_id").toString());
                                tvName.setText(document.get("name").toString());
                                tvHouseholdType.setText(document.get("household_type").toString());
                                tvBarangay.setText(document.get("barangay").toString());
                                tvLocation.setText(document.get("location").toString());
                                tvNumber.setText(document.get("number").toString());
                                tvEmail.setText(document.get("email").toString());
                            }else if(household_type.equalsIgnoreCase("Non-Household")){
                                tvUserIDN.setText(document.get("user_id").toString());
                                tvNameN.setText(document.get("name").toString());
                                tvHouseholdTypeN.setText(document.get("household_type").toString());
                                tvEstablishmentType.setText(document.get("establishment_type").toString());
                                tvBarangayN.setText(document.get("barangay").toString());
                                tvLocationN.setText(document.get("location").toString());
                                tvNumberN.setText(document.get("number").toString());
                                tvEmailN.setText(document.get("email").toString());
                            }
                        } else {

                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
        });
    }

    public void retrieveCollectorProfile(consolidator_scanner_result consolidator_scanner_result, consolidator_scanner_result consolidator_scanner_result1, String user_id, TextView tvCollectorName, TextView tvCollectorType, TextView tvContactPerson, TextView tvCollectorNumber, TextView tvCollectorEmail) {
        DocumentReference docRef = db.collection("Waste Collectors").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tvCollectorName.setText(document.get("fullname").toString());
                            tvCollectorType.setText(document.get("selectType").toString());
                            tvContactPerson.setText(document.get("contactPersonsFullname").toString());
                            tvCollectorNumber.setText(document.get("contactNumber").toString());
                            tvCollectorEmail.setText(document.get("email").toString());
                        } else {

                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
        });
    }
}

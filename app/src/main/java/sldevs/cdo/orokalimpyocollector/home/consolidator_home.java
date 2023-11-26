package sldevs.cdo.orokalimpyocollector.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.authentication.choose_user_type;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.profile.consolidator_profile;
import sldevs.cdo.orokalimpyocollector.records.consolidator_add_record;
import sldevs.cdo.orokalimpyocollector.records.scanned_contributions;
import sldevs.cdo.orokalimpyocollector.records.segregated_waste;
import sldevs.cdo.orokalimpyocollector.scanner.collector_scanner;
import sldevs.cdo.orokalimpyocollector.scanner.consolidator_scanner;
import sldevs.cdo.orokalimpyocollector.scanner.view_segregated_contributions;

public class consolidator_home extends AppCompatActivity implements View.OnClickListener {


    Button btnScan,btnShowScanned,btnRecords,btnShowSegregated,btnProfile,btnLogout,btnYes,btnNo;

    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_home);

        fc = new firebase_crud();

        btnScan = findViewById(R.id.btnScan);
        btnShowScanned = findViewById(R.id.btnShowScanned);
        btnRecords = findViewById(R.id.btnAddRecord);
        btnShowSegregated = findViewById(R.id.btnShowSegregated);
        btnProfile = findViewById(R.id.btnShowProfile);
        btnLogout = findViewById(R.id.btnLogout);


        btnScan.setOnClickListener(this);
        btnShowScanned.setOnClickListener(this);
        btnRecords.setOnClickListener(this);
        btnShowSegregated.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        Toast.makeText(this, "Waste Consolidator", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnScan){
            Intent i = new Intent(consolidator_home.this, consolidator_scanner.class);
            startActivity(i);
        }else if(id == R.id.btnShowScanned){
            Intent i = new Intent(consolidator_home.this, view_segregated_contributions.class);
            startActivity(i);
        }else if(id == R.id.btnAddRecord){
            Intent i = new Intent(consolidator_home.this, consolidator_add_record.class);
            startActivity(i);
        }else if(id == R.id.btnShowSegregated){
            Intent i = new Intent(consolidator_home.this, segregated_waste.class);
            startActivity(i);
        }else if(id == R.id.btnLogout){
            Dialog builder = new Dialog(consolidator_home.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.setContentView(R.layout.log_out_pop);
            builder.setCancelable(true);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            btnYes = builder.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.logOut(consolidator_home.this,getApplicationContext());
                    Intent i = new Intent(consolidator_home.this, choose_user_type.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Toast.makeText(consolidator_home.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                }
            });

            btnNo = builder.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.dismiss();
                }
            });
            builder.show();
        } else if (id == R.id.btnShowProfile) {
            Intent i = new Intent(consolidator_home.this, consolidator_profile.class);
            startActivity(i);
        }
    }
}
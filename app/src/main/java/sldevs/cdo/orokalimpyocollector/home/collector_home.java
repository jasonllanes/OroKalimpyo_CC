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
import sldevs.cdo.orokalimpyocollector.profile.collector_profile;
import sldevs.cdo.orokalimpyocollector.records.edit_contribution;
import sldevs.cdo.orokalimpyocollector.records.scanned_contributions;
import sldevs.cdo.orokalimpyocollector.scanner.collector_scanner;

public class collector_home extends AppCompatActivity implements View.OnClickListener {


    Button btnScan,btnRecords,btnProfile,btnLogout,btnYes,btnNo;

    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_home);

        fc = new firebase_crud();

        btnScan = findViewById(R.id.btnScan);
        btnRecords = findViewById(R.id.btnRecords);
        btnProfile = findViewById(R.id.btnShowProfile);
        btnLogout = findViewById(R.id.btnLogout);

        btnScan.setOnClickListener(this);
        btnRecords.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        Toast.makeText(this, "Waste Collector", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnScan){
            Intent i = new Intent(collector_home.this, collector_scanner.class);
            startActivity(i);
        }else if(id == R.id.btnRecords){
            Intent i = new Intent(collector_home.this, scanned_contributions.class);
            startActivity(i);
        }else if(id == R.id.btnLogout){
            Dialog builder = new Dialog(collector_home.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.setContentView(R.layout.log_out_pop);
            builder.setCancelable(true);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            btnYes = builder.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fc.logOut(collector_home.this,getApplicationContext());
                    Intent i = new Intent(collector_home.this, choose_user_type.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Toast.makeText(collector_home.this, "Log out successfully", Toast.LENGTH_SHORT).show();
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

        }else if(id == R.id.btnShowProfile){
            Intent i = new Intent(collector_home.this, collector_profile.class);
            startActivity(i);
        }
    }


}
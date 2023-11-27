package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.functions.other_functions;
import sldevs.cdo.orokalimpyocollector.scanner.collector_capture_proof;
import sldevs.cdo.orokalimpyocollector.scanner.collector_scanner_result;

public class update_contribution extends AppCompatActivity implements View.OnClickListener {
    TextView tvKilo;

    LinearLayout llH,llNH;

    MaterialSpinner sWaste;
    ProgressBar pbLoading;

    EditText etKilo;
    Button btnUpdate;
    ImageView ivBack;

    firebase_crud fc;
    other_functions of;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user_id,contribution_id,waste,kilo,gained_points,establishment_type,others,name,barangay,location,number,email;

    SimpleDateFormat month,day,year,week,date,hours,minutes,seconds,time;
    String currentMonth,currentDay,currentYear,currentWeek,currentDate,currentHour,currentMinute,currentSeconds,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contribution);

        fc = new firebase_crud();
        of = new other_functions();

        tvKilo = findViewById(R.id.tvKilo);

        sWaste = findViewById(R.id.sWaste);
        etKilo = findViewById(R.id.etKilo);

        pbLoading = findViewById(R.id.pbLoading);


        ivBack = findViewById(R.id.ivBack);
        btnUpdate = findViewById(R.id.btnUpdate);


        user_id = getIntent().getStringExtra("user_id");
        waste = getIntent().getStringExtra("waste");
        kilo = getIntent().getStringExtra("kilo");
        contribution_id = getIntent().getStringExtra("contribution_id");
        gained_points = getIntent().getStringExtra("gained_points");


        sWaste.setItems(of.populateWasteType());
        if(waste.equalsIgnoreCase("Recyclable"))
            sWaste.setSelectedIndex(0);
        else if(waste.equalsIgnoreCase("Biodegradable"))
            sWaste.setSelectedIndex(1);
        else if(waste.equalsIgnoreCase("Residual"))
            sWaste.setSelectedIndex(2);
        else if(waste.equalsIgnoreCase("Special Waste"))
            sWaste.setSelectedIndex(3);
        else if(waste.equalsIgnoreCase("Non-compliant"))
            sWaste.setSelectedIndex(4);
        sWaste.setText(waste);
        etKilo.setText(kilo);



        retrieveDate();



        ivBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        sWaste.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item.toString().equalsIgnoreCase("Non-compliant") ){

                    tvKilo.setVisibility(View.GONE);
                    etKilo.setVisibility(View.GONE);
                }else{

                    tvKilo.setVisibility(View.VISIBLE);
                    etKilo.setVisibility(View.VISIBLE);
                }
            }
        });





    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnUpdate){
            pbLoading.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            if(etKilo.getText().toString().isEmpty()){
                pbLoading.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                etKilo.setError("Please input the kilo.");
            }else{

                if(sWaste.getText().toString().equalsIgnoreCase(waste) && etKilo.getText().toString().equalsIgnoreCase(kilo)){
                    pbLoading.setVisibility(View.GONE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "No changes", Toast.LENGTH_SHORT).show();
                } else{
                    if(sWaste.getText().toString().equalsIgnoreCase("Recyclable")) {
                        double points = Double.parseDouble(etKilo.getText().toString()) * 1.0;
                        fc.updateContribution(pbLoading, btnUpdate,this, update_contribution.this,user_id,contribution_id,waste,"Recyclable", etKilo.getText().toString(), kilo, points, gained_points);
                    } else if(sWaste.getText().toString().equalsIgnoreCase("Biodegradable")){
                        double points = Double.parseDouble(etKilo.getText().toString()) * 0.5;
                        fc.updateContribution(pbLoading,btnUpdate,this, update_contribution.this,user_id,contribution_id,waste,"Biodegradable", etKilo.getText().toString(), kilo, points, gained_points);
                    } else if(sWaste.getText().toString().equalsIgnoreCase("Residual")){
                        double points = Double.parseDouble(etKilo.getText().toString()) * 0.3;
                        fc.updateContribution(pbLoading,btnUpdate,this, update_contribution.this,user_id,contribution_id, waste,"Residual", etKilo.getText().toString(), kilo, points,gained_points);
                    } else if(sWaste.getText().toString().equalsIgnoreCase("Special Waste")){
                        double points = Double.parseDouble(etKilo.getText().toString()) * 0.2;
                        fc.updateContribution(pbLoading,btnUpdate,this, update_contribution.this,user_id,contribution_id,waste,"Special Waste", etKilo.getText().toString(), kilo, points, gained_points);
                    } else if(sWaste.getText().toString().equalsIgnoreCase("Non-compliant")){
                        fc.updateContribution(pbLoading,btnUpdate,this, update_contribution.this,user_id,contribution_id,waste, "Non-compliant", etKilo.getText().toString(), kilo, 0, gained_points);
                    }
                }

            }
        } else if (id == R.id.ivBack) {
            Intent i = new Intent(this, scanned_contributions.class);
            startActivity(i);
            finish();
        }


    }

    public void retrieveDate(){
        month = new SimpleDateFormat("MM");
        day = new SimpleDateFormat("dd");
        year = new SimpleDateFormat("yy");

        week = new SimpleDateFormat("W");

        date = new SimpleDateFormat("MM/dd/yy");

        hours = new SimpleDateFormat("hh");
        minutes = new SimpleDateFormat("mm");
        seconds = new SimpleDateFormat("ss");

        time = new SimpleDateFormat("hh:mm:ss a");

        currentMonth = month.format(new Date());
        currentDay = day.format(new Date());
        currentYear = year.format(new Date());

        currentWeek = week.format(new Date());

        currentDate = date.format(new Date());

        currentHour = hours.format(new Date());
        currentMinute = minutes.format(new Date());
        currentSeconds = seconds.format(new Date());

        currentTime = time.format(new Date());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, scanned_contributions.class);
        startActivity(i);
        finish();
    }
}
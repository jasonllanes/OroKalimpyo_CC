package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    EditText etKilo;
    Button btnUpdate,btnBack;

    firebase_crud fc;
    other_functions of;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String contribution_id,waste,kilo,household_type,establishment_type,others,name,barangay,location,number,email;

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

        btnUpdate = findViewById(R.id.btnUpdate);


        waste = getIntent().getStringExtra("waste");
        kilo = getIntent().getStringExtra("kilo");
        contribution_id = getIntent().getStringExtra("contribution_id");

        sWaste.setText(waste);
        etKilo.setText(kilo);



        retrieveDate();
        sWaste.setItems(of.populateWasteType());


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
            if(etKilo.getText().toString().isEmpty()){
                etKilo.setError("Please input the kilo.");
            }else{
                if(sWaste.getText().toString().equalsIgnoreCase(waste) && etKilo.getText().toString().equalsIgnoreCase(kilo)){

                } else if (sWaste.getText().toString().equalsIgnoreCase(waste) && !etKilo.getText().toString().equalsIgnoreCase(kilo)) {
                    if(sWaste.getText().toString().equalsIgnoreCase("Recyclable")) {

                        fc.updateContribution(this, update_contribution.this,"Recyclable", contribution_id, sWaste.getText().toString(), 0);
                    } else if(sWaste.getText().toString().equalsIgnoreCase("Biodegradable")){
                        fc.updateContribution(this, update_contribution.this,"Biodegradable", contribution_id, sWaste.getText().toString(), 0);

                    } else if(sWaste.getText().toString().equalsIgnoreCase("Residual")){
                        fc.updateContribution(this, update_contribution.this,"Residual", contribution_id, sWaste.getText().toString(), 0);

                    } else if(sWaste.getText().toString().equalsIgnoreCase("Special Waste")){
                        fc.updateContribution(this, update_contribution.this,"Special Waste", contribution_id, sWaste.getText().toString(), 0);

                    } else if(sWaste.getText().toString().equalsIgnoreCase("Non-compliant")){
                        fc.updateContribution(this, update_contribution.this,"Non-compliant", contribution_id, sWaste.getText().toString(), 0);

                    }
                }

            }
        } else if (id == R.id.btnBack) {
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

}
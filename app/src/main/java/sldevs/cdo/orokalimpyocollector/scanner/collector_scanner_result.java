package sldevs.cdo.orokalimpyocollector.scanner;

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
import sldevs.cdo.orokalimpyocollector.home.collector_home;

public class collector_scanner_result extends AppCompatActivity implements View.OnClickListener {

    TextView tvUserID,tvName,tvHouseholdType,tvBarangay,tvLocation,tvNumber,tvEmail,
            tvUserIDN,tvNameN,tvHouseholdTypeN,tvEstablishmentType,tvBarangayN,tvLocationN,tvNumberN,tvEmailN,
            tvCollectorID,tvCollectorName,tvCollectorType,tvDate;

    LinearLayout llH,llNH;

    MaterialSpinner sWaste;

    EditText etKilo;
    Button btnNext,btnBack;

    firebase_crud fc;
    other_functions of;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user_id,user_type,household_type,establishment_type,others,name,barangay,location,number,email;

    SimpleDateFormat month,day,year,week,date,hours,minutes,seconds,time;
    String currentMonth,currentDay,currentYear,currentWeek,currentDate,currentHour,currentMinute,currentSeconds,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_scanner_result);

        fc = new firebase_crud();
        of = new other_functions();

        tvUserID = findViewById(R.id.tvUserID);
        tvName = findViewById(R.id.tvName);
        tvHouseholdType = findViewById(R.id.tvHouseholdType);
        tvBarangay = findViewById(R.id.tvBarangay);
        tvLocation = findViewById(R.id.tvLocation);
        tvNumber = findViewById(R.id.tvNumber);
        tvEmail = findViewById(R.id.tvEmail);

        tvUserIDN = findViewById(R.id.tvUserIDN);
        tvNameN = findViewById(R.id.tvNameN);
        tvHouseholdTypeN = findViewById(R.id.tvHouseholdTypeN);
        tvEstablishmentType = findViewById(R.id.tvEstablishmentTypeN);
        tvBarangayN = findViewById(R.id.tvBarangayN);
        tvLocationN = findViewById(R.id.tvLocationN);
        tvNumberN = findViewById(R.id.tvNumberN);
        tvEmailN = findViewById(R.id.tvEmailN);

        tvCollectorID = findViewById(R.id.tvCollectorID);
        tvCollectorName = findViewById(R.id.tvCollectorName);
        tvCollectorType = findViewById(R.id.tvCollectorType);
        tvDate = findViewById(R.id.tvDate);

        llH = findViewById(R.id.llH);
        llNH = findViewById(R.id.llNH);

        sWaste = findViewById(R.id.sWaste);

        etKilo = findViewById(R.id.etKilo);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);


        user_id = getIntent().getStringExtra("user_id");
        user_type = getIntent().getStringExtra("user_type");
        name = getIntent().getStringExtra("name");
        household_type = getIntent().getStringExtra("household_type");
        barangay = getIntent().getStringExtra("barangay");
        establishment_type = getIntent().getStringExtra("household_type");


        retrieveDate();
        sWaste.setItems(of.populateWasteType());
        tvDate.setText(currentDate + " " + currentTime);

        tvCollectorID.setText(mAuth.getUid());
        fc.retrieveCollectorProfile(this,collector_scanner_result.this,mAuth.getUid(),tvCollectorName,tvCollectorType);
        fc.retrieveUserDetails(this,collector_scanner_result.this,user_id,user_type,household_type,
                tvUserID,tvName,tvHouseholdType,tvBarangay,tvLocation,tvNumber,tvEmail,
                tvUserIDN,tvNameN,tvHouseholdTypeN,tvEstablishmentType,tvBarangayN,tvLocationN,tvNumberN,tvEmailN);

        if(household_type.equalsIgnoreCase("Household")){
            llNH.setVisibility(View.GONE);
            llH.setVisibility(View.VISIBLE);
            setDataHousehold();
        }else if(household_type.equalsIgnoreCase("Non-Household")){
            llH.setVisibility(View.GONE);
            llNH.setVisibility(View.VISIBLE);
            setDataNonHousehold();
        }



        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnNext){
            if(etKilo.getText().toString().isEmpty()){
                etKilo.setError("Please input the kilo.");
            }else{
                if(tvHouseholdType.getText().toString().equalsIgnoreCase("Household")){
                    Intent i = new Intent(collector_scanner_result.this, collector_capture_proof.class);
                    i.putExtra("collector_id",tvCollectorID.getText().toString().trim());
                    i.putExtra("collector_name",tvCollectorName.getText().toString().trim());
                    i.putExtra("collector_type",tvCollectorType.getText().toString().trim());
                    i.putExtra("date_time",tvDate.getText().toString());
                    i.putExtra("user_id", user_id);
                    i.putExtra("name",name);
                    i.putExtra("user_type",user_type);
                    i.putExtra("household_type",household_type);
                    i.putExtra("establishment_type",establishment_type);
                    i.putExtra("others",others);
                    i.putExtra("barangay",barangay);
                    i.putExtra("location",location);
                    i.putExtra("number",number);
                    i.putExtra("email",email);
                    i.putExtra("waste_type",sWaste.getText().toString());
                    i.putExtra("kilo",etKilo.getText().toString().trim());
                    startActivity(i);
                }else{
                    Intent i = new Intent(collector_scanner_result.this, collector_capture_proof.class);
                    i.putExtra("collector_id",tvCollectorID.getText().toString().trim());
                    i.putExtra("collector_name",tvCollectorName.getText().toString().trim());
                    i.putExtra("collector_type",tvCollectorType.getText().toString().trim());
                    i.putExtra("date_time",tvDate.getText().toString());
                    i.putExtra("user_id", user_id);
                    i.putExtra("name",name);
                    i.putExtra("user_type",user_type);
                    i.putExtra("household_type",household_type);
                    i.putExtra("establishment_type",establishment_type);
                    i.putExtra("others",others);
                    i.putExtra("barangay",barangay);
                    i.putExtra("location",location);
                    i.putExtra("number",number);
                    i.putExtra("email",email);
                    i.putExtra("waste_type",sWaste.getText().toString());
                    i.putExtra("kilo",etKilo.getText().toString().trim());
                    startActivity(i);
                }


            }
        } else if (id == R.id.btnBack) {
            finish();
        }


    }
    public void setDataHousehold(){
        tvUserID.setText(user_id);
        tvName.setText(name);
        tvHouseholdType.setText(household_type);
        tvBarangay.setText(barangay);
        tvLocation.setText(location);
        tvNumber.setText(number);
        tvEmail.setText(email);
    }

    public void setDataNonHousehold(){
        tvUserIDN.setText(user_id);
        tvNameN.setText(name);
        tvHouseholdTypeN.setText(household_type);
        tvEstablishmentType.setText(establishment_type);
        tvBarangayN.setText(barangay);
        tvLocationN.setText(location);
        tvNumberN.setText(number);
        tvEmailN.setText(email);
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
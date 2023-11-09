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

public class consolidator_scanner_result extends AppCompatActivity implements View.OnClickListener {

    TextView tvCollectorID,tvCollectorName,tvCollectorType,tvContactPerson,tvCollectorNumber,tvCollectorEmail,
            tvConsolidatorID,tvConsolidatorName,tvConsolidatorType,tvDate,tvUserType;
    MaterialSpinner sWaste;

    EditText etKilo;
    Button btnSend,btnShowCollected,btnScan;

    firebase_crud fc;
    other_functions of;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user_id,user_type,collector_type,houshold_type,establishment_type,others,name,barangay,location,contact_person,number,email;

    SimpleDateFormat month,day,year,week,date,hours,minutes,seconds,time;
    String currentMonth,currentDay,currentYear,currentWeek,currentDate,currentHour,currentMinute,currentSeconds,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_scanner_result);


        fc = new firebase_crud();
        of = new other_functions();

        tvCollectorID = findViewById(R.id.tvCollectorID);
        tvCollectorName = findViewById(R.id.tvCollectorName);
        tvCollectorType = findViewById(R.id.tvCollectorType);
        tvContactPerson = findViewById(R.id.tvContactPerson);
        tvCollectorNumber = findViewById(R.id.tvCollectorNumber);
        tvCollectorEmail = findViewById(R.id.tvCollectorEmail);

        tvConsolidatorID = findViewById(R.id.tvConsolidatorID);
        tvConsolidatorName = findViewById(R.id.tvConsolidatorName);
        tvConsolidatorType = findViewById(R.id.tvConsolidatorType);
        tvDate = findViewById(R.id.tvDate);
        tvUserType = findViewById(R.id.tvUserType);


        btnShowCollected = findViewById(R.id.btnShowCollected);
        btnScan = findViewById(R.id.btnScan);

        user_id = getIntent().getStringExtra("user_id");
        user_type = getIntent().getStringExtra("user_type");
        collector_type = getIntent().getStringExtra("collector_type");
//        name = getIntent().getStringExtra("name");
//        contact_person = getIntent().getStringExtra("contact_person");
//        number = getIntent().getStringExtra("number");
//        email = getIntent().getStringExtra("email");

        retrieveDate();

        fc.retriveConsolidatorProfile(this,consolidator_scanner_result.this,mAuth.getUid(),tvConsolidatorName,tvConsolidatorType);
        fc.retrieveCollectorProfile(this,consolidator_scanner_result.this,user_id,tvCollectorName,tvCollectorType,tvContactPerson,tvCollectorNumber,tvCollectorEmail);

        tvConsolidatorID.setText(mAuth.getUid());

        tvDate.setText(currentDate + " " + currentTime);


        setDataCollector();



        btnShowCollected.setOnClickListener(this);
        btnScan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnSend){

        }else if(id == R.id.btnScan){
            finish();
        }else if(id == R.id.btnShowCollected){
            Intent i = new Intent(consolidator_scanner_result.this, view_collected_contributions.class);
            i.putExtra("collector_id",tvCollectorID.getText().toString());
            i.putExtra("consolidator_name",tvConsolidatorName.getText().toString());
            startActivity(i);
        }


    }
    public void setDataCollector(){
        tvCollectorID.setText(user_id);
        tvCollectorName.setText(name);
        tvCollectorType.setText(collector_type);
        tvUserType.setText(user_type);
        tvContactPerson.setText(contact_person);
        tvCollectorNumber.setText(number);
        tvCollectorEmail.setText(email);
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
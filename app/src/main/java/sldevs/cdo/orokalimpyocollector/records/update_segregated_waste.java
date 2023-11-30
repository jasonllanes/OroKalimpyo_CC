package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.functions.other_functions;
import sldevs.cdo.orokalimpyocollector.scanner.view_segregated_contributions;

public class update_segregated_waste extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView tvWasteType, tvWasteTypeO;
    MaterialSpinner sPlasticWasteType, sPlasticWasteName, sBrand, sBrandO;
    EditText etOtherTypes, etOtherName, etOtherBrand, etOtherBrandO, etKilo, etKiloO;
    ImageView ivBack;

    CardView cvPlasticWaste, cvOther;
    Button btnNext;
    ProgressBar pbLoading;

    String segregated_id, consolidator_id, brand, plastic_type, plastic_name, waste, datee, kilo;

    other_functions of;
    firebase_crud fc;
    SimpleDateFormat month, day, year, week, date, hours, minutes, seconds, time;
    String currentMonth, currentDay, currentYear, currentWeek, currentDate, currentHour, currentMinute, currentSeconds, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_segregated_waste);

        fc = new firebase_crud();
        of = new other_functions();

        ivBack = findViewById(R.id.ivBack);
        tvWasteType = findViewById(R.id.tvWasteType);
        tvWasteTypeO = findViewById(R.id.tvWasteTypeO);

        sPlasticWasteType = findViewById(R.id.sPlasticWasteType);
        sPlasticWasteName = findViewById(R.id.sPlasticWasteName);
        sBrand = findViewById(R.id.sBrand);
        sBrandO = findViewById(R.id.sBrandO);


        etOtherTypes = findViewById(R.id.etOtherTypes);
        etOtherName = findViewById(R.id.etOtherName);
        etOtherBrand = findViewById(R.id.etOtherBrand);
        etOtherBrandO = findViewById(R.id.etOtherBrandO);
        etKilo = findViewById(R.id.etKilo);
        etKiloO = findViewById(R.id.etKiloO);

        cvPlasticWaste = findViewById(R.id.cvPlasticWaste);
        cvOther = findViewById(R.id.cvOther);

        btnNext = findViewById(R.id.btnNext);

        pbLoading = findViewById(R.id.pbLoading);


        segregated_id = getIntent().getStringExtra("segregated_id");
        consolidator_id = getIntent().getStringExtra("consolidator_id");
        brand = getIntent().getStringExtra("brand");
        plastic_type = getIntent().getStringExtra("plastic_type");
        plastic_name = getIntent().getStringExtra("plastic_name");
        waste = getIntent().getStringExtra("waste");
        kilo = getIntent().getStringExtra("kilo");


        sPlasticWasteName.setItems(of.populatePETList());



        ivBack.setOnClickListener(this);

        retrieveDate();
//        waste = getIntent().getStringExtra("waste_type");


        if (waste.equalsIgnoreCase("Plastic Waste")) {
            tvWasteType.setText(waste);
            cvOther.setVisibility(View.GONE);
            cvPlasticWaste.setVisibility(View.VISIBLE);
            of.populateBrandList().clear();
            sBrand.setItems(of.populateBrandList());
            sPlasticWasteType.setText(plastic_type);
            sPlasticWasteName.setText(plastic_name);
            sBrand.setText(brand);
            etKilo.setText(kilo);
        } else {
            tvWasteTypeO.setText(waste);
            cvPlasticWaste.setVisibility(View.GONE);
            cvOther.setVisibility(View.VISIBLE);
            of.populateBrandList().clear();
            sBrandO.setItems(of.populateBrandList());
            sBrandO.setText(brand);
            etKiloO.setText(kilo);
        }


        sPlasticWasteType.setItems(of.populateConsolidatorPlasticType());
        sPlasticWasteType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (view.getText().toString().equalsIgnoreCase("PET (Polyethylene Terephthalate)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePETList());
                } else if (view.getText().toString().equalsIgnoreCase("HDPE (High-Density Polyethylene)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populateHDPEList());
                } else if (view.getText().toString().equalsIgnoreCase("PVC (Polyvinyl Chloride)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePVCList());
                } else if (view.getText().toString().equalsIgnoreCase("LDPE (Low-Density Polyethylene)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populateLDPEList());
                } else if (view.getText().toString().equalsIgnoreCase("PP (Polypropylene)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePPList());
                } else if (view.getText().toString().equalsIgnoreCase("PS (Polystyrene)")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePSList());
                } else if (view.getText().toString().equalsIgnoreCase("Other...")) {
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.VISIBLE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populateOtherList());
                }
            }
        });

        sPlasticWasteName.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (view.getText().toString().equalsIgnoreCase("Other...")) {
                    etOtherName.setVisibility(View.VISIBLE);
                } else {
                    etOtherName.setVisibility(View.GONE);
                }
            }
        });


        sBrand.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (view.getText().toString().equalsIgnoreCase("Other...")) {
                    etOtherBrand.setVisibility(View.VISIBLE);
                } else {
                    etOtherBrand.setVisibility(View.GONE);
                }
            }
        });


        sBrandO.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (view.getText().toString().equalsIgnoreCase("Other...")) {
                    etOtherBrandO.setVisibility(View.VISIBLE);
                } else {
                    etOtherBrandO.setVisibility(View.GONE);
                }
            }
        });

        btnNext.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnNext) {
            btnNext.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);

            fc.updateSegregation(this,update_segregated_waste.this,btnNext,pbLoading,segregated_id,waste,sPlasticWasteType.getText().toString(),sPlasticWasteName.getText().toString(),sBrand.getText().toString(),sBrandO.getText().toString(),etKiloO.getText().toString(),etKilo.getText().toString(),currentDate,currentTime);

        } else if (id == R.id.ivBack) {

            Intent i = new Intent(update_segregated_waste.this, view_segregated_contributions.class);
            startActivity(i);
            finish();
        }

    }

    public void retrieveDate() {
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
        Intent i = new Intent(update_segregated_waste.this, view_segregated_contributions.class);
        startActivity(i);
        finish();
    }
}
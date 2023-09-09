package sldevs.cdo.orokalimpyocollector.records;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.functions.other_functions;

public class consolidator_add_record_2 extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView tvWasteType,tvWasteTypeO;
    MaterialSpinner sPlasticWasteType,sPlasticWasteName,sBrand,sBrandO;
    EditText etOtherTypes,etOtherName,etOtherBrand,etOtherBrandO,etKilo,etKiloO;
    ImageView ivBack;

    CardView cvPlasticWaste,cvOther;
    Button btnNext;

    String waste_type;

    other_functions of;
    SimpleDateFormat month,day,year,week,date,hours,minutes,seconds,time;
    String currentMonth,currentDay,currentYear,currentWeek,currentDate,currentHour,currentMinute,currentSeconds,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_add_record2);

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

        sPlasticWasteName.setItems(of.populatePETList());


        ivBack.setOnClickListener(this);

        retrieveDate();
        waste_type = getIntent().getStringExtra("waste_type");


        if(waste_type.equalsIgnoreCase("Plastic Waste")){
            tvWasteType.setText(waste_type);
            cvOther.setVisibility(View.GONE);
            cvPlasticWaste.setVisibility(View.VISIBLE);
            of.populateBrandList().clear();
            sBrand.setItems(of.populateBrandList());
        }else{
            tvWasteTypeO.setText(waste_type);
            cvPlasticWaste.setVisibility(View.GONE);
            cvOther.setVisibility(View.VISIBLE);
            of.populateBrandList().clear();
            sBrandO.setItems(of.populateBrandList());
        }

        sPlasticWasteType.setItems(of.populateConsolidatorPlasticType());
        sPlasticWasteType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(view.getText().toString().equalsIgnoreCase("PET (Polyethylene Terephthalate)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePETList());
                }else if(view.getText().toString().equalsIgnoreCase("HDPE (High-Density Polyethylene)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populateHDPEList());
                }else if(view.getText().toString().equalsIgnoreCase("PVC (Polyvinyl Chloride)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePVCList());
                }else if(view.getText().toString().equalsIgnoreCase("LDPE (Low-Density Polyethylene)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populateLDPEList());
                }else if(view.getText().toString().equalsIgnoreCase("PP (Polypropylene)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePPList());
                }else if(view.getText().toString().equalsIgnoreCase("PS (Polystyrene)")){
                    sPlasticWasteName.setVisibility(View.VISIBLE);
                    etOtherTypes.setVisibility(View.GONE);
                    of.populatePETList().clear();
                    sPlasticWasteName.setItems(of.populatePSList());
                }else if(view.getText().toString().equalsIgnoreCase("Other...")){
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
                if(view.getText().toString().equalsIgnoreCase("Other...")){
                    etOtherName.setVisibility(View.VISIBLE);
                }else{
                    etOtherName.setVisibility(View.GONE);
                }
            }
        });




        sBrand.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(view.getText().toString().equalsIgnoreCase("Other...")){
                    etOtherBrand.setVisibility(View.VISIBLE);
                }else{
                    etOtherBrand.setVisibility(View.GONE);
                }
            }
        });


        sBrandO.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(view.getText().toString().equalsIgnoreCase("Other...")){
                    etOtherBrandO.setVisibility(View.VISIBLE);
                }else{
                    etOtherBrandO.setVisibility(View.GONE);
                }
            }
        });

        btnNext.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnNext){
            if(waste_type.equalsIgnoreCase("Plastic Waste")){
                if(sPlasticWasteType.getText().toString().equalsIgnoreCase("Other...") && etOtherTypes.getText().toString().isEmpty()){
                    etOtherTypes.setError("Please specify the type of plastic.");
                }
                else if(sPlasticWasteName.getText().toString().equalsIgnoreCase("Other...") && etOtherName.getText().toString().isEmpty()){
                    etOtherName.setError("Please specify the name of plastic waste.");
                }
                else if(sBrand.getText().toString().equalsIgnoreCase("Other...") && etOtherBrand.getText().toString().isEmpty()){
                    etOtherBrand.setError("Please specify the brand.");
                }
                else if(etKilo.getText().toString().isEmpty()){
                    etKilo.setError("Please input the kilo.");
                }else{
                    Intent i = new Intent(consolidator_add_record_2.this, consolidator_add_record_summary.class);
                    i.putExtra("segregated_id",mAuth.getUid().substring(0,10)+""+currentMonth+currentDay+currentYear+currentHour+currentMinute+currentSeconds);
                    i.putExtra("waste_type",waste_type);
                    if(sPlasticWasteType.getText().toString().equalsIgnoreCase("Other...")){
                        i.putExtra("plastic_type",etOtherTypes.getText().toString());
                    }else{
                        i.putExtra("plastic_type",sPlasticWasteType.getText().toString());
                    }
                    if(sPlasticWasteName.getText().toString().equalsIgnoreCase("Other...")){
                        i.putExtra("plastic_name",etOtherName.getText().toString());
                    }else{
                        i.putExtra("plastic_name",sPlasticWasteName.getText().toString());
                    }
                    if(sBrand.getText().toString().equalsIgnoreCase("Other...")){
                        i.putExtra("brand",etOtherBrand.getText().toString());
                    }else{
                        i.putExtra("brand",sBrand.getText().toString());
                    }
                    i.putExtra("kilo",etKilo.getText().toString());
                    startActivity(i);
                }

                }else{
                    if(sBrandO.getText().toString().equalsIgnoreCase("Other...") && etOtherBrandO.getText().toString().isEmpty()){
                        etOtherBrandO.setError("Please specify the brand.");
                    }
                    else if(etKiloO.getText().toString().isEmpty()){
                        etKiloO.setError("Please input the kilo.");
                    }else{
                        Intent i = new Intent(consolidator_add_record_2.this, consolidator_add_record_summary.class);
                        i.putExtra("segregated_id",mAuth.getUid().substring(0,10)+""+currentMonth+currentDay+currentYear+currentHour+currentMinute+currentSeconds);
                        i.putExtra("waste_type",waste_type);
                        if(sBrandO.getText().toString().equalsIgnoreCase("Other...")){
                            i.putExtra("brand",etOtherBrandO.getText().toString());
                        }else{
                            i.putExtra("brand",sBrandO.getText().toString());
                        }
                        i.putExtra("kilo",etKiloO.getText().toString());
                        startActivity(i);
                    }

                }
            } else if (id == R.id.ivBack) {
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
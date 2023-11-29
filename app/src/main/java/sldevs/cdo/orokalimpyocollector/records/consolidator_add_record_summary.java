package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class consolidator_add_record_summary extends AppCompatActivity implements View.OnClickListener {

    TextView tvSegregationId,tvSegregationIdO,tvConsolidatorId,tvConsolidatorIdO,tvConsolidatorName,tvConsolidatorNameO,tvDateTime,tvDateTimeO,tvWasteType,tvWasteTypeO,tvPlasticType,tvPlasticWasteName,tvBrand,tvBrandO,tvKilo,tvKiloO;
    Button btnUpload,btnEdit;

    String segregated_id,waste_type,plastic_type,plastic_name,brand,kilo,date,time;
    LinearLayout llPlastic,llOther;
    ProgressBar pbLoading;

    FirebaseAuth mAuth;

    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_add_record_summary);
        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();


        tvSegregationId = findViewById(R.id.tvSegregationId);
        tvSegregationIdO = findViewById(R.id.tvSegregationIdO);
        tvConsolidatorId = findViewById(R.id.tvConsolidatorId);
        tvConsolidatorIdO = findViewById(R.id.tvConsolidatorIdO);
        tvConsolidatorName = findViewById(R.id.tvConsolidatorName);
        tvConsolidatorNameO = findViewById(R.id.tvConsolidatorNameO);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvDateTimeO = findViewById(R.id.tvDateTimeO);
        tvWasteType = findViewById(R.id.tvWasteType);
        tvWasteTypeO = findViewById(R.id.tvWasteTypeO);
        tvPlasticType = findViewById(R.id.tvPlasticType);
        tvPlasticWasteName = findViewById(R.id.tvPlasticWasteName);
        tvBrand = findViewById(R.id.tvBrand);
        tvBrandO = findViewById(R.id.tvBrandO);
        tvKilo = findViewById(R.id.tvKilo);
        tvKiloO = findViewById(R.id.tvKiloO);

        btnUpload = findViewById(R.id.btnUpload);
        btnEdit = findViewById(R.id.btnEdit);

        llPlastic = findViewById(R.id.llPlastic);
        llOther = findViewById(R.id.llOther);
        pbLoading = findViewById(R.id.pbLoading);


        segregated_id = getIntent().getStringExtra("segregated_id");
        waste_type = getIntent().getStringExtra("waste_type");
        plastic_type = getIntent().getStringExtra("plastic_type");
        plastic_name = getIntent().getStringExtra("plastic_name");
        brand = getIntent().getStringExtra("brand");
        kilo = getIntent().getStringExtra("kilo");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        setSummaryValues();
        fc.retrieveName(this,consolidator_add_record_summary.this,mAuth.getUid(),tvConsolidatorName,tvConsolidatorNameO,waste_type);

        btnUpload.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnUpload){
            pbLoading.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            if (waste_type.equalsIgnoreCase("Plastic Waste")){
                fc.sendSegregatedWasteData(this,consolidator_add_record_summary.this,pbLoading,btnUpload,btnEdit,tvConsolidatorName.getText().toString(),segregated_id,waste_type,plastic_type,plastic_name,brand,kilo,date,time);
            }else{
                fc.sendSegregatedWasteData(this,consolidator_add_record_summary.this,pbLoading,btnUpload,btnEdit,tvConsolidatorNameO.getText().toString(),segregated_id,waste_type,plastic_type,plastic_name,brand,kilo,date,time);

            }
        }else if(id == R.id.btnEdit){
            Intent i = new Intent(consolidator_add_record_summary.this,consolidator_add_record.class);
            startActivity(i);
            finish();
        }
    }

    public void setSummaryValues(){
        if(waste_type.equalsIgnoreCase("Plastic Waste")){
            llPlastic.setVisibility(View.VISIBLE);
            tvSegregationId.setText(segregated_id);
            tvConsolidatorId.setText(mAuth.getUid());
            tvDateTime.setText(date + " " + time);
            tvWasteType.setText(waste_type);
            tvPlasticType.setText(plastic_type);
            tvPlasticWasteName.setText(plastic_name);
            tvBrand.setText(brand);
            tvKilo.setText(kilo);

        }else{
            llOther.setVisibility(View.VISIBLE);
            tvSegregationIdO.setText(segregated_id);
            tvConsolidatorIdO.setText(mAuth.getUid());
            tvDateTimeO.setText(date + " " + time);
            tvWasteTypeO.setText(waste_type);
            tvBrandO.setText(brand);
            tvKiloO.setText(kilo);
        }
    }
}
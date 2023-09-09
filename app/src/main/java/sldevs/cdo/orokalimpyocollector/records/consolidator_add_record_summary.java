package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class consolidator_add_record_summary extends AppCompatActivity implements View.OnClickListener {

    TextView tvSegregationId,tvSegregationIdO,tvWasteType,tvWasteTypeO,tvPlasticType,tvPlasticWasteName,tvBrand,tvBrandO,tvKilo,tvKiloO;
    Button btnUpload,btnEdit;

    String segregated_id,waste_type,plastic_type,plastic_name,brand,kilo;
    LinearLayout llPlastic,llOther;



    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_add_record_summary);

        fc = new firebase_crud();


        tvSegregationId = findViewById(R.id.tvSegregationId);
        tvSegregationIdO = findViewById(R.id.tvSegregationIdO);
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

        segregated_id = getIntent().getStringExtra("segregated_id");
        waste_type = getIntent().getStringExtra("waste_type");
        plastic_type = getIntent().getStringExtra("plastic_type");
        plastic_name = getIntent().getStringExtra("plastic_name");
        brand = getIntent().getStringExtra("brand");
        kilo = getIntent().getStringExtra("kilo");

        setSummaryValues();

        btnUpload.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnUpload){
            fc.sendSegregatedWasteData(this,getApplicationContext(),segregated_id,waste_type,plastic_type,plastic_name,brand,kilo);
        }else if(id == R.id.btnEdit){
            finish();
        }
    }

    public void setSummaryValues(){
        if(waste_type.equalsIgnoreCase("Plastic Waste")){
            llPlastic.setVisibility(View.VISIBLE);
            tvSegregationId.setText(segregated_id);
            tvWasteType.setText(waste_type);
            tvPlasticType.setText(plastic_type);
            tvPlasticWasteName.setText(plastic_name);
            tvBrand.setText(brand);
            tvKilo.setText(kilo);

        }else{
            llOther.setVisibility(View.VISIBLE);
            tvSegregationIdO.setText(segregated_id);
            tvWasteTypeO.setText(waste_type);
            tvBrandO.setText(brand);
            tvKiloO.setText(kilo);
        }
    }
}
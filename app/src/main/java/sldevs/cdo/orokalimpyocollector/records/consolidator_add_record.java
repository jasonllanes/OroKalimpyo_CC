package sldevs.cdo.orokalimpyocollector.records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import sldevs.cdo.orokalimpyocollector.R;

public class consolidator_add_record extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    Button btnPlasticWaste,btnMetal,btnBottle,btnPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidator_add_record);


        ivBack = findViewById(R.id.ivBack);
        btnPlasticWaste = findViewById(R.id.btnPlasticWaste);
        btnMetal = findViewById(R.id.btnMetal);
        btnBottle = findViewById(R.id.btnBottle);
        btnPaper = findViewById(R.id.btnPaper);


        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        btnPlasticWaste.setOnClickListener(this);
        btnMetal.setOnClickListener(this);
        btnBottle.setOnClickListener(this);
        btnPaper.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnPlasticWaste){
            Intent i = new Intent(consolidator_add_record.this,consolidator_add_record_2.class);
            i.putExtra("waste_type","Plastic Waste");
            startActivity(i);
        }else if(id == R.id.btnMetal){
            Intent i = new Intent(consolidator_add_record.this,consolidator_add_record_2.class);
            i.putExtra("waste_type","Metal");
            startActivity(i);
        }else if(id == R.id.btnBottle){
            Intent i = new Intent(consolidator_add_record.this,consolidator_add_record_2.class);
            i.putExtra("waste_type","Glass Bottle");
            startActivity(i);
        }else if(id == R.id.btnPaper){
            Intent i = new Intent(consolidator_add_record.this,consolidator_add_record_2.class);
            i.putExtra("waste_type","Paper/Carton");
            startActivity(i);
        } else if (id == R.id.ivBack) {
            finish();
        }
    }
}
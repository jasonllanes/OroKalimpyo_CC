package sldevs.cdo.orokalimpyocollector.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sldevs.cdo.orokalimpyocollector.R;

public class choose_user_type extends AppCompatActivity implements View.OnClickListener {

    Button btnCollector,btnConsolidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);

        btnCollector = findViewById(R.id.btnCollector);
        btnConsolidator = findViewById(R.id.btnConsolidator);


        btnCollector.setOnClickListener(this);
        btnConsolidator.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnCollector){
            Intent i = new Intent(choose_user_type.this,log_in.class);
            i.putExtra("user_type","Waste Collectors");
            startActivity(i);
        }else if(id == R.id.btnConsolidator){
            Intent i = new Intent(choose_user_type.this,log_in.class);
            i.putExtra("user_type","Waste Consolidators");
            startActivity(i);
        }
    }
}
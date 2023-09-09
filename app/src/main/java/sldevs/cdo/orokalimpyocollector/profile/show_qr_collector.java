package sldevs.cdo.orokalimpyocollector.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class show_qr_collector extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageView ivQR;

    Button btnBack;

    firebase_crud fc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);

        ivQR = findViewById(R.id.ivQR);
        btnBack = findViewById(R.id.btnBack);


        fc = new firebase_crud();

        fc.retrieveQRCodeCollector(this,getApplicationContext(),mAuth.getUid(),ivQR);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
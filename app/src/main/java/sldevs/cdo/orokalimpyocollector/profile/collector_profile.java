package sldevs.cdo.orokalimpyocollector.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import de.hdodenhof.circleimageview.CircleImageView;
import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class collector_profile extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView tvName,tvCollectorType,tvContactPerson,tvNumber;
    Button btnBack;
    Bitmap bitmap;
    CircleImageView profile_image;

    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_profile);

        fc = new firebase_crud();
        profile_image = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvName);
        tvCollectorType = findViewById(R.id.tvCollectorType);
        tvContactPerson = findViewById(R.id.tvContactPerson);
        tvNumber = findViewById(R.id.tvNumber);


        btnBack = findViewById(R.id.btnBack);

        generateQRCode();
        fc.retrieveCollectorProfileAll(this,getApplicationContext(),mAuth.getUid(),tvName,tvCollectorType,tvContactPerson,tvNumber);

        profile_image.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnBack){
            finish();
        } else if (id == R.id.profile_image) {
            Intent i = new Intent(collector_profile.this, show_qr_collector.class);
            startActivity(i);
        }
    }

    public void generateQRCode(){

        QRGEncoder qrgEncoder = new QRGEncoder(mAuth.getUid(), null, QRGContents.Type.TEXT, 800);
        qrgEncoder.setColorBlack(Color.rgb(10,147,81));
        qrgEncoder.setColorWhite(Color.rgb(255,255,255));
        // Getting QR-Code as Bitmap
        bitmap = qrgEncoder.getBitmap();
        // Setting Bitmap to ImageView
        profile_image.setImageBitmap(bitmap);

    }
}
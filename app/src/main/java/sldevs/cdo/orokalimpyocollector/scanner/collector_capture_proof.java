package sldevs.cdo.orokalimpyocollector.scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class collector_capture_proof extends AppCompatActivity implements View.OnClickListener {

    ImageView ivProof;
    Bitmap image;

    Button btnCapture,btnBack,btnSend;

    String collector_id,collector_name,collector_type,user_id,user_type,household_type,establishment_type,others,name,barangay,location,number,email,waste_type,kilo;

    firebase_crud fc;
    SimpleDateFormat month,day,year,week,date,hours,minutes,seconds,time;
    String currentMonth,currentDay,currentYear,currentWeek,currentDate,currentHour,currentMinute,currentSeconds,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_capture_proof);

        fc = new firebase_crud();

        ivProof = findViewById(R.id.ivProof);
        btnCapture = findViewById(R.id.btnCapture);
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);

        collector_id = getIntent().getStringExtra("collector_id");
        collector_name = getIntent().getStringExtra("collector_name");
        collector_type = getIntent().getStringExtra("collector_type");
        user_id = getIntent().getStringExtra("user_id");
        user_type = getIntent().getStringExtra("user_type");
        household_type = getIntent().getStringExtra("household_type");
        establishment_type = getIntent().getStringExtra("establishment_type");
        others = getIntent().getStringExtra("others");
        name = getIntent().getStringExtra("name");
        barangay = getIntent().getStringExtra("barangay");
        location = getIntent().getStringExtra("location");
        number = getIntent().getStringExtra("number");
        email = getIntent().getStringExtra("email");
        waste_type = getIntent().getStringExtra("waste_type");
        kilo = getIntent().getStringExtra("kilo");

        retrieveDate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }

        btnCapture.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnSend.setOnClickListener(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                image = (Bitmap) data.getExtras().get("data");

//                    image = (Bitmap) data.getExtras().get("data");
//                    int dimension = Math.min(image.getWidth(), image.getHeight());
//                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100,baos);
                Bitmap newbitmap= BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
//                    newbitmap = Bitmap.createScaledBitmap(newbitmap, imageSize, imageSize, false);
                ivProof.setImageBitmap(newbitmap);
            }else{
                Uri dat = data.getData();
                image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                ivProof.setImageBitmap(image);
            }
        }else{
            //error message
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnCapture){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        } else if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnSend) {
            fc.sendWasteContribution(image,collector_capture_proof.this,getApplicationContext(),user_id,user_type,name,household_type,barangay,establishment_type,collector_name,collector_type,waste_type,kilo,
                    currentMonth,currentDay,currentYear,currentHour,currentMinute,currentSeconds,currentDate,currentTime);

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
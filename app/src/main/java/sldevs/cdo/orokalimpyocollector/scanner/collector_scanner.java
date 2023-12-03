package sldevs.cdo.orokalimpyocollector.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;

public class collector_scanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    String user_id,name,user_type,household_type,establishment_type,others,barangay,location,number,email;
    LinearLayout linearLayout;
    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector_scanner);

        fc = new firebase_crud();
        linearLayout = (LinearLayout) findViewById(R.id.mainLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            String details = result.getText();
                            String[] details_split = details.split("\n");
                            for (int i=0; i < details_split.length; i++){
                                user_id = details_split[0];
                            }

                            fc.retrieveHouseholdType(collector_scanner.this,getApplicationContext(),user_id,linearLayout);

                        } catch (ArrayIndexOutOfBoundsException e){
                            Snackbar snackbar = Snackbar
                                    .make(linearLayout, "Please scan a Waste Generator QR Code.", Snackbar.LENGTH_LONG).setTextColor(getResources().getColor(R.color.white)).setBackgroundTint(getResources().getColor(R.color.green));
                            snackbar.show();
                        }
//
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
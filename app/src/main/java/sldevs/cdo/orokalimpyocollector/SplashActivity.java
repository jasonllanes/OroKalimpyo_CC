package sldevs.cdo.orokalimpyocollector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sldevs.cdo.orokalimpyocollector.authentication.choose_user_type;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 8000;
    FirebaseAuth mAuth;
    ImageView ivLogo;

    String app_version;
    String currentVersion;


    TextView tvVersion;
    boolean isConnected;

    Dialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
//        ff = new firebase_functions();


        ivLogo = findViewById(R.id.ivLogo);
        tvVersion = findViewById(R.id.tvVersion);

//        ff.retrieveVersion(tvVersion);
        app_version = getResources().getString(R.string.version);




        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        ivLogo.startAnimation(animation);



        checkNetwork();


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        }else{
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        }else{
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                currentVersion = tvVersion.getText().toString();
//                Toast.makeText(SplashActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SplashActivity.this, choose_user_type.class);
                startActivity(i);
                finish();


//                if(isConnected == false){
//                    Toast.makeText(SplashActivity.this, "No internet connection. Please try to open the application again.", Toast.LENGTH_SHORT).show();
//                }else{
//                    if (user != null) {
//                        if(!(app_version.equalsIgnoreCase(currentVersion))){
//                            Toast.makeText(SplashActivity.this, "Please update your application.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SplashActivity.this, home.class);
//                            startActivity(intent);
//                            finish();
//                        }else{
//                            Intent intent = new Intent(SplashActivity.this, home.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }else{
//                        if(!(app_version.equalsIgnoreCase(currentVersion))){
//                            Toast.makeText(SplashActivity.this, "Please update your application.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SplashActivity.this, getting_started.class);
//                            startActivity(intent);
//                            finish();
//                        }else{
//                            SharedPreferences sharedPreferences = getSharedPreferences("getting_started",MODE_PRIVATE);
//                            String value = sharedPreferences.getString("was_launched", "");
//
//                            if(value.equalsIgnoreCase("true")){
//                                Intent intent = new Intent(SplashActivity.this, log_in.class);
//                                startActivity(intent);
//                                finish();
//                            }else{
//                                Intent intent = new Intent(SplashActivity.this, getting_started.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//
//
//                    }
//                }




            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void checkNetwork(){
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager connectivityManager =
                null;

        connectivityManager = (ConnectivityManager) getSystemService(ConnectivityManager.class);

        connectivityManager.requestNetwork(networkRequest, networkCallback);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);

        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Toast.makeText(SplashActivity.this, "Internet connection lost", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
    };
}
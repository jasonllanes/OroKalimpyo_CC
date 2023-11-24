package sldevs.cdo.orokalimpyocollector.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sldevs.cdo.orokalimpyocollector.R;
import sldevs.cdo.orokalimpyocollector.firebase.firebase_crud;
import sldevs.cdo.orokalimpyocollector.home.collector_home;

public class log_in extends AppCompatActivity implements View.OnClickListener {

    TextView tvUserType,tvSignUp,tvForgotPassword;
    EditText etEmail,etPassword;
    Button btnLogin;

    ImageView ivLogo;

    ProgressBar pbLoading;

    firebase_crud fc;

    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        fc = new firebase_crud();

        tvUserType = findViewById(R.id.tvUserType);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        pbLoading = findViewById(R.id.pbLoading);

        ivLogo = findViewById(R.id.ivLogo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        ivLogo.startAnimation(animation);

        user_type = getIntent().getStringExtra("user_type");
        tvUserType.setText(user_type + " Log In");


        btnLogin.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            btnLogin.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (etEmail.getText().toString().isEmpty()) {
                btnLogin.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                etEmail.setError("Please enter your email.");
            } else if (!isValidEmail(etEmail.getText().toString())) {
                btnLogin.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                etEmail.setError("Invalid email.");
            } else if (etPassword.getText().toString().isEmpty()) {
                btnLogin.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                etPassword.setError("Password is empty.");
            }else if (etPassword.getText().toString().length() < 6) {
                btnLogin.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                etPassword.setError("Password should be more 6 characters.");
            } else {
                fc.logInCollector(this, getApplicationContext(),user_type,etEmail.getText().toString(),password,pbLoading,btnLogin);
            }
        }
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
package com.example.wifiscan.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifiscan.R;
import com.example.wifiscan.model.UserModel;
import com.example.wifiscan.ui.viewModels.UserViewModel;


public class SignUpActivity extends AppCompatActivity {

    private Button signUp;
    private TextView signUpAddress;
    private TextView signUpEmail;
    private TextView signUpPhone;
    private TextView signUpPassword;
    private TextView signUpNatID;
    private TextView signUpName;
    private TextView signUpJob;
    private TextView signUpBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        UserViewModel userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.firebaseUserMutableLiveData.observe(this,s -> {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            if(s.equals("Sign up successfully")){
                Intent i=new Intent(SignUpActivity.this,Holder.class);
                startActivity(i);
            }
        });

        signUp=findViewById(R.id.signup);
        signUpName=findViewById(R.id.signupusername);
        signUpEmail=findViewById(R.id.signupemail);
        signUpPassword=findViewById(R.id.signuppassword);
        signUpPhone=findViewById(R.id.signupphone);
        signUpNatID=findViewById(R.id.signupnatid);
        signUpJob=findViewById(R.id.signupjob);
        signUpAddress=findViewById(R.id.signupaddress);
        signUpBack=findViewById(R.id.signupbaktext);

        signUp.setOnClickListener(v -> {
            UserModel user = new UserModel();
            user.setAddress(signUpAddress.getText().toString());
            user.setEmail(signUpEmail.getText().toString());
            user.setJob(signUpJob.getText().toString());
            user.setName(signUpName.getText().toString());
            user.setNationalID(signUpNatID.getText().toString());
            user.setPassword(signUpPassword.getText().toString());
            user.setPhone(signUpPhone.getText().toString());
            userViewModel.signUp(user);
        });

        signUpBack.setOnClickListener(v -> {
            Intent i=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(i);
        });
    }
}
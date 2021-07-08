package com.example.wifiscan.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifiscan.R;
import com.example.wifiscan.ui.viewModels.UserViewModel;


public class SignInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UserViewModel userViewModel= new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.isSigned();


        setContentView(R.layout.activity_sign_in);


        Button signIn=findViewById(R.id.signin);
        TextView signUp=findViewById(R.id.signinsignup);

        userViewModel.firebaseUserMutableLiveData.observe(this,s -> {
            switch (s) {
                case "You are already signed in":
                case "log in successfully":
                    Intent intent = new Intent(SignInActivity.this, Holder.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            }
        });



        signIn.setOnClickListener(v -> {
            EditText email=findViewById(R.id.signinemail);
            EditText password=findViewById(R.id.signinpassword);
            userViewModel.signIn(email.getText().toString(),password.getText().toString());
        });

        signUp.setOnClickListener(v -> {
            Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
            startActivity(intent);
        });
    }


}
package com.example.smartbus;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth mAuth;
    EditText editTextEmail;
    TextInputEditText editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextPassword = (TextInputEditText) findViewById(R.id.editPassword);
        progressBar=findViewById(R.id.progressBar);

        mAuth=FirebaseAuth.getInstance();

        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.logIn).setOnClickListener(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6 || password.length()>15){
            editTextPassword.setError("Password should be of 6-15 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(a);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.sign_up:
                Intent intentSignup = new Intent(this,SignUpActivity.class);
                intentSignup.addFlags(intentSignup.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSignup);
                finish();
                break;

            case R.id.logIn:
                userLogin();
                break;
        }

    }
}

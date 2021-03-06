package com.example.smartbus;

import android.content.Intent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressBar progressBar;
    EditText editTextEmail;
    TextInputEditText editTextPassword,editTextName,phone1,phone2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword= (TextInputEditText) findViewById((R.id.Password));
        editTextName=(TextInputEditText) findViewById(R.id.name);
        phone1=findViewById(R.id.phone1);
        phone2=findViewById(R.id.phone2);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.log_in).setOnClickListener(this);
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name=editTextName.getText().toString().trim();
        final String ph1=phone1.getText().toString().trim();
        final String ph2=phone2.getText().toString().trim();

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

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            //editTextPassword.setError("Password should contain atleast one upper case alphabet");
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String PhoneChars = "(^[0-9]*$)";
        if(ph1.length()!=10 || !ph1.matches(PhoneChars))
        {
            phone1.setError("invalid phone number");
            phone1.requestFocus();
            return;
        }
        if(ph2.length()!=10 || !ph1.matches(PhoneChars))
        {
            phone2.setError("invalid phone number");
            phone1.requestFocus();
            return;
        }

        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers))
        {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars ))
        {
            editTextPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    FirebaseUser user = mAuth.getCurrentUser(); //this wil fetch the firebase user obj
                    final String userId = user.getUid();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                User user=new User(ph1,ph2);
//                                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("phone1").setValue(phone1);
                               FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                Toast.makeText(getApplicationContext(),"registered successfully",Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(a);
                            }
                        }
                    });

                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        editTextPassword.setText("");
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        editTextPassword.setText("");
                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent a = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(a);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.signUp:
                registerUser();
                break;

            case R.id.log_in:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                finish();

                break;
        }
    }
}

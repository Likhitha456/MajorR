package com.example.smartbus;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class CallActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    DatabaseReference ref;//wait
    private EditText mEditTextNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        final String number;
//        mEditTextNumber = findViewById(R.id.phone);
        ImageView imageCall = findViewById(R.id.call_img);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();


            }
        });
    }
    private void makePhoneCall() {
        User user=new User();

        ref=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //User user = dataSnapshot.getValue(User.class);
                String number = dataSnapshot.child("phone2").getValue().toString();
                if (number.trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(CallActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CallActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        String dial = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                } else {
                    Toast.makeText(CallActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error while fetching data",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
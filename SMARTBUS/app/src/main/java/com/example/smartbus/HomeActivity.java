package com.example.smartbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.UserInfo;

public class HomeActivity extends AppCompatActivity {
    ImageView onClick1,onClick2,onClick3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onClick1=findViewById(R.id.imageView1);
        onClick2=findViewById(R.id.imageView2);
        onClick3=findViewById(R.id.imageView3);

        ImageView[] imageViews={onClick1,onClick2,onClick3};
        for (int i=0;i<imageViews.length;i++){
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imageView1:Intent intent1= new Intent(HomeActivity.this,CallActivity.class);
                        startActivity(intent1);
                        break;
                        //case R.id.imageView2:Intent intent3=new Intent(HomeActivity.this,SeatProbability.class);
                      //  startActivity(intent3);
                        //break;
                        case R.id.imageView3:Intent intent2=new Intent(HomeActivity.this,SeatProbability.class);
                            startActivity(intent2);
                            break;
                    }
                }
            });
        }

    }

}

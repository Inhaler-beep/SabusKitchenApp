package com.example.sabuskitchen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.InternalTokenProvider;

import java.net.NoRouteToHostException;

public class MainActivity extends AppCompatActivity {

    private static int STATIC_COUNT = 1500;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        UsersRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                FirebaseUser currenUser = mAuth.getCurrentUser();
                if(currenUser!=null)
                {

                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();

                }
                else
                {
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        },STATIC_COUNT);
    }
}
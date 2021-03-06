package com.example.sabuskitchen;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private CardView MessCardView,AddCardView,DueCardView,PaymentCardView;
    private ImageButton LogoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MessCardView = (CardView) findViewById(R.id.mess_card);
        AddCardView = (CardView) findViewById(R.id.add_card);
        LogoutButton = (ImageButton) findViewById(R.id.logout_button);
        DueCardView = (CardView) findViewById(R.id.dueCardView);
        PaymentCardView = (CardView) findViewById(R.id.payment_cardView);

        mAuth = FirebaseAuth.getInstance();


        MessCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(HomeActivity.this,MessActivity.class);
                startActivity(messIntent);
            }
        });

        AddCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(HomeActivity.this,AddActivity.class);
                startActivity(addIntent);
            }
        });

        DueCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dueIntent = new Intent(HomeActivity.this,DuesActivity.class);
                startActivity(dueIntent);
            }
        });

        PaymentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(HomeActivity.this, PaymentActivity.class);
                startActivity(paymentIntent);
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();




    }
}
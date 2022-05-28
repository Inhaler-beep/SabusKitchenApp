package com.example.sabuskitchen;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private CardView MessCardView,AddCardView,DueCardView,PaymentCardView,AddMenu,Menucard,OrdersCard,ReportCard,AddEmp,AddTable;
    private ImageButton LogoutButton;
    private FirebaseAuth mAuth;
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String Mode = "Employee";
     String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(currentuser.equals("tdahKAdP0AfSfASSKgOi2Rt02R92"))
        {
            Mode = "Admin";
        }
        setContentView(R.layout.activity_home);

        MessCardView = (CardView) findViewById(R.id.mess_card);
        AddCardView = (CardView) findViewById(R.id.add_card);
        LogoutButton = (ImageButton) findViewById(R.id.logout_button);
        DueCardView = (CardView) findViewById(R.id.dueCardView);
        PaymentCardView = (CardView) findViewById(R.id.payment_cardView);
        AddMenu = (CardView) findViewById(R.id.add_menu_card);
        Menucard = (CardView) findViewById(R.id.menu_card);
        OrdersCard = (CardView) findViewById(R.id.current_orders);
        ReportCard = (CardView) findViewById(R.id.completed_orders);
        AddEmp = (CardView) findViewById(R.id.emp_add_card);
        AddTable = (CardView) findViewById(R.id.add_table_card);

        mAuth = FirebaseAuth.getInstance();

        if(!Mode.equals("Admin"))
        {
            PaymentCardView.setClickable(false);
            PaymentCardView.setEnabled(false);
            PaymentCardView.setCardBackgroundColor(Color.parseColor("#dfe4ea"));

            ReportCard.setEnabled(false);
            ReportCard.setClickable(false);
            ReportCard.setCardBackgroundColor(Color.parseColor("#dfe4ea"));

            AddMenu.setEnabled(false);
            AddMenu.setClickable(false);
            AddMenu.setCardBackgroundColor(Color.parseColor("#dfe4ea"));


            DueCardView.setEnabled(false);
            DueCardView.setClickable(false);
            DueCardView.setCardBackgroundColor(Color.parseColor("#dfe4ea"));


            AddCardView.setClickable(false);
            AddCardView.setEnabled(false);
            AddCardView.setCardBackgroundColor(Color.parseColor("#dfe4ea"));

            AddEmp.setClickable(false);
            AddEmp.setEnabled(false);
            AddEmp.setCardBackgroundColor(Color.parseColor("#dfe4ea"));

            AddTable.setClickable(false);
            AddTable.setEnabled(false);
            AddTable.setCardBackgroundColor(Color.parseColor("#dfe4ea"));

        }


        ReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(HomeActivity.this,CompletedOrders.class);
                startActivity(messIntent);
            }
        });


        MessCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(HomeActivity.this,MessActivity.class);
                startActivity(messIntent);
            }
        });
        AddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(HomeActivity.this,TableActivity.class);
                startActivity(messIntent);
            }
        });

        AddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(HomeActivity.this,AddEmpActivity.class);
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
        AddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(HomeActivity.this,AddMenu.class);
                startActivity(menuIntent);
            }
        });
        Menucard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(HomeActivity.this,MenuActivity.class);
                startActivity(menuIntent);
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
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        OrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(HomeActivity.this, ChefActivity.class);
                startActivity(loginIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();



    }
}
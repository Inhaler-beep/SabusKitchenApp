package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.TextDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;
import com.google.zxing.Result;
import com.gpfreetech.awesomescanner.ui.GpCodeScanner;
import com.gpfreetech.awesomescanner.ui.ScannerView;
import com.gpfreetech.awesomescanner.util.DecodeCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    private String uniqueid,paymentid;
    private LottieAnimationView lottieAnimationView;
    private Button menuButton;
    private DatabaseReference OrdersRef;
    private TextView payment_id,paymentdate,totalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        uniqueid = getIntent().getExtras().getString("uniqueid");
        paymentid = getIntent().getExtras().getString("paymentid");
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottie_main);
        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        payment_id = (TextView) findViewById(R.id.payment_id);
        paymentdate = (TextView) findViewById(R.id.payment_date);
        totalamount = (TextView) findViewById(R.id.payment_amount);
        payment_id.setText(paymentid);
        menuButton = (Button) findViewById(R.id.gotomenu_button);



        OrdersRef.child(uniqueid).child("details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String paydate = "Date of Payment  : "+snapshot.child("date").getValue().toString();
                String payamount = "Amount Paid  : ₹"+snapshot.child("totalamount").getValue().toString();
                paymentdate.setText(paydate);
                totalamount.setText(payamount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(OrderActivity.this,ChefActivity.class);
                startActivity(menuIntent);
                finish();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}



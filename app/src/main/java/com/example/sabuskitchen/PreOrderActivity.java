package com.example.sabuskitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PreOrderActivity extends AppCompatActivity   {
    private ArrayList<String> orderItems;
    private LinearLayout placeOrder;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<Map.Entry<String, String> > listOfEntry;
    private DatabaseReference MenuRef;
    private EditText userInput;
    private ProgressDialog progressDialog;
    private ArrayList<String> ordereditem;
    private Button PlaceOrderGreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        orderItems = getIntent().getStringArrayListExtra("orderitems");
        int count = orderItems.size();


        progressDialog = new ProgressDialog(PreOrderActivity.this);
        progressDialog.setTitle("Loading Menu");


        recyclerView = (RecyclerView) findViewById(R.id.orders_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
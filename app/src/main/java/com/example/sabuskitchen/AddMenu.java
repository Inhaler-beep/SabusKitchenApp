package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMenu extends AppCompatActivity {

    private AutoCompleteTextView actv;
    String[] fruits = {"Starters","Appetizer", "Main Course", "Juices", "Shakes"};
    private FirebaseDatabase mn = FirebaseDatabase.getInstance();
    private DatabaseReference menu = mn.getReference().child("Menu");
    private Button addmenu;
    private EditText menu_name, menu_count,menu_price,menu_category;
    private String menuName, menuCount, menuPrice,menuCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
        actv.setInputType(1);
        menu_name = (EditText) findViewById(R.id.dish_name);
        menu_count = (EditText) findViewById(R.id.menu_count);
        menu_price = (EditText) findViewById(R.id.dish_price);
        menu_category = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        addmenu = (Button) findViewById(R.id.add_menu);
        addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuName = menu_name.getText().toString();
                menuPrice = menu_price.getText().toString();
                menuCount = menu_count.getText().toString();
                menuCategory = menu_category.getText().toString();

                HashMap<String,String> userMap = new HashMap<>();
                userMap.put("dishname",menuName);
                userMap.put("price",menuPrice);
                userMap.put("count",menuCount);
                userMap.put("category",menuCategory);


                menu.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddMenu.this, "Menu Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });





    }
}
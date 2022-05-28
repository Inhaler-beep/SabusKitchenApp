package com.example.sabuskitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {
    private ImageView sabusLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);



        sabusLogo = (ImageView) findViewById(R.id.sabuslogo);





        sabusLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(LandingActivity.this, "Long pressed", Toast.LENGTH_SHORT).show();

                return false;

            }
        });
    }
}
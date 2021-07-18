package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RenewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    private ProgressDialog mLoadingBar;
    private String expiry;
    private EditText dateInput,nameInput;
    private String mode;
    private TextView amountText;
    private Integer count = 1;
    private AutoCompleteTextView actv;
    private RadioButton Ten,Twenty,Thirty,Fifteen;
    private Integer days;
    private  String UserKey;
    private Button submitButton;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr;
    String[] fruits = {"St. Joseph's Hostel", "St. George Hostel", "St. Chavara Hostel", "St. Paul's Hostel", "Kristu Jayanti College", "Outsiders", "St. Aloysious Hostel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        actv = (AutoCompleteTextView) findViewById(R.id.RautoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
        dr = FirebaseDatabase.getInstance().getReference().child("Customers");

        mLoadingBar = new ProgressDialog(RenewActivity.this);

        dateInput = (EditText) findViewById(R.id.rdateInput);
        nameInput = (EditText) findViewById(R.id.RnameInput);
        submitButton =(Button) findViewById(R.id.rsubmit_button  );
        Ten = (RadioButton) findViewById(R.id.rtendays);
        Twenty = (RadioButton) findViewById(R.id.rtwentydays);
        Fifteen = (RadioButton) findViewById(R.id.rfifteendays);
        Thirty = (RadioButton) findViewById(R.id.rthirtydays);
        amountText = (TextView) findViewById(R.id.ramount_text);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rRadioHroup);

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String name = snapshot.child(UserKey).child("name").getValue().toString();
                String hostelname = snapshot.child(UserKey).child("phone").getValue().toString();
                nameInput.setText(name);
                actv.setText(hostelname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rtendays:
                        mode = "10";
                        days = 10;
                        amountText.setText("Rs. 1200");

                        break;
                    case R.id.rtwentydays:
                        mode = "20";
                        days = 20;
                        amountText.setText("Rs. 2400");

                        break;
                    case R.id.rthirtydays:
                        mode = "30";
                        days = 30;
                        amountText.setText("Rs. 3600");
                        break;

                    case R.id.rfifteendays:
                        mode = "15";
                        days = 15;
                        amountText.setText("Rs. 1800");
                        break;

                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateUserData();

            }
        });

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().matches("^[A-Za-z ]+$"))
                {
                    nameInput.setBackground(getDrawable(R.drawable.round_border_red));

                }
                else
                {
                    nameInput.setBackground(getDrawable(R.drawable.round_border));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void validateUserData()
    {
        String name = nameInput.getText().toString();
        String date = dateInput.getText().toString();
        String hostelname = actv.getText().toString();


        if(name.isEmpty())
        {
            Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
        }
        else if(date.isEmpty())
        {
            Toast.makeText(this, "Enter the joining date", Toast.LENGTH_SHORT).show();
        }
        else if(hostelname.isEmpty())
        {

            Toast.makeText(this, "Enter the hostelname", Toast.LENGTH_SHORT).show();
        }
        else if(!name.matches("^[A-Za-z ]+$")){
            Toast.makeText(this, "Please Enter a Valid Name", Toast.LENGTH_SHORT).show();

        }
        else
        {
            mLoadingBar.setTitle("Loading");
            mLoadingBar.setMessage("Please wait while we add the user");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            Map<String,Object> userMap = new HashMap<>();
            userMap.put("name",name);
            userMap.put("phone",hostelname);
            userMap.put("date",date);
            userMap.put("mode",mode);
            userMap.put("expiry",expiry);

            dr.child(UserKey).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(RenewActivity.this, "Successfully Rennewed", Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RenewActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
                        finish();
                    }

                }
            });

        }
    }






    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String currentStringDate = sdf1.format(c.getTime());
        dateInput.setText(currentStringDate);

        c.add(Calendar.DATE, days);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

        //expirydate = sdf1.format(c.getTime());
        expiry = sdf1.format(c.getTime());
        String sDate1=expiry;
        Date date1;
        try {
            date1=new SimpleDateFormat("dd-MM-yyyy").parse(expiry);
            Toast.makeText(this, "output is " +expiry, Toast.LENGTH_LONG).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    public void editdatebutton(View view)
    {
        if((Ten.isChecked() == true) || (Twenty.isChecked() == true) || (Thirty.isChecked() == true) || (Fifteen.isChecked() == true))
        {
            DialogFragment date = new DatePickerFragment();
            date.show(getSupportFragmentManager(),"date picker");

        }
        else
        {
            Toast.makeText(this, "Please Select Plan", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        UserKey = getIntent().getExtras().getString("userkey").toString();


    }
}
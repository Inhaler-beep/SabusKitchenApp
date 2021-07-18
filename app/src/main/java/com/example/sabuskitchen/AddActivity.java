package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ProgressDialog mLoadingBar;
    private String expiry;
    private EditText dateInput,nameInput;
    private String mode,payment_mode;
    private TextView amountText;
    private Integer count = 1;
    private AutoCompleteTextView actv;
    private RadioButton Ten,Twenty,Thirty,Fifteen,GooglePay,CashPay,PayLater;
    private Integer days = 0;
    private Button  submitButton;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr = db.getReference().child("Customers");
    private FirebaseDatabase userDue = FirebaseDatabase.getInstance();
    private DatabaseReference dues = userDue.getReference().child("Dues");
    private FirebaseDatabase payments = FirebaseDatabase.getInstance();
    private DatabaseReference payment = payments.getReference().child("Payments");
    
    String[] fruits = {"St. Joseph's Hostel", "St. George Hostel", "St. Chavara Hostel", "St. Paul's Hostel", "Kristu Jayanti College", "Outsiders", "St. Aloysious Hostel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
        actv.setInputType(1);

        mLoadingBar = new ProgressDialog(AddActivity.this);

        dateInput = (EditText) findViewById(R.id.dateInput);
        nameInput = (EditText) findViewById(R.id.nameInput);
        submitButton =(Button) findViewById(R.id.submit_button  );
        Ten = (RadioButton) findViewById(R.id.tendays);
        Twenty = (RadioButton) findViewById(R.id.twentydays);
        Fifteen = (RadioButton) findViewById(R.id.fifteendays); 
        Thirty = (RadioButton) findViewById(R.id.thirtydays);
        amountText = (TextView) findViewById(R.id.amount_text);
        GooglePay = (RadioButton) findViewById(R.id.googlepay);
        CashPay = (RadioButton) findViewById(R.id.cashpayment);
        PayLater  = (RadioButton) findViewById(R.id.pay_later);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioHroup);
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tendays:
                        mode = "10";
                        days = 10;
                        amountText.setText("Rs. 1200");
                        dateInput.setText("");

                        break;
                    case R.id.twentydays:
                        mode = "20";
                        days = 20;
                        amountText.setText("Rs. 2400");
                        dateInput.setText("");

                        break;
                    case R.id.thirtydays:
                        mode = "30";
                        days = 30;
                        amountText.setText("Rs. 3600");
                        dateInput.setText("");
                        break;

                    case R.id.fifteendays:
                        mode = "15";
                        days = 15;
                        amountText.setText("Rs. 1800");
                        dateInput.setText("");
                        break;

                }
            }
        });
        
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.RadioGroup);
        
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId) {
                    case R.id.googlepay:
                        payment_mode = "GooglePay";
                        break;
                        
                    case R.id.cashpayment:
                        payment_mode = "Cash";
                        break;
                        
                    case R.id.pay_later:
                        payment_mode = "PayLater";
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

            HashMap<String,String> userMap = new HashMap<>();
            userMap.put("name",name);
            userMap.put("phone",hostelname);
            userMap.put("date",date);
            userMap.put("mode",mode);
            userMap.put("expiry",expiry);
            userMap.put("paymentmode",payment_mode);

            

            
            if(payment_mode.equals("PayLater"))
            {
                Toast.makeText(this, "Paylater", Toast.LENGTH_SHORT).show();
                dues.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) 
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddActivity.this, "Payment is Due", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            
            dr.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) 
                {
                    if(task.isSuccessful())
                    {
                        if(!payment_mode.equals("PayLater"))
                        {
                            payment.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(AddActivity.this, "Payment Added", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                        Toast.makeText(AddActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
                        AlertDialog.Builder adb = new AlertDialog.Builder(AddActivity.this);
                        adb.setView(R.layout.popup_add);
                        adb.setTitle("Added");
                        adb.setIcon(R.drawable.ic_person);
                        adb.setCancelable(false);


                        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                finish();
                            }
                        });

                        adb.show();


                    
                    }
                    else
                    {
                        Toast.makeText(AddActivity.this, "Ho..its failed", Toast.LENGTH_SHORT).show();
                        mLoadingBar.dismiss();
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
}
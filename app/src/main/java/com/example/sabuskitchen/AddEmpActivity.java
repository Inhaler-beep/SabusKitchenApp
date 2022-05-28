package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AddEmpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button addEmp;
    private EditText empName,empSalary,empPhone,empDate;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr = db.getReference().child("Employees");
    private ProgressDialog mLoadingBar;
    private FirebaseAuth mAuth;
    private String empuser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emp);

        mLoadingBar = new ProgressDialog(AddEmpActivity.this);
        mAuth = FirebaseAuth.getInstance();

        addEmp = (Button) findViewById(R.id.emp_submit_button);
        empName = (EditText) findViewById(R.id.emp_nameInput);
        empSalary = (EditText) findViewById(R.id.emp_salary);
        empDate = (EditText) findViewById(R.id.emp_dateInput);
        empPhone = (EditText) findViewById(R.id.emp_phonenumber);
        empDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(),"date picker");
            }
        });
        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserData();

            }
        });

    }

    private void validateUserData()
    {
        String uniqueID = UUID.randomUUID().toString();
        String name = empName.getText().toString();
        String date  = empDate.getText().toString();
        String phone = empPhone.getText().toString();
        String salary = empSalary.getText().toString();


        if(name.isEmpty())
        {
            Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show();
        }
        else if(date.isEmpty())
        {
            Toast.makeText(this, "Enter the joining date", Toast.LENGTH_SHORT).show();
        }
        else if(phone.isEmpty())
        {

            Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show();
        }
        else if(!name.matches("^[A-Za-z ]+$")){
            Toast.makeText(this, "Please Enter a Valid Name", Toast.LENGTH_SHORT).show();

        }
        else if(salary.isEmpty())
        {
            Toast.makeText(this, "Enter the salary", Toast.LENGTH_SHORT).show();

        }
        else
        {
            mLoadingBar.setTitle("Loading");
            mLoadingBar.setMessage("Please wait while we add the user");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            HashMap<String,String> userMap = new HashMap<>();
            userMap.put("empname",name);
            userMap.put("empphone",phone);
            userMap.put("empdate",date);
            userMap.put("empsalary",salary);
            String email = name.trim().toLowerCase()+"@megabite.com";
            String password = name.toLowerCase().replaceAll("\\s","")+(phone.substring(0,2));
            userMap.put("empemail",email.replaceAll("\\s",""));


            mAuth.createUserWithEmailAndPassword(email.replaceAll("\\s", ""), password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                empuser = user.getUid();
                                String adminemail = "admin@megabite.com";
                                String adminpassword = "admin@123";
                                pushempdata(empuser,userMap);
                                loginasadmin(adminemail,adminpassword);


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AddEmpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });







        }
    }

    public void pushempdata(String empuser,HashMap userMap) {
        dr.child(empuser).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddEmpActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                mLoadingBar.dismiss();
                AlertDialog.Builder adb = new AlertDialog.Builder(AddEmpActivity.this);
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
        });

    }

    private void loginasadmin(String adminemail, String adminpassword) {

        mAuth.signInWithEmailAndPassword(adminemail, adminpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());

                        }
                    }
                });
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
        empDate.setText(currentStringDate);





    }



}
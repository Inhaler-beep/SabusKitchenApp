package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Usermess extends AppCompatActivity {
    private Switch breakfast,lunch,dinner;
    private String facultykey,customername,userhostel;
    public String faculty1234key;
    private DatabaseReference UsersRef;
    private TextView UserName, HostelName,UserDate,DueText;
    private String currentdate;
    private FirebaseFirestore db;
    private String breakfast1 = "False";
    private String lunch1 = "False";
    private String dinner1 = "False";
    private ProgressDialog progressDialog;
    private Button renewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermess);
        breakfast = (Switch) findViewById(R.id.switch1);
        lunch = (Switch) findViewById(R.id.switch2);
        dinner = (Switch) findViewById(R.id.switch3);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        UserName = (TextView) findViewById(R.id.UserName);
        HostelName = (TextView) findViewById(R.id.HosetName);
        UserDate   = (TextView) findViewById(R.id.userprofiledate);
        progressDialog = new ProgressDialog(Usermess.this);
        progressDialog.setTitle("Loading...");
        DueText = (TextView) findViewById(R.id.due_text);
        renewButton = (Button) findViewById(R.id.update_button);




        Date date = new Date();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentdate = dateFormat.format(date);

        db = FirebaseFirestore.getInstance();




        breakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Create a new user with a first and last name
               if(isChecked == true)
               {
                   HashMap<String,String> userMap = new HashMap<>();
                   userMap.put("breakfast","True");
                   userMap.put("lunch",lunch1);
                   userMap.put("dinner",dinner1);
                   db.collection("Customers")
                           .document(facultykey+currentdate)
                           .set(userMap)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   
                                   if(task.isSuccessful())
                                   {

                                   }
                                   else
                                   {
                                       Toast.makeText(Usermess.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
               }
            }
        });

        renewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(Usermess.this);
                adb.setMessage("Are you sure you want to delete?");
                adb.setTitle("Warning");
                adb.setIcon(R.drawable.ic_person);
                adb.setCancelable(true);

                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        UsersRef.child(facultykey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {

                                if(task.isSuccessful())
                                {
                                    Intent messIntent = new Intent(Usermess.this,HomeActivity.class);
                                    startActivity(messIntent);
                                    finish();
                                    Toast.makeText(Usermess.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();


                

            }
        });

        lunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {


                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("breakfast",breakfast1);
                    userMap.put("lunch","True");
                    userMap.put("dinner",dinner1);
                    db.collection("Customers")
                            .document(facultykey+currentdate)
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {


                                    }
                                    else
                                    {
                                        Toast.makeText(Usermess.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        dinner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {


                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("breakfast",breakfast1);
                    userMap.put("lunch",lunch1);
                    userMap.put("dinner","True");
                    db.collection("Customers")
                            .document(facultykey+currentdate)
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {

                                    }
                                    else
                                    {
                                        Toast.makeText(Usermess.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userfullname = snapshot.child(facultykey).child("name").getValue().toString();
                String userhostel = snapshot.child(facultykey).child("phone").getValue().toString();
                String expiry = snapshot.child(facultykey).child("expiry").getValue().toString();
                String payment_mode = snapshot.child(facultykey).child("paymentmode").getValue().toString();
                UserName.setText(userfullname);
                HostelName.setText(userhostel);
                UserDate.setText("Expires on : "+expiry);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date strDate = null;
                try {
                    strDate = sdf.parse(expiry);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() > strDate.getTime()) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(Usermess.this);
                    adb.setView(R.layout.popup_view);
                    adb.setTitle("Expired");
                    adb.setIcon(R.drawable.ic_person);
                    adb.setCancelable(false);
                    String userkey = getIntent().getExtras().getString("userkey");
                    adb.setPositiveButton("Renew", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent renewIntent = new Intent(Usermess.this,RenewActivity.class);
                            renewIntent.putExtra("userkey",userkey);

                            startActivity(renewIntent);
                            finish();
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adb.show();
                }

               else
               {
               }
                if(payment_mode.equals("PayLater"))
                {
                    DueText.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    protected void onStart()
    {
        super.onStart();

        DueText.setVisibility(View.INVISIBLE);
        progressDialog.show();
        facultykey = getIntent().getExtras().get("userkey").toString();
        faculty1234key = getIntent().getExtras().get("userkey").toString();

        String fkey = getIntent().getExtras().get("userkey").toString();
        DocumentReference document = db.collection("Customers").document(fkey+currentdate);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    breakfast1 = documentSnapshot.getString("breakfast");
                    lunch1 = documentSnapshot.getString("lunch");
                    dinner1 = documentSnapshot.getString("dinner");

                    if(breakfast1.equals("True"))
                    {
                        breakfast.setChecked(true);
                        breakfast.setClickable(false);
                        progressDialog.dismiss();

                    }
                    if(lunch1.equals("True"))
                    {
                        lunch.setChecked(true);
                        lunch.setClickable(false);
                        progressDialog.dismiss();
                    }
                    if(dinner1.equals("True"))
                    {
                        dinner.setChecked(true);
                        dinner.setClickable(false);
                        progressDialog.dismiss();
                    }



                }
                else
                {
                    progressDialog.dismiss();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
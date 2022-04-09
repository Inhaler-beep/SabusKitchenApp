package com.example.sabuskitchen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class ReportFragment extends DialogFragment
{

    private String expiry1,name1,hostel1,mode1,payment_mode1,date1;

    private String payment_mode = "",userKey;
    private TextView amountText,Name1,Mode1,PaymentMode1,Expiry1,Hostel1,PayDate1;


    private Button okButton;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr = db.getReference().child("Customers");
    private FirebaseDatabase payments = FirebaseDatabase.getInstance();
    private DatabaseReference payment = payments.getReference().child("Payments");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        PaymentActivity paymentActivity = (PaymentActivity) getActivity();

        userKey = paymentActivity.getMyData();

        View view = inflater.inflate(R.layout.report_view, container, false);

        Name1 = (TextView) view.findViewById(R.id.name_report);
        Hostel1 = (TextView) view.findViewById(R.id.hostel_report);
        PayDate1 = (TextView) view.findViewById(R.id.date_report);
        Expiry1 = (TextView) view.findViewById(R.id.expiry_report);
        PaymentMode1 = (TextView) view.findViewById(R.id.paymentmode_report);
        Mode1 = (TextView) view.findViewById(R.id.mode_report);
        okButton = (Button) view.findViewById(R.id.report_ok_button);

        payment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                name1 = snapshot.child(userKey).child("name").getValue().toString();
                hostel1 = snapshot.child(userKey).child("phone").getValue().toString();
                date1 = snapshot.child(userKey).child("date").getValue().toString();
                mode1 = snapshot.child(userKey).child("mode").getValue().toString();
                expiry1 = snapshot.child(userKey).child("expiry").getValue().toString();
                payment_mode = snapshot.child(userKey).child("paymentmode").getValue().toString();

                Name1.setText(name1);
                Hostel1.setText(hostel1);
                PayDate1.setText(date1);
                Expiry1.setText(expiry1);
                PaymentMode1.setText(payment_mode);
                Mode1.setText(mode1+" Days");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();

            }
        });
        return view;
    }
}

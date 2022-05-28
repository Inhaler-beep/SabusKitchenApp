package com.example.sabuskitchen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;


public class CompletedFragment extends DialogFragment {

    private String Amount,Dishes,Date,Mode,PayID,OrderID,EmpID,EmpName;

    private String userKey;
    private TextView compAmount,compDate,compMode,compPayid,compDishes,compOrderID,compEmpname;


    private Button okButton;
    private FirebaseDatabase payments = FirebaseDatabase.getInstance();
    private DatabaseReference payment = payments.getReference().child("CompletedOrders");
    private FirebaseDatabase emp = FirebaseDatabase.getInstance();
    private DatabaseReference employee = emp.getReference().child("Employees");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        CompletedOrders completedOrders = (CompletedOrders) getActivity();
        userKey = completedOrders.getMyData();

        View view = inflater.inflate(R.layout.completed_view, container, false);

        compDate = (TextView) view.findViewById(R.id.comp_pay_date_report);
        compAmount = (TextView) view.findViewById(R.id.comp_amount_report);
        compPayid = (TextView) view.findViewById(R.id.comp_payid_report);
        compMode = (TextView) view.findViewById(R.id.comp_paymentmode_report);
        compDishes = (TextView) view.findViewById(R.id.comp_dishes_report);
        compOrderID = (TextView) view.findViewById(R.id.comp_order_report);
        okButton = (Button) view.findViewById(R.id.comp_report_ok_button);
        compEmpname = (TextView) view.findViewById(R.id.comp_emp_name);


        payment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Amount = snapshot.child(userKey).child("totalamount").getValue().toString();
                OrderID = snapshot.child(userKey).child("orderid").getValue().toString();
                Date = snapshot.child(userKey).child("datentime").getValue().toString();
                Mode = "UPI";
                PayID = snapshot.child(userKey).child("paymentid").getValue().toString();
                Dishes = snapshot.child(userKey).child("dishes").getValue().toString();
                EmpID = snapshot.child(userKey).child("empid").getValue().toString();
                employee.child(EmpID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EmpName = snapshot.child("empname").getValue().toString();
                        compEmpname.setText(EmpName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                compAmount.setText(Amount);
                compDate.setText(Date);
                compOrderID.setText(OrderID);
                compMode.setText(Mode);
                compPayid.setText(PayID);
                compDishes.setText(Dishes);
                compEmpname.setText(EmpName);

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
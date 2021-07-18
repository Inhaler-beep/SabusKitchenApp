package com.example.sabuskitchen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PayFragment extends DialogFragment

{
    private ProgressDialog mLoadingBar;
    private String expiry1,name1,hostel1,mode1,payment_mode1,date1;
    private EditText dateInput,nameInput;
    private String payment_mode = "",userKey;
    private TextView amountText;
    private Integer count = 1;
    private AutoCompleteTextView actv;
    private RadioButton Ten,Twenty,Thirty,Fifteen;
    private Integer days;
    private  String UserKey;
    private Button submitButton;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr = db.getReference().child("Customers");
    private FirebaseDatabase payments = FirebaseDatabase.getInstance();
    private DatabaseReference payment = payments.getReference().child("Payments");


    public static String TAG = "PurchaseConfirmationDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DuesActivity duesActivity = (DuesActivity) getActivity();
        userKey = duesActivity.getMyData();
        View view = inflater.inflate(R.layout.payment_renew, container, false);
        amountText = (TextView) view.findViewById(R.id.payment_renew_text);
        RadioButton googlepay = (RadioButton) view.findViewById(R.id.renew_googlepay);
        RadioButton cashpay = (RadioButton) view.findViewById(R.id.renew_cashpayment);
        Button okButton = (Button) view.findViewById(R.id.ok_Button);

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                name1 = snapshot.child(userKey).child("name").getValue().toString();
                hostel1 = snapshot.child(userKey).child("phone").getValue().toString();
                date1 = snapshot.child(userKey).child("date").getValue().toString();
                mode1 = snapshot.child(userKey).child("mode").getValue().toString();
                expiry1 = snapshot.child(userKey).child("expiry").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(googlepay.isChecked())
                {
                    payment_mode = "GooglePay";
                }
                else if(cashpay.isChecked())
                {
                    payment_mode = "Cash";
                }

                if(payment_mode.isEmpty())
                {
                    Toast.makeText(getContext(), "Select a payment method", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    HashMap<String,Object> payValue = new HashMap<>();
                    payValue.put("paymentmode",payment_mode);
                    dr.child(userKey).updateChildren(payValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("name",name1);
                                userMap.put("phone",hostel1);
                                userMap.put("date",date1);
                                userMap.put("mode",mode1);
                                userMap.put("expiry",expiry1);
                                userMap.put("paymentmode",payment_mode);
                                payment.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getContext(), "...", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Sorry..Try Again", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    getActivity().onBackPressed();

                }


            }

        });
        return view;
    }


    //    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        DuesActivity duesActivity = (DuesActivity) getActivity();
//        userKey = duesActivity.getMyData();
////        RadioGroup radioGroup = (RadioGroup) getView().findViewById(R.id.renew_RadioGroup);
////
////        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup group, int checkedId) {
////                switch (checkedId) {
////                    case R.id.renew_googlepay:
////                        payment_mode = "10";
////
////                        break;
////                    case R.id.cashpayment:
////                        payment_mode = "20";
////
////                        break;
////
////
////                }
////            }
////        });
////        //MyActivity activity = (MyActivity) getActivity();
//        //String myDataFromActivity = activity.getMyData();
//    }
}

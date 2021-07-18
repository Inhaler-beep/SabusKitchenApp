package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sabuskitchen.Models.Payments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private DatabaseReference UsersRef;
    private EditText userInput;
    private String payKey;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Payments");
        UsersRef.keepSynced(true);

        progressDialog = new ProgressDialog(PaymentActivity.this);
        progressDialog.setTitle("Loading Users");



        recyclerView = (RecyclerView) findViewById(R.id.payment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        


        LoadUserData();
    }


    private void LoadUserData()
    {

        FirebaseRecyclerOptions<Payments> options =
                new FirebaseRecyclerOptions.Builder<Payments>()
                .setQuery(UsersRef,Payments.class)
                .build();

        FirebaseRecyclerAdapter<Payments,UserViewHolder2> adapter =
                new FirebaseRecyclerAdapter<Payments, UserViewHolder2>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder2 holder, int position, @NonNull Payments model)
                    {
                        final String key = getRef(position).getKey();
                        holder.Payname.setText(model.getName());
                        holder.PayDate.setText(model.getDate());

                        String mode = model.getMode();
                        if(mode.equals("10"))
                        {
                            holder.PayAmount.setText("Rs.1200");

                        }
                        else if(mode.equals("15"))
                        {
                            holder.PayAmount.setText("Rs.1800");
                        }
                        else if(mode.equals("20"))
                        {
                            holder.PayAmount.setText("Rs.2400");
                        }
                        else if(mode.equals("30"))
                        {
                            holder.PayAmount.setText("Rs.3600");
                        }
                        holder.BinButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                payKey = key;
                                new ReportFragment().show(getSupportFragmentManager(),"Payment Report");

                            }
                        });
                        progressDialog.dismiss();

                        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                UsersRef.child(key).removeValue();
                                return true;
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public UserViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments_layout,parent,false);

                        return new UserViewHolder2(view);
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class UserViewHolder2 extends RecyclerView.ViewHolder
    {
        TextView Payname,PayAmount,PayDate;
        ImageButton BinButton;
        View mView;
        public UserViewHolder2(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            Payname = (TextView) itemView.findViewById(R.id.pay_name);
            PayDate = (TextView) itemView.findViewById(R.id.pay_date);
            PayAmount = (TextView) itemView.findViewById(R.id.pay_amount);
            BinButton = (ImageButton) itemView.findViewById(R.id.select_button);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        LoadUserData();
    }

    public String getMyData() {
        return payKey;
    }

}
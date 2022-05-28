package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sabuskitchen.Models.Competed;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firestore.v1.StructuredQuery;

public class CompletedOrders extends AppCompatActivity {

    private FirebaseDatabase co = FirebaseDatabase.getInstance();
    private DatabaseReference comporder = co.getReference().child("CompletedOrders");
    private RecyclerView recyclerView;
    private String payKey = "";
    private LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        recyclerView = (RecyclerView) findViewById(R.id.completed_payments_recyclerView);
        recyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        LoadUserData();


    }
    public void LoadUserData()
    {
        Query query = comporder.orderByChild("datentime");


        FirebaseRecyclerOptions<Competed> options =
                new FirebaseRecyclerOptions.Builder<Competed>()
                .setQuery(query,Competed.class)
                .build();
        FirebaseRecyclerAdapter<Competed,UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<Competed, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Competed model) {
                        final String key = getRef(position).getKey();

                        holder.comp_date.setText(model.getDatentime());
                        holder.comp_id.setText(model.getOrderid());
                        holder.comp_amount.setText(model.getTotalamount());
                        holder.comp_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payKey = key ;
                                new CompletedFragment().show(getSupportFragmentManager(),"Completed Report");
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.completed_layout, parent, false);
                        return new CompletedOrders.UserViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadUserData();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView comp_date,comp_amount,comp_id;
        ImageButton comp_button;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            comp_id = itemView.findViewById(R.id.comp_pay_order_id);
            comp_amount  = itemView.findViewById(R.id.completed_pay_amount);
            comp_date = itemView.findViewById(R.id.com_pay_date);
            comp_button = itemView.findViewById(R.id.comp_select_button);

        }
    }
    public String getMyData() {
        return payKey;
    }
}
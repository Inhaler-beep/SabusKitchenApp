package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.AbstractThreadedSyncAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class ChefDetailActivity extends AppCompatActivity {

    private ListView listView;
    private String key;
    private DatabaseReference OrdersRef,MenuRef,PrepareRef,CompletedRef,RemoveOrder;
    private HashMap<String,String> items1 = new HashMap<>();
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> itemswithcomma = new ArrayList<>();
    private Button PreparedButton,ServedButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_detail);

        key = getIntent().getExtras().getString("key").toString();
        mAuth = FirebaseAuth.getInstance();

        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(key).child("Items");
        PrepareRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(key).child("details");
        RemoveOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        CompletedRef = FirebaseDatabase.getInstance().getReference().child("CompletedOrders");
        MenuRef = FirebaseDatabase.getInstance().getReference().child("Menu");
        listView = (ListView) findViewById(R.id.order_details_listview);

        PreparedButton = (Button) findViewById(R.id.prepared_button);
        ServedButton = (Button) findViewById(R.id.served_button);

        PreparedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                PrepareRef.child("status").setValue("Prepared").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            
                            
                            //items1.put("completedtime",)
                            Toast.makeText(ChefDetailActivity.this, "Item moved to prepared state.", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });


            }
        });
        ServedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PrepareRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        items1.put("orderid",key);
                        items1.put("dishes",itemswithcomma.toString());
                        String totalamount = snapshot.child("totalamount").getValue().toString();
                        String paymentid = snapshot.child("paymentid").getValue().toString();
                        String date = snapshot.child("date").getValue().toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String empuser = user.getUid();
                        items1.put("datentime",date);
                        items1.put("totalamount",totalamount);
                        items1.put("paymentid",paymentid);
                        items1.put("empid",empuser);
                        CompletedRef.child(key).setValue(items1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                RemoveOrder.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ChefDetailActivity.this, "Item served sucecssfully", Toast.LENGTH_SHORT).show();
                                        Intent ordersintent = new Intent(ChefDetailActivity.this,OrderActivity.class);
                                        startActivity(ordersintent);

                                    }
                                });

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                
            }
        });






        OrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    String value = ds.getValue().toString();



                    MenuRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String dishname = snapshot.child(key).child("dishname").getValue().toString();
                            items.add(dishname+ "  : "+value);
                            itemswithcomma.add(dishname+","+value);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    ChefDetailActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    items );
                            listView.setAdapter(arrayAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
  /*  public void LoadUserData()
    {
        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(OrdersRef,Orders.class)
                        .build();
        FirebaseRecyclerAdapter<Orders,ChefDetailActivity.UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Orders model) {



                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.orders_layout, parent, false);

                        return new ChefDetailActivity.UserViewHolder(view);
                    }
                };



        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //LoadUserData();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView orderid,orderamount,orderdate,orderstatus;
        LinearLayout order_linear;
        Button additem,cancelbutton,orderplus,orderminus;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            orderdate = (TextView) itemView.findViewById(R.id.order_date);
            orderamount = (TextView) itemView.findViewById(R.id.order_amount);
            orderid = (TextView) itemView.findViewById(R.id.order_id);
            orderstatus = (TextView) itemView.findViewById(R.id.order_status);
            order_linear = (LinearLayout)  itemView.findViewById(R.id.order_linear_layout);
        }
    }*/
}
package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabuskitchen.Models.Tables;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class TableActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dr = db.getReference().child("Tables");
    private DatabaseReference OrdersRef;
    private RecyclerView recyclerView;
    private EditText tableNo,tableCapacity,tableStatus;
    private TextView orderTable;
    String tablekey = "";
    String uniqueid="";
    private Button addTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        tableNo = (EditText) findViewById(R.id.table_no);

        tableCapacity = (EditText) findViewById(R.id.table_capacity);
        tableStatus = (EditText) findViewById(R.id.table_status);
        addTable = (Button) findViewById(R.id.add_table);
        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderTable = (TextView) findViewById(R.id.order_table);


        recyclerView = (RecyclerView) findViewById(R.id.table_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        addTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableno = tableNo.getText().toString();
                String tablecapacity = tableCapacity.getText().toString();
                String tablestatus = tableStatus.getText().toString();
                HashMap<String,String> tableMap = new HashMap<>();
                tableMap.put("tableno",tableno);
                tableMap.put("status",tablestatus);
                tableMap.put("capacity",tablecapacity);
                if(!tablekey.isEmpty())
                {

                    dr.child(tablekey).setValue(tableMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            tableNo.setText("");
                            tableCapacity.setText("");
                            tableStatus.setText("");
                            try {
                                uniqueid = getIntent().getExtras().getString("uniqueid");
                                OrdersRef.child(uniqueid).child("details").child("tableid").setValue(tablekey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Table Updated Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            catch (Exception e)
                            {

                            }

                            tablekey = "";
                        }
                    });
                }
                else
                {
                    dr.push().setValue(tableMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                tablekey = "";
                                tableNo.setText("");
                                tableCapacity.setText("");
                                tableStatus.setText("");
                                Toast.makeText(getApplicationContext(), "Table Updated Successfully", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
        LoadUserData();


    }
    public void LoadUserData()
    {
        FirebaseRecyclerOptions<Tables> options =
                new FirebaseRecyclerOptions.Builder<Tables>()
                .setQuery(dr,Tables.class)
                .build();

        FirebaseRecyclerAdapter<Tables,UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<Tables, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Tables model) {
                        final String key = getRef(position).getKey();
                        holder.tableno.setText("Table No: "+model.getTableno());
                        holder.tablestatus.setText("Status: "+model.getStatus());
                        holder.tablecapacity.setText("Capacity: "+model.getCapacity());
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dr.child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String tabno = snapshot.child("tableno").getValue().toString();
                                        String tabstatus = snapshot.child("status").getValue().toString();
                                        String tabcap = snapshot.child("capacity").getValue().toString();
                                        tablekey = key;
                                        tableNo.setText(tabno);
                                        tableCapacity.setText(tabcap);
                                        tableStatus.setText(tabstatus);
                                        try {
                                            uniqueid = getIntent().getExtras().getString("uniqueid");
                                            orderTable.setText(uniqueid);
                                        }
                                        catch (Exception e)
                                        {

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.tableview, parent, false);

                        return new TableActivity.UserViewHolder(view);
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
        TextView tableno,tablecapacity,tablestatus;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            tableno = itemView.findViewById(R.id.table_number_view);
            tablecapacity = itemView.findViewById(R.id.table_capacity_view);
            tablestatus = itemView.findViewById(R.id.table_status_view);
        }
    }
}
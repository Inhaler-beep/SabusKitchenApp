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
import android.widget.Toast;

import com.example.sabuskitchen.Models.DueUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DuesActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private DatabaseReference UsersRef;
    private EditText userInput;
    private ProgressDialog progressDialog;
    private String userKey;
    private TextView EmptyFlag;
    private Integer adaptercount = 0;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dues);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        UsersRef.keepSynced(true);

        progressDialog = new ProgressDialog(DuesActivity.this);
        progressDialog.setTitle("Loading Users");
    
        EmptyFlag = (TextView) findViewById(R.id.empty_flag);


        recyclerView = (RecyclerView) findViewById(R.id.due_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        
        LoadUserData();
        CheckForEmptyFlag();




       

    }

    private void CheckForEmptyFlag()
    {
        adaptercount = recyclerView.getChildCount();
        if(adaptercount == 0)
        {
            EmptyFlag.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }
    }


    private void LoadUserData()
    {
            Query query = UsersRef.orderByChild("paymentmode").equalTo("PayLater");
            FirebaseRecyclerOptions<DueUsers> options =
                    new FirebaseRecyclerOptions.Builder<DueUsers>()
                    .setQuery(query,DueUsers.class)
                    .build();

            FirebaseRecyclerAdapter<DueUsers,UserViewHolder1> adapter =
                    new FirebaseRecyclerAdapter<DueUsers, UserViewHolder1>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull UserViewHolder1 holder, int position, @NonNull DueUsers model)
                        {
                            final String key = getRef(position).getKey();

                            holder.Duename.setText(model.getName());
                            holder.Duehostel.setText(model.getPhone());
                            String mode = model.getMode();
                            
                            
                            if(mode.equals("10"))
                            {
                                holder.DueAmount.setText("1200");

                            }
                            else if(mode.equals("15"))
                            {
                                holder.DueAmount.setText("1800");
                            }
                            else if(mode.equals("20"))
                            {
                                holder.DueAmount.setText("2400");
                            }
                            else if(mode.equals("30"))
                            {
                                holder.DueAmount.setText("3600");
                            }

                            holder.BinButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) 
                                {
                                    userKey = key;

                                    new PayFragment().show(getSupportFragmentManager(),"Renew Payment");

                                }
                            });

                            progressDialog.dismiss();

                            


                        }

                        @NonNull
                        @Override
                        public UserViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_layout,parent,false);

                            return new UserViewHolder1(view);
                        }
                    };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        progressDialog.dismiss();


    }

    public String getMyData() {
        return userKey;
    }


    public static class UserViewHolder1 extends RecyclerView.ViewHolder
    {
        TextView Duename,Duehostel,DueAmount;
        ImageButton BinButton;
        View mView;
        public UserViewHolder1(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            Duename = itemView.findViewById(R.id.due_name);
            Duehostel = itemView.findViewById(R.id.due_hostel);
            BinButton = itemView.findViewById(R.id.bin_button);
            DueAmount = itemView.findViewById(R.id.due_amount1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.show();
        EmptyFlag.setVisibility(View.INVISIBLE);
        LoadUserData();

    }
}
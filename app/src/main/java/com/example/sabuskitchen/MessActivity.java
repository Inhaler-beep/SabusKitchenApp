package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sabuskitchen.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private DatabaseReference UsersRef;
    private Button expiredButton;
    private EditText userInput;
    private ProgressDialog progressDialog;
    private CheckBox expiredCheck;
    private HashMap<String,Integer> expiryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        UsersRef.keepSynced(true);

        progressDialog = new ProgressDialog(MessActivity.this);
        progressDialog.setTitle("Loading Users");

        expiredCheck = (CheckBox) findViewById(R.id.expiry_checkbox);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userInput = (EditText) findViewById(R.id.userInput);

        expiredCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    LoadExpiredUsers();
                }
                else
                {
                    LoadUserData("");
                }
            }
        });

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (s.toString()!=null)
                {
                    LoadUserData(s.toString());

                }
                else
                {
                    LoadUserData("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        LoadUserData("");





    }



    private void LoadUserData(String data)
    {

        Query query = UsersRef.orderByChild("name").startAt(data).endAt(data+"\uf8ff");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query,Users.class)
                        .build();

       FirebaseRecyclerAdapter<Users,UserViewHolder> adapter =
               new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model)
                   {
                       final String key = getRef(position).getKey();
                       holder.name.setText(model.getName());
                       holder.phone.setText(model.getPhone());
                       progressDialog.dismiss();
                       String expiry = model.getExpiry();
                       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                       Date strDate = null;
                       try {
                           strDate = sdf.parse(expiry);
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                       if(System.currentTimeMillis() > strDate.getTime())
                       {
                           holder.name.setText(model.getName());
                       }
                       holder.mView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               Intent messuserIntent = new Intent(MessActivity.this,Usermess.class);
                               messuserIntent.putExtra("userkey",key);
                               startActivity(messuserIntent);

                           }
                       });

                   }

                   @NonNull
                   @Override
                   public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.userlayout, parent, false);

                       return new UserViewHolder(view);
                   }
               };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    protected void onStart() {
        super.onStart();


        progressDialog.show();
        LoadUserData("");
        //adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    private void LoadExpiredUsers() {
        Query query = UsersRef.orderByChild("name");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query,Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users,UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model)
                    {
                        final String key = getRef(position).getKey();
                        holder.name.setText(model.getName());
                        holder.phone.setText(model.getPhone());
                        progressDialog.dismiss();
                        String expiry = model.getExpiry();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(expiry);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(System.currentTimeMillis() < strDate.getTime())
                        {
                            holder.mView.setVisibility(View.GONE);
                            holder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                        }
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent messuserIntent = new Intent(MessActivity.this,Usermess.class);
                                messuserIntent.putExtra("userkey",key);
                                startActivity(messuserIntent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.userlayout, parent, false);

                        return new UserViewHolder(view);
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,phone;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}
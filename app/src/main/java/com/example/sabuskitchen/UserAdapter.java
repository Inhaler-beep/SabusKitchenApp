package com.example.sabuskitchen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabuskitchen.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class UserAdapter extends FirebaseRecyclerAdapter<Users, UserAdapter.UserViewHOlder> {


    public UserAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHOlder holder, int position, @NonNull Users model)
    {
        final String key = getRef(position).getKey();
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityMess();

            }
        });


    }

    private void startActivityMess()
    {


    }

    @NonNull
    @Override
    public UserViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlayout, parent, false);

        return new UserViewHOlder(view);
    }

    class UserViewHOlder extends RecyclerView.ViewHolder{

        TextView name,phone;
        View mView;
        ProgressBar progressBar;

        public UserViewHOlder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}

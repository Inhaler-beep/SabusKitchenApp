package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabuskitchen.Models.AddMenuModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddMenu extends AppCompatActivity {

    private AutoCompleteTextView actv;
    private RecyclerView recyclerView;
    String menukey = "";
    String[] fruits = {"Starters","Appetizer", "Main Course", "Juices", "Shakes"};
    private FirebaseDatabase mn = FirebaseDatabase.getInstance();
    private DatabaseReference menu = mn.getReference().child("Menu");
    private Button addmenu;
    private EditText menu_name, menu_count,menu_price,menu_category,add_menu_input;
    private String menuName, menuCount, menuPrice,menuCategory,name,count,price,category;
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        recyclerView = (RecyclerView) findViewById(R.id.add_menu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLACK);
        actv.setInputType(1);
        add_menu_input = (EditText) findViewById(R.id.addmenuInput);
        menu_name = (EditText) findViewById(R.id.dish_name);
        menu_count = (EditText) findViewById(R.id.menu_count);
        menu_price = (EditText) findViewById(R.id.dish_price);
        menu_category = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        addmenu = (Button) findViewById(R.id.add_menu);
        addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuName = menu_name.getText().toString();
                menuPrice = menu_price.getText().toString();
                menuCount = menu_count.getText().toString();
                menuCategory = menu_category.getText().toString();
                if(menuName.isEmpty() || menuPrice.isEmpty() || menuCategory.isEmpty() | menuCount.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("dishname",menuName);
                    userMap.put("price",menuPrice);
                    userMap.put("count",menuCount);
                    userMap.put("category",menuCategory);

                    if(!menukey.isEmpty())
                    {
                        HashMap<String,String> updateuserMap = new HashMap<>();
                        updateuserMap.put("dishname",menuName);
                        updateuserMap.put("price",menuPrice);
                        updateuserMap.put("count",menuCount);
                        updateuserMap.put("category",menuCategory);
                        Toast.makeText(getApplicationContext(), "key "+menukey, Toast.LENGTH_SHORT).show();
                        menu.child(menukey).setValue(updateuserMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(getApplicationContext(), "Menu Updated Successfully", Toast.LENGTH_SHORT).show();
                                menukey = "";
                                menu_category.setText("");
                                menu_count.setText("");
                                menu_price.setText("");
                                menu_name.setText("");
                            }
                        });
                    }
                    else
                    {
                        menu.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(AddMenu.this, "Menu Added", Toast.LENGTH_SHORT).show();
                                    menu_category.setText("");
                                    menu_count.setText("");
                                    menu_price.setText("");
                                    menu_name.setText("");
                                }
                            }
                        });
                    }
                }





            }
        });
        add_menu_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString()!=null)
                {
                    LoadUserData(s.toString());

                }
                else
                {
                    LoadUserData(" ");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        LoadUserData("");



    }


    public void LoadUserData(String data)
    {
        Query query = menu.orderByChild("dishname").startAt(data).endAt(data+"\uf8ff");

        FirebaseRecyclerOptions<AddMenuModel> options =
                new FirebaseRecyclerOptions.Builder<AddMenuModel>()
                .setQuery(query,AddMenuModel.class)
                .build();
        FirebaseRecyclerAdapter<AddMenuModel,AddMenu.UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<AddMenuModel, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull AddMenuModel model) {
                        holder.dishname.setText(model.getDishname());
                        final String key = getRef(position).getKey();

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                menu.child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        name = snapshot.child("dishname").getValue().toString();
                                        price = snapshot.child("price").getValue().toString();
                                        count = snapshot.child("count").getValue().toString();
                                        category = snapshot.child("category").getValue().toString();
                                        menukey = key;
                                        menu_name.setText(name);
                                        menu_count.setText(count);
                                        menu_price.setText(price);
                                        menu_category.setText(category);

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
                                .inflate(R.layout.add_menu_layout, parent, false);
                        return new AddMenu.UserViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadUserData(" ");
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView dishname,price,category,quantity;
        Button additem,cancelbutton,orderplus,orderminus;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            dishname = itemView.findViewById(R.id.dishname);

        }
    }
}
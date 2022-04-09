package com.example.sabuskitchen;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabuskitchen.Models.Menu;
import com.example.sabuskitchen.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private LinearLayout placeOrder;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<Map.Entry<String, String> > listOfEntry;
    private DatabaseReference MenuRef,OrdersRef;
    private EditText userInput;
    private TextView orderQuantity;
    private ProgressDialog progressDialog;
    private ArrayList<String> ordereditem;
    private Button PlaceOrderGreen,orderMinus,orderPlus;
    private int finalamount=0;
    HashMap<String,Integer> orderMap = new HashMap<>();
    HashMap<String,String> orderDetails = new HashMap<>();
    String uniqueID;
    HashMap<String,Integer> prices = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Checkout.preload(getApplicationContext());

        MenuRef = FirebaseDatabase.getInstance().getReference().child("Menu");
        OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        MenuRef.keepSynced(true);

        progressDialog = new ProgressDialog(MenuActivity.this);
        progressDialog.setTitle("Loading Menu");


        recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userInput = (EditText) findViewById(R.id.menuInput);
        placeOrder = (LinearLayout) findViewById(R.id.place_order_linear_layout);
        PlaceOrderGreen = (Button) findViewById(R.id.place_order_green);


        PlaceOrderGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalamount=0;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                uniqueID = UUID.randomUUID().toString();
                for (Map.Entry mapElement : prices.entrySet()) {
                    String key = (String)mapElement.getKey();
                    Integer value = ((Integer)mapElement.getValue());
                    totalamount = totalamount + value;


                }

               orderDetails.put("totalamount",Integer.toString(totalamount));
                orderDetails.put("date",timeStamp);
                orderDetails.put("status","Ordered");



                OrdersRef.child(uniqueID).child("Items").setValue(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(MenuActivity.this, "Completed succesffuly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                OrdersRef.child(uniqueID).child("details").setValue(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MenuActivity.this, "Successfull", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                startPayment(uniqueID,Integer.toString(totalamount));



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

        Query query = MenuRef.orderByChild("dishname").startAt(data).endAt(data+"\uf8ff");

        FirebaseRecyclerOptions<Menu> options =
                new FirebaseRecyclerOptions.Builder<Menu>()
                        .setQuery(query,Menu.class)
                        .build();

        FirebaseRecyclerAdapter<Menu, MenuActivity.UserViewHolder> adapter=
                new FirebaseRecyclerAdapter<Menu, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Menu model) {
                        holder.cancelbutton.setVisibility(View.GONE);
                        holder.orderminus.setVisibility(View.GONE);
                        holder.orderplus.setVisibility(View.GONE);
                        holder.quantity.setVisibility(View.GONE);
                        ordereditem  = new ArrayList<>();

                        final String key = getRef(position).getKey();
                        holder.dishname.setText(model.getDishname());
                        holder.price.setText(model.getPrice());
                        progressDialog.dismiss();
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

/*
                                Intent menuIntent = new Intent(MenuActivity.this,PreOrderActivity.class);
                                menuIntent.putExtra("menukey",key);


                                startActivity(menuIntent);*/

                            }
                        });
                        holder.orderplus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int quantity = parseInt(holder.quantity.getText().toString());
                                quantity = quantity + 1;
                                String revised_quantity = Integer.toString(quantity);
                                holder.quantity.setText(revised_quantity);
                                orderMap.put(key,quantity);
                                int price = parseInt(model.getPrice());
                                price = price * quantity;
                                String revised_price = Integer.toString(price);
                                holder.price.setText(revised_price);
                                if(prices.containsKey(key))
                                {
                                    prices.remove(key);
                                    prices.put(key,price);
                                }
                                else
                                {
                                    prices.put(key,price);
                                }



                            }
                        });
                        holder.orderminus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int quantity = parseInt(holder.quantity.getText().toString());
                                quantity = quantity - 1;
                                String revised_quantity = Integer.toString(quantity);
                                holder.quantity.setText(revised_quantity);
                                orderMap.put(key,quantity);
                                int price = parseInt(model.getPrice());
                                price = price * quantity;
                                String revised_price = Integer.toString(price);
                                holder.price.setText(revised_price);
                                if(prices.containsKey(key))
                                {
                                    prices.remove(key);
                                    prices.put(key,price);
                                }
                                else
                                {
                                    prices.put(key,price);
                                }




                            }
                        });
                        holder.additem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.additem.setVisibility(View.GONE);
                                holder.cancelbutton.setVisibility(View.VISIBLE);
                                holder.orderplus.setVisibility(View.VISIBLE);
                                holder.orderminus.setVisibility(View.VISIBLE);
                                holder.quantity.setVisibility(View.VISIBLE);
                                orderMap.put(key, parseInt(holder.quantity.getText().toString()));
                                ordereditem.add(key);
                                updatePlaceOrderCard(ordereditem.size());
                                if(prices.containsKey(key))
                                {
                                    prices.remove(key);
                                    prices.put(key,parseInt(holder.price.getText().toString()));
                                }
                                else
                                {
                                    prices.put(key,parseInt(holder.price.getText().toString()));
                                }
                            }
                        });
                        holder.cancelbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.additem.setVisibility(View.VISIBLE);
                                holder.cancelbutton.setVisibility(View.GONE);
                                holder.quantity.setVisibility(View.GONE);
                                holder.orderminus.setVisibility(View.GONE);
                                holder.orderplus.setVisibility(View.GONE);
                                ordereditem.remove(key);
                                orderMap.remove(key);
                                prices.remove(key);
                                updatePlaceOrderCard(ordereditem.size());

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.menu_layout, parent, false);

                        return new MenuActivity.UserViewHolder(view);
                    }
                };

      /*  FirebaseRecyclerAdapter<Menu, MenuActivity.UserViewHolder> adapter1 =
                new FirebaseRecyclerAdapter<Menu, MenuActivity.UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessActivity.UserViewHolder holder, int position, @NonNull Users model)
                    {


                    }

                    @NonNull
                    @Override
                    public MessActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    }
                };*/


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    protected void onStart() {
        super.onStart();

        placeOrder.setVisibility(View.INVISIBLE);
        progressDialog.show();
        LoadUserData("");

        //adapter.startListening();



    }

    public void updatePlaceOrderCard(int size)
    {
        if(size>0)
        {
            placeOrder.setVisibility(View.VISIBLE);
        }
        else
        {
            placeOrder.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView dishname,price,category,quantity;
        Button additem,cancelbutton,orderplus,orderminus;
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            dishname = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.phone);
            additem = itemView.findViewById(R.id.add_item_button);
            cancelbutton = itemView.findViewById(R.id.cancel_item);
            quantity = itemView.findViewById(R.id.order_quantity);
            orderplus = itemView.findViewById(R.id.order_plus);
            orderminus = itemView.findViewById(R.id.order_minus);
        }
    }
    public void startPayment(String uniqueID,String amount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        Toast.makeText(MenuActivity.this, "Uniqueid"+uniqueID, Toast.LENGTH_SHORT).show();
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount+"00");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "megabite@gmail.com");
            preFill.put("contact", "973734734");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Intent orderplacedintent = new Intent(MenuActivity.this, OrderActivity.class);
            Toast.makeText(MenuActivity.this, "unique id"+uniqueID, Toast.LENGTH_LONG).show();
            orderplacedintent.putExtra("uniqueid",uniqueID);
            orderplacedintent.putExtra("paymentid",razorpayPaymentID);
            startActivity(orderplacedintent);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            OrdersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
            OrdersRef.child(uniqueID).removeValue();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}
package com.example.sabuskitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText EmailInput, PasswordInput;
    private Button LoginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailInput = (EditText) findViewById(R.id.emailInput);
        PasswordInput = (EditText) findViewById(R.id.passwordInput);
        LoginButton = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progessingbar);
       progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = EmailInput.getText().toString().trim();
                String password = PasswordInput.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Fill the fields", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                String uid = mAuth.getCurrentUser().getUid().toString();

                                Toast.makeText(LoginActivity.this, "Logged in Succesffully",   Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(homeIntent);
                                finish();

                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Sorry wrong password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }


            }
        });
    }
}
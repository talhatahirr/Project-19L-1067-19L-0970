package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText name, email, password;
    Button register_btn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = findViewById(R.id.Name);
        email = findViewById(R.id.email_Address);
        password = findViewById(R.id.reg_password);
        register_btn = findViewById(R.id.reg_button);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        //if(firebaseAuth.getCurrentUser() != null){
           // Intent intent = new Intent(getApplicationContext(), Homepage.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // startActivity(intent);
           // finish();
      //  }

        TextView log_in = (TextView) findViewById(R.id.log_in_text);
        String text = "Not have an account? LOGIN";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        };
        ss.setSpan(clickableSpan, 21, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        log_in.setText(ss);
        log_in.setMovementMethod(LinkMovementMethod.getInstance());

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uEmail = email.getText().toString().trim();
                String uPassword = password.getText().toString().trim();
                if(TextUtils.isEmpty(uEmail))
                {
                    email.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(uPassword))
                {
                    password.setError("Password is required!");
                    return;
                }
                if(uPassword.length() < 6)
                {
                    password.setError("Password must be more than 6 characters long");
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            insertData();
                            Toast.makeText(Signup.this, "User Created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Homepage.class);
                            intent.putExtra("email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Signup.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void insertData(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("user").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Signup.this, "Data Inserted Successfully!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Error while insertion!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
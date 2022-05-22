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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login_btn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password_reg);
        login_btn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();

        TextView sign_up = (TextView) findViewById(R.id.sign_up_text);
        String text = "Not have an account? SIGNUP";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent activity = new Intent(Login.this, Signup.class);
                startActivity(activity);
                finish();
            }
        };
        ss.setSpan(clickableSpan, 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sign_up.setText(ss);
        sign_up.setMovementMethod(LinkMovementMethod.getInstance());

        login_btn.setOnClickListener(new View.OnClickListener() {
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

                firebaseAuth.signInWithEmailAndPassword(uEmail, uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Homepage.class);
                            intent.putExtra("email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}
package com.example.expensify;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    EditText username,phn_no,email,password;
    MaterialButton signup;
    FirebaseAuth mAuth;
    Intent intent;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.name);
        phn_no = findViewById(R.id.phno);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.register);
        login = findViewById(R.id.textsignin2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr,phno,eml,psw;
                usr=username.getText().toString();
                phno=phn_no.getText().toString();
                eml = String.valueOf(email.getText());
                psw = String.valueOf(password.getText());

                if(TextUtils.isEmpty(usr))
                {
                    Toast.makeText(SignUp.this, "Username field empty! Enter username...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phno))
                {
                    Toast.makeText(SignUp.this, "Phone NO. field empty! Enter Phone No....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(eml))
                {
                    Toast.makeText(SignUp.this, "Email field empty! Enter Email...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(psw))
                {
                    Toast.makeText(SignUp.this, "Password field empty! Enter Password...", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(eml,psw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    {
                                        Toast.makeText(SignUp.this, "Account created.",
                                                Toast.LENGTH_SHORT).show();
                                        intent = new Intent(SignUp.this,DashBoard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e("SignUp", "Authentication failed", task.getException());
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
package com.example.alumnet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements  View.OnClickListener {

    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTextEmail = findViewById(R.id.et_email_login);
        mTextPassword = findViewById(R.id.et_password_login);
        mButtonLogin = findViewById(R.id.button_login);
        mTextViewRegister = findViewById(R.id.tv_register);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //start Profile Activity
            Toast.makeText(getApplicationContext(), "Successfull login!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), dashBoard.class));
        }
        mButtonLogin.setOnClickListener(this);
        mTextViewRegister.setOnClickListener(this);
    }

    private void userLogin(){
        String email = mTextEmail.getText().toString().trim();
        String password = mTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start profile activity
                            finish();
                            Toast.makeText(getApplicationContext(), "Succesfull login!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), dashBoard.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if(view == mButtonLogin){
            userLogin();
        }

        if(view == mTextViewRegister){
            finish();
            startActivity(new Intent(this, Register.class));
        }
    }
}

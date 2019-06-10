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
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText etFirstName, etLastName, etEmail, etMiddleName, etPassword, etPasswordConfirm;
    Button buttonRegister;
    TextView tv_login;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Get root reference to firebase db
        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference("reg");

        etFirstName = (EditText) findViewById(R.id.et_firstName);
        etLastName = (EditText)findViewById(R.id.et_lastName);
        etEmail= (EditText)findViewById(R.id.et_email);
        etMiddleName = (EditText)findViewById(R.id.et_middleName);
        etPassword = (EditText)findViewById(R.id.reg_password);
        etPasswordConfirm = (EditText)findViewById(R.id.et_passwordConfirm);
        buttonRegister = (Button) findViewById(R.id.button_register);
        tv_login = (TextView) findViewById(R.id.tv_login);



        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(Register.this, Login.class);
                startActivity(LoginIntent);
            }
        });



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passconfirm = etPasswordConfirm.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passconfirm.equals(password)){
                    Toast.makeText(getApplicationContext(), "Password not matched!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {

                            private void addData() {
                                String email = etEmail.getText().toString().trim();
                                String fname = etFirstName.getText().toString().toUpperCase();
                                String mname = etMiddleName.getText().toString().toUpperCase();
                                String lname = etLastName.getText().toString().toUpperCase();

                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                String id=user.getUid();

                                RegData newdata=new RegData(fname,mname,lname,email);
                                rootref.child(id).setValue(newdata);

                            }

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    addData();
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }
                            }

                        });

            }
        });


    }


}

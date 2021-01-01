package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // widgets
    TextView btn_to_signup;
    TextInputLayout txt_username, txt_password;

    // values
    private String username;
    private String password;
    String usernameDB, fullnameDB, phoneDB, passwordDB;

    //firebase
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private final String FIREBASE_USERS_PATH = "users";
    private Query userQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        containsKey();
    }

    /* ON CLICK */
    public void setBtn_to_signup(View view) {
        callSignUp();
    }

    public void loginUser(View view) {
        if (!validateUserName() | !validatePassword()) {
            Log.d("LOGIN:", "VALIDATOR");

            return;
        } else {

            isUser();
        }
    }


    // init widgets
    private void containsKey() {
        txt_username = findViewById(R.id.login_txt_username);
        txt_password = findViewById(R.id.login_txt_password);
    }


    /* METHODS */
    private void callSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    //get values from db
    private void getValue() {
        username = txt_username.getEditText().getText().toString();
        password = txt_password.getEditText().getText().toString();
    }

    private void getValueFromDB(DataSnapshot snapshot) {
        usernameDB = snapshot.child(username).child("username").getValue(String.class);
//        fullnameDB = snapshot.child(username).child("fullname").getValue(String.class);
//        phoneDB = snapshot.child(username).child("phoneNo").getValue(String.class);
//        passwordDB = snapshot.child(username).child("password").getValue(String.class);
    }

    private void setupFirebase() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference(FIREBASE_USERS_PATH);
    }

    // login to user
    private void isUser() {
        getValue();
        setupFirebase();
        //query
        userQuery = reference.orderByChild("username").equalTo(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String _passwordDB = snapshot.child(username).child("password").getValue(String.class);

                    if (_passwordDB.equals(password)) {
                        Log.d("LOGIN: ", "LOGIN SUCCESS");
                        getValueFromDB(snapshot);

                        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        intent.putExtra("username", usernameDB);
//                        intent.putExtra("phoneNo", phoneDB);
//                        intent.putExtra("password", passwordDB);


                        Toast.makeText(LoginActivity.this, "login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

                    } else {
                        Log.d("LOGIN: ", "LOGIN FAIL");
                        Toast.makeText(LoginActivity.this, "Username or password is invalid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("LOGIN: ", "LOGIN FAIL");
                    Toast.makeText(LoginActivity.this, "Username or password is invalid!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /* VALIDATORS */
    // validator username
    private boolean validateUserName() {
        String value = txt_username.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_username.setError("This cannot be empty!");
            return false;
        } else {
            txt_username.setError(null);
            txt_username.setErrorEnabled(false);
            return true;
        }

    }

    // validator password
    private boolean validatePassword() {
        String value = txt_password.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_password.setError("This cannot be empty!");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }
    }

}
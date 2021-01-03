package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.vlustore.models.Users;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    // values
    private String username, password, phone, fullname, confirm_password;

    // models
    private Users user;

    // firebase
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;


    //widgets
    private Button btn_signup, btn_back_to_login;
    private TextInputLayout txt_username, txt_fullname, txt_password, txt_cfpassword, txt_phoneno;

    //query
    private Query usernameQuery;

    //
    private final String FIREBASE_USERS_PATH = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        containsKey();

        btn_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin();
                finish();
            }
        });

    }

    // ON CLICK
    public void registerUser(View view) {
        if (!validateFullName() | !validateUserName() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }
        getValues();
        setupFirebase();
        checkIfUsernameExist(username);
    }

    /**
     * ------------------------------------------------------------------------------------------
     **/

    /**
     * VALIDATOR
     **/

    private void checkIfUsernameExist(final String username) {
        usernameQuery = reference.orderByChild("username").equalTo(username);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    Toast.makeText(SignUpActivity.this, "Username is already exist!", Toast.LENGTH_LONG).show();
                } else {
                    user = new Users(fullname, username, password, phone);
                    reference.child(username).setValue(user);
                    Toast.makeText(SignUpActivity.this, "Registed", Toast.LENGTH_SHORT).show();
                    callLogin();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // validator fullname
    private boolean validateFullName() {
        String value = txt_fullname.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_fullname.setError("This cannot be empty!");
            return false;
        } else {
            txt_fullname.setError(null);
            txt_fullname.setErrorEnabled(false);
            return true;
        }
    }

    // validator username
    private boolean validateUserName() {
        String value = txt_username.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_username.setError("This cannot be empty!");
            return false;

        } else if ((6 > value.length()) | (value.length() >= 15)) {
            txt_username.setError("Username must be between 6 and 15");
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
        } else if ((6 > value.length())) {
            txt_password.setError("Password too weak");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }
    }

    // validator confirm password
    private boolean validateConfirmPassword() {
        String value = txt_cfpassword.getEditText().getText().toString();
        String password = txt_password.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_cfpassword.setError("Confirm password is not the same as password!");
            return false;
        } else if (!value.equals(password)) {
            txt_cfpassword.setError("Confirm password is not the same as password");
            return false;
        } else {
            txt_cfpassword.setError(null);
            txt_cfpassword.setErrorEnabled(false);
            return true;
        }
    }


    /**
     * ------------------------------------------------------------------------------------------
     **/

    // GET VALUE FROME WIDGETS
    private void getValues() {
        fullname = txt_fullname.getEditText().getText().toString();
        username = txt_username.getEditText().getText().toString();
        password = txt_password.getEditText().getText().toString();
        phone = txt_phoneno.getEditText().getText().toString();
    }


    private void setupFirebase() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference(FIREBASE_USERS_PATH);
    }


    // INIT WIDGETS
    private void containsKey() {
        txt_fullname = findViewById(R.id.signup_txt_fullname);
        txt_username = findViewById(R.id.sigup_txt_username);
        txt_password = findViewById(R.id.signup_txt_password);
        txt_cfpassword = findViewById(R.id.signup_txt_cfpassword);
        txt_phoneno = findViewById(R.id.sigup_txt_phone);
        btn_signup = findViewById(R.id.signup_btn);
        btn_back_to_login = findViewById(R.id.signup_btn_back_to_login);
    }

    // CALL BACK TO LOGIN ACTIVITY
    private void callLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /* ------------------------------------------------------------------------*/
}
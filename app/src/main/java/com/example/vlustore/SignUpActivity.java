package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    // values
    private String username, password, phone, fullname;
    private String append;

    // models
    private Users users;

    // firebase
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    //widgets
    private Button btn_signup, btn_back_to_login;
    private TextInputLayout txt_username, txt_fullname, txt_password, txt_cfpassword, txt_phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        containsKey();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = FirebaseAuth.getInstance();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference();
                getValues(reference);
                setupFirebaseAuth();
            }
        });

    }

    // SET UP FIREBASE AUTHENTICATOR
    private void setupFirebaseAuth() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // username not exist
                            if (!validateFullName() | !validateUserName() | !validatePassword() | !validateConfirmPassword()){
                                if (checkIfUserNameExist(username, snapshot)){
                                    Toast.makeText(SignUpActivity.this,"username already exist",Toast.LENGTH_LONG).show();
                                }
                            }
                            Toast.makeText(SignUpActivity.this,"done",Toast.LENGTH_LONG).show();

                            // add user name to database

                            //
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {

                }
            }
        };
    }

    // VALIDATOR
    private Boolean checkIfUserNameExist(String username, DataSnapshot dataSnapshot) {
        Log.d("Username validator", "checkIfUserNameExist: check if " + username + " already exist.");
        Users users = new Users();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            users.setUsername(ds.getValue(Users.class).getUsername());

            if (expandUsername(users.getUsername()).equals(username)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private Boolean validateFullName() {
        String value = txt_fullname.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_fullname.setError("Không được để trống");
            return false;
        } else {
            txt_fullname.setError(null);
            txt_fullname.setEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName() {
        String value = txt_username.getEditText().getText().toString();
        String noSpecicalCharacter = "( @ - _ ^ * # & $ .\\)";
        if (value.isEmpty()) {
            txt_username.setError("This cannot be empty!");
            return false;
        } else if ((8 >= value.length()) && (value.length() >= 15)) {
            txt_username.setError("username must be between 8 and 15");
            return false;
        } else {
            txt_username.setError(null);
            txt_username.setEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword() {
        String value = txt_password.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_password.setError("This cannot be empty!");
            return false;
        } else if ((8 >= value.length()) && (value.length() >= 15)) {
            txt_password.setError("username must be between 8 and 15");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String value = txt_cfpassword.getEditText().getText().toString();
        String password = txt_password.getEditText().getText().toString();
        if (value.isEmpty()) {
            txt_cfpassword.setError("Confirm password is not the same as password!");
            return false;
        } else if (!value.equals(password)) {
            txt_cfpassword.setError("Confirm password is not the same as password");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setEnabled(false);
            return true;
        }

    }


    /**
     * ------------------------------------------------------------------------------------------
     **/

    // GET VALUE TO DATABASE
    private void getValues(DatabaseReference reference) {
        fullname = txt_fullname.getEditText().getText().toString();
        username = txt_username.getEditText().getText().toString();
        password = txt_password.getEditText().getText().toString();
        phone = txt_phoneno.getEditText().getText().toString();

        users = new Users(fullname, username, password, phone);
        reference.child(username).setValue(users);
    }

    // INIT WIDGETS
    private void containsKey() {
        txt_fullname = findViewById(R.id.signup_txt_fullname);
        txt_username = findViewById(R.id.sigup_txt_username);
        txt_password = findViewById(R.id.signup_txt_password);
        txt_cfpassword = findViewById(R.id.signup_txt_cfpassword);
        txt_phoneno = findViewById(R.id.sigup_txt_phone);
        btn_signup = findViewById(R.id.signup_btn);
    }

    //STRING MANIPULATION
    private static String expandUsername(String username) {
        return username.replace(".", " ");
    }

    private static String condenseUsername(String username) {
        return username.replace(" ", ".");
    }
    /* ------------------------------------------------------------------------*/
}
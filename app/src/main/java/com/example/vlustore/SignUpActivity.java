package com.example.vlustore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vlustore.controller.Users;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    Users users;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    Button btn_signup, btn_back_to_login;
    TextInputLayout txt_username, txt_fullname, txt_password, txt_cfpassword, txt_phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        containsKey();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                getValues(reference);
            }
        });

    }

    private void getValues(DatabaseReference reference) {
        String fullname = txt_fullname.getEditText().getText().toString();
        String username = txt_username.getEditText().getText().toString();
        String password = txt_password.getEditText().getText().toString();
        String phone = txt_phoneno.getEditText().getText().toString();

        users = new Users(fullname, username, password, phone);
        reference.child(username).setValue(users);
    }

    private void containsKey() {
        txt_fullname = findViewById(R.id.signup_txt_fullname);
        txt_username = findViewById(R.id.sigup_txt_username);
        txt_password = findViewById(R.id.signup_txt_password);
        txt_cfpassword = findViewById(R.id.signup_txt_cfpassword);
        txt_phoneno = findViewById(R.id.sigup_txt_phone);
        btn_signup = findViewById(R.id.signup_btn);
    }
}
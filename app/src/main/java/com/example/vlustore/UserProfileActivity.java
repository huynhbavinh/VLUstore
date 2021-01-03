package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class UserProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView nav_username;
    //firebase
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private final String FIREBASE_USERS_PATH = "users";
    private Query userQuery;

    // Inits
    TextView txt_profile_name, txt_profile_username;
    TextInputLayout txt_fullname, txt_phone, txt_password;

    //intents
    private Intent intent;

    //values
    private String _fullname;
    private String _phoneNo;
    private String _password;
    private String _username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        // get username from intent
        getUsername();
        nav_username = findViewById(R.id.navigation_txt_username);
        // set username to navigation menu
        nav_username.setText(_username);

        // init drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        containsKey();
        showAllUserData();
    }

    private void getUsername() {
        intent = getIntent();
        _username = getIntent().getStringExtra("username");
    }


    /*------------------------------------------------------------------------*/
    //ON CLICK
    public void updateUserData(View view) {
        setupFirebase();
        _fullname = txt_fullname.getEditText().getText().toString();
        _phoneNo = txt_phone.getEditText().getText().toString();

        reference.child(_username).child("fullname").setValue(_fullname);
        reference.child(_username).child("phoneNo").setValue(_phoneNo);

        Toast.makeText(UserProfileActivity.this, "Updated!", Toast.LENGTH_SHORT).show();


        finish();
        startActivity(getIntent());
    }

    private void setupFirebase() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference(FIREBASE_USERS_PATH);
    }

    // show all user's data
    private void showAllUserData() {
        intent = getIntent();
        setupFirebase();
        _username = intent.getStringExtra("username");

        Log.d("Get intent", "showAllUserData: " + _username);
        userQuery = reference.orderByChild("username").equalTo(_username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getValueFromDB(snapshot);

                txt_profile_name.setText(_fullname);
                txt_profile_username.setText(_username);
                txt_fullname.getEditText().setText(_fullname);
                txt_phone.getEditText().setText(_phoneNo);
                txt_password.getEditText().setText(_password);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to get data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // init widgets
    private void containsKey() {
        txt_profile_name = findViewById(R.id.profile_fullname);
        txt_profile_username = findViewById(R.id.profile_username);

        txt_fullname = findViewById(R.id.profile_txt_fullname);
        txt_phone = findViewById(R.id.profile_txt_phone);
        txt_password = findViewById(R.id.profile_txt_password);
    }

    // value from db
    private void getValueFromDB(DataSnapshot snapshot) {
        _fullname = snapshot.child(_username).child("fullname").getValue(String.class);
        _phoneNo = snapshot.child(_username).child("phoneNo").getValue(String.class);
        _password = snapshot.child(_username).child("password").getValue(String.class);
    }


    //  /* Navigation Drawer MENU*/

    public void ClickMenu(View view) {
        // open drawer
        ProductDisplayActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        // close drawer
        ProductDisplayActivity.closeDrawer(drawerLayout);
    }

    public void ClickUsername(View view) {
        ProductDisplayActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        ProductDisplayActivity.redirectActivity(this, ProductDisplayActivity.class);
    }


    public void ClickShopingCart(View view) {
        ProductDisplayActivity.redirectActivity(this, CartActivity.class);
    }

    public void ClickBill(View view) {
        ProductDisplayActivity.redirectActivity(this, userBillManagement.class);
    }

    public void ClickExit(View view) {
        ProductDisplayActivity.logout(this);
    }

}
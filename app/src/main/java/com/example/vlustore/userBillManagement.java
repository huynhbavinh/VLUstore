package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vluadmin.Billitems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userBillManagement extends AppCompatActivity {
    private String _username;

    private DrawerLayout drawerLayout;
    private TextView nav_username;
    private Intent intent;
    private RecyclerView billRecycleView;
    private RecyclerView.Adapter billAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<com.example.vluadmin.Billitems> billitems;


    private DatabaseReference billReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bill_management);

        // get username from intent
        getUsername();
        nav_username = findViewById(R.id.navigation_txt_username);
        // set username to navigation menu
        nav_username.setText(_username);

        // init drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getUsername();
        billReference = FirebaseDatabase.getInstance().getReference();
        getData();

    }

    private void getData() {
        billitems = new ArrayList<>();
        Query query = billReference.child("order").child(_username).orderByChild(_username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billitems.clear();
                String uid = snapshot.getKey();
                String date = snapshot.child("0").child("time").getValue().toString();
                billitems.add(new Billitems(uid, date));
                setBillRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUsername() {
        intent = getIntent();
        _username = intent.getStringExtra("username");
    }

    private void setBillRecycleView() {
        billRecycleView = findViewById(R.id.bill_recycler_view);
        billRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        billAdapter = new BillRecyclerAdapter(billitems);

        billRecycleView.setLayoutManager(layoutManager);
        billRecycleView.setAdapter(billAdapter);
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
        ProductDisplayActivity.redirectActivity(this, UserProfileActivity.class);
    }

    public void ClickHome(View view) {
        ProductDisplayActivity.redirectActivity(this, ProductDisplayActivity.class);
    }


    public void ClickShopingCart(View view) {
        ProductDisplayActivity.redirectActivity(this, CartActivity.class);
    }

    public void ClickBill(View view) {
        ProductDisplayActivity.closeDrawer(drawerLayout);
    }

    public void ClickExit(View view) {
        ProductDisplayActivity.logout(this);
    }
}
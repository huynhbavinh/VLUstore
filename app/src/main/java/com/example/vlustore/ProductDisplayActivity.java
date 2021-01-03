package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.vlustore.models.Product;

import java.util.ArrayList;

public class ProductDisplayActivity extends AppCompatActivity {
    private static String _username;
    private DrawerLayout drawerLayout;
    private TextView nav_username;
    RecyclerView recyclerView;
    private Intent intent;
    private DatabaseReference ProductsRef;
    //ListView lvSP;
    ArrayList<Product> mangSP;
    String TAG = "loi";
    //private RecyclerAdapter recyclerAdapter;
    FloatingActionButton floaticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        // get username from intent
        getUsername();
        nav_username = findViewById(R.id.navigation_txt_username);
        // set username to navigation menu
        nav_username.setText(_username);

        // init drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // init recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recy_list);

        ProductsRef = FirebaseDatabase.getInstance().getReference();
        mangSP = new ArrayList<>();
        getData();

    }

    private void getUsername() {
        intent = getIntent();
        _username = getIntent().getStringExtra("username");
    }

    void getData() {
        Query query = ProductsRef.child("Products");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangSP.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);

                    mangSP.add(product);
                }
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getApplicationContext(), mangSP,_username);

                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductDisplayActivity.this));
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /* Navigation Drawer MENU*/
    public void ClickMenu(View view) {
        // open drawer

        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        // open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        // close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        // close drawer layout
        // check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // USER PROFILE
    public void ClickUsername(View view) {
        intent = new Intent(ProductDisplayActivity.this,UserProfileActivity.class);
        intent.putExtra("username", _username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // PRODUCT DISPLAY
    public void ClickHome(View view) {
        closeDrawer(drawerLayout);
    }

    // SHOPING CART
    public void ClickShopingCart(View view) {
        redirectActivity(this, CartActivity.class);
    }

    // BILL
    public void ClickBill(View view) {
        redirectActivity(this, userBillManagement.class);
    }

    // LOG OUT
    public void ClickExit(View view) {
        logout(this);
    }

    public static void logout(Activity activity) {
        activity.finishAffinity();
        System.exit(0);
    }

    public static void redirectActivity(Activity activity, Class aClass) {

        Intent intent = new Intent(activity, aClass);
        intent.putExtra("username", _username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
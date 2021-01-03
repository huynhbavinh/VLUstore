package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlustore.models.Bill;
import com.example.vlustore.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    private String _username;
    private Intent intent;
    private DrawerLayout drawerLayout;
    private TextView nav_username;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn, cancel;
    private TextView txtTotalAmount;
    private DatabaseReference ProductsRef;
    private String child_product;
    String chuyen_soluong_bill;
    String soluong = "";

    ListView list;
    ArrayAdapter<String> adapter;
    ArrayList<String> sanpham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ProductsRef = FirebaseDatabase.getInstance().getReference();
        // get username from intent
        getUsername();
        nav_username = findViewById(R.id.navigation_txt_username);
        // set username to navigation menu
        nav_username.setText(_username);

        // init drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        sanpham = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list_bills);
        list.setAdapter(adapter);

        getData();
        setData();

        NextProcessBtn = (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        cancel = (Button) findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
                intent.putStringArrayListExtra("list_sp", sanpham);
                Log.d("goiuser", "onItemClick: " + _username);
                intent.putExtra("username", _username);

                startActivity(intent);
                finish();

            }
        });
        //fire-base


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = adapter.getItem(position);
                String key = data.split("\n")[0];
                Intent intent = new Intent(CartActivity.this, Cart_System_Activity.class);
                intent.putExtra("pid", key);
                intent.putExtra("username", _username);
                startActivity(intent);

            }
        });
    }

    private void getData() {

        if (getIntent().hasExtra("bill_product_pname")
                && getIntent().hasExtra("chuyen_soluong_bill")
        ) {

            child_product = getIntent().getStringExtra("bill_product_pname");
            chuyen_soluong_bill = getIntent().getStringExtra("chuyen_soluong_bill");


        } else {
            Log.d("loi", "getData: fails ");
        }
    }

    private void setData() {
        try {

            Query query = ProductsRef.child("Cart List").child(_username);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        adapter.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String key = data.getKey();

                            soluong = data.child("qual").getValue(String.class);

                            String bill = data.getValue(Bill.class).toString() + "\n" + "số lượng : " + soluong + " cái";


                            adapter.add(key + bill);
                            sanpham.add(key + bill);
                        }
                    } catch (Exception e) {
                        Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("loi~", "onCancelled: ", error.toException());
                }
            });

        } catch (Exception e) {
            Log.d("Exception error", "setData: " + e.getMessage());
        }

    }

    private void getUsername() {
        intent = getIntent();
        _username = intent.getStringExtra("username");
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
        ProductDisplayActivity.closeDrawer(drawerLayout);
    }

    public void ClickBill(View view) {
        ProductDisplayActivity.redirectActivity(this, userBillManagement.class);
    }

    public void ClickExit(View view) {
        ProductDisplayActivity.logout(this);
    }

}
package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,cancel;
    private TextView txtTotalAmount;
    private DatabaseReference ProductsRef;
    private String child_product;
    String chuyen_soluong_bill;
    String soluong="";

    ListView list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        list =(ListView)findViewById(R.id.list_bills);
        list.setAdapter(adapter);
/*
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


 */
        NextProcessBtn = (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        cancel = (Button) findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,ProductDisplayActivity.class);
                startActivity(intent);
            }
        });
        //fire-base
        ProductsRef = FirebaseDatabase.getInstance().getReference();

        getData();
        setData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=adapter.getItem(position);
                String key=data.split("\n")[0];
                Intent intent=new Intent(CartActivity.this,Cart_System_Activity.class);
                intent.putExtra("pid",key);
                startActivity(intent);

            }
        });
    }

    private void getData() {

        if (getIntent().hasExtra("bill_product_pname")
            && getIntent().hasExtra("chuyen_soluong_bill")
        )
        {

            child_product = getIntent().getStringExtra("bill_product_pname");
            chuyen_soluong_bill = getIntent().getStringExtra("chuyen_soluong_bill");
            Log.d("kiemtra", "getData: ok" + chuyen_soluong_bill);

        }else {
            Log.d("loi", "getData: fails ");
        }
    }

    private void setData() {

        Query query = ProductsRef.child("Cart List").child("0982");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    adapter.clear();
                    for (DataSnapshot data : snapshot.getChildren()){

                            String key = data.getKey();
                            Log.d("loi~", "loi adapter -"+ key);
                            soluong = data.child("qual").getValue(String.class);

                            String bill = data.getValue(Bill.class).toString() + "\n" + "số lượng : " + soluong + " cái" ;

                            adapter.add(key + bill);

                    }
                }catch (Exception e){
                    Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("loi~", "loi adapter "+ soluong);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("loi~", "onCancelled: ",error.toException());
            }
        });
    }
}
package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,cancel;
    private TextView txtTotalAmount;
    private DatabaseReference ProductsRef;
    private String child_product;
    String chuyen_soluong_bill;
    String soluong="";
    private String _username ="0982";
    ListView list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //get intent username
//        _username = getIntent().getStringExtra("username");


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
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
                for (int i = 0 ; i < adapter.getCount(); i++){
                    String[] item =  new String[adapter.getCount()];
                    item[i] = adapter.getItem(i);

                    ConfirmInformation(item[i]);
                }
                startActivity(intent);
                finish();

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

        Query query = ProductsRef.child("Cart List").child(_username);
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
    private void ConfirmInformation(String item){

        final String saveCurrentDate, saveCurrentTime;


        Calendar Cafordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(Cafordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(Cafordate.getTime());

        final String KeyBill = saveCurrentTime + saveCurrentDate;
        final DatabaseReference Information = FirebaseDatabase.getInstance().getReference().child("order").child(_username);

        HashMap<String, Object> orderMap  = new HashMap<>();
        orderMap.put("sanpham",item);
        orderMap.put("pid", KeyBill);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);

        Information.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child(_username)
                            .removeValue();

            }
        });


    }
}
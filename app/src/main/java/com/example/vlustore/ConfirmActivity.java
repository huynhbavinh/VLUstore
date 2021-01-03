package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vlustore.models.Bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmActivity extends AppCompatActivity {
    private EditText Name, Phone, Address;
    private Button confirm;
    private String totalAmount = "";
    String KeyProduct;

    private String _username;
    private Intent intent;
    private DatabaseReference ProductsRef;
    private ArrayList<String> sanpham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        confirm = (Button) findViewById(R.id.confirm_btn);
        Name = (EditText) findViewById(R.id.edname);
        Phone = (EditText) findViewById(R.id.edNum);
        Address = (EditText) findViewById(R.id.edaddress);

        getUsername();
        getData();
        ProductsRef = FirebaseDatabase.getInstance().getReference();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    void getData() {
        sanpham = getIntent().getStringArrayListExtra("list_sp");
        Log.d("loisanpham", "getData: "+sanpham.size());
    }

    private void Check() {
        if (TextUtils.isEmpty(Name.getText().toString())) {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Phone.getText().toString())) {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Address.getText().toString())) {
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        } else {
            for (String item : sanpham) {
                ConfirmInformation(item);
            }
        }
    }

    //    private void ConfirmInformation(){
//        final String saveCurrentDate, saveCurrentTime;
//
//        Calendar Cafordate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
//        saveCurrentDate = currentDate.format(Cafordate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentDate.format(Cafordate.getTime());
//
//        Log.d("loiusername", "ConfirmInformation: " + _username);
//        final DatabaseReference Information = FirebaseDatabase.getInstance().getReference().child("order").child(_username);
//
//        HashMap<String, Object> orderMap  = new HashMap<>();
//        orderMap.put("pid", KeyProduct);
//        orderMap.put("name", Name.getText().toString());
//        orderMap.put("phone", Phone.getText().toString());
//        orderMap.put("address", Address.getText().toString());
//        orderMap.put("date", saveCurrentDate);
//        orderMap.put("time", saveCurrentTime);
//
//        ConfirmInformation();
//    }
    private void getUsername() {
        intent = getIntent();
        _username = intent.getStringExtra("username");
    }

    private void ConfirmInformation(String item) {

        final String saveCurrentDate, saveCurrentTime;


        Calendar Cafordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(Cafordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(Cafordate.getTime());

        final String KeyBill = saveCurrentTime + saveCurrentDate;
        final DatabaseReference Information = FirebaseDatabase.getInstance().getReference().child("order").child(_username);

        final HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("sanpham", item);
        orderMap.put("pid", KeyBill);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("pid", KeyProduct);
        orderMap.put("name", Name.getText().toString());
        orderMap.put("phone", Phone.getText().toString());
        orderMap.put("address", Address.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);

        Information.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductsRef.child("order").child(_username).setValue(orderMap);

                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child(_username)
                        .removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
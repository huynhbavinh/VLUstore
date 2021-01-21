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

    private String _username;
    private Intent intent;
    private DatabaseReference ProductsRef;
    private ArrayList<String> sanpham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        getUsername();
        sanpham = new ArrayList<String>();

        Log.d("user", "onCreate: tao" + _username);
        ProductsRef = FirebaseDatabase.getInstance().getReference();

        getData();

        confirm = (Button) findViewById(R.id.confirm_btn);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String item : sanpham
                ) {
                    ConfirmInformation(item);

                }
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child(_username)
                        .removeValue();
                Toast.makeText(ConfirmActivity.this, "thank you", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    void getData() {
        sanpham = getIntent().getStringArrayListExtra("list_sp");
    }

    private boolean Check() {
        String valName = Name.getText().toString();
        String valPhone = Phone.getText().toString();
        String valAddress = Address.getText().toString();
        if (valName.isEmpty() | valPhone.isEmpty() | valAddress.isEmpty()) {
            return false;
        } else
            return true;
    }


    private void getUsername() {
        intent = getIntent();
        _username = intent.getStringExtra("username");
    }

    private void ConfirmInformation(String item) {
        int counter = 0;
        String temp = String.valueOf(counter);
        final String bo_dem = temp;
        Name = (EditText) findViewById(R.id.edname);
        Phone = (EditText) findViewById(R.id.edNum);
        Address = (EditText) findViewById(R.id.edaddress);
        if (Check() == false) {
            Toast.makeText(this, "Hãy nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            Log.d("validator :", "ConfirmInformation: " + Check());
            return;
        } else {

            final String saveCurrentDate, saveCurrentTime;

            Calendar Cafordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(Cafordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(Cafordate.getTime());

            final String KeyBill = saveCurrentTime + saveCurrentDate;


            final HashMap<String, Object> orderMap = new HashMap<>();

            String nameUserBill = Name.getText().toString();
            String phoneUserBill = Phone.getText().toString();
            String addresUserBill = Address.getText().toString();


            orderMap.put("sanpham", sanpham);
            orderMap.put("pid", KeyBill);
            orderMap.put("date", saveCurrentDate);
            orderMap.put("time", saveCurrentTime);
            orderMap.put("name", nameUserBill);
            orderMap.put("phone", phoneUserBill);
            orderMap.put("address", addresUserBill);

            counter++;

            ProductsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ProductsRef.child("order").child(_username).child(KeyBill).setValue(orderMap);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
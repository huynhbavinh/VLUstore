package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vlustore.models.Bill;
import com.example.vlustore.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class mainDetailProduct extends AppCompatActivity {
    private String _username;
    private Intent intent;
    TextView ednoidung,edgiatien,edtensanpham,edsoluong;
    TextView noidung,giatien,tensanpham,quantity;
    Button quaylai,giohang;
    ImageView img;
    String d_pname,d_price,d_des,d_img;
    String keyProduct;
    int soluong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail_product);
        getUsername();
        Log.d("product details: ", "onCreate: " + _username);

        ednoidung = (TextView) findViewById(R.id.ednoidung);
        edgiatien = (TextView) findViewById(R.id.edgiatien);
        edtensanpham = (TextView) findViewById(R.id.edsanpham);
        noidung = (TextView) findViewById(R.id.mota);
        giatien = (TextView) findViewById(R.id.giatien);
        edsoluong = (EditText) findViewById(R.id.edsoluong);
        quantity = (TextView) findViewById(R.id.quantity);
        tensanpham = (TextView) findViewById(R.id.sanpham);
        img = (ImageView) findViewById(R.id.d_imageView);
        quaylai = (Button) findViewById(R.id.cancelbtn);
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        setData();
        giohang = (Button) findViewById(R.id.giohang);
        giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void getUsername() {
        intent = getIntent();
        _username = intent.getStringExtra("username");
    }


    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(_username).child(d_pname);

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",keyProduct);
        cartMap.put("pnmane",edtensanpham.getText().toString());
        cartMap.put("price",edgiatien.getText().toString());
        cartMap.put("des",ednoidung.getText().toString());
        cartMap.put("qual",edsoluong.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);

        soluong = Integer.valueOf(edsoluong.getText().toString());
        //DatabaseReference load_product = FirebaseDatabase.getInstance().getReference().child("Products").child(keyProduct);
        cartListRef.updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  Toast.makeText(mainDetailProduct.this,"Added to Cart List.",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(mainDetailProduct.this,CartActivity.class);
                  intent.putExtra("bill_product_pname",d_pname);
                  Bill bill = new Bill(edtensanpham.getText().toString(),ednoidung.getText().toString(),edgiatien.getText().toString(),soluong);
                  String chuyen_soluong_bill = String.valueOf(bill.getQuality());
                  intent.putExtra("chuyen_soluong_bill",chuyen_soluong_bill);
                  intent.putExtra("username", _username);
                  startActivity(intent);
              }
            }
        });
    }

    private void getData(){
            if (getIntent().hasExtra("pname")

                    && getIntent().hasExtra("price")

                    && getIntent().hasExtra("des")

                    && getIntent().hasExtra("img"))
            {
                Log.d("loidata", "getData: "+getIntent().getStringExtra("img"));
                d_pname = getIntent().getStringExtra("pname");
                d_price = getIntent().getStringExtra("price");
                d_des = getIntent().getStringExtra("des");
                d_img = getIntent().getStringExtra("img");
                keyProduct = getIntent().getStringExtra("day_create") + getIntent().getStringExtra("time_create") ;

                Log.d("kiem ta", "getData: ok");
            }else {
                Log.d("loi", "getData: fails ");
            }
    }
    private void setData(){
        Glide.with(this)
                .load(d_img)
                .into(img);
        edtensanpham.setText(d_pname);
        edgiatien.setText(d_price);
        ednoidung.setText(d_des);
    }
}


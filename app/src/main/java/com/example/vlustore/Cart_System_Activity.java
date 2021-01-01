package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cart_System_Activity extends AppCompatActivity {
    EditText ednoidung,edgiatien,edtensanpham,edsoluong;
    TextView noidung,giatien,tensanpham,quantity;
    Button update,delete;
    ImageView img;
    String d_pname,d_price,d_des,d_img;
    String keyProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__system_);

        ednoidung = (EditText) findViewById(R.id.ednoidung);
        edgiatien = (EditText) findViewById(R.id.edgiatien);
        edtensanpham = (EditText) findViewById(R.id.edsanpham);
        noidung = (TextView) findViewById(R.id.mota);
        giatien = (TextView) findViewById(R.id.giatien);
        edsoluong = (EditText) findViewById(R.id.edsoluong);
        quantity = (TextView) findViewById(R.id.quantity);
        tensanpham = (TextView) findViewById(R.id.sanpham);
        img = (ImageView) findViewById(R.id.d_imageView);
        getContactDetail();
    }
    private void getContactDetail(){
        Intent intent = getIntent();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("Cart List");
        myReference.child(keyProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    edtensanpham.setText(hashMap.get("pnname").toString());
                    edgiatien.setText(hashMap.get("price").toString());
                    ednoidung.setText(hashMap.get("des").toString());
                    quantity.setText(hashMap.get("qual").toString());

                } catch (Exception e) {
                    Log.d("LOI_JSON",e.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());

            }
        });
        delete = (Button)findViewById(R.id.deleted);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart List");
                myRef.child(keyProduct).removeValue();
                finish();


            }
        });
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart List");
                String contactId=keyProduct;
                String pnname =ednoidung.getText().toString();
                String price =edgiatien.getText().toString();
                String des =ednoidung.getText().toString();
                String qual =edsoluong.getText().toString();
                myRef.child(contactId).child("pnname").setValue(edtensanpham);
                myRef.child(contactId).child("price").setValue(edgiatien);
                myRef.child(contactId).child("des").setValue(ednoidung);
                myRef.child(contactId).child("qual").setValue(edsoluong);
                finish();
            }
        });
    }
}
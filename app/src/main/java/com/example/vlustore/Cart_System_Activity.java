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

import com.example.vlustore.models.Bill;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cart_System_Activity extends AppCompatActivity {
    EditText ednoidung,edgiatien,edtensanpham,edsoluong;
    TextView noidung,giatien,tensanpham,quantity;
    Button update,delete;
    ImageView img;
    String d_pname,d_price,d_des,d_img;
    String keyProduct;
    String TAG = "loiCartSystem";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__system_);

        edsoluong = (EditText) findViewById(R.id.edsoluong);
        quantity = (TextView) findViewById(R.id.quantity);
        getContactDetail();
    }
    private void getContactDetail(){
        Intent intent = getIntent();

        if(getIntent().hasExtra("pid"))
        {
            keyProduct = getIntent().getStringExtra("pid");
        }else{
            Log.d("loiCartSystem", "getContactDetail:fail " );
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Cart List");
        //DatabaseReference myReference = database.getReference("Cart List");

        Query query = database.child("0982").orderByChild(keyProduct);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  String qual = snapshot.child(keyProduct).child("qual").getValue().toString();
                  edsoluong.setText(qual);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        myReference.child("0982").child(keyProduct).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                q
                try {
                    Bill bills = snapshot.getValue(Bill.class);


//                    edtensanpham.setText(snapshot.child("pnname").getValue(String.class));
//                    Log.d(TAG, "onDataChange1: " + snapshot.child("pnname").getValue(String.class));
//
//
//                    edgiatien.setText(snapshot.child(keyProduct).child("price").getValue(String.class));
//                    ednoidung.setText(snapshot.child(keyProduct).child("des").getValue(String.class));
//                    quantity.setText(snapshot.child(keyProduct).child("qual").getValue(String.class));
//                    edtensanpham.setText(hashMap.get("pnname").toString());
//                    edgiatien.setText(hashMap.get("price").toString());
//                    ednoidung.setText(hashMap.get("des").toString());
//                    quantity.setText(hashMap.get("qual").toString());




                } catch (Exception e) {
                    Log.d("LOI_JSON",e.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LOI_CHITIET", "loadPost:onCancelled", databaseError.toException());

            }
        });
        */


        delete = (Button)findViewById(R.id.deleted);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart List");
                myRef.child("0982").child(keyProduct).removeValue();
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
                String qual =edsoluong.getText().toString();
                myRef.child("0982").child(contactId).child("qual").setValue(qual);
                finish();
            }
        });
    }
}
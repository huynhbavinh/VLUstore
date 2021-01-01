package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vlustore.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference ProductsRef;
    //ListView lvSP;
    ArrayList<Product> mangSP;
    String TAG = "loi";
    //private RecyclerAdapter recyclerAdapter;
    Button floaticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        recyclerView = (RecyclerView)findViewById(R.id.recy_list);

        ProductsRef = FirebaseDatabase.getInstance().getReference();
        //lvSP = (ListView) findViewById(R.id.listviewSP);
        mangSP = new ArrayList<>();
        getData();
    }
    void getData(){
        Query query = ProductsRef.child("Products");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangSP.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    /*
                    product.setImage(data.child("image").getValue().toString());
                    product.setPname(data.child("pname").toString());
                    product.setCategory(data.child("category").toString());
                    product.setDate(data.child("date").toString());
                    product.setDescription(data.child("description").toString());
                    product.setPid(data.child("pid").toString());
                    product.setPrice(data.child("price").toString());
                    product.setTime(data.child("time").toString());
                     */

                    mangSP.add(product);
                }
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getApplicationContext(),mangSP);

                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductDisplayActivity.this));
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*
    void ClearAll(){
        if (mangSP != null){
            mangSP.clear();
            if (recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }

    }
     */
}
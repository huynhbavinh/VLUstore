package com.example.vlustore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userBillManagement extends AppCompatActivity {
    private RecyclerView billRecycleView;
    private RecyclerView.Adapter billAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<com.example.vluadmin.Billitems> billitems;


    private DatabaseReference billReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bill_management);

        billReference = FirebaseDatabase.getInstance().getReference();
        getData();

    }
    private void getData() {
        billitems = new ArrayList<>();
        Query query = billReference.child("order");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billitems.clear();
                for (DataSnapshot item : snapshot.getChildren()) {

                    String _user = item.getKey();
                    String date = item.child("time").getValue().toString();
                    billitems.add(new com.example.vluadmin.Billitems(_user,date));
                    Log.d("bill:", "onDataChange: " + date);
                }
                setBillRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setBillRecycleView() {
        billRecycleView = findViewById(R.id.bill_recycler_view);
        billRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        billAdapter = new BillRecyclerAdapter(billitems);

        billRecycleView.setLayoutManager(layoutManager);
        billRecycleView.setAdapter(billAdapter);
    }
}
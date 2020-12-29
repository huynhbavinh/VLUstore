package com.example.vlustore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class mainDetailProduct extends AppCompatActivity {
    EditText ednoidung,edgiatien,edtensanpham;
    TextView noidung,giatien,tensanpham;
    Button quaylai,giohang;
    ImageView img;
    String d_pname,d_price,d_des,d_img;
    String keyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail_product);


        ednoidung = (EditText) findViewById(R.id.ednoidung);
        edgiatien = (EditText) findViewById(R.id.edgiatien);
        edtensanpham = (EditText) findViewById(R.id.edsanpham);
        noidung = (TextView) findViewById(R.id.mota);
        giatien = (TextView) findViewById(R.id.giatien);
        tensanpham = (TextView) findViewById(R.id.sanpham);
        img = (ImageView) findViewById(R.id.d_imageView);
        quaylai = (Button) findViewById(R.id.cancel);

        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        setData();
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


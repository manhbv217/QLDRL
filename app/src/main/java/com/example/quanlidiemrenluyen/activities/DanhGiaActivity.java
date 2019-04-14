package com.example.quanlidiemrenluyen.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.quanlidiemrenluyen.R;

public class DanhGiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        Toolbar toolbar = findViewById(R.id.toolbardanhgia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hoàn Thiện Phiếu Đánh Giá");
    }
}

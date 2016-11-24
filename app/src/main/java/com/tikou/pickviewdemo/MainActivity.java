package com.tikou.pickviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tikou.library_pickaddress.AddressPickView.AddressPickView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressPickView addressPickView=new AddressPickView(MainActivity.this,224);
                addressPickView.showPopWin(MainActivity.this);
                addressPickView.setAddresskListener(new AddressPickView.OnAddressCListener() {
                    @Override
                    public void onClick(String province, String city, String area) {
                        Log.e("===",province+"==="+city+"==="+area);
                    }
                });
            }
        });
    }
}

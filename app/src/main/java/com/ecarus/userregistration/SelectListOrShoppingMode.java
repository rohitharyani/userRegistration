package com.ecarus.userregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectListOrShoppingMode extends AppCompatActivity implements View.OnClickListener{

    private TextView mWelcome;
    private LinearLayout mShoppingMode, mListMode;
    private ImageButton mProfile, mPreviousOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_list_or_shopping_mode);


        mProfile = (ImageButton)findViewById(R.id.profileImageButton);
        mProfile.setOnClickListener(this);
        mPreviousOrder = (ImageButton)findViewById(R.id.previousOrderList);
        mPreviousOrder.setOnClickListener(this);
        mWelcome = (TextView)findViewById(R.id.welcomeName);
        mWelcome.setText("Hello, "+SharedPrefManager.getInstance(this).getUserName());
        mShoppingMode = (LinearLayout) findViewById(R.id.shoppingMode);
        mShoppingMode.setOnClickListener(this);
        mListMode = (LinearLayout) findViewById(R.id.listMode);
        mListMode.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == mProfile){
            startActivity(new Intent(SelectListOrShoppingMode.this, UserProfile.class));
        }
        else if(v == mPreviousOrder){
            startActivity(new Intent(SelectListOrShoppingMode.this, PreviousOrder.class));
        }
        else if (v == mShoppingMode){
            startActivity(new Intent(SelectListOrShoppingMode.this, ShoppingMode.class));
        }
        if (v == mListMode){
            startActivity(new Intent(SelectListOrShoppingMode.this,ListProductCategory.class));
        }
    }
}

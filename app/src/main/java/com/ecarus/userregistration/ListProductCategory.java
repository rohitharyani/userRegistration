package com.ecarus.userregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ListProductCategory extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mCart,mRecommender;
    private LinearLayout mDairyProducts,mFrozenFood,mGroceryAndStaples,mPersonalCareAndHouseHold;
    private LinearLayout mFruitsAndVegetables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_product_category);

        mCart = (ImageButton)findViewById(R.id.productCategoryCartImageButton);
        mRecommender = (ImageButton)findViewById(R.id.RecommendationImageButton);
        mFruitsAndVegetables = (LinearLayout) findViewById(R.id.fruitsAndVegetablesButton);
        mDairyProducts = (LinearLayout) findViewById(R.id.dairyProductsButton);
        mFrozenFood = (LinearLayout)findViewById(R.id.frozenFoodButton);
        mGroceryAndStaples = (LinearLayout)findViewById(R.id.groceryAndStaplesButton);
        mPersonalCareAndHouseHold = (LinearLayout)findViewById(R.id.personalCareAndHouseholdButton);

        mCart.setOnClickListener(this);
        mRecommender.setOnClickListener(this);
        mFruitsAndVegetables.setOnClickListener(this);
        mDairyProducts.setOnClickListener(this);
        mFrozenFood.setOnClickListener(this);
        mGroceryAndStaples.setOnClickListener(this);
        mPersonalCareAndHouseHold.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == mCart){
            startActivity(new Intent(this,ListCartActivity.class));

        }else if(v == mRecommender){
            startActivity(new Intent(this,RecommendationSystem.class));
        }
        else if(v == mFruitsAndVegetables){
            startActivity(new Intent(this,ListFruitsAndVegetables.class));

        }
        else if(v == mDairyProducts){
            startActivity(new Intent(this,ListDairyProducts.class));
        }
        else if(v == mFrozenFood){
            startActivity(new Intent(this,ListFrozenFood.class));
        }
        else if(v == mGroceryAndStaples){
            startActivity(new Intent(this,ListGroceryAndStaples.class));
        }
        else if(v == mPersonalCareAndHouseHold){
            startActivity(new Intent(this,ListPersonalAndHouse.class));
        }
    }
}

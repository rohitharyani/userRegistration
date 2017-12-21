package com.ecarus.userregistration;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    private TextView mProfileName, mProfileEmail,mProfilePhone;
    private ImageButton mBarcode, mUpdate, mWallet;
    private Button mLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,UserLogin.class));

        }

        mProfileName = (TextView)findViewById(R.id.profileNameTextView);
        mProfileEmail = (TextView)findViewById(R.id.profileEmailTextView);
        mProfilePhone = (TextView)findViewById(R.id.profilePhoneTextView);

        mProfileName.setText(SharedPrefManager.getInstance(this).getUserName());
        mProfileEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        mProfilePhone.setText(SharedPrefManager.getInstance(this).getUserPhone());

        mBarcode = (ImageButton)findViewById(R.id.profileBarcode);
        mBarcode.setOnClickListener(this);
        mLogout = (Button)findViewById(R.id.logoutButton);
        mLogout.setOnClickListener(this);
        mUpdate = (ImageButton)findViewById(R.id.updateDetails);
        mUpdate.setOnClickListener(this);
        mWallet = (ImageButton)findViewById(R.id.wallet);
        mWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBarcode){
            startActivity(new Intent(this,UserProfileBarcode.class));
        }
        else if(v == mLogout){
            SharedPrefManager.getInstance(this).logout();
            finish();
            Intent intent = new Intent(UserProfile.this, UserLogin.class);
            // set the new task and clear flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(v == mUpdate){
            startActivity(new Intent(this,UserUpdateProfile.class));
        }
        else if(v == mWallet){
            startActivity(new Intent(this,WalletAddMoney.class));
        }
    }
}

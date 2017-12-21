package com.ecarus.userregistration;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WalletAddMoney extends AppCompatActivity implements View.OnClickListener{


    private TextView mBalance;
    private Button mAddMoney,m100,m500,m1000;
    private Button mVerifyOtp;
    private EditText mMoney,mConfirmOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_add_money);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},1);

        mBalance=(TextView)findViewById(R.id.walletBalance);
        mMoney = (EditText)findViewById(R.id.walletMoney);
        m100 = (Button)findViewById(R.id.Button100);
        m100.setOnClickListener(this);
        m500 = (Button)findViewById(R.id.Button500);
        m500.setOnClickListener(this);
        m1000 = (Button)findViewById(R.id.Button1000);
        m1000.setOnClickListener(this);
        mAddMoney = (Button)findViewById(R.id.walletAddMoneyButton);
        mAddMoney.setOnClickListener(this);

        getBalance();
    }

    private void getBalance() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading balance...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_WALLET_BALANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("walletBalance");

                            JSONObject o = array.getJSONObject(0);
                            mBalance.setText("Balance: ₹"+o.getString("balance"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", SharedPrefManager.getInstance(getApplicationContext()).getUserPhone());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void addMoney() {
       final String money = mMoney.getText().toString().trim();
       if(TextUtils.isEmpty(money)) {
           mMoney.setError("Please enter a valid amount.");
           return;
       } else{
           final ProgressDialog progressDialog = new ProgressDialog(this);
           progressDialog.setMessage("SMS charges may apply...");
           progressDialog.setTitle("Sending OTP!");
           progressDialog.show();

           Random OTP = new Random();
           final int otp = OTP.nextInt(9999 - 1111 + 1) + 1111;
           String message = "Welcome to ECARUS! Your One Time Password is: " + otp + ". Use this OTP to add money successfully to your account.";
           final String phone = SharedPrefManager.getInstance(getApplicationContext()).getUserPhone();

           SmsManager manager = SmsManager.getDefault();
           manager.sendTextMessage(phone, null, message, null, null);

  /*         StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_SEND_OTP,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           progressDialog.dismiss();
                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               if (jsonObject.getString("status") == "success") {
                                   Toast.makeText(getApplicationContext(), "SMS sent successfully!", Toast.LENGTH_LONG).show();
                                  // enterOTP();
                               } else {
                                   Toast.makeText(getApplicationContext(), jsonObject.getString("status"), Toast.LENGTH_LONG).show();
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   progressDialog.hide();
                   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
               }
           }) {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   params.put("phone", SharedPrefManager.getInstance(getApplicationContext()).getUserPhone());
                   params.put("otp", String.valueOf(otp));
                   return params;
               }
           };

           RequestHandler.getInstance(this).addToRequestQueue(stringRequest1);
*/

           StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_WALLET_SET_OTP,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           progressDialog.dismiss();
                           try {
                               JSONObject jsonObject = new JSONObject(response);
                               if (!jsonObject.getBoolean("error")) {
                                   //Toast.makeText(getApplicationContext(), "SMS sent successfully!", Toast.LENGTH_LONG).show();
                                   enterOTP();
                               } else {
                                   Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   progressDialog.hide();
                   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
               }
           }) {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   params.put("phone", SharedPrefManager.getInstance(getApplicationContext()).getUserPhone());
                   params.put("otp", String.valueOf(otp));
                   return params;
               }
           };

           RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
       }
    }



    private void enterOTP() throws JSONException{
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm_otp, null);
        mVerifyOtp = (Button)confirmDialog.findViewById(R.id.confirmOtpButton);
        mConfirmOtp = (EditText)confirmDialog.findViewById(R.id.otpEditText);

        final String moneyAdd = mMoney.getText().toString().trim();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        mVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp1 = mConfirmOtp.getText().toString().trim();
                if(TextUtils.isEmpty(otp1)){
                    mConfirmOtp.setError("Please enter a valid OTP.");
                    return;
                }
                else{
                    alertDialog.dismiss();
                    final ProgressDialog loading = ProgressDialog.show(WalletAddMoney.this, "Verifying OTP", "Please wait while we check the entered OTP.", false,false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_WALLET_ADD_MONEY,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (!jsonObject.getBoolean("error")) {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(),WalletAddMoney.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.hide();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("phone", SharedPrefManager.getInstance(getApplicationContext()).getUserPhone());
                            params.put("otp", otp1);
                            params.put("balance",moneyAdd);
                            return params;
                        }
                    };

                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });
    }





    @Override
    public void onClick(View v) {
        if (v == mAddMoney){
            addMoney();
        }
        else if (v == m100){
            mMoney.setText("100");
        }
        else if (v == m500){
            mMoney.setText("500");
        }
        else if (v == m1000){
            mMoney.setText("1000");
        }
    }

}

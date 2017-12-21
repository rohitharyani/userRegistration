package com.ecarus.userregistration;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText mForgotPhone, mForgotPassword1, mForgotPassword2, mConfirmOtp;
    private Button mUpdatePassword;
    private Button mVerifyOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_forgot_password);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SelectListOrShoppingMode.class));
            return;
        }

        mForgotPhone = (EditText) findViewById(R.id.forgotPhone);
        mForgotPassword1 = (EditText) findViewById(R.id.forgotPassword1);
        mForgotPassword2 = (EditText) findViewById(R.id.forgotPassword2);
        mUpdatePassword = (Button) findViewById(R.id.updatePasswordButton);
        mUpdatePassword.setOnClickListener(this);

    }

    private void updatePassword() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm_otp, null);
        mVerifyOtp = (Button) confirmDialog.findViewById(R.id.confirmOtpButton);
        mConfirmOtp = (EditText) confirmDialog.findViewById(R.id.otpEditText);

        final String phone = mForgotPhone.getText().toString().trim();
        final String password = mForgotPassword1.getText().toString().trim();

        AlertDialog.Builder alert = new AlertDialog.Builder(UserForgotPassword.this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        mVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp1 = mConfirmOtp.getText().toString().trim();
                if (TextUtils.isEmpty(otp1)) {
                    mConfirmOtp.setError("Please enter a valid OTP.");
                    return;
                } else {
                    alertDialog.dismiss();
                    final ProgressDialog loading = ProgressDialog.show(UserForgotPassword.this, "Verifying OTP", "Please wait while we check the entered OTP.", false, false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_FORGOT_PASSWORD,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (!jsonObject.getBoolean("error")) {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            finish();
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
                            params.put("phone", phone);
                            params.put("password", password);
                            params.put("otp", otp1);
                            return params;
                        }
                    };

                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });
    }


    private void sendOTP() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        int permissionCheck = ContextCompat.checkSelfPermission(UserForgotPassword.this,
                Manifest.permission.SEND_SMS);

        final String phone = mForgotPhone.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("SMS charges may apply...");
        progressDialog.setTitle("Sending OTP!");
        progressDialog.show();

        Random OTP = new Random();
        final int otp = OTP.nextInt(9999 - 1111 + 1) + 1111;
        String message = "Welcome to ECARUS! Your One Time Password is: " + otp + ". Use this OTP to change your password.";

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, message, null, null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_SET_PASSWORD_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), "SMS sent successfully!", Toast.LENGTH_LONG).show();
                                updatePassword();
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
                params.put("phone", phone);
                params.put("otp", String.valueOf(otp));
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if (v == mUpdatePassword) {
            userPhoneExists();
        }
    }

    private void userPhoneExists() {

        final String phone = mForgotPhone.getText().toString().trim();
        final String password1 = mForgotPassword1.getText().toString().trim();
        final String password2 = mForgotPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || phone.length() != 10) {
            mForgotPhone.setError("Please enter a valid 10 digit phone number.");
            return;
        } else if (TextUtils.isEmpty(password1) || password1.length() != 5) {
            mForgotPassword1.setError("Please enter a 5 digit numeric password.");
            return;
        } else if (TextUtils.isEmpty(password2) || password2.length() != 5) {
            mForgotPassword1.setError("Please enter a 5 digit numeric password.");
            return;
        } else {
            if (!password1.equals(password2)) {
                mForgotPassword2.setError("Passwords do not match!");
                return;
            } else {
                final ProgressDialog loading = ProgressDialog.show(UserForgotPassword.this,
                        "Please wait...", "We are searching for the user!", false, false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_EXISTS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        sendOTP();
                                    } else {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserForgotPassword.this);
                                        builder1.setTitle("User does not exist!");
                                        builder1.setMessage("Please enter a valid phone number.");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                        //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                        params.put("phone", phone);

                        return params;
                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        }
    }
}
package com.ecarus.userregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UserLogin extends AppCompatActivity implements View.OnClickListener{

    private EditText mPhone, mPassword;
    private Button mSignUp, mLogin, mLoginForgot;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, SelectListOrShoppingMode.class));
            return;
        }

        mPhone = (EditText)findViewById(R.id.loginPhone);
        mPassword = (EditText)findViewById(R.id.loginPassword);
        mLogin = (Button)findViewById(R.id.loginButton);
        mLogin.setOnClickListener(this);
        mSignUp = (Button)findViewById(R.id.signUpButton);
        mSignUp.setOnClickListener(this);
        mLoginForgot = (Button)findViewById(R.id.loginForgotPasswordButton);
        mLoginForgot.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Please wait...");

    }

    private void userLogin(){

        final String phone = mPhone.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 10) {
            mPhone.setError("Please enter a valid 10 digit phone number.");
            return;
        }
        else if (TextUtils.isEmpty(password) || password.length() != 5) {
            mPassword.setError("Please enter a 5 digit numeric password.");
            return;
        }
        else {

            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("phone") , jsonObject.getString("password"));
                                    Toast.makeText(getApplicationContext(), "User login successful!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), SelectListOrShoppingMode.class);
                                    startActivity(intent);
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
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("password", password);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mSignUp){
          //  finish();
            startActivity(new Intent(this,UserRegister.class));

        }else if (v == mLogin){
            userLogin();
        }
        else if (v == mLoginForgot){
           // finish();
            startActivity(new Intent(this,UserForgotPassword.class));
        }
    }
}

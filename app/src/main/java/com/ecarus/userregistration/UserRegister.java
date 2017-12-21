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

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private EditText mName, mEmail, mPhone, mPassword, mPassword2;
    private Button mRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ListProductCategory.class));
            return;
        }

        mName = (EditText)findViewById(R.id.registerName);
        mEmail = (EditText)findViewById(R.id.registerEmail);
        mPhone = (EditText)findViewById(R.id.registerPhone);
        mPassword = (EditText)findViewById(R.id.registerPassword);
        mPassword2 = (EditText)findViewById(R.id.registerPassword2);
        mRegister = (Button) findViewById(R.id.registerButton);

        progressDialog = new ProgressDialog(this);

        mRegister.setOnClickListener(this);

    }

    private void registerUser() {
        final String name = mName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        final String phone = mPhone.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String password2 = mPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mName.setError("Please enter your name.");
            return;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError("Please enter your email id.");
            return;
        } else if (TextUtils.isEmpty(phone) || phone.length() != 10) {
            mPhone.setError("Please enter a valid 10 digit phone number.");
            return;
        } else if (TextUtils.isEmpty(password) || password.length() != 5) {
            mPassword.setError("Please enter a 5 digit numeric password.");
            return;
        } else if(TextUtils.isEmpty(password2) || !password.equals(password2)){
            mPassword2.setError("Passwords do not match!");
            return;
        }
        else {
            progressDialog.setTitle("Registering user");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
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
                    params.put("name", name);
                    params.put("email", email);
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
        if(v == mRegister) {
            registerUser();
        }


    }
}

package com.ecarus.userregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserUpdateProfile extends AppCompatActivity implements View.OnClickListener {

    private TextView mUpdatePhone;
    private EditText mUpdateName, mUpdateEmail, mUpdatePassword;
    private Button mUpdateDeatils;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update_profile);

        mUpdatePhone = (TextView)findViewById(R.id.updateProfilePhoneTextView);
        mUpdateName = (EditText)findViewById(R.id.updateProfileNameEditText);
        mUpdateEmail = (EditText)findViewById(R.id.updateProfileEmailEditText);
        mUpdatePassword = (EditText)findViewById(R.id.updateProfilePasswordEditText);
        mUpdateDeatils = (Button)findViewById(R.id.wallet);

        mUpdateName.setText(SharedPrefManager.getInstance(this).getUserName());
        mUpdateName.setSelection(mUpdateName.getText().length());
        mUpdateEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        mUpdateEmail.setSelection(mUpdateEmail.getText().length());
        mUpdatePhone.setText(SharedPrefManager.getInstance(this).getUserPhone());
        mUpdatePassword.setText(SharedPrefManager.getInstance(this).getUserPassword());
        mUpdatePassword.setSelection(mUpdatePassword.getText().length());

        mUpdateDeatils.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);


    }

    private void updateDetails() {
        final String name = mUpdateName.getText().toString().trim();
        final String email = mUpdateEmail.getText().toString().trim();
        final String password = mUpdatePassword.getText().toString().trim();
        final String phone = mUpdatePhone.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mUpdateName.setError("Please enter your name.");
            return;
        } else if (TextUtils.isEmpty(email)) {
            mUpdateEmail.setError("Please enter your email id.");
            return;
        } else if (TextUtils.isEmpty(password) || password.length() != 5) {
            mUpdatePassword.setError("Please enter a 5 digit numeric password.");
            return;
        } else {
            progressDialog.setTitle("Updating user details");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_UPDATE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(UserUpdateProfile.this, UserLogin.class);
                        startActivity(intent);
                        SharedPrefManager.getInstance(UserUpdateProfile.this).logout();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                //    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone",phone);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }



    @Override
    public void onClick(View v) {
        if(v == mUpdateDeatils){
            updateDetails();
        }
    }
}

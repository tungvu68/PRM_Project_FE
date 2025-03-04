package com.example.prm_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    EditText email, password;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validate()){
                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();
                }
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url = "http://10.0.2.2:9080/api/v1/user/login";

                //set parameter
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject responseData = response.getJSONObject("response_data");

                            JSONObject userData = responseData.getJSONObject("data");
                            int userId = userData.getInt("userId");
                            String detail = userData.getString("firstName") + userData.getString("lastName");

                            Toast.makeText(LoginActivity.this,
                                    "Login successful! Welcome " + detail,
                                    Toast.LENGTH_SHORT).show();

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,
                                    "Response parsing error", Toast.LENGTH_SHORT).show();


                            Log.e("LoginActivity", "Error: " + e.getMessage());

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,
                                "Login error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("LoginActivity", "Error: " + error.getMessage());

                    }
                });


                queue.add(jsonObjectRequest);


            }
        });
    }

    public void authenticateUser(){

    }
    public boolean validate() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        boolean valid = true;

        //check email
        if (emailStr.isEmpty()) {
            email.setError("Email không được để trống");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("Email không hợp lệ");
            valid = false;
        } else {
            email.setError(null);
        }

        //check password
        if (passwordStr.isEmpty()) {
            password.setError("Mật khẩu không được để trống");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }



}
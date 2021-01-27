package com.example.agendamovil;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.session.VolleyCallback;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    FrameLayout body;
    Intent startAgenda;
    Toolbar toolbar;
    EditText email, password;
    Button login_btn;
    SharedPreferences session;
    List<EditText> inputs = new ArrayList<>();
    ProgressBarAgenda progressBar;
    InputValidator inputValidator = new InputValidator();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BackendConnexion.isLogged(this)){
            startActivity(BackendConnexion.start_agenda);
        }
        setContentView(R.layout.activity_login);
        session = getSharedPreferences("com.example.agendamovil", MODE_PRIVATE);
        body = findViewById(R.id.login_body);
        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.action_sign_in_short);
        progressBar = new ProgressBarAgenda(this);
        body.addView(progressBar);
        progressBar.bringToFront();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);

        inputs.add(email);

        email.addTextChangedListener(new ValidatorOnTextChange(this, email, InputValidator.email) {
            @Override
            public void validator() {
                super.validator();
            }
        });
        login_btn.setEnabled(true);
        login_btn.setOnClickListener(v -> {
            if(inputValidator.validForm(inputs)){
            valid_login(); }
        });
    }

    private void valid_login(){
        Map<String, String> params = new HashMap<>();
        BackendConnexion connexion = new BackendConnexion(this, BackendConnexion.LOGIN, progressBar);

        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());

        connexion.jsonRequest(params, new VolleyCallback() {
            @Override
            public void onResponseString(String response) {

            }

            @Override
            public void onResponseJsonObject(JSONObject response) {
                try {
                    String message = response.get("message").toString();
                    String email = response.get("email").toString();
                    String username = response.get("username").toString();

                    switch (message.trim()){
                        case "correct login":
                            SharedPreferences.Editor editor = session.edit();
                            editor.putString("email", email);
                            editor.putString("username", username);
                            editor.putBoolean("logged", true);
                           editor.apply();
                            if(BackendConnexion.isLogged(getApplicationContext())){
                                startActivity(BackendConnexion.start_agenda);
                            }
                            break;
                        case "incorrect password":
                        case "invalid params":
                            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.incorrect_pass_or_email), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                    }
                }catch (Exception e){
                    Log.e("array", e.toString());
                }
            }

            @Override
            public void onResponseJsonArray(JSONArray response) {

            }


        });
    }

    public void goSingup(View v){
        Intent singup = new Intent(this, Signup.class);
        startActivity(singup);
    }


}

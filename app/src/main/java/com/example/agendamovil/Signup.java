package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.session.VolleyCallback;
import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signup extends AppCompatActivity {

    FrameLayout body;
    EditText email, username, pass, confirmPass;
    Button sendButton;
    InputValidator inputValidator;
    Toolbar toolbar;
    ToolbarFunctions toolbarFunctions;
    List<EditText> inputs = new ArrayList<>();
    ProgressBarAgenda progressBarAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        body = findViewById(R.id.singup_body);

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.sign_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputValidator = new InputValidator();

        progressBarAgenda = new ProgressBarAgenda(this);
        body.addView(progressBarAgenda);
        progressBarAgenda.bringToFront();

        toolbarFunctions = new ToolbarFunctions(this);

        email = findViewById(R.id.singup_email);
        username = findViewById(R.id.singup_username);
        pass = findViewById(R.id.singup_pass);
        confirmPass = findViewById(R.id.singup_pass_confirm);

        inputs.add(email);
        inputs.add(username);
        inputs.add(pass);
        inputs.add(confirmPass);

        sendButton = findViewById(R.id.singup_send);

        email.addTextChangedListener(new ValidatorOnTextChange(this, email, InputValidator.email) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        username.addTextChangedListener(new ValidatorOnTextChange(this, username, InputValidator.name) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        pass.addTextChangedListener(new ValidatorOnTextChange(this, pass, InputValidator.password) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        pass.addTextChangedListener(new ValidatorOnTextChange(this ,pass, confirmPass, InputValidator.pass_confirm){
            @Override
            public void validator() {
                super.validator();
            }
        });

        confirmPass.addTextChangedListener(new ValidatorOnTextChange(this ,pass, confirmPass, InputValidator.pass_confirm){
            @Override
            public void validator() {
                super.validator();
            }
        });

    }

    public void validForm(View v){
        if(inputValidator.validForm(inputs)){
            uploadSignup();
        }
    }

    public void uploadSignup(){
        Map<String,String> params = new HashMap<>();
        BackendConnexion connexion = new BackendConnexion(this, BackendConnexion.SIGNUP, progressBarAgenda);

        params.put("user_email", email.getText().toString());
        params.put("user_name", username.getText().toString());
        params.put("user_pass", pass.getText().toString());

        connexion.stringRequest(params, new VolleyCallback() {
            @Override
            public void onResponseString(String response) {
                switch (response.trim()){
                    case "OK":
                        Toast OK = Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT);
                        OK.show();
                        toolbarFunctions.goLogin();
                        break;
                    case "error":
                        Toast error = Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT);
                        error.show();
                        break;
                    case "email_unavailable":
                        Toast unavailable = Toast.makeText(getApplicationContext(), getString(R.string.email_unavailable), Toast.LENGTH_SHORT);
                        unavailable.show();
                        break;
                    case "invalid  params":
                        Toast params = Toast.makeText(getBaseContext(), getString(R.string.invalid_params), Toast.LENGTH_SHORT);
                        params.show();
                        break;
                }
            }

            @Override
            public void onResponseJsonObject(JSONObject response) {

            }

            @Override
            public void onResponseJsonArray(JSONArray response) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            toolbarFunctions.goLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}
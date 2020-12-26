package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity {

    EditText email, username, pass, confirmPass;
    Button sendButton;
    InputValidator inputValidator;
    Toolbar toolbar;
    List<EditText> inputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.sign_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputValidator = new InputValidator();

        email = (EditText) findViewById(R.id.singup_email);
        username = (EditText) findViewById(R.id.singup_username);
        pass = (EditText) findViewById(R.id.singup_pass);
        confirmPass = (EditText) findViewById(R.id.singup_pass_confirm);

        inputs.add(email);
        inputs.add(username);
        inputs.add(pass);
        inputs.add(confirmPass);

        sendButton = (Button) findViewById(R.id.singup_send);

        email.addTextChangedListener(new ValidatorOnTextChange(email, InputValidator.email, getString(R.string.invalid_email)) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        username.addTextChangedListener(new ValidatorOnTextChange(username, InputValidator.name, getString(R.string.name_info)) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        pass.addTextChangedListener(new ValidatorOnTextChange(pass, InputValidator.password, getString(R.string.pass_info)) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        confirmPass.addTextChangedListener(new ValidatorOnTextChange(confirmPass, "^"+pass.getText().toString()+"$", getString(R.string.pass_not_match)) {
            @Override
            public void validator() {
                super.validator();
            }
        });

    }

    public void validForm(View v){
        if(inputValidator.validForm(inputs)){
            //TODO: Subir datos
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ToolbarFunctions toolbarFunctions = new ToolbarFunctions(this);
        if(item.getItemId() == android.R.id.home){
            toolbarFunctions.goLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}
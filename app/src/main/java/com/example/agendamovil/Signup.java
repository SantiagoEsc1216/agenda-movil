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

public class Signup extends AppCompatActivity {

    EditText email, username, pass, confirmPass;
    Button sendButton;
    InputValidator inputValidator;
    Toolbar toolbar;

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

        sendButton = (Button) findViewById(R.id.singup_send);

        email.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validEmail(email.getText())){
                    email.setError(null);
                    sendButton.setEnabled(true);
                }else{
                    email.setError(getString(R.string.invalid_email));
                }
            }
        });

        username.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validName(username.getText())){
                    username.setError(null);
                    sendButton.setEnabled(true);
                }else{
                    username.setError(getString(R.string.name_info));
                }
            }
        });

        pass.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validPass(pass.getText())){
                    pass.setError(null);
                    sendButton.setEnabled(true);
                }else{
                    pass.setError(getString(R.string.pass_info));
                }
            }
        });

        confirmPass.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.confirmPass(pass.getText(), confirmPass.getText())){
                    confirmPass.setError(null);
                    sendButton.setEnabled(true);
                }else{
                    confirmPass.setError(getString(R.string.pass_not_match));
                }
            }
        });

    }

    public void validForm(View v){
        if(username.getText().toString().matches("") || email.getText().toString().matches("") || pass.getText().toString().matches("")
        || confirmPass.getText().toString().matches("")){
            sendButton.setEnabled(false);
        }else{
            if(username.getError()==null && email.getError()==null && pass.getError()==null && confirmPass.getError()==null){
                //TODO: Enviar datos
            }else{
                sendButton.setEnabled(false);
            }
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
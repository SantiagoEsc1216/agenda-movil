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
import com.example.agendamovil.validators.InputValidator_2;
import com.example.agendamovil.validators.ValidatorOnTextChange;

public class  Singup extends AppCompatActivity {

    EditText email, username, pass, confirmPass;
    Button sendButton;
    InputValidator_2 inputValidator;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputValidator = new InputValidator_2();

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
                    email.setError("E-Mail invalido");
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
                    username.setError("Nombre invalido");
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
                    pass.setError("Contraseña invalida");
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
                    confirmPass.setError("Las contraseñas no coinciden");
                }
            }
        });

    }

    public void validForm(View v){

        boolean validName = inputValidator.validName(username.getText());
        boolean validEmail = inputValidator.validEmail(email.getText());
        boolean validPass = inputValidator.validPass(pass.getText());
        boolean samePass = inputValidator.confirmPass(pass.getText(),confirmPass.getText());

        if (validName == false){
            username.setError("Nombre no valido");
        }
        if (validEmail == false){
            email.setError("Email invalido");
        }
        if (validPass == false){
            pass.setError("Contraseña invalida");
        }
        if (samePass == false){
            confirmPass.setError("Las contraseñas no coinciden");
        }

        if(validName && validEmail && validPass && samePass){
            sendButton.setEnabled(true);
        }else{
            sendButton.setEnabled(false);
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
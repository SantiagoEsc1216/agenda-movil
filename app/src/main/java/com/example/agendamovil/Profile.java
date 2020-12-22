package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

public class Profile extends AppCompatActivity {

    //TODO: Validar que los formularios no esten vacios

    LinearLayout name_form, email_form, pass_form;
    Button send_name, send_email, send_pass;
    EditText new_name, new_email, new_pass, pass_confirm;
    ValidatorOnTextChange name_validator, email_validator, pass_validator, confirm_pass;
    InputValidator inputValidator;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.profile_tittle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.baseline_home_white_18dp));


        name_form = (LinearLayout) findViewById(R.id.name_form);
        email_form = (LinearLayout) findViewById(R.id.email_form);
        pass_form = (LinearLayout) findViewById(R.id.pass_form);

        send_name = (Button) findViewById(R.id.change_name);
        send_email = (Button)findViewById(R.id.change_email);
        send_pass = (Button)findViewById(R.id.change_pass);

        new_name = (EditText)findViewById(R.id.new_name);
        new_email = (EditText)findViewById(R.id.new_mail);
        new_pass = (EditText)findViewById(R.id.new_pass);
        pass_confirm = (EditText)findViewById(R.id.pass_confirm);

        inputValidator = new InputValidator();


    }
    private  void visibleForm(LinearLayout visible_form){
        name_form.setVisibility(View.GONE);
        email_form.setVisibility(View.GONE);
        pass_form.setVisibility(View.GONE);
        if(visible_form != null){
            visible_form.setVisibility(View.VISIBLE);
        }
    }
    public void visibleName(View v){
        new_name.addTextChangedListener(name_validator =  new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validName(new_name.getText())){
                    new_name.setError(null);
                    send_name.setEnabled(true);
                }else{
                    new_name.setError(getString(R.string.name_info));
                }
            }
        });
        visibleForm(name_form);
    }
    public void visibleEmail(View v){
        new_email.addTextChangedListener(email_validator = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if (inputValidator.validEmail(new_email.getText())){
                    new_email.setError(null);
                    send_email.setEnabled(true);
                }else{
                    new_email.setError(getString(R.string.invalid_email));
                }
            }
        } );
        visibleForm(email_form);
    }
    public void visiblePass(View v){
        new_pass.addTextChangedListener(pass_validator = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validPass(new_pass.getText())){
                    new_pass.setError(null);
                    send_pass.setEnabled(true);
                }else{
                    new_pass.setError(getString(R.string.pass_info));
                }
            }
        });

        pass_confirm.addTextChangedListener(confirm_pass = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.confirmPass(new_pass.getText(), pass_confirm.getText())){
                    pass_confirm.setError(null);
                    send_pass.setEnabled(true);
                }else{
                    pass_confirm.setError(getString(R.string.pass_not_match));
                }
            }
        });
        visibleForm(pass_form);
    }

    public void cancelForm(View v){
        new_name.removeTextChangedListener(name_validator);
        new_email.removeTextChangedListener(email_validator);
        new_pass.removeTextChangedListener(pass_validator);
        pass_confirm.removeTextChangedListener(confirm_pass);
        visibleForm(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ToolbarFunctions toolbarFunctions = new ToolbarFunctions(this);
        switch (item.getItemId()){
            case R.id.close_session:
                toolbarFunctions.closeSession();
                return true;
            case android.R.id.home:
                toolbarFunctions.backAgenda();
                return true;
            case R.id.profile_options:
                toolbarFunctions.openProfile();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
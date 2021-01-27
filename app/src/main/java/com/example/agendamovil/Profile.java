package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.io.Console;
import java.security.CodeSigner;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class Profile extends AppCompatActivity {

    //TODO: Validar que los formularios no esten vacios

    FrameLayout body;
    LinearLayout name_layout, email_layout, pass_layout;
    Button name_button, email_button, psss_button;
    Toolbar toolbar;
    FormProfile name_form, email_form, pass_form;
    TextView username, email;
    SharedPreferences preferences;
    String session_username, session_email;
    ProgressBarAgenda progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        body = findViewById(R.id.profile_body);

        if(BackendConnexion.isLogged(this) == false){
            startActivity(BackendConnexion.go_login);
        }

        preferences = this.getSharedPreferences("com.example.agendamovil",MODE_PRIVATE);
        session_username =  preferences.getString("username", "error");
        session_email =  preferences.getString("email", "error");

        progressBar = new ProgressBarAgenda(this);
        body.addView(progressBar);
        progressBar.bringToFront();

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.profile_tittle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.baseline_home_white_18dp));

        name_button = findViewById(R.id.profile_btn_name);
        email_button = findViewById(R.id.profile_btn_email);
        psss_button = findViewById(R.id.profile_btn_pass);

        name_layout =  findViewById(R.id.layout_name_change);
        email_layout = findViewById(R.id.layout_email_change);
        pass_layout = findViewById(R.id.layout_pass_change);

        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);

        username.setText(getString(R.string.username)+": "+ session_username);
        email.setText(getString(R.string.prompt_email)+": "+ session_email);

        name_form = new FormProfile(this, FormProfile.CHANGE_NAME, username, progressBar);
        email_form = new FormProfile(this, FormProfile.CHANGE_EMAIL, email, progressBar);
        pass_form = new FormProfile(this, FormProfile.CHANGE_PASS, progressBar);

        name_button.setOnClickListener(v -> {
            try {
                name_layout.addView(name_form);
                name_form.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                name_form.setVisibility(View.VISIBLE);
            }
        });

        email_button.setOnClickListener(v -> {
            try {
                email_layout.addView(email_form);
                email_form.setVisibility(View.VISIBLE);

            }catch (Exception e){
                email_form.setVisibility(View.VISIBLE);
            }
        });

        psss_button.setOnClickListener(v -> {
            try {
                pass_layout.addView(pass_form);
                pass_form.setVisibility(View.VISIBLE);
            }catch (Exception e){
                pass_form.setVisibility(View.VISIBLE);
            }
        });

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
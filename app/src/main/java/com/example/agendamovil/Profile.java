package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

public class Profile extends AppCompatActivity {

    //TODO: Validar que los formularios no esten vacios

    LinearLayout name_layout, email_layout, pass_layout, form_layout;
    Button send_button, cancel_button;
    EditText change_input, pass, pass_confirm;
    TextView tv_change_input, tv_pass_confirm;
    ValidatorOnTextChange change_input_listener;
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

        tv_change_input = (TextView) findViewById(R.id.profile_label_1);
        tv_pass_confirm = (TextView)findViewById(R.id.profile_label_2);

        name_layout = (LinearLayout) findViewById(R.id.layout_name_change);
        email_layout = (LinearLayout) findViewById(R.id.layout_email_change);
        pass_layout = (LinearLayout) findViewById(R.id.layout_pass_change);
        form_layout = (LinearLayout)findViewById(R.id.layout_profile_form);

        send_button = (Button)findViewById(R.id.button_profile_upload);
        cancel_button = (Button)findViewById(R.id.button_profile_cancel);

        change_input = (EditText)findViewById(R.id.input_profile_change);
        pass_confirm = (EditText)findViewById(R.id.input_profile_confirmpass);
        pass = (EditText)findViewById(R.id.input_profile_pass);

        inputValidator = new InputValidator();

    }




    public void cancelForm(View v){
       change_input.removeTextChangedListener(change_input_listener);
        tv_pass_confirm.setVisibility(View.GONE);
        pass_confirm.setVisibility(View.GONE);
        change_input.setText("");
        pass.setText("");
        pass_confirm.setText("");
        form_layout.setVisibility(View.GONE);
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
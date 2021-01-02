package com.example.agendamovil;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.util.ArrayList;
import java.util.List;

public class FormProfile extends LinearLayout {
    final static String CHANGE_NAME = "NAME";
    final static String CHANGE_EMAIL = "EMAIL";
    final static String CHANGE_PASS = "PASSWORD";

    String type;
    Context context;
    TextView label_change, label_confirm_pass;
    EditText input_change, input_confirm_pass, input_pass;
    Button btn_cancel, btn_accept;
    InputValidator inputValidator = new InputValidator();
    List<EditText> inputs = new ArrayList<>();

    public FormProfile(Context context, String type) {
        super(context);
        this.type = type;
        this.context = context;
        init(context);
    }

    private void init(Context context){

        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.form_profile, this, true);

        label_change = findViewById(R.id.profile_label_1);
        label_confirm_pass = findViewById(R.id.profile_label_2);
        input_change = findViewById(R.id.input_profile_change);
        input_confirm_pass = findViewById(R.id.input_profile_confirmpass);
        input_pass = findViewById(R.id.input_profile_pass);

        btn_cancel = findViewById(R.id.button_profile_cancel);
        btn_accept = findViewById(R.id.button_profile_upload);

        inputs.add(input_change);
        inputs.add(input_pass);

        input_pass.addTextChangedListener(new ValidatorOnTextChange(context ,input_pass, InputValidator.password){
            @Override
            public void validator() {
                super.validator();
            }
        });
        switch (type){
            case CHANGE_NAME:
                formName();
                break;
            case CHANGE_EMAIL:
                formEmail();
                break;
            case CHANGE_PASS:
                formPass();
                break;
        }

      btn_accept.setOnClickListener( v-> validForm());
        btn_cancel.setOnClickListener(v -> cancelForm());

    }

    private void formName (){
        label_change.setText(context.getText(R.string.new_name));
        input_change.addTextChangedListener(new ValidatorOnTextChange(context ,input_change, InputValidator.name) {
            @Override
            public void validator() {
                super.validator();
            }
        });

    }

    private void formEmail(){
        label_change.setText(context.getText(R.string.new_email));
        input_change.addTextChangedListener(new ValidatorOnTextChange(context ,input_change, InputValidator.email) {
            @Override
            public void validator() {
                super.validator();
            }
        });
    }

    private void formPass(){
        label_change.setText(context.getString(R.string.new_pass));
        input_change.setTransformationMethod(PasswordTransformationMethod.getInstance());
        label_confirm_pass.setVisibility(VISIBLE);
        input_confirm_pass.setVisibility(VISIBLE);

        input_change.addTextChangedListener(new ValidatorOnTextChange(context, input_change, InputValidator.password) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        input_change.addTextChangedListener(new ValidatorOnTextChange(context, input_change, input_confirm_pass, InputValidator.pass_confirm) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        input_confirm_pass.addTextChangedListener(new ValidatorOnTextChange(context ,input_change, input_confirm_pass, InputValidator.pass_confirm) {
            @Override
            public void validator() {
                super.validator();
            }
        });

        inputs.add(input_confirm_pass);

    }

    private void cancelForm(){
        this.setVisibility(GONE);
    }
    private  void validForm(){
        if(inputValidator.validForm(inputs)){
            //TODO: Upload info
        }else{

        }
    }
}

package com.example.agendamovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class FormProfile extends LinearLayout {
    final static String CHANGE_NAME = "NAME";
    final static String CHANGE_EMAIL = "EMAIL";
    final static String CHANGE_PASS = "PASSWORD";


    String type;
    Context context;
    TextView label_change, label_confirm_pass, textChange;
    EditText input_change, input_confirm_pass, input_pass;
    Button btn_cancel, btn_accept;
    InputValidator inputValidator = new InputValidator();
    List<EditText> inputs = new ArrayList<>();
    Map<String, String> params = new HashMap<>();
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    ProgressBarAgenda progressBar;

    public FormProfile(Context context, String type, ProgressBarAgenda progressBar) {
        super(context);
        this.type = type;
        this.context = context;
        this.progressBar = progressBar;
        init(context);
    }
    public FormProfile(Context context, String type, TextView text_change, ProgressBarAgenda progressBar) {
        super(context);
        this.type = type;
        this.context = context;
        this.textChange = text_change;
        this.progressBar = progressBar;
        init(context);
    }

    private void init(Context context){

        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.form_profile, this, true);

        userSession = context.getSharedPreferences("com.example.agendamovil", Context.MODE_PRIVATE);
        editor = userSession.edit();

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

        btn_cancel.setOnClickListener(v -> cancelForm());
        btn_accept.setOnClickListener(v ->{
            if(inputValidator.validForm(inputs)){
                switch (type){
                    case CHANGE_NAME:
                        params.put("new_name", input_change.getText().toString());
                        break;
                    case CHANGE_EMAIL:
                        params.put("new_email", input_change.getText().toString());
                        break;
                    case CHANGE_PASS:
                        params.put("new_pass", input_change.getText().toString());
                        break;
                }
                params.put("name_session", userSession.getString("username",""));
                params.put("email_session", userSession.getString("email", ""));
                params.put("password", input_pass.getText().toString());
                upload_info();
            }
        });

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
        for (EditText input: inputs) {
            input.setText("");
            input.setError(null);
        }
    }

    public void upload_info(){
        BackendConnexion backendConnexion = new BackendConnexion(context, BackendConnexion.PROFILE, progressBar);
        backendConnexion.stringRequest(params, new VolleyCallback() {
            @Override
            public void onResponseString(String response) {
                switch (response.trim()){
                    case "OK_name":
                        Toast name_change = Toast.makeText(context, context.getString(R.string.ok_name), Toast.LENGTH_SHORT);
                        editor.putString("username", input_change.getText().toString());
                        editor.apply();
                        textChange.setText(context.getString(R.string.username)+": "+userSession.getString("username", "error"));
                        name_change.show();
                        cancelForm();
                        break;

                    case "OK_email":
                        Toast email_change = Toast.makeText(context, context.getString(R.string.ok_email), Toast.LENGTH_SHORT);
                        editor.putString("email", input_change.getText().toString());
                        editor.apply();
                        textChange.setText(context.getString(R.string.prompt_email)+": "+userSession.getString("email", "error"));
                        email_change.show();
                        cancelForm();
                        break;

                    case "OK_password":
                        Toast password_ok = Toast.makeText(getContext(),context.getString(R.string.ok_pass), Toast.LENGTH_SHORT);
                        password_ok.show();
                        cancelForm();
                        break;
                    case "error":
                        Toast toast = Toast.makeText(getContext(),context.getString(R.string.error), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case "email unavailable":
                        Toast toast1 = Toast.makeText(getContext(), context.getString(R.string.email_unavailable), Toast.LENGTH_SHORT);
                        toast1.show();
                        break;
                    case "incorrect password":
                    case "invalid params":
                        Toast toast2 = Toast.makeText(getContext(), getContext().getString(R.string.incorrect_pass), Toast.LENGTH_SHORT);
                        Log.e("params", params.toString());
                        toast2.show();
                        break;

                    default:
                        Toast toast3 = Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT);
                        toast3.show();
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

}

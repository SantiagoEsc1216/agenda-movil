package com.example.agendamovil.validators;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.agendamovil.R;

public abstract class ValidatorOnTextChange implements TextWatcher {

    Context context;
    EditText input, input_original, input_confirm;
    String type,  error;

    public ValidatorOnTextChange(Context context, EditText input, String type){
        this.input = input;
        this.type = type;
        this.context = context;
        switch (type){
            case InputValidator.name:
                this.error = context.getString(R.string.name_info);
                break;
            case InputValidator.email:
                this.error = context.getString(R.string.invalid_email);
                break;
            case InputValidator.phone:
                this.error = context.getString(R.string.phone_info);
                break;
            case InputValidator.password:
                this.error = context.getString(R.string.pass_info);
                break;
        }
    }


    public ValidatorOnTextChange(Context context, EditText input_original, EditText input_confirm , String type){
        this.input_original = input_original;
        this.input_confirm = input_confirm;
        this.type = type;
        this.context = context;
        this.error = context.getString(R.string.pass_not_match);
    }

    InputValidator inputValidator = new InputValidator();


    public void validator(){
        try{
            if (type == InputValidator.pass_confirm){
                String regex = "^"+input_confirm.getText()+"$";
                if (inputValidator.validInput(input_original.getText(), regex)){
                    input_confirm.setError(null);
                }else{
                    input_confirm.setError(error);
                }
            }else{
                if (inputValidator.validInput(input.getText(), type)){
                    input.setError(null);
                }else{
                    input.setError(error);
                }
            }
        } catch (Exception e) {
            try {
                input_confirm.setError(error);
                input.setError(error);
            } catch (Exception exception) {
        
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        validator();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        validator();
    }
}

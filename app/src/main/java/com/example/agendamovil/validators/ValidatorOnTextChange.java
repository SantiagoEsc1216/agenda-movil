package com.example.agendamovil.validators;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.agendamovil.R;

public abstract class ValidatorOnTextChange implements TextWatcher {

    EditText input;
    String type,  error;

    public ValidatorOnTextChange(EditText input, String type, String error){
        this.input = input;
        this.type = type;
        this.error = error;
    }

    InputValidator inputValidator = new InputValidator();


    public void validator(){
        if (inputValidator.validInput(input.getText(), type)){
            input.setError(null);
        }else{
            input.setError(error);
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

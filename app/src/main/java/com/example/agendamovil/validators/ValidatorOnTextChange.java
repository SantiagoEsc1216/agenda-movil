package com.example.agendamovil.validators;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class ValidatorOnTextChange implements TextWatcher {

    public abstract void validator();

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

package com.example.agendamovil;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FormProfile extends LinearLayout {
    public FormProfile(Context context) {
        super(context);
    }

    private void init(Context context){

        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.form_profile, this, true);
    }
}

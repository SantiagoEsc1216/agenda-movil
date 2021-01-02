package com.example.agendamovil.validators;

import android.text.Editable;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputValidator {

    public static final String name = "^[a-zA-Zá-ýÁ-Ý0-9\\u00f1\\u00d1\\(\\)+ ]+$";
    public static final String email = "^([a-zA-Z-0-9_\\.-]+)@([a-z_\\.-]+)\\.([a-z\\.]{2,6})$";
    public static final String phone = "^[0-9\\+\\- ]{10,18}$";
    public static final String password = "^([a-zA-Zá-ýÁ-Ý0-9\\u00f1\\u00d1]{6,})+$";
    public static final String pass_confirm  = "";


    public boolean validInput(Editable input, String type){
        Pattern pattern = Pattern.compile(type);
        boolean match = pattern.matcher(input).find();

        if (match){
            return true;
        }else{
            return false;
        }
    }


    public boolean validForm(List<EditText> inputs){
        int errors = 0;
        for (EditText inp: inputs){
            if(inp.getText().toString().matches("")){
               errors++;
            }else{
                if(inp.getError() == null){

                }else{
                    errors++;
                }
            }
        }
        if(errors>0){
            return false;
        }else{
            return true;
        }
    }

}

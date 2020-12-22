package com.example.agendamovil.validators;

import android.text.Editable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: 18/12/2020 renombrar a inputValidator 
// TODO: 18/12/2020 Mensajes de error a recursos string

public class InputValidator {

    public static final String regexName = "^[a-zA-Zá-ýÁ-Ý0-9\\u00f1\\u00d1\\(\\)+ ]+$";
    public static final String regexEmail = "^([a-zA-Z-0-9_\\.-]+)@([a-z_\\.-]+)\\.([a-z\\.]{2,6})$";
    public static final String regexPhone = "^[0-9\\+\\- ]{10,18}$";
    public static final String regexPass = "^([a-zA-Zá-ýÁ-Ý0-9\\u00f1\\u00d1]{6,})+$";


    public boolean validName(Editable name){
        Pattern pattern = Pattern.compile(regexName);
        boolean match = pattern.matcher(name).find();

        if (match){
            return true;
        }else{
            return false;
        }
    }

    public boolean validEmail(Editable email){
        Pattern pattern = Pattern.compile(regexEmail);
        boolean match = pattern.matcher(email).find();

        if(match){
            return true;
        }else{
            return false;
        }
    }

    public boolean validPhone(Editable phone){
        Pattern pattern = Pattern.compile(regexPhone);
        boolean match = pattern.matcher(phone).find();

        if (match){
            return true;
        }else{
            return false;
        }
    }

    public boolean validPass(Editable password){
        Pattern pattern = Pattern.compile(regexPass);
        boolean match = pattern.matcher(password).find();

        if (match){
            return true;
        }else{
            return false;
        }
    }

    public boolean confirmPass(Editable password, Editable passwordConfirm){
        Pattern pattern = Pattern.compile("^"+password.toString()+"$");
        boolean match = pattern.matcher(passwordConfirm).find();

        if (match){
            return true;
        }else{
            return false;
        }
    }

}

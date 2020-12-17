package com.example.agendamovil.toolbar;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.agendamovil.Agenda;
import com.example.agendamovil.CardContact;
import com.example.agendamovil.Profile;
import com.example.agendamovil.ui.login.LoginActivity;

import java.util.List;
import java.util.regex.Pattern;

public class ToolbarFunctions {

    Context context;

    public ToolbarFunctions(Context context){
        this.context = context;
    }

    public void backAgenda(){
        Intent home = new Intent(this.context, Agenda.class);
        context.startActivity(home);
    }

    public void openProfile(){
        Intent profile = new Intent(this.context, Profile.class);
        this.context.startActivity(profile);
    }

    public void searchContact(List<CardContact> contacts, String inputSearch){
        Pattern pattern = Pattern.compile(inputSearch);
        for(int i=0; i<contacts.size(); i++){
            String name, email, phone;
            boolean matchName, matchEmail, matchPhone;

            name = contacts.get(i).name_contact;
            email = contacts.get(i).email_contact;
            phone = contacts.get(i).phone_contact;

            matchName = pattern.matcher(name).find();
            matchEmail = pattern.matcher(email).find();
            matchPhone = pattern.matcher(phone).find();

            if (matchName || matchEmail || matchPhone){
                contacts.get(i).setVisibility(View.VISIBLE);
            }else{
                contacts.get(i).setVisibility(View.GONE);
            }

        }
    }

    public void closeSession(){

    }

    public void goLogin(){
        Intent login = new Intent(this.context, LoginActivity.class);
        this.context.startActivity(login);
    }
}

package com.example.agendamovil.toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.example.agendamovil.Agenda;
import com.example.agendamovil.CardContact;
import com.example.agendamovil.Profile;
import com.example.agendamovil.LoginActivity;

import java.util.List;
import java.util.regex.Pattern;

public class ToolbarFunctions {

    Context context;
    SharedPreferences session;

    public ToolbarFunctions(Context context){
        this.context = context;
        session = context.getSharedPreferences("com.example.agendamovil", Context.MODE_PRIVATE);
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

            matchName = pattern.matcher("^"+name).find();
            matchEmail = pattern.matcher("^"+email).find();
            matchPhone = pattern.matcher("^"+phone).find();

            if (matchName || matchEmail || matchPhone){
                contacts.get(i).setVisibility(View.VISIBLE);
            }else{
                contacts.get(i).setVisibility(View.GONE);
            }

        }
    }

    public void closeSession(){
        Intent login = new Intent(context, LoginActivity.class);
        SharedPreferences.Editor editor = session.edit();
        editor.remove("username");
        editor.remove("email");
        editor.remove("logged");
        editor.apply();
        if(!session.getBoolean("logged", false)){
            context.startActivity(login);
        }

    }

    public void goLogin(){
        Intent login = new Intent(this.context, LoginActivity.class);
        this.context.startActivity(login);
    }
}

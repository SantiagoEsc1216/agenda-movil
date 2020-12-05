package com.example.agendamovil.toolbar;

import android.content.Context;
import android.content.Intent;

import com.example.agendamovil.Agenda;
import com.example.agendamovil.Profile;
import com.example.agendamovil.ui.login.LoginActivity;

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

    public void searchContact(){

    }

    public void closeSession(){

    }

    public void goLogin(){
        Intent login = new Intent(this.context, LoginActivity.class);
        this.context.startActivity(login);
    }
}

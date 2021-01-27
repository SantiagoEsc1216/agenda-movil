package com.example.agendamovil.session;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agendamovil.Agenda;
import com.example.agendamovil.CustomRequest;
import com.example.agendamovil.LoginActivity;
import com.example.agendamovil.ProgressBarAgenda;
import com.example.agendamovil.R;
import com.example.agendamovil.validators.InputValidator;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BackendConnexion {
    public static final String LOGIN = "login_api.php";
    public static final String SIGNUP = "signup_api.php";
    public static final String GET_CONTACTS = "get_contacts_api.php?";
    public static final String PROFILE = "profile_api.php";
    public static final String CREATE_CONTACT = "create_contact_api.php";
    public static final String EDIT_CONTACT = "edit_contact_api.php";

    private static final String Server = "http://192.168.1.67/agenda/web_services/";

    public static Toast error_server, invalid_params, incorrect_password;

    public static Intent start_agenda, go_login;

    String type;
    Context context;
    ProgressBarAgenda progressBar;
    RequestQueue requestQueue;
    private String URL;


    public BackendConnexion(Context context ,String type, ProgressBarAgenda progressBar){
        this.type = type;
        this.context = context;
        this.progressBar = progressBar;


        requestQueue = Volley.newRequestQueue(context);

        switch (type){
            case LOGIN:
                URL = Server+LOGIN;
                break;
            case SIGNUP:
                URL = Server+SIGNUP;
                break;
            case GET_CONTACTS:
                URL = Server+GET_CONTACTS;
                break;
            case PROFILE:
                URL = Server+PROFILE;
                break;
            case CREATE_CONTACT:
                URL = Server+CREATE_CONTACT;
                break;
            case EDIT_CONTACT:
                URL = Server+EDIT_CONTACT;
        }

        error_server = Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT);
        invalid_params = Toast.makeText(context, context.getString(R.string.invalid_params), Toast.LENGTH_SHORT);
        incorrect_password = Toast.makeText(context, context.getString(R.string.incorrect_pass), Toast.LENGTH_SHORT);

    }

    public void stringRequest(Map<String, String> params, VolleyCallback callback){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL , response -> {
            callback.onResponseString(response);
            progressBar.hide();

        }, error->{
            callback.errorHandler( context ,error);
           progressBar.hide();

        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };


        progressBar.show();
        requestQueue.add(stringRequest);

    }

    public void jsonRequest(Map<String,String> params, VolleyCallback callback){

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL,null , response->{
            callback.onResponseJsonObject(response);
            progressBar.hide();

        }, error -> {
            callback.errorHandler(context,  error);
            progressBar.hide();

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        progressBar.show();
        requestQueue.add(customRequest);

    }

    public void getRequest(String session_email, VolleyCallback callback){
        String url_get = this.URL+"session_email="+session_email;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_get, null, response -> {
            callback.onResponseJsonArray(response);
            progressBar.hide();

        }, error -> {
            callback.errorHandler(context, error);
            progressBar.hide();
        });

        progressBar.show();
        requestQueue.add(jsonArrayRequest);
    }

    public static boolean isLogged (Context context){
        start_agenda = new Intent(context, Agenda.class);
        go_login = new Intent(context, LoginActivity.class);
       SharedPreferences session = context.getSharedPreferences("com.example.agendamovil", MODE_PRIVATE);

        if(session.getBoolean("logged", false)){
            return true;
        }else{
            return false;
        }
    }

}

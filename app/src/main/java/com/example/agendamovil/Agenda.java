package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.session.VolleyCallback;
import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// TODO: progessbar, icono y nombre de app, solicitar permiso de almacenamiento y cambiar a pestaña en inicio de sesion en todas las actividades
public class Agenda extends AppCompatActivity {
    LinearLayout scrollContacts;
    ProgressBarAgenda progressBar;
    FloatingActionButton addNewContact;
    TextView messageEmpty;
    List<CardContact> cardContactList = new ArrayList<CardContact>();
    Toolbar toolbar;
    ToolbarFunctions toolbarFunctions;
    SharedPreferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(BackendConnexion.isLogged(this) == false){
            startActivity(BackendConnexion.go_login);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        session = getSharedPreferences("com.example.agendamovil", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar_custom);
        toolbarFunctions = new ToolbarFunctions(this);
        toolbar.setTitle(R.string.agenda);
        setSupportActionBar(toolbar);
        progressBar = new ProgressBarAgenda(this);
        scrollContacts = findViewById(R.id.scroll_contacts);
        scrollContacts.addView(progressBar);
        addNewContact = findViewById(R.id.create_contact);
        messageEmpty = findViewById(R.id.agenda_empty);
        getContacts();

    }

    private void createCardContacts(JSONArray response){

        try {
            for (int i = 0; i< response.length(); i++){
                int finalI = i;
                CardContact this_card;
                JSONObject contact = response.getJSONObject(i);
                int id = contact.getInt("ID_Contact");
                String name = contact.getString("Name_Contact");
                String phone = contact.getString("Phone_Contact");
                String email = contact.getString("Mail_Contact");
                String base64Img = contact.getString("base64_img");

                this_card = new CardContact(this, id,name, phone, base64Img, email);
                cardContactList.add( i , this_card);
                this_card = cardContactList.get(i);

                CardContact finalThis_card = this_card;
                this_card.btn_img.setOnClickListener(v -> {
                    startActivityForResult(finalThis_card.PickPhoto(), finalI);
                });
                this_card.btn_edit.setOnClickListener(v -> finalThis_card.editContact(cardContactList));
                this_card.btn_cancel.setOnClickListener(v -> finalThis_card.cancel_edit(cardContactList));

                scrollContacts.addView(this_card);

            }
        }catch (JSONException e){
            Log.e("error_contact",e.toString() );
        }

    }

    private void getContacts(){
        BackendConnexion connexion = new BackendConnexion(this, BackendConnexion.GET_CONTACTS, progressBar);
        Map<String, String> params = new HashMap<>();

        connexion.getRequest(session.getString("email", ""), new VolleyCallback() {

            @Override
            public void onResponseString(String response) {

            }

            @Override
            public void onResponseJsonObject(JSONObject response) {

            }

            @Override
            public void onResponseJsonArray(JSONArray response) {

                if(response.length() > 0){
                    createCardContacts(response);
                }else{
                    messageEmpty.setVisibility(View.VISIBLE);
                }
            }


        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            cardContactList.get(requestCode).setImage(data);
        }
    }

    public void addNewContact(View v){
        Intent newContact = new Intent(this, new_contact.class);
        startActivity(newContact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                toolbarFunctions.searchContact(cardContactList,newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.close_session:
                toolbarFunctions.closeSession();
                return true;
            case android.R.id.home:
                toolbarFunctions.backAgenda();
                return true;
            case R.id.profile_options:
                toolbarFunctions.openProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
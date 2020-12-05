package com.example.agendamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class Agenda extends AppCompatActivity {
    LinearLayout scrollContacts;
    FloatingActionButton addNewContact;
    List<CardContact> cardContactList = new ArrayList<CardContact>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        setSupportActionBar(toolbar);

        scrollContacts = (LinearLayout) findViewById(R.id.scroll_contacts);
        addNewContact = (FloatingActionButton)findViewById(R.id.create_contact);
        createCardContacts();
    }

    private void createCardContacts(){
        for (int i = 0; i < 3; i++){
            int finalI = i;
            CardContact this_card;

            cardContactList.add( i ,new CardContact(this, "contact" + i, "444433442" + i, "default.jpeg", "email_" + i + "@mail.com"));
            this_card = cardContactList.get(i);

           this_card.btn_img.setOnClickListener(v -> {
               startActivityForResult(this_card.PickPhoto(), finalI);
           });
           this_card.btn_edit.setOnClickListener(v -> this_card.editContact(cardContactList));
           this_card.btn_cancel.setOnClickListener(v -> this_card.cancel_edit(cardContactList));

            scrollContacts.addView(this_card);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = cardContactList.get(requestCode).img_view;
        if (resultCode == RESULT_OK) {
            cardContactList.get(requestCode).setImage(data, imageView, this);
        }
    }

    public void addNewContact(View v){
        Intent newContact = new Intent(this, new_contact.class);
        startActivity(newContact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ToolbarFunctions toolbarFunctions = new ToolbarFunctions(this);
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
            case R.id.search:
                toolbarFunctions.searchContact();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
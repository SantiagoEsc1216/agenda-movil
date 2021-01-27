package com.example.agendamovil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.method.KeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.session.VolleyCallback;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardContact extends LinearLayout {
    private final int id;
    private  SharedPreferences session;
    public String name_contact, phone_contact, base64_img, name_img, email_contact;
    FrameLayout body;
    EditText name_card, email_card, phone_card;
    ImageView img_view;
    Button btn_edit, btn_cancel, btn_accept, btn_delete, btn_img;
    ValidatorOnTextChange name_validator, email_validator, phone_validator;
    InputValidator inputValidator;
    Context context;
    List<EditText> inputs = new ArrayList<>();
    Bitmap bitmapImage, newImage;
    Map<String, String> params = new HashMap<>();
    ProgressBarAgenda progressBar;

    public CardContact(Context context, int id ,String name_contact, String phone_contact, String base64_img, String email_contact) {
        super(context);
        this.id = id;
        this.name_contact = name_contact;
        this.phone_contact = phone_contact;
        this.base64_img = base64_img;
        this.name_img = base64_img;
        this.email_contact = email_contact;
        this.context = context;
        this.session = context.getSharedPreferences("com.example.agendamovil", Context.MODE_PRIVATE);
        init(context);
        card_values();
    }

    private void init(Context context){
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.card_contact, this, true);

        body = findViewById(R.id.layout_card);

        inputValidator = new InputValidator();
        bitmapImage = decodeStringImage(base64_img);
        progressBar = new ProgressBarAgenda(context);
        body.addView(progressBar);
        progressBar.bringToFront();

        name_card = (EditText)findViewById(R.id.name);
        email_card = (EditText)findViewById(R.id.email);
        phone_card =(EditText)findViewById(R.id.phone);
        img_view = (ImageView)findViewById(R.id.img_view_card);
        img_view = findViewById(R.id.img_view_card);

        newImage = bitmapImage;

        inputs.add(name_card);
        inputs.add(email_card);
        inputs.add(phone_card);

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_accept = (Button)findViewById(R.id.btn_accept);
        btn_img = (Button)findViewById(R.id.btn_img);

        for (EditText input: inputs){
            input.setTag(input.getKeyListener());
            input.setKeyListener(null);
        }

        btn_cancel.setVisibility(View.GONE);
        btn_accept.setVisibility(View.GONE);
        btn_img.setVisibility(View.GONE);

        img_view.setImageBitmap(bitmapImage);

        name_card.addTextChangedListener(name_validator = new ValidatorOnTextChange(context, name_card, InputValidator.name){
            @Override
            public void validator() {
                super.validator();
            }
        });
        email_card.addTextChangedListener(email_validator = new ValidatorOnTextChange(context, email_card, InputValidator.email){
            @Override
            public void validator() {
                super.validator();
            }
        });

        phone_card.addTextChangedListener(phone_validator = new ValidatorOnTextChange(context, phone_card, InputValidator.phone) {
            @Override
            public void validator() {
                super.validator();
            }
        });


        btn_accept.setOnClickListener(v -> {
            if(inputValidator.validForm(inputs)){
             uploadEdit();
            }
        });

    btn_delete.setOnClickListener(v->{
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_delete).
                setPositiveButton(R.string.yes, (dialog, which) -> {
                    uploadDelete();
                }).
                setNegativeButton(R.string.no, (dialog, which) -> {

                });

        AlertDialog dialog = builder.create();
        dialog.show();

    });


    }

    private void uploadEdit() {
        BackendConnexion connexion = new BackendConnexion(context, BackendConnexion.EDIT_CONTACT, progressBar);
        String newName, newEmail, newPhone;
        newName = name_card.getText().toString();
        newPhone = phone_card.getText().toString();
        newEmail = email_card.getText().toString();

        params.put("btn_accept", "true");
        params.put("id_contact", String.valueOf(id));
        params.put("name_contact", newName);
        params.put("email_contact", newEmail );
        params.put("phone_contact", newPhone);
        params.put("email_user", session.getString("email", ""));
        connexion.stringRequest(params, new VolleyCallback() {
            @Override
            public void onResponseString(String response) {
               if(response.trim().equals("OK")){
                   name_contact = newName;
                   email_contact = newEmail;
                   phone_contact = newPhone;
                   bitmapImage = newImage;
                   Toast ok = Toast.makeText(context, context.getString(R.string.contact_edited), Toast.LENGTH_SHORT);
                   btn_cancel.performClick();
                   ok.show();
               }else{
                    BackendConnexion.error_server.show();
               }
            }
            @Override
            public void onResponseJsonObject(JSONObject response) {
            }
            @Override
            public void onResponseJsonArray(JSONArray response) {
            }
        });

    }

    private void uploadDelete() {
        BackendConnexion connexion = new BackendConnexion(context, BackendConnexion.EDIT_CONTACT, progressBar);
        params.put("btn_confirm_delete", "true");
        params.put("id_contact", String.valueOf(id));
        params.put("email_user", session.getString("email", ""));

        connexion.stringRequest(params, new VolleyCallback() {
            @Override
            public void onResponseString(String response) {
                Log.e("response_edit", response);
                switch (response.trim()){
                    case "OK":
                        deleteCard();
                        Toast ok = Toast.makeText(context, context.getString(R.string.contact_deleted), Toast.LENGTH_SHORT);
                        ok.show();
                        break;
                    case "error":
                       BackendConnexion.error_server.show();
                }
            }

            @Override
            public void onResponseJsonObject(JSONObject response) {

            }

            @Override
            public void onResponseJsonArray(JSONArray response) {

            }
        });
    }

    private Bitmap decodeStringImage(String img_contact) {
        byte[] imgByte = Base64.decode(img_contact, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        return bitmap;
    }


    public void editContact(List<CardContact> cardContactList){
        name_card.setKeyListener((KeyListener) name_card.getTag());
        email_card.setKeyListener((KeyListener) name_card.getTag());
        phone_card.setKeyListener((KeyListener)phone_card.getTag());


        btn_delete.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);

        btn_accept.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_img.setVisibility(View.VISIBLE);

        for (int i = 0; cardContactList.size() > i; i++){
            cardContactList.get(i).setAlpha((float) 0.5);
            cardContactList.get(i).btn_edit.setEnabled(false);
            cardContactList.get(i).btn_delete.setEnabled(false);
        }

        this.setAlpha((float) 1.0);
    }

    public void cancel_edit(List<CardContact> cardContactList){
        for(EditText input: inputs){
            input.setKeyListener(null);
            input.setError(null);

        }

        btn_cancel.setVisibility(View.GONE);
        btn_accept.setVisibility(View.GONE);
        btn_img.setVisibility(View.GONE);

        btn_edit.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.VISIBLE);

        img_view.setImageBitmap(bitmapImage);

        params.clear();

        for (int i = 0; cardContactList.size() > i; i++){
            cardContactList.get(i).setAlpha((float) 1.0);
            cardContactList.get(i).btn_edit.setEnabled(true);
            cardContactList.get(i).btn_delete.setEnabled(true);
        }

        card_values();
    }

    private void card_values(){
        name_card.setText(this.name_contact);
        phone_card.setText(this.phone_contact);
        email_card.setText(this.email_contact);
    }

    public Intent PickPhoto(){
        String[] img_types = {"image/jpeg", "image/png"};
        Intent pick_photo = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pick_photo.setType("image/jpeg");
        pick_photo.putExtra(Intent.EXTRA_MIME_TYPES, img_types);
        return pick_photo;
    }

    public void setImage (Intent data){
        try {
            final Uri imageUri = data.getData();
            final InputStream imgStream = context.getContentResolver().openInputStream(imageUri);
            newImage = BitmapFactory.decodeStream(imgStream);
            final String imgString =  new_contact.convertToString(newImage);
            params.put("img_contact", imgString);
            img_view.setImageBitmap(newImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteCard(){
        this.setVisibility(GONE);
    }

    }

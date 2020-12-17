package com.example.agendamovil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.agendamovil.validators.InputValidator_2;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class CardContact extends LinearLayout {
    public String name_contact, phone_contact, img_contact, email_contact;
    EditText name_card, email_card, phone_card;
    ImageView img_view;
    Button btn_edit, btn_cancel, btn_accept, btn_delete, btn_img;
    ValidatorOnTextChange name_validator, email_validator, phone_validator;
    InputValidator_2 inputValidator;

    public CardContact(Context context, String name_contact, String phone_contact, String img_contact, String email_contact) {
        super(context);
        init(context);
        this.name_contact = name_contact;
        this.phone_contact = phone_contact;
        this.img_contact = img_contact;
        this.email_contact = email_contact;
        card_values();
    }

    private void init(Context context){
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.card_contact, this, true);

        inputValidator = new InputValidator_2();

        name_card = (EditText)findViewById(R.id.name);
        email_card = (EditText)findViewById(R.id.email);
        phone_card =(EditText)findViewById(R.id.phone);
        img_view = (ImageView)findViewById(R.id.img_view);

        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_accept = (Button)findViewById(R.id.btn_accept);
        btn_img = (Button)findViewById(R.id.btn_img);

        name_card.setTag(name_card.getKeyListener());
        email_card.setTag(email_card.getKeyListener());
        phone_card.setTag(phone_card.getKeyListener());

        name_card.setKeyListener(null);
        email_card.setKeyListener(null);
        phone_card.setKeyListener(null);

        btn_cancel.setVisibility(View.GONE);
        btn_accept.setVisibility(View.GONE);
        btn_img.setVisibility(View.GONE);

        btn_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validFormContact();
            }
        });

    }


    public void editContact(List<CardContact> cardContactList){
        name_card.setKeyListener((KeyListener) name_card.getTag());
        email_card.setKeyListener((KeyListener) name_card.getTag());
        phone_card.setKeyListener((KeyListener)phone_card.getTag());

        name_card.addTextChangedListener(name_validator = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validName(name_card.getText())){
                    name_card.setError(null);
                    btn_accept.setEnabled(true);
                }else{
                    name_card.setError("Nombre invalido");
                }
            }
        });

        email_card.addTextChangedListener(email_validator = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validEmail(email_card.getText())){
                    email_card.setError(null);
                    btn_accept.setEnabled(true);
                }else{
                    email_card.setError("E-Mail invalido");
                }
            }
        });

        phone_card.addTextChangedListener(phone_validator = new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if (inputValidator.validPhone(phone_card.getText())){
                    phone_card.setError(null);
                    btn_accept.setEnabled(true);
                }else{
                    phone_card.setError("Telefono invalido");
                }
            }
        });

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
        name_card.setKeyListener(null);
        email_card.setKeyListener(null);
        phone_card.setKeyListener(null);

        name_card.removeTextChangedListener(name_validator);
        email_card.removeTextChangedListener(email_validator);
        phone_card.removeTextChangedListener(phone_validator);

        name_card.setError(null);
        email_card.setError(null);
        phone_card.setError(null);

        btn_cancel.setVisibility(View.GONE);
        btn_accept.setVisibility(View.GONE);
        btn_img.setVisibility(View.GONE);

        btn_edit.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.VISIBLE);

        img_view.setImageResource(R.drawable.default_img);

        for (int i = 0; cardContactList.size() > i; i++){
            cardContactList.get(i).setAlpha((float) 1.0);
            cardContactList.get(i).btn_edit.setEnabled(true);
            cardContactList.get(i).btn_delete.setEnabled(true);
        }

        card_values();
    }

    public void validFormContact(){
        boolean validName = inputValidator.validName(name_card.getText());
        boolean validEmail = inputValidator.validEmail(email_card.getText());
        boolean validPhone = inputValidator.validPhone(phone_card.getText());

        if(validName==false){
            name_card.setError("Nombre invalido");
        }
        if(validEmail==false){
            email_card.setError("E-Mail Invalido");
        }
        if(validPhone==false){
            phone_card.setError("Telefono invalido");
        }

        if(validName && validEmail && validPhone){
            btn_accept.setEnabled(true);
        }else{
            btn_accept.setEnabled(false);
        }
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

    public void setImage (Intent data, ImageView imageView, Context context){
        try {
            final Uri imageUri = data.getData();
            final InputStream imgStream = context.getContentResolver().openInputStream(imageUri);
            final Bitmap selectImage = BitmapFactory.decodeStream(imgStream);
            imageView.setImageBitmap(selectImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    }

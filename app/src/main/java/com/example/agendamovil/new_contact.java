 package com.example.agendamovil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import 	android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.agendamovil.session.AgendaPermissions;
import com.example.agendamovil.session.BackendConnexion;
import com.example.agendamovil.session.VolleyCallback;
import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class new_contact extends AppCompatActivity {



    FrameLayout body;
    EditText name, email, phone;
    Button btn_select_img, btnNewContact;
    ImageView img_contact;
    InputValidator inputValidator;
    Toolbar toolbar;
    List<EditText> inputs = new ArrayList<>();
    SharedPreferences session;
    ProgressBarAgenda progressBar;

    Map<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        if(BackendConnexion.isLogged(this) == false){
            startActivity(BackendConnexion.go_login);
        }

        body = findViewById(R.id.body_new_contact);

        session = getSharedPreferences("com.example.agendamovil", MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.create_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.baseline_home_white_18dp));

        progressBar = new ProgressBarAgenda(this);
        body.addView(progressBar);
        progressBar.bringToFront();

        name = (EditText)findViewById(R.id.name_new_contact);
        email = (EditText)findViewById(R.id.email_new_contact);
        phone = (EditText)findViewById(R.id.phone_new_contact);
        btn_select_img = (Button)findViewById(R.id.upload_img_new_contact);
        btnNewContact = (Button)findViewById(R.id.btn_new_contact) ;
        img_contact = (ImageView)findViewById(R.id.imgContact);

        inputs.add(name);
        inputs.add(email);
        inputs.add(phone);

        btn_select_img.setOnClickListener(v->{
            AgendaPermissions.permissionsApp(new_contact.this, AgendaPermissions.PICK_FROM_GALLERY);
        });


        inputValidator = new InputValidator();


        name.addTextChangedListener(new ValidatorOnTextChange(this, name, InputValidator.name){
            @Override
            public void validator() {
                super.validator();
            }
        });

        email.addTextChangedListener(new ValidatorOnTextChange(this, email, InputValidator.email){
            @Override
            public void validator() {
                super.validator();
            }
        });

        phone.addTextChangedListener(new ValidatorOnTextChange(this, phone, InputValidator.phone){

        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                AgendaPermissions.selectImage(new_contact.this, AgendaPermissions.PICK_FROM_GALLERY);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (resultCode == RESULT_OK){
          try {

              final Uri imageUri = data.getData();
              final InputStream imgStream = getContentResolver().openInputStream(imageUri);

              Bitmap bitmapImage = BitmapFactory.decodeStream(imgStream);
              int width = bitmapImage.getWidth();
              int height = bitmapImage.getHeight();
              Picasso.get().load(imageUri).resize(width, height).centerCrop().into(img_contact);

              if(bitmapImage != null){
                  params.put("img_contact", convertToString(bitmapImage));

              }else {
                  Dialog dialog = errorProcessImageDialog(this);
                  dialog.show();
              }

          }catch (FileNotFoundException e){
              e.printStackTrace();
          }
      }

  }


    public static String convertToString(Bitmap selectImage) {

        ByteArrayOutputStream arrayOutputStream =  new ByteArrayOutputStream();

        final int height = selectImage.getHeight();
        final int width = selectImage.getWidth();

        int newHeight;
        int newWidth;

        int resize = 2;

        while(height > 300){

            newHeight = height/resize;
            newWidth = width / resize;

            if(newHeight < 600){
                selectImage = Bitmap.createScaledBitmap(selectImage, newWidth, newHeight, false);
                break;
            }
            resize *= 2;
        }
        selectImage.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
        byte[] imageByte = arrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageByte, Base64.DEFAULT);

        return imageString;
    }

    public static Dialog errorProcessImageDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.error_image).setPositiveButton( R.string.okey , (dialog, which) -> {});
        return  builder.create();
    }

    public void validNewContactForm(View v){

        if(inputValidator.validForm(inputs)){
            uploadContact();
        }
  }

  public void uploadContact(){
      BackendConnexion connexion = new BackendConnexion(this, BackendConnexion.CREATE_CONTACT, progressBar);

      params.put("email_contact", email.getText().toString());
      params.put("name_contact", name.getText().toString());
      params.put("phone_contact", phone.getText().toString());
      params.put("session_email", session.getString("email", ""));

      connexion.stringRequest(params, new VolleyCallback() {
          @Override
          public void onResponseString(String response) {
              Log.e("response_add", response.trim());
              Log.e("params_add", params.toString());
              switch (response.trim()){
                  case "OK":
                      Toast OK = Toast.makeText(getApplicationContext(), R.string.ok_contact, Toast.LENGTH_SHORT);
                      OK.show();
                      resetForm();
                      break;
                  case "error":
                     BackendConnexion.error_server.show();
                      break;

                  case "invalid params":
                      BackendConnexion.invalid_params.show();
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

  public void resetForm(){
      for (EditText input: inputs) {
          input.setText("");
          input.setError(null);
      }
      img_contact.setImageDrawable(getDrawable(R.drawable.default_img));
      params.remove("img_contact");

  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.search).setVisible(false);

        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item){
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

        }

        return true;
    }


}
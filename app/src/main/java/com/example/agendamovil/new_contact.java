package com.example.agendamovil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agendamovil.toolbar.ToolbarFunctions;
import com.example.agendamovil.validators.InputValidator_2;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.io.FileNotFoundException;
import java.io.InputStream;
// TODO:  Centrar formulario en vista horizontal
public class new_contact extends AppCompatActivity {

    EditText name, email, phone;
    Button upload_img, btnNewContact;
    ImageView img_contact;
    InputValidator_2 inputValidator;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        toolbar = findViewById(R.id.toolbar_custom);
        toolbar.setTitle(R.string.create_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.baseline_home_white_18dp));

        name = (EditText)findViewById(R.id.name_new_contact);
        email = (EditText)findViewById(R.id.email_new_contact);
        phone = (EditText)findViewById(R.id.phone_new_contact);
        upload_img = (Button)findViewById(R.id.upload_img);
        btnNewContact = (Button)findViewById(R.id.btn_new_contact) ;
        img_contact = (ImageView)findViewById(R.id.imgContact);

        inputValidator = new InputValidator_2();


        name.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validName(name.getText())){
                    name.setError(null);
                    btnNewContact.setEnabled(true);
                }else{
                    name.setError("Nombre invalido");
                }
            }
        });

        email.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validEmail(email.getText())){
                    email.setError(null);
                    btnNewContact.setEnabled(true);
                }else{
                    email.setError("Nombre invalido");
                }
            }
        });

        phone.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validPhone(phone.getText())){
                    phone.setError(null);
                    btnNewContact.setEnabled(true);
                }else{
                    phone.setError("Nombre invalido");
                }
            }
        });

    }

    public void selectImage(View V) {
        String[] img_types = {"image/jpeg", "image/png"};
        Intent pick_photo = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pick_photo.setType("image/jpeg");
        pick_photo.putExtra(Intent.EXTRA_MIME_TYPES, img_types);
        startActivityForResult(pick_photo, 1);
    }


  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (resultCode == RESULT_OK){
          try {
              final Uri imageUri = data.getData();
              final InputStream imgStream = getContentResolver().openInputStream(imageUri);
              final Bitmap selectImage = BitmapFactory.decodeStream(imgStream);
              img_contact.setImageBitmap(selectImage);
          }catch (FileNotFoundException e){
              e.printStackTrace();
          }
      }

  }

  public void validNewContactForm(View v){
      boolean validName = inputValidator.validName(name.getText());
      boolean validEmail = inputValidator.validEmail(email.getText());
      boolean validPhone = inputValidator.validPhone(phone.getText());

      if(validName==false){
          name.setError("Nombre invalido");
      }
      if(validEmail==false){
          email.setError("E-Mail Invalido");
      }
      if(validPhone==false){
          phone.setError("Telefono invalido");
      }

      if(validName && validEmail && validPhone){
          btnNewContact.setEnabled(true);
      }else{
          btnNewContact.setEnabled(false);
      }
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
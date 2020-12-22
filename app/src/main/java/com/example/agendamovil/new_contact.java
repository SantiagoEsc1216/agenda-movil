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
import com.example.agendamovil.validators.InputValidator;
import com.example.agendamovil.validators.ValidatorOnTextChange;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class new_contact extends AppCompatActivity {

    EditText name, email, phone;
    Button upload_img, btnNewContact;
    ImageView img_contact;
    InputValidator inputValidator;
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

        inputValidator = new InputValidator();


        name.addTextChangedListener(new ValidatorOnTextChange() {
            @Override
            public void validator() {
                if(inputValidator.validName(name.getText())){
                    name.setError(null);
                    btnNewContact.setEnabled(true);
                }else{
                    name.setError(getString(R.string.name_info));
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
                    email.setError(getString(R.string.invalid_email));
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
                    phone.setError(getString(R.string.phone_info));
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

        if(name.getText().toString().matches("") || email.getText().toString().matches("") || phone.getText().toString().matches("")){
            btnNewContact.setEnabled(false);
        }else{
            if(name.getError()==null && email.getError()==null && phone.getError()==null){
                //TODO: Enviar datos
            }else{
                btnNewContact.setEnabled(false);
            }
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
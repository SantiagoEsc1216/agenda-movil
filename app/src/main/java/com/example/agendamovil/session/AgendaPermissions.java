package com.example.agendamovil.session;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.agendamovil.R;

public final class AgendaPermissions{

        final static String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        public final static int PICK_FROM_GALLERY = 1;

        public static void permissionsApp(Activity activity, int requestCode){
            if(ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, PERMISSIONS, requestCode);
            }else {
                selectImage(activity, requestCode);
            }
        }



    public static void selectImage(Activity activity, int requestCode) {
            String[] img_types = {"image/jpeg", "image/png"};
            Intent pick_photo = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pick_photo.setType("image/jpeg");
            pick_photo.putExtra(Intent.EXTRA_MIME_TYPES, img_types);
            activity.startActivityForResult(pick_photo, requestCode);
        }

}

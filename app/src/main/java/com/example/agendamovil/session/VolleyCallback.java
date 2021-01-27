package com.example.agendamovil.session;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onResponseString(String response);

    void onResponseJsonObject(JSONObject response);

    void onResponseJsonArray(JSONArray response);


    default void errorHandler(Context context, VolleyError error){
        Toast toast = Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT);
        toast.show();
        Log.e("error_connexion",error.toString() );
    }

}

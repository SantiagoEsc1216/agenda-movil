package com.example.agendamovil;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressBarAgenda extends ConstraintLayout {
    Context context;
    ProgressBar progressBar;
    public ProgressBarAgenda(Context  context){
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.progress_bar, this, true);
        progressBar = findViewById(R.id.agenda_progressbar);
        this.setVisibility(GONE);

    }

    public void show(){
        this.setVisibility(VISIBLE);
        Log.e("progressBar", "show");
    }
    public void hide(){
        this.setVisibility(GONE);
        Log.e("progressBar", "hide");
    }
}

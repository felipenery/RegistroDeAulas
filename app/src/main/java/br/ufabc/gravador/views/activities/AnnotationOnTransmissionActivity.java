package br.ufabc.gravador.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.ufabc.gravador.R;

//DOESNT EXTENDS MYMENUACTIVITY!!!!!!
public class AnnotationOnTransmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_on_transmission);
    }
}

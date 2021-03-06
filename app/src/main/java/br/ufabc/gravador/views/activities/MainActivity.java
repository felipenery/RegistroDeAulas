package br.ufabc.gravador.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufabc.gravador.R;

public class MainActivity extends AbstractMenuActivity {

    Button initRecord, joinHostRecord, viewRecords;

    @SuppressLint( "MissingSuperCall" )
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState, R.layout.activity_main, R.id.my_toolbar, false, null);

        initRecord = findViewById(R.id.initRecord);
        initRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                initRecordOnClick(view);
            }
        });

        joinHostRecord = findViewById(R.id.joinHostRecord);
        joinHostRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                joinHostRecordOnClick(view);
            }
        });
        viewRecords = findViewById(R.id.viewRecords);
        viewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                viewRecordsOnClick(view);
            }
        });

    }

    void initRecordOnClick ( View view ) {
        Intent intent = new Intent(this, NewRecordActivity.class);
        startActivity(intent);
    }

    void joinHostRecordOnClick ( View view ) {
        Intent intent = new Intent(this, SharedRecordActivity.class);
        startActivity(intent);
    }

    void viewRecordsOnClick ( View view ) {
        Intent intent = new Intent(this, ViewGravacoesActivity.class);
        startActivity(intent);
    }
}

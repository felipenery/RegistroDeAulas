package br.ufabc.gravador.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufabc.gravador.R;
// alterei as linhas 14,15,22, 25,26,27, 28. Tem que dar um extends na classe ConfigAll que é a classe abstract que voce pediu.
public class MainActivity extends AbstractMenuActivity {

    Button initRecord, joinHostRecord, viewRecords;
    private ConstraintLayout tela;
    private int corTela;

    @SuppressLint( "MissingSuperCall" )
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState, R.layout.activity_main, R.id.my_toolbar, false, null);

        tela = (ConstraintLayout) findViewById(R.id.layoutId);
        initRecord = findViewById(R.id.initRecord);
        
        if(sharedPreferences.getInt(KEY_COR_TELA, corTela) != 0) {
            corTela = sharedPreferences.getInt(KEY_COR_TELA, corTela);
            linearLayout.setBackgroundColor(corTela);
        }
        
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

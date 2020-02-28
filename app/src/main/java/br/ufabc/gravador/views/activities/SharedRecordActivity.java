package br.ufabc.gravador.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.ufabc.gravador.R;

public class SharedRecordActivity extends AppCompatActivity {

    Button joinRoom, newRoom;
    Toolbar myToolbar;
    ActionBar myActionBar;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_record);

        joinRoom = findViewById(R.id.joinRoom);
        newRoom = findViewById(R.id.newRoom);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                joinRoomOnClick(view);
            }
        });
        newRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                newRoomOnClick(view);
            }
        });
    }

    void joinRoomOnClick ( View view ) {
        Intent intent = new Intent(this, JoinRoomActivity.class);
        startActivity(intent);
    }

    void newRoomOnClick ( View view ) {
        Intent intent = new Intent(this, CreateRoomActivity.class);
        startActivity(intent);
    }
}

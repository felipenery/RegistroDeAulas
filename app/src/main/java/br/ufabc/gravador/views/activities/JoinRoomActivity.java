package br.ufabc.gravador.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.ufabc.gravador.R;

public class JoinRoomActivity extends AppCompatActivity {

    Button readQRCode, joinRoomConfirm;
    TextView roomName;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        readQRCode = findViewById(R.id.readQRCode);
        joinRoomConfirm = findViewById(R.id.joinRoomConfirm);
        roomName = findViewById(R.id.RoomName);

        readQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) { readQRCodeOnClick(view); }
        });

        joinRoomConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) { joinRoomConfirmOnClick(view); }
        });
    }

    void readQRCodeOnClick ( View view ) {}

    void joinRoomConfirmOnClick ( View view ) {}
}

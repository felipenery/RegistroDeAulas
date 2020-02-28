package br.ufabc.gravador.views.activities;

import android.hardware.Camera;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.ufabc.gravador.R;

public class VideoRecordActivity extends AppCompatActivity {

    public static Camera getCameraInstance () {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch ( Exception e ) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
    }
}


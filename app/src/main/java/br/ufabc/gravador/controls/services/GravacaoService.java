package br.ufabc.gravador.controls.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import br.ufabc.gravador.controls.helpers.MyFileManager;
import br.ufabc.gravador.controls.helpers.NotificationHelper;
import br.ufabc.gravador.models.Gravacao;

public class GravacaoService extends Service {

    public static final String SERVICE_ACTION = "SERVICE_ACTION";
    public static final String ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE", ACTION_NEXT = "ACTION_NEXT", ACTION_PREV = "ACTION_PREV";
    public static final int MEDIATYPE_AUDIO = 1, MEDIATYPE_VIDEO = 2, MEDIATYPE_NULL = 0;
    public static final String AUDIO_EXTENSION = ".3gp", VIDEO_EXTENSION = "??";//TODO
    public static final int STATUS_IDLE = 0, STATUS_RECORDING = 1, STATUS_WAITING_SAVE = 2, STATUS_LOADING_SAVING = 3, STATUS_PLAYING = 4, STATUS_PAUSED = 5;
    private static final int NOTIFICATION_ID = 44444;
    private final IBinder binder = new LocalBinder();
    private int currMediaType = MEDIATYPE_NULL;
    private int serviceStatus = STATUS_IDLE;
    private NotificationHelper notificationHelper;
    private Gravacao gravacao;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private MyFileManager fileManager;

    /*
     * TIME-LOGIC BLOCK
     */

    public interface TimeUpdateListener {
        void onTimeUpdate ( long time );
    }
    private TimeUpdateListener registeredTimeListener;
    private long currentTime = 0, runnableStartTime = 0;
    private Handler timeHandler = new Handler();
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run () {
            currentTime = SystemClock.uptimeMillis() - runnableStartTime;
            timeHandler.postDelayed(this, 500);
            onTimeUpdate();
        }
    };

    private void onTimeUpdate () {
        if ( registeredTimeListener != null ) registeredTimeListener.onTimeUpdate(currentTime);
    }

    public void setTimeUpdateListener ( TimeUpdateListener listener ) { registeredTimeListener = listener;}

    private void startTimer ( long offset ) {
        runnableStartTime = SystemClock.uptimeMillis() - offset;
        timeHandler.postDelayed(timeRunnable, 0);
    }

    private void stopTimer () { timeHandler.removeCallbacks(timeRunnable); }

    /*
     * END TIME-LOGIC
     */

    /*
     * LIFECYCLE-LOGIC BLOCK
     */

    @Override
    public void onCreate () {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
        fileManager = MyFileManager.getInstance();
    }

    @Override
    public IBinder onBind ( Intent intent ) {
        return binder;
    }

    @Override
    public int onStartCommand ( Intent intent, int flags, int startId ) {
        if ( intent != null )
            if ( intent.getExtras() != null ) {
                String action = intent.getExtras().getString(SERVICE_ACTION);
                if ( action != null ) // abriram uma notificação de play/pause
                    switch ( action ) {
                        case ACTION_PLAY_PAUSE:
                            //TODO pausePlaying();
                            break;
                        case ACTION_NEXT:
                            //TODO jumpNextPrev(true);
                            break;
                        case ACTION_PREV:
                            //TODO jumpNextPrev(false);
                            break;
                    }
            }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        //TODO stopPlaying();
        stopRecording();
        stopTimer();
    }

    /*
     * END LIFECYCLE-LOGIC
     */

    /*
     *  RECORD-LOGIC BLOCK
     */

    public void setGravacao ( Gravacao gravacao ) {
        this.gravacao = gravacao;
    }

    private void goBackground () {
        notificationHelper.clearNotifications();
    }

    public void prepareGravacaoForRecord ( Gravacao g, int mediaType ) {
        if ( g == null ) return;
        String location = null, extension = null;
        switch ( mediaType ) {
            case MEDIATYPE_AUDIO:
                location = fileManager.getDirectory(MyFileManager.AUDIO_DIR).getPath();
                extension = AUDIO_EXTENSION;
                break;
            case MEDIATYPE_VIDEO:
                //TODO;
                break;
            case MEDIATYPE_NULL:
                break;
            default:
                throw new IllegalArgumentException("Unexpected mediatype: " + mediaType);
        }

        g.setFileLocation(location);
        g.setFileName(MyFileManager.newTempName());
        g.setFileExtension(extension);
    }

    private boolean startRecording () {
        if ( gravacao == null ) return false;

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(gravacao.getFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError ( MediaRecorder mr, int what, int extra ) {
                Log.e("MediaRecorder ERROR", "what = " + what + ", extra = " + extra);
            }
        });

        try {
            recorder.prepare();
        } catch ( IOException e ) {
            Log.e("AudioRecord", "prepare() failed", e);
            Toast.makeText(null, "Falha em iniciar gravação", Toast.LENGTH_LONG);
            prepareGravacaoForRecord(gravacao, MEDIATYPE_NULL);
            return false;
        }

        recorder.start();
        onRecordStarted();
        return true;
    }

    private void onRecordStarted () {
        startTimer(0);
        startForeground(
                NOTIFICATION_ID,
                notificationHelper.newRecordAudioNotification(
                        notificationHelper.buildRecordPendingIntent()
                ));
        serviceStatus = STATUS_RECORDING;
    }

    public void stopRecording () {
        if ( recorder != null ) {
            recorder.stop();
            recorder.release();
            recorder = null;
            onRecordStopped();
        }
    }

    private void onRecordStopped () {
        stopTimer();
        notificationHelper.pushNotification(
                NOTIFICATION_ID,
                notificationHelper.newRecordAudioNotification(
                        notificationHelper.buildRecordPendingIntent()
                ));
        serviceStatus = STATUS_WAITING_SAVE;
    }

    /*
     * END RECORD-LOGIC
     */


    //TODO continuarDaqui
    /*

        private boolean configPlayer ( Gravacao gravacao ) {
            if ( mediaPlayer == null )
                mediaPlayer = new MediaPlayer();
            else
                mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(gravacao.getFilePath());
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
                isPrepared = true;
            } catch ( IllegalArgumentException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(null, "Falha em iniciar gravação", Toast.LENGTH_LONG).show();
                stopPlaying();
                return false;
            }
            return true;
        }

        private boolean startStopPlaying ( Gravacao gravacao, boolean playPause ) {
            if ( mediaPlayer == null || !isPrepared )
                if ( !configPlayer(gravacao) ) return false;

            try {
                if ( playPause )
                    mediaPlayer.start();
                else
                    mediaPlayer.pause();

            } catch ( IllegalStateException e ) {
                Log.e("AudioPlayer", "bad state", e);
                stopPlaying();
                return false;
            }
            return true;
        }

        private int jumpTo ( Gravacao gravacao, int time ) {
            if ( mediaPlayer == null || !isPrepared )
                if ( !configPlayer(gravacao) )
                    return 0;

            mediaPlayer.seekTo(time);
            this.playTime = time;
            return time;
        }

        private int nextPrev ( Gravacao gravacao, boolean isNext ) {
            if ( mediaPlayer == null || !isPrepared )
                if ( !configPlayer(gravacao) ) return 0;

            int time, playTime = this.playTime;
            int[] times = gravacao.getAnnotationTimes();

            List<Integer> lTimes = Arrays.stream(times)
                    .filter(x -> isNext ? x > playTime : x < playTime)
                    .sorted()
                    .boxed()
                    .collect(Collectors.toList());

           time = lTimes.isEmpty() ?
                   isNext ? mediaPlayer.getDuration() : 0 :
                   isNext ? lTimes.get(0) : lTimes.get(lTimes.size()-1);

           return jumpTo(gravacao, time);
        }

        private void stopPlaying () {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPrepared = false;
        }

        void startTimer () {
            timeHandler.postDelayed(timeRunnable, 0);
        }

        void stopTimer () { timeHandler.removeCallbacks(timeRunnable); }

        // --- END TIME ---

        @Override
        public void onDestroy () {
            super.onDestroy();
            if ( mediaPlayer != null ) stopPlaying();
            if ( timeHandler != null ) stopTimer();
        }
    }

     */
    private void goForeground () {
        Notification n = null;
        if ( serviceStatus == STATUS_WAITING_SAVE )
            n = notificationHelper.newSaveNotification(notificationHelper.buildSavePendingIntent());
        else if ( serviceStatus == STATUS_RECORDING )
            n = notificationHelper.newRecordAudioNotification(
                    notificationHelper.buildRecordPendingIntent());
        else if ( serviceStatus == STATUS_PLAYING )
            n = notificationHelper.newPlayAudioNotification(
                    notificationHelper.buildPlayAudioPendingIntent(), gravacao.getName(), true);

        if ( n != null )
            notificationHelper.pushNotification(0, null);//TODO REDO
    }

    public class LocalBinder extends Binder {
        GravacaoService getService () { return GravacaoService.this; }
    }
}

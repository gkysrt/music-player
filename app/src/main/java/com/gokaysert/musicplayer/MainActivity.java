package com.gokaysert.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
    private ImageView albumCover;
    private SeekBar songSeekBar;
    private SeekBar volumeSeekBar;
    private TextView artistName;
    private TextView songName;
    private TextView songTime;
    private Button previousButton;
    private Button pauseButton;
    private Button nextButton;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private String songDuration;
    private Timer songTimer;
    private ArrayList<String> songList;
    private SongDataAccess songDataAccess;
    private SongAdapter songAdapter;
    private SongModel songModel;
    private Thread songThread;
    private Button songListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previousButton = findViewById(R.id.previous_button);
        pauseButton = findViewById(R.id.pause_button);
        nextButton = findViewById(R.id.next_button);
        songListButton = findViewById(R.id.playlist_button);
        artistName = findViewById(R.id.artist_name);
        songTime = findViewById(R.id.song_time);
        songName = findViewById(R.id.song_name);
        songSeekBar = findViewById(R.id.song_seekBar);
        volumeSeekBar = findViewById(R.id.volume_seekBar);
        albumCover = findViewById(R.id.album_cover);
        songDataAccess = new SongDataAccess();
        songAdapter = new SongAdapter();
        songModel = new SongModel();
        songList = new ArrayList<>();
        songThread = new Thread();
        songTimer = new Timer();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        } else {
            songDataAccess.scanDirectory();
            ArrayList<String> scanData = songDataAccess.getScannedSongPaths();
            songAdapter.setAdapterSource(scanData);
            songModel.setSongList(songAdapter.requestSongList());
            songModel.setPathList(scanData);
            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    playNextSong(nextButton);
//                }
//            });
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build());
            try {
                Song firstSong = songModel.firstSong();
                mediaPlayer.setDataSource(firstSong.getAbsolutePath());
                mediaPlayer.prepare();
                initializeEnvironment();
                attachMediaMetaData(firstSong);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeEnvironment() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private boolean touched;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(touched == true)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                touched = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touched = false;
            }
        });


        songSeekBar.setMax(mediaPlayer.getDuration());
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private boolean touched;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (touched == true)
                    mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                touched = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                touched = false;
            }
        });

    }

    public void attachMediaMetaData(Song song) {
        if(song.getArtist() == null)
            artistName.setText("Unknown Artist");
        else
            artistName.setText(song.getArtist());

        if(song.getSong() == null)
            songName.setText(song.getAbsolutePath().substring(song.getAbsolutePath().lastIndexOf("/")+1));
        else
            songName.setText(song.getSong());

        byte[] albumCoverBytes = song.getAlbumCoverBytes();

        if (albumCoverBytes == null)
            albumCover.setImageResource(R.drawable.default_album_cover2);
        else
            albumCover.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(albumCoverBytes, 0, albumCoverBytes.length), 640, 640, false));
    }

    public void playNextSong() {
        mediaPlayer.reset();
        Song nextSong = songModel.nextSong();
        try {
            mediaPlayer.setDataSource(nextSong.getAbsolutePath());
            mediaPlayer.prepare();
            attachMediaMetaData(nextSong);
            songSeekBar.setProgress(0);
            songSeekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        runSongTimer();
    }

    public String millisecondsToText(int milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) milliseconds / 60000;

        String stringSeconds = null;
        if (seconds < 10)
            stringSeconds = "0" + Integer.toString(seconds);
        else
            stringSeconds = Integer.toString(seconds);

        String stringMinutes = Integer.toString(minutes);

        return stringMinutes + ":" + stringSeconds;
    }

    public void runSongTimer()
    {
        songTimer.cancel();
        songTimer = new Timer();
        songTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                songTime.setText(millisecondsToText(mediaPlayer.getCurrentPosition()));
            }
        },0, 100);
    }

    public void playPreviousSong() {
        try {

            Song previousSong;
            if (mediaPlayer.getCurrentPosition() < 5000)
                previousSong = songModel.previousSong();

            else
                previousSong = songModel.currentSong();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(previousSong.getAbsolutePath());
            mediaPlayer.prepare();
            attachMediaMetaData(previousSong);
            songSeekBar.setProgress(0);
            songSeekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runSongTimer();
    }

    public void playAndPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            songTimer.cancel();
        } else {
            mediaPlayer.start();
            // Timer that runs on a different thread to update songSeekBar
            runSongTimer();
        }
    }

    /* ****************************** BUTTON FUNCTIONS ****************************** */

    //R.id.pause_button
    public void playSongButton(View view) {
        playAndPause();
    }

    //R.id.previous_button
    public void previousSongButton(View view) {
        playPreviousSong();
    }

    //R.id.next_button
    public void nextSongButton(View view) {
        playNextSong();
    }

    public void songListButton(View view) {
        Intent intentToStartPlaylistActivity = new Intent(MainActivity.this, PlaylistActivity.class);
        intentToStartPlaylistActivity.putStringArrayListExtra("pathList", songModel.getPathList());
        startActivity(intentToStartPlaylistActivity);
    }

    public void shuffleRepeatButton(View view) {

    }
}

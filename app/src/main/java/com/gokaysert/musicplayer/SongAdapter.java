package com.gokaysert.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ALLDe on 17/11/2019.
 */

public class SongAdapter {
    private MediaMetadataRetriever mediaMetadataRetriever;
    private ArrayList<Song> songs;
    private ArrayList<String> pathList;

    public SongAdapter()
    {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        pathList = null;
    }

    public ArrayList<Song> requestSongList()
    {
        songs = new ArrayList<>();

        for(String path : pathList)
        {
            Song song = new Song();
            try {
                mediaMetadataRetriever.setDataSource(path);
                song.setAbsolutePath(path);
                song.setSong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                song.setArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
                byte[] albumCoverBytes = mediaMetadataRetriever.getEmbeddedPicture();
                song.setAlbumCoverBytes(albumCoverBytes);
                song.setAuthor(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                song.setDuration(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                song.setComposer(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
                song.setTrackNumber(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
                song.setDate(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE));

                songs.add(song);
            }
            catch (Exception e)
            {
                Log.e("SONG_ADAPTER_EXCEPTION", e.toString());
            }
        }
        return songs;
    }

    public void setAdapterSource(ArrayList<String> pathList)
    {
        this.pathList = pathList;
    }

    public ArrayList<String> getAdapterSource()
    {
        return this.pathList;
    }

}

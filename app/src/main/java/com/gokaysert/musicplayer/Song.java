package com.gokaysert.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ALLDe on 17/11/2019.
 */

public class Song implements Parcelable{
    private String absolutePath;
    private String song;
    private String artist;
    private String duration;
    private byte[] albumCoverBytes;
    private String author;
    private String composer;
    private String date;
    private String trackNumber;

    public Song()
    {
        absolutePath = null;
        song = null;
        artist = null;
        duration = null;
        albumCoverBytes = null;
        author = null;
        composer = null;
        date = null;
        trackNumber = null;
    }

    public Song(Parcel in)
    {
        String[] stringData = new String[8];
        in.readStringArray(stringData);
        this.song = stringData[0];
        this.artist = stringData[1];
        this.author = stringData[2];
        this.duration = stringData[3];
        this.date = stringData[4];
        this.composer = stringData[5];
        this.absolutePath = stringData[6];
        this.trackNumber = stringData[7];

        in.readByteArray(this.albumCoverBytes);
    }
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public void setAlbumCoverBytes(byte[] albumCoverBytes) {
        this.albumCoverBytes = albumCoverBytes;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public byte[] getAlbumCoverBytes() {
        return albumCoverBytes;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getArtist() {
        return artist;
    }

    public String getAuthor() {
        return author;
    }

    public String getComposer() {
        return composer;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public String getSong() {
        return song;
    }

    public String getTrackNumber() {
        return trackNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.song,
                this.artist,
                this.author,
                this.duration,
                this.date,
                this.composer,
                this.absolutePath,
                this.trackNumber,
        });

        parcel.writeByteArray(this.albumCoverBytes);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

}

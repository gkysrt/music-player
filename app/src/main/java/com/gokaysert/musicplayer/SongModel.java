package com.gokaysert.musicplayer;

import java.util.ArrayList;

/**
 * Created by ALLDe on 17/11/2019.
 */

public class SongModel{
    private ArrayList<Song> songList;
    private ArrayList<String> pathList;
    private int songIndex;
    private boolean cycle;

    public SongModel()
    {
        cycle = true;
        songList = new ArrayList<>();
        songIndex = 0;
    }

    public void addSong(Song s)
    {
        songList.add(s);
    }

    public int getSongCount()
    {
        return songList.size();
    }

    public Song firstSong()
    {
        songIndex = 0;
        return songList.get(songIndex);
    }

    public Song nextSong()
    {
        songIndex = (songIndex + 1) % songList.size();
        return songList.get(songIndex % songList.size());
    }

    public Song previousSong()
    {
        if (songIndex != 0)
            songIndex -= 1;
        else if (cycle && songIndex == 0)
            songIndex = songList.size() - 1;

        return songList.get(songIndex);
    }

    public Song currentSong()
    {
        return songList.get(songIndex);
    }

    public int currentIndex()
    {
        return this.songIndex;
    }

    public Song getSongAtIndex(int i)
    {
        return songList.get(i);
    }

    public void removeSongAtIndex(int i)
    {
        songList.remove(i);
    }

    public void removeSong(Song s)
    {
        songList.remove(s);
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public boolean getCycle()
    {
        return this.cycle;
    }

    public void setSongIndex(int songIndex) {
        this.songIndex = songIndex;
    }

    public int getSongIndex() {
        return this.songIndex;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }
}


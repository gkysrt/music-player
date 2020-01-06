package com.gokaysert.musicplayer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ALLDe on 17/11/2019.
 */

public class SongDataAccess {
    private File musicDirectory;
    private ArrayList<String> pathList;

    public SongDataAccess()
    {
        musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        pathList = new ArrayList<>();
    }

    public void scanDirectory()
    {
        if(musicDirectory.isDirectory())
        {
            File listFile[] = this.musicDirectory.listFiles();

            if(listFile != null)
            {
                for(int i = 0; i < listFile.length ; i++)
                {
                    if(listFile[i].isFile())
                    {
                        pathList.add(listFile[i].getAbsolutePath());
                    }
                    else if(listFile[i].isDirectory())
                    {
                        scanDirectory(listFile[i]);
                    }
                }
            }
        }
    }

    public void scanDirectory(File file)
    {
        if(musicDirectory.isDirectory())
        {
            File listFile[] = file.listFiles();

            if(listFile != null)
            {
                for(int i = 0; i < listFile.length ; i++)
                {
                    if(listFile[i].isFile())
                    {
                        pathList.add(listFile[i].getAbsolutePath());
                    }

                    else if(listFile[i].isDirectory())
                    {
                        scanDirectory(listFile[i]);
                    }
                }
            }
        }
    }

    public ArrayList<String> getScannedSongPaths()
    {
        return this.pathList;
    }

    public void setMusicDirectory(File musicDirectory)
    {
        this.musicDirectory = musicDirectory;
    }

    public File getMusicDirectory()
    {
        return this.musicDirectory;
    }
}

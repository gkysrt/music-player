package com.gokaysert.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity implements PlaylistAdapter.ListItemClickListener{
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private ArrayList<String> songModelPathData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Intent intentStartedThisActivity = getIntent();

        songModelPathData = null;
        if(intentStartedThisActivity.hasExtra("pathList"))
        {
            songModelPathData = intentStartedThisActivity.getStringArrayListExtra("pathList");
        }

        if(songModelPathData == null)
        {
            return;
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlaylistAdapter(songModelPathData, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, View view) {
        Toast.makeText(this, Integer.toString(clickedItemIndex), Toast.LENGTH_SHORT).show();
    }
}

package com.gokaysert.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ALLDe on 20/11/2019.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistActivityViewHolder>{
    private ArrayList<String> songModelPathData;
    final private ListItemClickListener listItemClickListener;
    private MediaMetadataRetriever metadataRetriever;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, View view);
    }
    public PlaylistAdapter(ArrayList<String> songModelPathData, ListItemClickListener listener)
    {
        metadataRetriever = new MediaMetadataRetriever();
        this.songModelPathData = songModelPathData;
        this.listItemClickListener = listener;
    }
    @Override
    public PlaylistActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.playlist_item_layout;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        PlaylistActivityViewHolder viewHolder = new PlaylistActivityViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount()
    {
        return this.songModelPathData.size();
    }

    @Override
    public void onBindViewHolder(PlaylistActivityViewHolder holder, int position) {
        String path = songModelPathData.get(position);
        try{
            metadataRetriever.setDataSource(path);
        }
        catch (Exception e)
        {
            Log.e("METADATA_BIND_ERROR", e.toString());
            return;
        }

        String songTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        byte[] albumCover = metadataRetriever.getEmbeddedPicture();

        String songTitleToBind = songTitle;
        if(songTitle == null)
            songTitleToBind = path.substring(path.lastIndexOf("/")+1);

        String artistToBind = artist;
        if(artist == null)
            artistToBind = "Unknown Artist";

        Bitmap bitmapToBind = null;
        if(albumCover != null)
            bitmapToBind = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(albumCover, 0, albumCover.length), 640, 640, false);
//            bitmapToBind = BitmapFactory.decodeByteArray(albumCover, 0, albumCover.length);

        holder.bind(songTitleToBind, artistToBind, bitmapToBind);
    }

    public class PlaylistActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView songName;
        private TextView artistName;
        private ImageView albumCover;

        public PlaylistActivityViewHolder(View itemView)
        {
            super(itemView);
            this.songName= (TextView) itemView.findViewById(R.id.list_item_song_name);
            this.artistName = (TextView) itemView.findViewById(R.id.list_item_artist_name);
            this.albumCover = (ImageView) itemView.findViewById(R.id.list_item_album_cover);
            itemView.setOnClickListener(this);
        }
        public void bind(String songName, String artistName, Bitmap albumCover)
        {
            this.songName.setText(songName);
            this.artistName.setText(artistName);

            if(albumCover == null)
                this.albumCover.setImageResource(R.drawable.default_album_cover2);

            else
                this.albumCover.setImageBitmap(albumCover);
        }

        @Override
        public void onClick(View view)
        {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition, view);
        }
    }

}

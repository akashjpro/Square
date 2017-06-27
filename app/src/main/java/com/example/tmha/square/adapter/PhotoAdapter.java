package com.example.tmha.square.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tmha.square.R;
import com.example.tmha.square.listener.ListenerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmha on 6/23/2017.
 */

public class PhotoAdapter extends
        RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    List<String> mListPath;
    Activity mContext;
    ListenerItem mListenerItem;

    public PhotoAdapter(Activity mContext, List<String> mListPath,
                        ListenerItem mListenerItem) {
        this.mListPath = mListPath;
        this.mContext = mContext;
        this.mListenerItem = mListenerItem;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder,
                                 final int position) {
        String path = mListPath.get(position);
        if(!path.equals("000")){
            Picasso.with(mContext).load(path)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.imgPhoto);
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerItem.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPath.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete, imgPhoto;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDeletePhoto);
        }
    }
}

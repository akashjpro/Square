package com.example.tmha.square.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.FullPhotoActivity;
import com.example.tmha.square.listener.ListenerCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aka on 6/25/2017.
 */

public class CardPhotoAdapter extends PagerAdapter implements ListenerCard {

    private Activity mContext;
    private List<CardView> mListCard;
    private List<String> mData;
    private float mBaseElevation;

    public CardPhotoAdapter(Activity mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
        mListCard = new ArrayList<>();
    }

    public void addCardItem(List<String> listPath) {
        for (int i =0; i< listPath.size(); i++) {
            mListCard.add(null);
            mData.add(listPath.get(i));
        }
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardView(int position) {
        return mListCard.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
    // object is key value when change page, if we return view then set view = object
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.card_photo, container, false);
        container.addView(view);
        bindView(mData.get(position), view, position);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if (mBaseElevation == 0){
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mListCard.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mListCard.set(position, null);
    }

    private void bindView(final String path, View view, final int position) {
        ImageView imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
        Picasso.with(mContext).load(path)
                .error(android.R.drawable.stat_notify_error)
                .into(imgPhoto);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FullPhotoActivity.class);
                intent.putExtra("path", path);
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.scale_zoom_in, R.anim.scale_zoom_out);
            }
        });


    }

}

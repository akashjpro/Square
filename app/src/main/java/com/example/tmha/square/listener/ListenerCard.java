package com.example.tmha.square.listener;

import android.support.v7.widget.CardView;

/**
 * Created by Aka on 6/25/2017.
 */

public interface ListenerCard {
    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardView(int position);

    int getCount();
}

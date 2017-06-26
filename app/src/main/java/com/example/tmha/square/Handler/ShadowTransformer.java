package com.example.tmha.square.Handler;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.tmha.square.listener.ListenerCard;

/**
 * Created by Aka on 6/26/2017.
 */

public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ViewPager mViewPager;
    private ListenerCard mListenerCard;
    private float mLastOffset;

    public ShadowTransformer(ViewPager mViewPager, ListenerCard mListenerCard) {
        this.mViewPager = mViewPager;
        this.mListenerCard = mListenerCard;
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = mListenerCard.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > mListenerCard.getCount() - 1
                || realCurrentPosition > mListenerCard.getCount() - 1) {
            return;
        }


        CardView currentCard = mListenerCard.getCardView(realCurrentPosition);

        //scale current card
        if (currentCard != null) {
            currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
            currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
        }

        CardView nextCard = mListenerCard.getCardView(nextPosition);
        //scalex next or previous card when scrolling
        if (nextCard != null) {
            nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
            nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));


        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void transformPage(View page, float position) {

    }
}

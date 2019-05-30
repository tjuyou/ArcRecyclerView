package com.you.arcrecyclerview;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Author: you
 * Data：2019/5/30 15:36
 * Description:
 */
public class ArcRecyclerView extends RecyclerView {

    private static final int DEFAULT_SELECTION = (Integer.MAX_VALUE >> 1) + 1;
    private boolean mIsForcePlaced = true;
    private View mCurrentFirstView;
    private final FirstRunnable mFirstRunnable = new FirstRunnable();
    private int mCircleOffset = 120;
    private float mDegToRad = 1.0f / 180.0f * (float) Math.PI;
    private float mTranslationRatio = 0.15f;
    private SoundPool mSoundPool;
    private View lastView;
    private SparseIntArray soundID = new SparseIntArray();
    private boolean isByUser = false;

    public ArcRecyclerView(@NonNull Context context) {
        super(context);
        initSound();
    }

    public ArcRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSound();
    }

    public ArcRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSound();
    }

    private void initSound() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(10);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        } else {
            //设置最多可容纳10个音频流，音频的品质为5
            mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        }
        soundID.put(1, mSoundPool.load(getContext(), R.raw.tock, 1));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        不可行，包含滚动轨迹
//        getLayoutManager().smoothScrollToPosition(this, null, DEFAULT_SELECTION);
        scrollToPosition(DEFAULT_SELECTION);
//        scrollToPosition没调用onScrollChanged，目前解决方案，手动调用一下
        onScrollChanged(l,t,r,b);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            if (!mIsForcePlaced) {
                mIsForcePlaced = true;
                mCurrentFirstView = findViewAtFirst();
                mFirstRunnable.setView(mCurrentFirstView);
                ViewCompat.postOnAnimation(this, mFirstRunnable);
            }
        }
    }

    private View findViewAtFirst() {
//        return findViewAt(getWidth() / 8, 0); //根据图标个数设定,4个的设定
        return findViewAt(getWidth() / 10, 0);//根据图标个数设定,5个的设定
    }

    public View findViewAt(int x, int y) {
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            final View v = getChildAt(i);
            final int x0 = v.getLeft();
            final int y0 = v.getTop();
            final int x1 = v.getWidth() + x0;
            final int y1 = v.getHeight() + y0;
            if (x >= x0 && x <= x1) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        removeCallbacks(mFirstRunnable);
        mIsForcePlaced = false;
        isByUser = true;
        return super.onTouchEvent(e);
    }

    public void smoothScrollToView(View v) {
        int distance = 0;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            if (getLayoutManager().canScrollHorizontally()) {
                final float x = v.getX() + v.getWidth() * 0.5f;
//                final float halfWidth = getWidth() * 0.125f;
                final float halfWidth = getWidth() * 0.1f; //5个图标设定
                distance = (int) (x - halfWidth);
            }
        } else {
            throw new IllegalArgumentException("CircleRecyclerView just support T extend LinearLayoutManager!");
        }
        smoothScrollBy(distance, distance);
        mSoundPool.play(soundID.get(1), 1, 1, 1, 0, 1 );
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = findViewAtFirst();
        if (isByUser && view != lastView) {
            mSoundPool.play(soundID.get(1), 1, 1, 1, 0, 1 );
            lastView = view;
        }
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            float halfWidth = v.getWidth() * 0.5f;
            float parentHalfWidth = this.getWidth() * 0.5f;
            float x = v.getX();
            float rot = parentHalfWidth - halfWidth - x;
            ViewCompat.setTranslationY(v, -1 * (float) (-Math.cos(rot * mTranslationRatio * mDegToRad) + 1) * mCircleOffset);
        }
    }

    public void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    public class FirstRunnable implements Runnable {

        private WeakReference<View> mView;

        public void setView(View v) {
            mView = new WeakReference<View>(v);
        }

        @Override
        public void run() {
            smoothScrollToView(mView.get());
            mIsForcePlaced = true;
        }
    }
}

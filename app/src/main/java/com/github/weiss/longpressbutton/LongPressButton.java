package com.github.weiss.longpressbutton;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Weiss on 2016/12/30.
 */

public class LongPressButton extends Button {
    private int mLastMotionX, mLastMotionY;
    //是否移动了
    private boolean isMoved;
    //移动的阈值
    private static final int TOUCH_SLOP = 100;
    private int count;
    //长按多少秒进入倒计时
    private int longPressSecond = 3;
    //倒计时秒数
    private int countDownSecond = 60;

    public CountDownTimer timer;

    //长按的runnable
    private Runnable mLongPressRunnable = new Runnable() {

        @Override
        public void run() {
            if (count == longPressSecond - 1) {
                Countdown(countDownSecond + "");
                count = 0;
                performLongClick();
                return;
            } else {
                setText(longPressSecond - 1 - count + "");
                count++;
                postDelayed(mLongPressRunnable, 1000);
            }
        }

    };

    public LongPressButton(Context context) {
        super(context);
        initCountDownTimer();
    }

    public LongPressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCountDownTimer();
    }

    public LongPressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCountDownTimer();
    }


    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                isMoved = false;
                StartLongPress(longPressSecond + "");
                postDelayed(mLongPressRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoved) break;
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                        || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                    //移动超过阈值，则表示移动了
                    isMoved = true;
                    removeCallbacks(mLongPressRunnable);
                    Start();
                }
                break;
            case MotionEvent.ACTION_UP:
                //释放了
                removeCallbacks(mLongPressRunnable);
                Start();
                break;
        }
        return true;
    }

    private void initCountDownTimer() {
        timer = new CountDownTimer(countDownSecond * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                Start();
            }
        };
    }

    public int getLongPressSecond() {
        return longPressSecond;
    }

    public void setLongPressSecond(int longPressSecond) {
        this.longPressSecond = longPressSecond;
    }

    public int getCountDownSecond() {
        return countDownSecond;
    }

    public void setCountDownSecond(int countDownSecond) {
        this.countDownSecond = countDownSecond;
        initCountDownTimer();
    }

    /**
     * 整装待发
     */
    public void Start() {
        setEnabled(true);
        setText("Hold");
        count = 0;
    }

    /**
     * 开始长按
     *
     * @param str
     */
    public void StartLongPress(String str) {
        setEnabled(true);
        setText(str);
        count = 0;
    }

    /**
     * 倒计时
     *
     * @param str
     */
    public void Countdown(String str) {
        setEnabled(false);
        setText(str);
    }
}

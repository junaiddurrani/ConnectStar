package com.game.android.connectstar;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

/**
 * This class uses the native CountDownTimer to
 * create a timer which could be paused and then
 * started again from the previous point. You can
 * provide implementation for onTick() and onFinish()
 * then use it in your projects.
 */
public abstract class Timer {
    long millisInFuture = 0;
    long countDownInterval = 0;
    long millisRemaining =  0;

    CountDownTimer countDownTimer = null;

    boolean isPaused = true;

    public Timer(long millisInFuture, long countDownInterval) {
        super();
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.millisRemaining = this.millisInFuture;
    }

    public Timer() {

    }

    private void createCountDownTimer(){
        countDownTimer = new CountDownTimer(millisRemaining,countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;
                Timer.this.onTick(millisUntilFinished);

            }

            @Override
            public void onFinish() {
                Timer.this.onFinish();

            }
        };
    }

    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();

    /**
     * Cancel the countdown.
     */
    public final void cancel(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        this.millisRemaining = 0;
    }

    /**
     * Start or Resume the countdown.
     * @return CountDownTimerPausable current instance
     */
    public synchronized final Timer start(){
        if(isPaused){
            createCountDownTimer();
            countDownTimer.start();
            isPaused = false;
        }
        return this;
    }

    /**
     * Pauses the CountDownTimerPausable, so it could be resumed(start)
     * later from the same point where it was paused.
     */
    public void pause() throws IllegalStateException{
        if(!isPaused) {
            countDownTimer.cancel();
            isPaused = true;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Format duration between two interval of time in millis into hour and minute
     *@return integer array - minutes at index 0 and seconds at index 1
     */
    public static int[] formatDuration(long durationInMillis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        durationInMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
        return new int[]{(int) minutes, (int) seconds};
    }

    public abstract void schedule(MoreScreen.MyTimer myTimer, int i, int i1);
}

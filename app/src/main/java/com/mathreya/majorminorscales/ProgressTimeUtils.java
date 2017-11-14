package com.mathreya.majorminorscales;

/**
 * Created by mithileshathreya on 11/3/17.
 */

public class ProgressTimeUtils {
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimeString = "";
        String secondsString;
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimeString += minutes + ":" + secondsString;
        return finalTimeString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        double percentage;
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        percentage = (((double) currentSeconds) / totalSeconds) * 100;
        return (int) percentage;
    }
}

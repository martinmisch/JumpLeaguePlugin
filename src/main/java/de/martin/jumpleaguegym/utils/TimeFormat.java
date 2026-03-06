package de.martin.jumpleaguegym.utils;

public class TimeFormat {
    public TimeFormat() {
    }

    public static String getTimeSM(int sec) {
        String s = sec / 60 + ":";
        if (sec % 60 >= 10) {
            s = s + sec % 60;
        } else {
            s = s + "0" + sec % 60;
        }

        return s;
    }

    public static String getTimeMSM(int millis) {
        int min = millis / 60000;
        int sec = (millis / 1000) % 60;
        int ms = millis % 1000;

        if (min > 0) {
            return String.format("%02d:%02d:%03d", min, sec, ms);
        } else {
            return String.format("%02d:%03d", sec, ms);
        }
    }

}

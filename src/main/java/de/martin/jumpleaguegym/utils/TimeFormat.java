package de.martin.jumpleaguegym.utils;

public class TimeFormat {
    public TimeFormat() {
    }

    public static String getTime(int sec) {
        String s = sec / 60 + ":";
        if (sec % 60 >= 10) {
            s = s + sec % 60;
        } else {
            s = s + "0" + sec % 60;
        }

        return s;
    }
}

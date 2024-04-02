package com.jRyun.demo.planProject.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MakeRandomStr {
    private static long NUM = (long)Math.pow(10,12);

    public static String randomChar(long seed) {
        Random rand = new Random(seed);

        int myRandInt = (int)(rand.nextInt(36));
        if(myRandInt < 10) {
            return Integer.toString(myRandInt);
        } else {
            int asciiVal = myRandInt - 10 + 65;
            return Character.toString((char)asciiVal);
        }
    }

    public static long shiftRightSeed(long seed) {
        long result = (seed / 10) + ((seed%10>0?seed%10:1)*NUM);
        return result;
    }

    public static String makeRandomPk(int randomKeySize) {
        StringBuilder sb = new StringBuilder();
        long seed = System.currentTimeMillis();

        for(int i=0;i<randomKeySize;i++) {
            sb.append(randomChar(seed));
            seed  = shiftRightSeed(seed);
        }

        return sb.toString();
    }

    public static String makeRandomPk(int randomSubKeySize, String dateFormat) {

        DateFormat formatter;
        formatter = new SimpleDateFormat(dateFormat);
        Calendar today = Calendar.getInstance();
        String date = formatter.format(today.getTime());

        StringBuilder sb = new StringBuilder();
        long seed = System.currentTimeMillis();

        for(int i=0;i<randomSubKeySize;i++) {
            sb.append(randomChar(seed));
            seed  = shiftRightSeed(seed);
        }
        sb.append(date);

        return sb.toString();
    }

    public static String makeRandomPk(int randomSubKeySize, String dateFormat, boolean isConvert) {

        DateFormat formatter;
        formatter = new SimpleDateFormat(dateFormat);
        Calendar today = Calendar.getInstance();
        StringBuilder date = new StringBuilder();
        date.append(formatter.format(today.getTime()));

        StringBuilder sb = new StringBuilder();
        long seed = System.currentTimeMillis();

        for(int i=0;i<randomSubKeySize;i++) {
            sb.append(randomChar(seed));
            seed  = shiftRightSeed(seed);
        }
        sb = date.append(sb);

        return sb.toString();
    }
}

package domini;

import java.io.File;
import java.time.Instant;

public class Statistics {
    private static long startingTime;
    private static long endingTime;
    private static long iniFileSize;
    private static long finFileSize;

    public Statistics () {
        startingTime = 0;
        endingTime = 0;
        iniFileSize = 0;
        finFileSize = 0;
    }

    public static void setStartingTime () {
        startingTime = System.currentTimeMillis();
    }

    public static void setEndingTime () {
        endingTime = System.currentTimeMillis();
    }

    public static void setIniFileSize (String filename) {
        iniFileSize = new File(filename).length();
    }

    public static void setFinFileSize (String filename) {
        finFileSize = new File(filename).length();
    }

    public static double getTime () {
        return (double)(endingTime - startingTime)/1000.0;
    }

    public static double getIniFileSize() {
        return iniFileSize/1000;
    }

    public static double getFinFileSize() {
        return finFileSize/1000;
    }

    public static long getBytesCompressed () {
        return iniFileSize - finFileSize;
    }

    public static long getBytesDecompressed () {
        return finFileSize - iniFileSize;
    }

    public static double getPercentageCompressed () {
        return Math.round(10000.0 * (double) getBytesCompressed() / iniFileSize)/100.0;
    }

    public static double getPercentageDecompressed () {
        return Math.round(10000.0 * (double) getBytesDecompressed() / iniFileSize)/100.0;
    }

    public static double getSpeedCompressed () {
        return (double)iniFileSize / getTime() / 1000.0;
    }

    public static double getSpeedDecompressed () {
        return (double)finFileSize / getTime() / 1000.0;
    }
}

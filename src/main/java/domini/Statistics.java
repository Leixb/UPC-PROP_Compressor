package domini;

import java.io.File;
import java.time.Instant;

public class Statistics {
    private static long startingTime;
    private static long endingTime;
    private static long iniFileSize;
    private static long finFileSize;

    public static void start () {
        startingTime = Instant.now().getEpochSecond();
    }

    public static void stop () {
        endingTime = Instant.now().getEpochSecond();
    }

    public static long time (){
        return endingTime - startingTime;
    }

    public static void initialFileSize (File file) {
        iniFileSize = file.length();
    }

    public static void finalFileSize (File file) {
        finFileSize = file.length();
    }

    public static long bytesCompressed () {
        return finFileSize - iniFileSize;
    }

    public static long percentageCompressed () {
        return ((finFileSize - iniFileSize)/iniFileSize) * 100;
    }

    public static long ratioBytesxTime () {
        return (finFileSize - iniFileSize)/ (endingTime - startingTime);
    }
}

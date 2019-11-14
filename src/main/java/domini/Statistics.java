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

    public static long getTime (){
        return endingTime - startingTime;
    }

    public static void initialFileSize (String filename) {
        File file = new File(filename);
        iniFileSize = file.length();
    }

    public static void finalFileSize (String filename) {
        File file = new File(filename);
        finFileSize = file.length();
    }

    public static long getBytesCompressed () {
        return finFileSize - iniFileSize;
    }

    public static long getPercentageCompressed () {
        return ((finFileSize - iniFileSize)/iniFileSize) * 100;
    }

    public static long getBytesPerTime () {
        return (finFileSize - iniFileSize)/ (endingTime - startingTime);
    }
}

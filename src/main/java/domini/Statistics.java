package domini;

import java.io.File;
import java.time.Instant;

public class Statistics {
    private static long startingTime;
    private static long endingTime;
    private static long iniFileSize;
    private static long finFileSize;

    public static void start () { startingTime = Instant.now().getEpochSecond(); }

    public static void stop () {
        endingTime = Instant.now().getEpochSecond();
    }

    public static long getTime (){
        return endingTime - startingTime;
    }

    public static void initialFileSize (String filename) { iniFileSize = new File(filename).length(); }

    public static void finalFileSize (String filename) { finFileSize = new File(filename).length(); }

    public static long getBytesCompressed () {
        return finFileSize - iniFileSize;
    }

    public static long getBytesDecompressed () {
        return iniFileSize - finFileSize;
    }

    public static double getPercentageCompressed () { return 100 * ((double)(finFileSize - iniFileSize))/iniFileSize; }

    public static double getPercentageDecompressed () { return 100 * ((double)(iniFileSize - finFileSize))/finFileSize; }

    public static double getSpeedCompression () { return ((double) (finFileSize - iniFileSize))/ (endingTime - startingTime); }

    public static double getSpeedDecompression () { return ((double) (iniFileSize - finFileSize))/ (endingTime - startingTime); }
}

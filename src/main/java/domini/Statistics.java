package domini;

import java.io.File;

public class Statistics {
    private long startingTime;
    private long endingTime;
    private long iniFileSize;
    private long finFileSize;

    public Statistics () {
        startingTime = 0;
        endingTime = 0;
        iniFileSize = 0;
        finFileSize = 0;
    }

    public void setStartingTime () {
        startingTime = System.currentTimeMillis();
    }

    public void setEndingTime () {
        endingTime = System.currentTimeMillis();
    }

    public void setIniFileSize (final String filename) {
        iniFileSize = new File(filename).length();
    }

    public void setFinFileSize(final String filename) {
        finFileSize = new File(filename).length();
    }

    public double getTime () {
        return (double)(endingTime - startingTime)/1000.0;
    }

    public long getIniFileSize() {
        return iniFileSize;
    }

    public long getFinFileSize() {
        return finFileSize;
    }

    public long getBytesCompressed () {
        return iniFileSize - finFileSize;
    }

    public long getBytesDecompressed () {
        return finFileSize - iniFileSize;
    }

    public double getPercentageCompressed () {
        return (double) getBytesCompressed()*100.0 / iniFileSize;
    }

    public double getPercentageDecompressed () {
        return (double) getBytesDecompressed()*100.0 / iniFileSize;
    }

    public double getSpeedCompressed () {
        return (double)iniFileSize / getTime();
    }

    public double getSpeedDecompressed () {
        return (double)finFileSize / getTime();
    }
}

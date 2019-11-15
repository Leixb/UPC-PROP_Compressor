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

    public void setIniFileSize (String filename) {
        iniFileSize = new File(filename).length();
    }

    public void setFinFileSize (String filename) {
        finFileSize = new File(filename).length();
    }

    public double getTime () {
        return (double)(endingTime - startingTime)/1000.0;
    }

    public double getIniFileSize() {
        return iniFileSize/1000;
    }

    public double getFinFileSize() {
        return finFileSize/1000;
    }

    public long getBytesCompressed () {
        return iniFileSize - finFileSize;
    }

    public long getBytesDecompressed () {
        return finFileSize - iniFileSize;
    }

    public double getPercentageCompressed () {
        return Math.round(10000.0 * (double) getBytesCompressed() / iniFileSize)/100.0;
    }

    public double getPercentageDecompressed () {
        return Math.round(10000.0 * (double) getBytesDecompressed() / iniFileSize)/100.0;
    }

    public double getSpeedCompressed () {
        return (double)iniFileSize / getTime() / 1000.0;
    }

    public double getSpeedDecompressed () {
        return (double)finFileSize / getTime() / 1000.0;
    }
}

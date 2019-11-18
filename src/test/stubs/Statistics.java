package domini;

public class Statistics {
    public Statistics () {
        System.out.printf("Llamada a Statistics()\n");
    }

    public void setStartingTime () {
        System.out.printf("Llamada a setStartingTime()\n");
    }
    public void setEndingTime () {
        System.out.printf("Llamada a setEndingTime()\n");
    }

    public void setIniFileSize (final String filename) {
        System.out.printf("Llamada a setIniFileSize(Strin filename=\"%s\")\n", filename);
    }

    public void setFinFileSize(final String filename) {
        System.out.printf("Llamada a setFinFileSize(Strin filename=\"%s\")\n", filename);
    }

    public double getTime () {
        System.out.printf("Llamada a getTime() -> 10.0\n");
        return 10.0;
    }

    public long getIniFileSize() {
        System.out.printf("Llamada a getIniFileSize() -> 500\n");
        return 500;
    }

    public long getFinFileSize() {
        System.out.printf("Llamada a getFinFileSize() -> 250\n");
        return 250;
    }

    public long getBytesCompressed () {
        System.out.printf("Llamada a getBytesCompressed() -> 100\n");
        return 100;
    }

    public long getBytesDecompressed () {
        System.out.printf("Llamada a getBytesDecompressed() -> 100\n");
        return 100;
    }

    public double getPercentageCompressed () {
        System.out.printf("Llamada a getPercentageCompressed() -> 0.785\n");
        return 78.5;
    }

    public double getPercentageDecompressed () {
        System.out.printf("Llamada a getPercentageDecompressed() -> 78.5\n");
        return 78.5;
    }

    public double getSpeedCompressed () {
        System.out.printf("Llamada a getSpeedCompressed() -> 505.34\n");
        return 505.34;
    }

    public double getSpeedDecompressed () {
        System.out.printf("Llamada a getSpeedDecompressed() -> 505.34\n");
        return 505.34;
    }
}

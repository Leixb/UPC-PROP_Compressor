package presentacio;

import java.text.DecimalFormat;

import domini.Statistics;

public class CtrlPresentacio {
    public static void printStatsCompress(long iniFileS, long finFileS, double speed, double time, double percentage) {
        String iniFileSize = readableFileSize(iniFileS);
        String finFileSize = readableFileSize(finFileS);
        String compSpeed = readableFileSize(speed);

        System.out.println("--- Compressió exitosa ---");
        System.out.println("--------- Stats ----------");
        System.out.printf("- Temps:     %.2fs\n", time);
        System.out.printf("- Desinflat: %s -> %s (%.2f%%)\n", iniFileSize, finFileSize, percentage);
        System.out.printf("- Velocitat: %sps\n", compSpeed);
        System.out.println("--------------------------");
    }

    public static void printStatsDecompress(long iniFileS, long finFileS, double speed, double time, double percentage) {
        String iniFileSize = readableFileSize(iniFileS);
        String finFileSize = readableFileSize(finFileS);
        String decompSpeed = readableFileSize(speed);

        System.out.println("--- Decompressió exitosa ---");
        System.out.println("---------- Stats -----------");
        System.out.printf("- Temps:     %.2fs\n", time);
        System.out.printf("- Inflat:    %s -> %s (%.2f%%)\n", iniFileSize, finFileSize, percentage);
        System.out.printf("- Velocitat: %s/s\n", decompSpeed);
        System.out.println("----------------------------");
    }

    public static String readableFileSize(double d) {
        if (d <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(d) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(d / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

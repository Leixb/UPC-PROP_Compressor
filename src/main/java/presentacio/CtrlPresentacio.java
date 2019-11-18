package presentacio;

import java.text.DecimalFormat;

import domini.Statistics;

/**
 * @author Albert Mercadé Plasencia / Aleix Boné
 * @brief Imprime por pantalla
 */

public class CtrlPresentacio {
    /**
     * @brief Imprime las estadisticas de compresión
     * @param stats Estadisticas generadas al comprimir
     */
    public static void printStatsCompress(Statistics stats) {
        String iniFileSize = readableFileSize(stats.getIniFileSize());
        String finFileSize = readableFileSize(stats.getFinFileSize());
        String compSpeed = readableFileSize(stats.getSpeedCompressed());

        System.out.println("--- Compressió exitosa ---");
        System.out.println("--------- Stats ----------");
        System.out.printf("- Temps:     %.2fs\n", stats.getTime());
        System.out.printf("- Desinflat: %s -> %s (%.2f%%)\n", iniFileSize, finFileSize, stats.getPercentageCompressed());
        System.out.printf("- Velocitat: %sps\n", compSpeed);
        System.out.println("--------------------------");
    }

    /**
     * @brief Imprime las estadisticas de descompresión
     * @param stats Estadisticas generadas al descomprimir
     */
    public static void printStatsDecompress(Statistics stats) {
        String iniFileSize = readableFileSize(stats.getIniFileSize());
        String finFileSize = readableFileSize(stats.getFinFileSize());
        String decompSpeed = readableFileSize(stats.getSpeedDecompressed());

        System.out.println("--- Decompressió exitosa ---");
        System.out.println("---------- Stats -----------");
        System.out.printf("- Temps:     %.2fs\n", stats.getTime());
        System.out.printf("- Inflat:    %s -> %s (%.2f%%)\n", iniFileSize, finFileSize, stats.getPercentageDecompressed());
        System.out.printf("- Velocitat: %s/s\n", decompSpeed);
        System.out.println("----------------------------");
    }

    /**
     * @brief Da el formato correcto al tamaño de un fichero (B,kB,etc)
     * @param d Tamaño de un fichero en bytes
     * @return Tamaño del fichero en la magnitud que le corresponda
     */
    public static String readableFileSize(double d) {
        if (d <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(d) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(d / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

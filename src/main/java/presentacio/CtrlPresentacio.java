/**
 * @file ./src/main/java/presentacio/CtrlPresentacio.java
 * @author Albert Mercadé Plasencia / Aleix Boné
*/
package presentacio;

import java.text.DecimalFormat;
import java.util.Scanner;

import domini.CtrlDomini;
import domini.Statistics;

/**
 * @brief Imprime por pantalla
 */
public class CtrlPresentacio {
    CtrlDomini cd = new CtrlDomini();

    /**
     * @brief Da a elegir al usuario el algoritmo para la compresión y le pide el nombre del archivo a comprimir y
     *        el nombre del archivo comprimido que luego pasa al CtrlDomini.
     */
    public void compress(int alg, String fileIn, String fileOut, short qualityJPEG) {
        try {
            cd.compress(alg, fileIn, fileOut, qualityJPEG);
        } catch (Exception e) {
            System.out.println("Error en la compressió:" + e.getMessage());
        }
    }

    /**
     * @brief Le pide al usuario el nombre del archivo comprimido y del archivo destino y se los pasa a CtrlDomini.
     */
    public void decompress(String fileIn, String fileOut) {
        try{
            cd.decompress(fileIn,fileOut);
        } catch (Exception e) {
            System.out.println("Error en la descompressió:" + e.getMessage());
        }
    }

    public String getTime() {
        return cd.getTime();
    }

    public String getInflated() {
        return cd.getInflated();
    }

    public String getSpeedCompress () {
        return cd.getSpeedCompress();
    }

    /**
     * @brief Imprime las estadisticas de compresión
     * @param stats Estadisticas generadas al comprimir
     */
    private static void printStatsCompress(Statistics stats) {
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
    private static void printStatsDecompress(Statistics stats) {
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
    private static String readableFileSize(double d) {
        if (d <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(d) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(d / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

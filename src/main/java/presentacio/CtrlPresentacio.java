package presentacio;

import java.text.DecimalFormat;
import java.util.Scanner;

import domini.CtrlDomini;
import domini.Statistics;

/**
 * @author Albert Mercadé Plasencia / Aleix Boné
 * @brief Imprime por pantalla
 */

public class CtrlPresentacio {
    private static String banner =
            "██████╗ ██╗███████╗     ██████╗ ██████╗ ███╗   ███╗██████╗ ██████╗ ███████╗███████╗███████╗ ██████╗ ██████╗ \n" +
            "██╔══██╗██║╚══███╔╝    ██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗██╔════╝██╔════╝██╔════╝██╔═══██╗██╔══██╗\n" +
            "██████╔╝██║  ███╔╝     ██║     ██║   ██║██╔████╔██║██████╔╝██████╔╝█████╗  ███████╗███████╗██║   ██║██████╔╝\n" +
            "██╔═══╝ ██║ ███╔╝      ██║     ██║   ██║██║╚██╔╝██║██╔═══╝ ██╔══██╗██╔══╝  ╚════██║╚════██║██║   ██║██╔══██╗\n" +
            "██║     ██║███████╗    ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║     ██║  ██║███████╗███████║███████║╚██████╔╝██║  ██║\n" +
            "╚═╝     ╚═╝╚══════╝     ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝\n" +
            "                                                                                                            \n";

    private static Scanner scanner;

    /**
     * @brief Dadas una opciones las imprime y da a escoger una de ellas
     * @param options opciones a presentar al usuario
     * @return Devuelve la opción elegida por el usuario
     */
    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Escull una opció (%d-%d): ", 1, options.length);
        System.out.println();
        return scanner.nextInt();
    }


    public static void presentacio() {
        scanner = new Scanner(System.in);

        System.out.println("BENVINGUT A");
        System.out.println(banner);

        System.out.println("Menú:");
        String[] options = {"Comprimir", "Descomprimir", "Sortir"};

        int action = prompt(options);

        while (action != 3) {

            if (action == 1) comprimir();
            else if (action == 2) descomprimir();
            else System.out.println("[ERROR] Opció no vàlida");
            System.out.println("Menú:");
            action = prompt(options);
        }
        System.out.println("Finalitzant execució");
        scanner.close();
    }

    /**
     * @brief Da a elegir al usuario el algoritmo para la compresión y le pide el nombre del archivo a comprimir y
     *        el nombre del archivo comprimido que luego pasa al CtrlDomini.
     */
    private static void comprimir() {
        int action;
        boolean invalid;
        do {
            System.out.println("Tipus de compressió:");
            String[] options = {"Automàtic", "LZ78", "LZSS", "LZW", "JPEG", "Menú"};

            action = prompt(options);
            invalid = (action < 1 || action > 6);
            if (invalid) System.out.println("[ERROR] Opció no vàlida");
        } while (invalid);

        if(action!=6) {
            System.out.println("Fitxer a comprimir:");
            scanner.nextLine();
            String fileIn = scanner.nextLine();

            System.out.println("Fitxer desti (*.piz):");
            String fileOut = scanner.nextLine();

            short quality = 0;

            CtrlDomini.Alg alg = null;
            if (action == 1) {
                if(fileIn.endsWith(".ppm")){
                    quality = 80; // auto JPEG qualitat 80.
                    alg = CtrlDomini.Alg.JPEGd;
                } else alg = CtrlDomini.Alg.LZ78d;
            } else if(action==2) alg = CtrlDomini.Alg.LZ78d;
            else if(action==3) alg = CtrlDomini.Alg.LZSSd;
            else if(action==4) alg = CtrlDomini.Alg.LZWd;
            else if(action==5) {
                alg = CtrlDomini.Alg.JPEGd;
                System.out.println("Qualitat [1-99]:");
                quality = scanner.nextShort();
            }

            try {
                Statistics stats = CtrlDomini.compress(alg, fileIn, fileOut, quality);
                printStatsCompress(stats);
            } catch (Exception e) {
                System.out.println("Error en la compressió:" + e.getMessage());
            }
        }
    }

    /**
     * @brief Le pide al usuario el nombre del archivo comprimido y del archivo destino y se los pasa a CtrlDomini.
     */
    private static void descomprimir() {
        System.out.println("Fitxer a descomprimir (*.piz):");
        scanner.nextLine();
        String fileIn = scanner.nextLine();

        System.out.println("Fitxer desti:");
        String fileOut = scanner.nextLine();

        try{
            Statistics stats = CtrlDomini.decompress(fileIn,fileOut);
            CtrlPresentacio.printStatsDecompress(stats);
        } catch (Exception e) {
            System.out.println("Error en la descompressió:" + e.getMessage());
        }
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

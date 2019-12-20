/**
 * @file ./src/main/java/presentacio/Main.java
 * @author ***REMOVED***
 */
package presentacio;

import domini.CtrlDomini;

import java.io.IOException;
import java.util.Scanner;

/**
 * @brief Recibe todos los inputs necesarios del usuario para la compresión/descompresión
 */

public class Main {

    private static String banner =
            "██████╗ ██╗███████╗     ██████╗ ██████╗ ███╗   ███╗██████╗ ██████╗ ███████╗███████╗███████╗ ██████╗ ██████╗ \n" +
            "██╔══██╗██║╚══███╔╝    ██╔════╝██╔═══██╗████╗ ████║██╔══██╗██╔══██╗██╔════╝██╔════╝██╔════╝██╔═══██╗██╔══██╗\n" +
            "██████╔╝██║  ███╔╝     ██║     ██║   ██║██╔████╔██║██████╔╝██████╔╝█████╗  ███████╗███████╗██║   ██║██████╔╝\n" +
            "██╔═══╝ ██║ ███╔╝      ██║     ██║   ██║██║╚██╔╝██║██╔═══╝ ██╔══██╗██╔══╝  ╚════██║╚════██║██║   ██║██╔══██╗\n" +
            "██║     ██║███████╗    ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║     ██║  ██║███████╗███████║███████║╚██████╔╝██║  ██║\n" +
            "╚═╝     ╚═╝╚══════╝     ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝\n" +
            "                                                                                                            \n";

    private static Scanner scanner;

    private static CtrlDomini cd;

    /**
     * @brief Dadas una opciones las imprime y da a escoger una de ellas
     * @param options opciones a presentar al usuario
     * @return Devuelve la opción elegida por el usuario
     */
    public static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Escull una opció (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }


    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        cd = CtrlDomini.getInstance();

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

        if (action!=6) {
            System.out.println("Fitxer a comprimir:");
            scanner.nextLine();
            String fileIn = scanner.nextLine();

            System.out.println("Fitxer desti (*.piz):");
            String fileOut = scanner.nextLine();

            short quality = 0;

            if(action==5) {
                System.out.println("Qualitat [1-99]:");
                quality = scanner.nextShort();
            }

            try {
                cd.compress(action, fileIn, fileOut, quality);
                printStatsCompress();
            } catch (Exception e) {
                System.out.println("Error en la compressió:" + e.getMessage());
            }
            promptObrirFitxer(fileOut);
        }

    }

    /**
     * @throws IOException
     * @brief Le pide al usuario el nombre del archivo comprimido y del archivo destino y se los pasa a CtrlDomini.
     */
    private static void descomprimir() {
        System.out.println("Fitxer a descomprimir (*.piz):");
        scanner.nextLine();
        String fileIn = scanner.nextLine();

        System.out.println("Fitxer desti:");
        String fileOut = scanner.nextLine();

        try{
            cd.decompress(fileIn,fileOut);
            printStatsDecompress();
        } catch (Exception e) {
            System.out.println("Error en la descompressió:" + e.getMessage());
        }

        promptObrirFitxer(fileOut);

    }

    private static void promptObrirFitxer(String filename) {
        try {
            for(;;) {
                System.out.print("Obrir fitxer?: [S/N] ");
                String option = scanner.nextLine();

                if (option.charAt(0) == 'N') return;
                else if (option.charAt(0) == 'S') {
                    if(filename.endsWith(".piz")) filename = filename.substring(0, filename.lastIndexOf('/') + 1);
                    Runtime.getRuntime().exec("xdg-open " + filename);
                    return;
                }

                System.out.println("[ERROR] Opció no vàlida");
            }
        } catch (IOException e) {
            System.out.println("Error en la opertura de fitxer:" + e.getMessage());
        }
    }

    /**
     * @brief Imprime las estadisticas de compresión
     */
    public static void printStatsCompress() {
        System.out.println("--- Compressió exitosa ---");
        System.out.println("--------- Stats ----------");
        System.out.println("- Temps:     " + cd.getTime());
        System.out.println("- Desinflat: " + cd.getDeflated());
        System.out.println("- Velocitat: " + cd.getSpeedCompress());
        System.out.println("--------------------------");
    }

    /**
     * @brief Imprime las estadisticas de descompresión
     * @param stats Estadisticas generadas al descomprimir
     */
    public static void printStatsDecompress() {
        System.out.println("--- Decompressió exitosa ---");
        System.out.println("---------- Stats -----------");
        System.out.println("- Temps:     " + cd.getTime());
        System.out.println("- Desinflat: " + cd.getInflated());
        System.out.println("- Velocitat: " + cd.getSpeedDecompress());
        System.out.println("----------------------------");
    }
}

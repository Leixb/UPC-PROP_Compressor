/**
 * @file ./src/main/java/presentacio/Main.java
 * @author Albert Mercadé Plasencia / Alex Herrero Pons / Aleix Boné
*/
package presentacio;

import domini.CtrlDomini;
import domini.Statistics;

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
                CtrlPresentacio.printStatsCompress(stats);
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
}

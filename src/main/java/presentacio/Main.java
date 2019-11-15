package presentacio;

import domini.CtrlDomini;

import java.io.IOException;
import java.util.Scanner;

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

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("BENVINGUT A");
        System.out.println(banner);
        System.out.println("[1] Test");
        System.out.println("[2] Normal");
        short opt = scanner.nextShort();

        if(opt == 1) testMain();
        else if(opt == 2) normalMain();
        else quit();
    }

    private static void testMain() {
        System.out.println("[1] LZ78");
        System.out.println("[2] LZSS");
        System.out.println("[3] LZW");
        System.out.println("[4] Domini.JPEG");
        short opt = scanner.nextShort();

        switch (opt) {
            case 1:
                testLZ78();
                break;
            case 2:
                testLZSS();
                break;
            case 3:
                testLZW();
                break;
            case 4:
                testJPEG();
                break;
            default:
                quit();
        }
    }

    private static void normalMain() {
        System.out.println("Seleccioneu l'opció desitjada:");
        System.out.println("[1] Comprimir");
        System.out.println("[2] Descomprimir");
        short opt = scanner.nextShort();

        if(opt == 1) comprimir();
        else if (opt == 2) descomprimir();
        else quit();
    }

    private static void quit() {
        System.out.println("Aborted");
        System.exit(-1);
    }

    private static void comprimir() {
        System.out.println("Tipus de compressió:");
        System.out.println("[1] Automàtic");
        System.out.println("[2] LZ78");
        System.out.println("[3] LZSS");
        System.out.println("[4] LZW");
        System.out.println("[5] JPEG");
        short opt = scanner.nextShort();

        System.out.println("Fitxer a comprimir:");
        scanner.nextLine();
        String fileIn = scanner.nextLine();

        System.out.println("Fitxer desti (*.piz):");
        String fileOut = scanner.nextLine();

        short quality = 0;

        CtrlDomini.Alg alg = CtrlDomini.Alg.AUTOd;
        if(opt==2) alg = CtrlDomini.Alg.LZ78d;
        else if(opt==3) alg = CtrlDomini.Alg.LZSSd;
        else if(opt==4) alg = CtrlDomini.Alg.LZWd;
        else if(opt==5) {
            alg = CtrlDomini.Alg.JPEGd;
            System.out.println("Amb quina qualitat vol que es comprimeixi [1-99]?");
            quality = scanner.nextShort();
        }

        try{
            CtrlDomini.compress(alg,fileIn,fileOut,quality);
        } catch (Exception e) {
            System.out.println("Error en la compressió.");
        }
    }

    private static void descomprimir() {
        System.out.println("Fitxer a descomprimir:");
        scanner.nextLine();
        String fileIn = scanner.nextLine();

        System.out.println("Fitxer desti:");
        String fileOut = scanner.nextLine();

        try{
            CtrlDomini.decompress(fileIn,fileOut);
        } catch (Exception e) {
            System.out.println("Error en la compressió.");
            e.printStackTrace();
        }
    }

    private static void testLZ78() {}
    private static void testLZSS() {}
    private static void testLZW() {}
    private static void testJPEG() {}
}

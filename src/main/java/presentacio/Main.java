package presentacio;

import java.util.Scanner;

import domini.JPEG;

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
        System.out.println("[4] JPEG");
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

        System.out.println("Introdueix el nom del fitxer:");
        scanner.nextLine();
        String filename = scanner.nextLine();

        //TODO
        switch (opt) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                quit();
        }
    }

    private static void descomprimir() {
        System.out.println("Introdueix el nom del fitxer:");
        String filename = scanner.nextLine();

        // TODO
    }

    private static void testLZ78() {}
    private static void testLZSS() {}
    private static void testLZW() {}
    private static void testJPEG() {
        try {
            JPEG.compress("images/boat.ppm", "boat.out", (short) 90);
            JPEG.decompress("boat.out", "boat_rec.ppm", (short) 90);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

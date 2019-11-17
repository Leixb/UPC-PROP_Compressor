import domini.LZW;

import java.util.Scanner;

public class DriverLZW {

    private static Scanner scanner;

    private static void DriverComprimir () {
        String input;
        String output;
        LZW.compress(input,output);
    }

    private static void DriverDescomprimir  () {
        String input;
        String output;
        LZW.decompress(input,output);
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Driver de LZW.");
            System.out.println("[1] Comprimir");
            System.out.println("[2] Descomprimir");
            System.out.println("[3] Sortir");
            short opt = scanner.nextShort();

            if (opt == 1) DriverComprimir();
            else if (opt == 2) DriverDescomprimir();
            else if (opt == 3) bye();
            else invalidOpt();
        }
    }

    private static void bye() {
        System.out.println("Fins aviat! Finalitzant execució...");
        System.exit(-1);
    }

    private static void invalidOpt() {
        System.out.println("Opció invàlida. Esculli una opció del 1 al 3.");
    }
}

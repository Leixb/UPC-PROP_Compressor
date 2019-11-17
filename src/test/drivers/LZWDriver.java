import domini.LZW;

import java.util.Scanner;

public class LZWDriver {

    private static Scanner scanner;

    private static void testCompress () {
        System.out.print("Input file (file to compress): ");
        String inputFile = scanner.next();
        System.out.print("Output file: ");
        String outputFile = scanner.next();

        try {
            LZW.compress(inputFile,outputFile);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("LZW compress failed!");
            e.printStackTrace();
        }
    }

    private static void testDecompress  () {
        System.out.print("Input file (file to decompress): ");
        String inputFile = scanner.next();
        System.out.print("Output file: ");
        String outputFile = scanner.next();

        try {
            LZW.decompress(inputFile,outputFile);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("LZW decompress failed!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Driver de LZW.");
            System.out.println("[1] Compress");
            System.out.println("[2] Decompress");
            System.out.println("[3] Exit");
            short opt = scanner.nextShort();

            if (opt == 1) testCompress();
            else if (opt == 2) testDecompress();
            else if (opt == 3) bye();
            else invalidOpt();
        }
    }

    private static void bye() {
        System.out.println("Fins aviat! Finalitzant execució...");
        System.exit(-1);
    }

    private static void invalidOpt() {
        System.out.println("Invalid option. Choose an option from 1 to 3.");
    }
}

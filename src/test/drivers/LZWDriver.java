import domini.LZW;

import java.util.Scanner;

class LZWDriver {
    private static Scanner scanner;

    public static void testCompress () {
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

    public static void testDecompress  () {
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

    public static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {"compress", "decompress", "exit"};

        int action = prompt(options);

        while (action != 3) {

            if (action == 1) testCompress();
            else if (action == 2) testDecompress();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

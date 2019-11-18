import domini.LZ78;

import java.util.Scanner;

class LZ78Driver {
    private static Scanner scanner;

    public static void testCompress() {
        System.out.print("Input file to compress: ");
        String inputFilename = scanner.next();
        System.out.print("Output file compressed: ");
        String outputFilename = scanner.next();

        try {
            LZ78.compress(inputFilename,outputFilename);
            System.out.println("Successful compression!");
        } catch (Exception e) {
            System.out.println("LZ78 compress failed!");
            e.printStackTrace();
        }
    }

    public static void testDecompress() {
        System.out.print("Input file to decompress: ");
        String inputFilename = scanner.next();
        System.out.print("Output file decompressed: ");
        String outputFilename = scanner.next();

        try {
            LZ78.decompress(inputFilename, outputFilename);
            System.out.println("Successful decompression");
        } catch (Exception e) {
            System.out.println("LZ78 decompress failed!");
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

        String[] options = {"Compress", "Decompress", "Exit"};

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

import domini.JPEG;

import java.util.Scanner;

class JPEGDriver {
    private static Scanner scanner;

    private static JPEG jpeg;

    private static void testConstructor(){
        String outputFilename = scanner.next();
        System.out.print("Quality: ");
        short quality = scanner.nextShort();
        jpeg = new JPEG(quality);
        System.out.println("DONE");
    }

    private static void testCompress() {
        System.out.print("Input file to compress: ");
        String inputFilename = scanner.next();
        System.out.print("Output file compressed: ");

        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename);
             IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            jpeg.compress(input, output);
            System.out.println("Successful compression!");
        } catch (Exception e) {
            System.out.println("JPEG compress failed!");
            e.printStackTrace();
        }
    }

    private static void testDecompress() {
        System.out.print("Input file (file to decompress): ");
        String inputFile = scanner.next();
        System.out.print("Output file: ");
        String outputFile = scanner.next();

        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
             IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
            jpeg.decompress(input, output);
        } catch (Exception e) {
            System.out.println("JPEG decompress failed!");
            e.printStackTrace();
        }
    }

    private static void testGetMagicByte() {
        System.out.print("getMagicByte: " + jpeg.getMagicByte());
    }

    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {"compress", "decompress", "getMagicByte", "exit"};

        int action = prompt(options);

        while (action != 4) {

            if (action == 1) testCompress();
            else if (action == 2) testDecompress();
            else if (action == 3) testGetMagicByte();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

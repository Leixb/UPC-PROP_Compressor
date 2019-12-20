import domini.LZSS;
import persistencia.IO;

import java.util.Scanner;

class LZSSDriver {
    private static Scanner scanner;

    private static LZSS lzss;

    private static void testConstructor(){
        lzss = new LZSS();
        System.out.println("DONE");
    }
    private static void testCompress() {
        System.out.print("Input file to compress: ");
        String inputFilename = scanner.next();
        System.out.print("Output file compressed: ");
        String outputFilename = scanner.next();

        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename);
             IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            lzss.compress(input, output);
            System.out.println("Successful compression!");
        } catch (Exception e) {
            System.out.println("LZSS compress failed!");
            e.printStackTrace();
        }
    }

    private static void testDecompress() {
        System.out.print("Input file to decompress: ");
        String inputFilename = scanner.next();
        System.out.print("Output file decompressed: ");
        String outputFilename = scanner.next();

        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
             IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
            lzss.decompress(input, output);
            System.out.println("Successful decompression");
        } catch (Exception e) {
            System.out.println("LZSS decompress failed!");
            e.printStackTrace();
        }
    }

    private static void testGetMagicByte() {
        System.out.print("getMagicByte: " + lzss.getMagicByte());
    }

    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i - 1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {"constructor", "compress", "decompress", "getMagicByte", "exit"};

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) testConstructor();
            else if (action == 2) testCompress();
            else if (action == 3) testDecompress();
            else if (action == 4) testGetMagicByte();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

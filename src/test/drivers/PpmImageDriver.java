import persistencia.PpmImage;
import persistencia.IO;

import java.util.Scanner;
import java.io.IOException;

class PpmImageDriver {
    private static Scanner scanner;

    private static PpmImage.Reader imgReader;
    private static PpmImage.Writer imgWriter;

    public static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void testReaderReadBlock() {
        try {
            byte[][][] block = imgReader.readBlock();

            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    for (int chan = 0; chan < 3; ++chan) {
                        System.out.printf("%02x ", block[i][j][chan]);
                    }
                    System.out.println();
                }
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }
    public static void testReaderWidthBlocks() {
        System.out.printf("int: %d\n", imgReader.widthBlocks());
    }
    public static void testReaderHeightBlocks() {
        System.out.printf("int: %d\n", imgReader.heightBlocks());
    }

    public static void testReader() {
        System.out.print("Input file to read: ");
        String filename = scanner.next();

        String[] options = {
            "readBlock",
            "widthBlocks",
            "heightBlocks"
        };

        try (IO.Byte.reader input = new IO.Byte.reader(filename)) {
            imgReader = new PpmImage.Reader(input);

            int action = prompt(options);

            while (action != options.length) {

                if (action == 1) testReaderReadBlock();
                else if (action == 2) testReaderWidthBlocks();
                else if (action == 3) testReaderHeightBlocks();
                else System.out.println("Invalid option");

                action = prompt(options);
            }
        } catch (IOException e) {
            System.out.println("FAILED");
            e.printStackTrace();
        } finally {
            imgReader = null;
        }
    }

    public static void testWriterWriteBlock() {
        byte[][][] block = new byte[8][8][3];
        try {
            System.out.print("Enter byte to fill block (hex): ");
            byte b = scanner.nextByte(16);
            for (int i = 0; i < 8; ++i)
                for (int j = 0; j < 8; ++j)
                    for (int chan = 0; chan < 3; ++chan)
                        block[i][j][chan] = b;

            imgWriter.writeBlock(block);
        } catch (IOException e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }
    public static void testWriterWidthBlocks() {
        System.out.printf("int: %d\n", imgWriter.widthBlocks());
    }
    public static void testWriterHeightBlocks() {
        System.out.printf("int: %d\n", imgWriter.heightBlocks());
    }

    public static void testWriter() {
        System.out.print("Input file to write: ");
        String filename = scanner.next();

        System.out.print("width: ");
        int width = scanner.nextInt();
        System.out.print("height: ");
        int height = scanner.nextInt();

        String[] options = {
            "writeBlock",
            "widthBlocks",
            "heightBlocks"
        };

        try (IO.Byte.writer output = new IO.Byte.writer(filename)) {
            imgWriter = new PpmImage.Writer(output, width, height);

            int action = prompt(options);

            while (action != options.length) {

                if (action == 1) testWriterWriteBlock();
                else if (action == 2) testWriterWidthBlocks();
                else if (action == 3) testWriterHeightBlocks();
                else System.out.println("Invalid option");

                action = prompt(options);
            }

        } catch (IOException e) {
            System.out.println("FAILED");
            e.printStackTrace();
        } finally {
            imgWriter = null;
        }
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {
            "Reader",
            "Writer",
            "exit"
        };

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) testReader();
            else if (action == 2) testWriter();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

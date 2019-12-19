import persistencia.PpmImage;

import java.io.File;
import java.util.Scanner;

class PpmImageDriver {
    private static Scanner scanner;

    private static PpmImage image;

    private static void testReaderConstructor() {
        System.out.print("Filename: ");
        String filename = scanner.next();
        IO.Byte.reader file = IO.Byte.reader(filename);

        try {
            image.Reader = new PpmImage.Reader(file);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testReadBlock() {
        try {
            byte[][][] block = image.Reader.readBlock();
            System.out.println("readBlock: " + block);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testGetWidth() {
        System.out.println("getWidth: " + image.Reader.getWidth());
        System.out.println("DONE");
    }

    private static void testGetHeight() {
        System.out.println("getHeight: " + image.Reader.getHeight());
        System.out.println("DONE");
    }

    private static void testReaderWidhtBlocks() {
        System.out.println("Reader widthBlocks: " + image.Reader.widthBlocks());
        System.out.println("DONE");
    }

    private static void testReaderHeightBlocks() {
        System.out.println("Reader heightBlocks: " + image.Reader.heightBlocks());
        System.out.println("DONE");
    }

    private static void reader() {
        String[] options = {
                "constructor",
                "readBlock",
                "getWidth",
                "getHeight",
                "widthBlocks",
                "heightBlocks"
        };

        int action = prompt(options);
        while (action<1 || action>6){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testReaderConstructor();
        else if (action == 2) testReadBlock();
        else if (action == 3) testGetWidth();
        else if (action == 4) testGetHeight();
        else if (action == 5) testReaderWidhtBlocks();
        else if (action == 6) testReaderHeightBlocks();
    }

    private static void testWriterConstructor() {
        System.out.print("Filename: ");
        String filename = scanner.next();
        IO.Byte.writer file = new IO.Byte.writer(filename);
        System.out.print("Height: ");
        int height = scanner.next();
        System.out.print("Width: ");
        int width = scanner.next();

        try {
            image.Writer = new PpmImage.Writer(file,height,width);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriteBlock() {
        System.out.print("Block: ");
        byte[][][] block = scanner.next();
        try {
            image.Writer.writeBlock(block);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriterWidthBlocks() {
        System.out.println("Writer widthBlocks: " + image.Writer.widthBlocks());
        System.out.println("DONE");
    }

    private static void testWriterHeightBlocks() {
        System.out.println("Writer heightBlocks: " + image.Writer.heightBlocks());
        System.out.println("DONE");
    }

    private static void writer() {
        String[] options = {
                "constructor",
                "writeBlock",
                "widthBlocks",
                "heightBlocks"
        };

        int action = propmt(options);
        while (action<1 || action>4){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testWriterConstructor();
        else if (action == 2) testWriteBlock();
        else if (action == 3) testWriterWidthBlocks();
        else if (action == 4) testWriterHeightBlocks();
    }

    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {
            "reader",
            "writer",
            "exit"
        };

        image = new PpmImage();

        int action = prompt(options);

        while (action != 3) {

            if (action == 1) reader();
            else if (action == 2) writer();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

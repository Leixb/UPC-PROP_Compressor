import persistencia.IO;
import domini.BitSetL;

import java.util.Scanner;
import java.io.IOException;

class IODriver{
    private static Scanner scanner;

    private static IO io;

    //Char
    public static void testCharReader() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Char.reader = new IO.Char.reader.reader(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testCharWriter() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Char.writer = new IO.Char.writer.writer(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    //Byte
    public static void testByteReader() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Byte.reader = new IO.Byte.reader.reader(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testByteWriter() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Byte.writer = new IO.Byte.writer.writer(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    //Bit
    private static void testBitReader() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Bit.reader = new IO.Bit.reader.reader(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testRead() {
        try {
            System.out.println("read: " + io.Bit.reader.read());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testReadByte() {
        try {
            System.out.println("readByte: " + io.Bit.reader.readByte());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testReadChar() {
        try {
            System.out.println("readChar: " + io.Bit.reader.readChar());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testReadInt() {
        try {
            System.out.println("readInt: " + io.Bit.reader.readInt());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testReadBitSet() {
        System.out.print("Length: ");
        int length = scanner.nextInt();

        try {
            BitSetL bs = io.Bit.reader.readBitSet(length);
            System.out.println("read: " + bs.toString());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void bitReader(){
        String[] options = {
                "creadora",
                "readBit"
                "readByte"
                "readChar"
                "readInt"
                "readBitSet"
                "close"
        };

        int action = prompt(options);
        while (action<1 || action>7){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testBitReader();
        else if (action == 2) testRead();
        else if (action == 3) testReadByte();
        else if (action == 4) testReadChar();
        else if (action == 5) testReadInt();
        else if (action == 6) testReadBitSet();
        else if (action == 7) testReaderClose();
    }

    private static void testBitWriter() {
        System.out.print("Filename: ");
        String filename = scanner.next();

        try {
            io.Bit.writer = new IO.Bit.writer.writer(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWrite() {
        System.out.print("Bit: ");
        boolean b = scanner.nextInt() != 0;
        try {
            io.Bit.writer.write(b);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriteByte() {
        System.out.print("Byte: ");
        byte b = scanner.next();
        try {
            io.Bit.writer.write(b);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriteChar() {
        System.out.print("Char: ");
        char c = scanner.next();
        try {
            io.Bit.writer.write(c);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriteInt() {
        System.out.print("Int: ");
        int i = scanner.next();
        try {
            io.Bit.writer.write(i);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void testWriteBitSet() {
        System.out.print("Bitset to write: ");
        BitSetL bs = new BitSetL(scanner.next());
        try {
            io.Bit.writer.write(bs);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    private static void bitWriter(){
        String[] options = {
                "creadora",
                "writeBit"
                "writeByte"
                "writeChar"
                "writeInt"
                "writeBitSet"
                "close"
        };

        int action = prompt(options);
        while (action<1 || action>7){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testBitWriter();
        else if (action == 2) testWrite();
        else if (action == 3) testWriteByte();
        else if (action == 4) testWriteChar();
        else if (action == 5) testWriteInt();
        else if (action == 6) testWriteBitSet();
        else if (action == 7) testWriterClose();
    }

    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options1 = {
            "Char",
            "Byte",
            "Bit",
            "exit"
        };

        int action = prompt(options1);

        while (action != 4) {
            String[] options2 = {
                    "Reader",
                    "Writer"
            };

            int action2 = prompt(options2);
            while (action2<1 || action2>2){
                System.out.println("Invalid option");
                action2 = prompt(options2);
            }

            if (action == 1){
                if (action2 == 1) testCharReader();
                else (action2 == 2) testCharWriter();
            } else if (action == 2){
                if (action2 == 1) testByteReader();
                else (action2 == 2) testByteWriter();
            } else if (action==3){
                if (action2 == 1) bitReader();
                else (action2 == 2) bitWriter();
            } else System.out.println("Invalid option");
            action = prompt(options1);
        }

        scanner.close();
    }
}

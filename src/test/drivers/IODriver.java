import persistencia.IO;
import domini.BitSetL;

import java.util.Scanner;
import java.io.IOException;

class IODriver{

    private static Scanner scanner;

    //Char
    public static void testCharRead() {
        System.out.print("Input file to read a char: ");
        String inputFilename = scanner.next();

        String[] options = {"read", "back"};
        try (IO.Char.reader input = new IO.Char.reader(inputFilename)) {
            while (prompt(options) != 2) {
                System.out.printf("byte: %c\n", input.read());
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testCharWrite() {
        System.out.print("Input file to write a char: ");
        String outputFilename = scanner.next();

        String[] options = {"write", "back"};
        try (IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            while (prompt(options) != 2) {
                System.out.print("Input a char to write: ");
                char test = scanner.next().charAt(0);
                output.write(test);
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    //Byte
    public static void testByteRead() {
        System.out.print("Input file to read a byte: ");
        String inputFilename = scanner.next();

        String[] options = {"read", "back"};
        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename)) {
            while (prompt(options) != 2) {
                System.out.printf("byte: %x\n", input.read());
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testByteWrite(){
        System.out.print("Input file to write a byte: ");
        String outputFilename = scanner.next();
        String[] options = {"write", "back"};
        try (IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
            while (prompt(options) != 2) {
                System.out.print("Input byte to write (hex): ");
                byte test = scanner.nextByte(16);
                output.write(test);
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testBitReader() {
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();

        String[] options = {"read", "readByte", "readChar", "readInt", "readBitSet", "back"};

        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)) {

            int action = prompt(options);

            while (action != options.length) {
                if (action == 1) testBitReaderRead(input);
                if (action == 2) testBitReaderReadByte(input);
                if (action == 3) testBitReaderReadChar(input);
                if (action == 4) testBitReaderReadInt(input);
                if (action == 5) testBitReaderReadBitSet(input);
                else System.out.println("Invalid option");

                action = prompt(options);
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testBitReaderRead(IO.Bit.reader input) throws IOException {
        System.out.printf("read bool: %b\n", input.read());
    }
    public static void testBitReaderReadByte(IO.Bit.reader input) throws IOException {
        System.out.printf("read byte: %x\n", input.readByte());
    }
    public static void testBitReaderReadChar(IO.Bit.reader input) throws IOException {
        System.out.printf("read char: %c\n", input.readChar());
    }
    public static void testBitReaderReadInt(IO.Bit.reader input) throws IOException {
        System.out.printf("read int: %d\n", input.readInt());
    }
    public static void testBitReaderReadBitSet(IO.Bit.reader input) throws IOException {
        System.out.print("Length: ");
        int length = scanner.nextInt();

        BitSetL bs = input.readBitSet(length);

        System.out.printf("read BitSet: %s\n", bs.toString());
    }

    public static void testBitWriter() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();

        String[] options = {"write(boolean)", "write(byte)", "write(char)",
            "write(int)", "write(BitSetL)", "back"};

        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {

            int action = prompt(options);

            while (action != options.length) {
                if (action == 1) testBitWriterBool(output);
                if (action == 2) testBitWriterByte(output);
                if (action == 3) testBitWriterChar(output);
                if (action == 4) testBitWriterInt(output);
                if (action == 5) testBitWriterBitSetL(output);
                else System.out.println("Invalid option");

                action = prompt(options);
            }
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testBitWriterBool(IO.Bit.writer output) throws IOException {
        System.out.print("Bit to write: ");
        output.write(scanner.nextInt() != 0);
    }
    public static void testBitWriterByte(IO.Bit.writer output) throws IOException {
        System.out.print("Byte to write: ");
        output.write(scanner.nextByte());
    }
    public static void testBitWriterChar(IO.Bit.writer output) throws IOException {
        System.out.print("Char to write: ");
        output.write(scanner.next().charAt(0));
    }
    public static void testBitWriterInt(IO.Bit.writer output) throws IOException {
        System.out.print("Int to write: ");
        output.write(scanner.nextInt());
    }
    public static void testBitWriterBitSetL(IO.Bit.writer output) throws IOException {
        System.out.print("Bitset to write: ");
        BitSetL bs = new BitSetL(scanner.next());
        output.write(bs);
    }

    public static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options1 = {"Char", "Byte", "Bit", "exit"};

        int action = prompt(options1);

        while (action != options1.length) {

            if (action == 1){
                String[] options2 = {"Read", "Write", "back"};
                int action2 = prompt(options2);
                while (action2 != options2.length){
                    if (action2==1) testCharRead();
                    else if (action2==2) testCharWrite();
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            } else if (action == 2){
                String[] options2 = {"Read", "Write", "back"};
                int action2 = prompt(options2);
                while (action2 != options2.length){
                    if (action2==1) testByteRead();
                    else if (action2==2) testByteWrite();
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            } else if (action==3){
                String[] options2 = {"Read", "Write", "back"};
                int action2 = prompt(options2);
                while (action2 != options2.length){
                    if (action2==1) testBitReader();
                    else if (action2==2) testBitWriter();
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            } else System.out.println("Invalid option");
            action = prompt(options1);
        }

        scanner.close();
    }
}

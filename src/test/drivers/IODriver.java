import domini.IO;
import domini.BitSetL;

import java.util.Scanner;

class IODriver{

    private static Scanner scanner;

    //Char
    public static void testCharRead() {
        System.out.print("Input file to read a char: ");
        String inputFilename = scanner.next();
        try (IO.Char.reader input = new IO.Char.reader(inputFilename)){
            System.out.println((char) input.read());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("File Not Found!");
            e.printStackTrace();
        }
    }

    public static void testCharWrite() {
        System.out.print("Input file to write a char: ");
        String outputFilename = scanner.next();
        try (IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            System.out.print("Input a char to write: ");
            char test = scanner.next().charAt(0);
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }


    //Byte
    public static void testByteRead() {
        System.out.print("Input file to read a byte: ");
        String inputFilename = scanner.next();
        try (IO.Byte.reader input = new IO.Byte.reader(inputFilename)){
            System.out.println((byte) input.read());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("File Not Found!");
            e.printStackTrace();
        }
    }

    public static void testByteWrite(){
        System.out.print("Input file to write a byte: ");
        String outputFilename = scanner.next();
        try (IO.Byte.writer output = new IO.Byte.writer(outputFilename)) {
            System.out.print("Input a byte to write: ");
            byte test = scanner.nextByte();
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }


    //Bit
    public static void testBitReadReader() {
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)) {
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadRead() {
        System.out.print("Input file to read a bit: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            System.out.println(input.read());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadReadByte(){
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            System.out.println(input.readByte());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadChar(){
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            System.out.println(input.readChar());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadInt(){
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            System.out.println(input.readInt());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadBitSet(){
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            System.out.print("Input the number of bits you want to read: ");
            int n_bits = scanner.nextInt();
            System.out.println(input.readBitSet(n_bits));
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitReadClose(){
        System.out.print("Input file to read: ");
        String inputFilename = scanner.next();
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename)){
            input.close();
            System.out.println("DONE");
        }catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }


    public static void testBitWriteWriter() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("File Not Found!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteBit() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.print("Input a bit to write: ");
            boolean test = scanner.nextBoolean();
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteBitSetL() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.print("Input an integer number and the number of bits you want it to be codified: ");
            int int_test = scanner.nextInt();
            int bits_test = scanner.nextInt();
            BitSetL bs = new BitSetL (int_test, bits_test);
            output.write(bs);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteByte() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.print("Input a byte to write: ");
            byte test = scanner.nextByte();
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteChar() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.print("Input a char to write: ");
            char test = scanner.next().charAt(0);
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteInt() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            System.out.print("Input an int to write: ");
            int test = scanner.nextInt();
            output.write(test);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
            e.printStackTrace();
        }
    }

    public static void testBitWriteClose() {
        System.out.print("Input file to write: ");
        String outputFilename = scanner.next();
        try (IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            output.close();
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("Read / Write Fail!");
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

        String[] options1 = {"Char", "Byte", "Bit", "exit"};

        int action = prompt(options1);

        while (action != 4) {

            if (action == 1){
                scanner = new Scanner(System.in);
                String[] options2 = {"Read", "Write", "exit"};
                int action2 = prompt(options2);
                while (action2!=3){
                    if (action2==1) testCharRead();
                    else if (action2==2) testCharWrite();
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            }
            else if (action == 2){
                scanner = new Scanner(System.in);
                String[] options2 = {"Read", "Write", "exit"};
                int action2 = prompt(options2);
                while (action2!=3){
                    if (action2==1) testByteRead();
                    else if (action2==2) testByteWrite();
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            }
            else if (action==3){
                scanner = new Scanner(System.in);
                String[] options2 = {"Read", "Write", "exit"};
                int action2 = prompt(options2);
                while (action2!=3){
                    if (action2==1) {
                        scanner = new Scanner(System.in);
                        String[] options3 = {"Reader", "Read", "ReadByte", "ReadChar", "ReadInt",
                                "ReadBitSet", "Close", "exit"};
                        int action3 = prompt(options3);
                        while (action3!=8){
                            if (action3==1) testBitReadReader();
                            else if (action3==2) testBitReadRead();
                            else if (action3==3) testBitReadReadByte();
                            else if (action3==4) testBitReadChar();
                            else if (action3==5) testBitReadInt();
                            else if (action3==6) testBitReadBitSet();
                            else if (action3==7) testBitReadClose();
                            else System.out.println("Invalid option");
                            action3 = prompt(options3);
                        }
                    }
                    else if (action2==2){
                        scanner = new Scanner(System.in);
                        String[] options3 = {"Writer", "WriteBit", "WriteBitSetL", "WriteByte", "WriteChar", "WriteInt",
                                "Close", "exit"};
                        int action3 = prompt(options3);
                        while (action3!=8){
                            if (action3==1) testBitWriteWriter();
                            else if (action3==2) testBitWriteBit();
                            else if (action3==3) testBitWriteBitSetL();
                            else if (action3==4) testBitWriteByte();
                            else if (action3==5) testBitWriteChar();
                            else if (action3==6) testBitWriteInt();
                            else if (action3==7) testBitWriteClose();
                            else System.out.println("Invalid option");
                            action3 = prompt(options3);
                        }
                    }
                    else System.out.println("Invalid option");
                    action2 = prompt(options2);
                }
            }
            else System.out.println("Invalid option");
            action = prompt(options1);
        }

        scanner.close();
    }
}
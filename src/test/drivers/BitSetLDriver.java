import domini.BitSetL;

import java.util.Scanner;

class BitSetLDriver {

    private static BitSetL bitSetL;

    private static Scanner scanner;

    private static void testEmptyConstructor () {
        bitSetL = new BitSetL();
        System.out.println("DONE");
    }

    private static void testLengthConstructor () {
        System.out.print("Length: ");
        int length = scanner.nextInt();
        bitSetL = new BitSetL(length);
        System.out.println("DONE");
    }

    private static void testIntLengthConstructor () {
        System.out.print("Int: ");
        int num = scanner.nextInt();
        System.out.print("Length: ");
        int length = scanner.nextInt();
        bitSetL = new BitSetL(num,length);
        System.out.println("DONE");
    }

    private static void testStringConstructor () {
        System.out.print("String: ");
        String string = scanner.next();
        bitSetL = new BitSetL(string);
        System.out.println("DONE");
    }

    private static void constructor () {
        System.out.println("Constructor type:");
        String [] options = {
                "empty",
                "with length",
                "with int and lenght",
                "with string"
        };

        int action = prompt(options);
        while (action<1 || action>4){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testEmptyConstructor();
        else if (action == 2) testLengthConstructor();
        else if (action == 3) testIntLengthConstructor();
        else if (action == 4) testStringConstructor();

    }

    private static void testAsInt () {
        System.out.println("asInt: " + bitSetL.asInt());
        System.out.println("DONE");
    }

    private static void testFlip () {
        bitSetL.flip();
        System.out.println("DONE");
    }

    private static void testPositionSet () {
        System.out.print("Position: ");
        int pos = scanner.nextInt();
        bitSetL.set(pos);
        System.out.println("DONE");
    }

    private static void testPositionValueSet () {
        System.out.print("Position: ");
        int pos = scanner.nextInt();
        System.out.print("Value (true-false): ");
        boolean value = scanner.nextBoolean();
        bitSetL.set(pos,value);
        System.out.println("DONE");
    }

    private static void set () {
        System.out.println("Set type:");
        String [] options = {
                "position",
                "position and value"
        };

        int action = prompt(options);
        while (action<1 || action>2){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testPositionSet();
        else if (action == 2) testPositionValueSet();
    }

    private static void testClear () {
        System.out.print("Position: ");
        int pos = scanner.nextInt();
        bitSetL.clear(pos);
        System.out.println("DONE");
    }

    private static void testLength () {
        System.out.println("length" + bitSetL.length());
        System.out.println("DONE");
    }

    private static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        bitSetL = new BitSetL();

        String[] options = {
                "constructor",
                "asInt",
                "flip",
                "set",
                "clear",
                "length",
                "exit"
        };

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) constructor();
            else if (action == 2) testAsInt();
            else if (action == 3) testFlip();
            else if (action == 4) set();
            else if (action == 5) testClear();
            else if (action == 6) testLength();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

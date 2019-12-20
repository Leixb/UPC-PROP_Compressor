import domini.Huffman;

import java.util.Scanner;

class HuffmanDriver {
    private static Scanner scanner;

    private static Huffman huff;

    public static void testConstructora() {
        System.out.print("isAC (true/false): ");
        boolean isAC = scanner.nextBoolean();
        System.out.print("isChrominance (true/false): ");
        boolean isChrominance = scanner.nextBoolean();

        try {
            huff = new Huffman(isAC, isChrominance);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testEncode() {
        System.out.print("Value: ");
        short value = scanner.nextShort();

        try {
            huff.encode(value);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testDecode() {
        System.out.print("Bit: ");
        boolean bit = scanner.nextInt() != 0;
        Huffman.Node n = huff.decode(bit);

        if (n == null) {
            System.out.println("NOT found");
            System.out.println("DONE");
            return;
        }

        while (!n.isLeaf()) {
            System.out.print("Next bit: ");
            bit = scanner.nextInt() != 0;
            n = huff.decode(n, bit);
            if (n == null) {
                System.out.println("NOT found");
                System.out.println("DONE");
                return;
            }
        }

        System.out.printf("Decoded: %d", n.getValue());
        System.out.println("DONE");
    }

    public static int prompt(String[] options) {
        for (int i = 1; i <= options.length; ++i)
            System.out.printf("- [%d] : %s\n", i, options[i-1]);
        System.out.printf("Chose one option (%d-%d): ", 1, options.length);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        String[] options = {
            "Constructora",
            "encode",
            "decode",
            "exit"
        };

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) testConstructora();
            else if (action == 2) testEncode();
            else if (action == 3) testDecode();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

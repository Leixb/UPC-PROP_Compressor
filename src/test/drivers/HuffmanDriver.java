import domini.Huffman;

import java.util.Scanner;

class HuffmanDriver {
    private static Scanner scanner;

    private static Huffman huff;

    private static void testConstructora() {
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

    private static void testIsLeaf() {
        System.out.println("isLeaf: " + huff.Node.isLeaf());
        System.out.println("DONE");
    }

    private static void testGetValue() {
        System.out.println("getValue: " + huff.Node.getValue());
        System.out.println("DONE");
    }

    private static void testEncode() {
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

    private static void testDecode() {
        System.out.print("bit: ");
        boolean bit = scanner.nextInt() != 0;

        System.out.println("decode: " + huff.decode(bit));
        System.out.println("DONE");
    }

    private static void testDecodeNode() {
        System.out.print("node: ");
        Node node = scanner.next();
        System.out.print("bit: ");
        boolean bit = scanner.nextInt() != 0;

        System.out.println("decodeNode: " + huff.decode(node,bit));
        System.out.println("DONE");
    }

    private static void decode() {
        String[] options = {
                "decode(bool)",
                "decode(Node,bool)"
        };

        int action = prompt(options);
        while (action<1 || action>2){
            System.out.println("Invalid option");
            action = prompt(options);
        }

        if (action == 1) testDecode();
        else if (action == 2) testDecodeNode();
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
            "Constructora",
            "isLeaf",
            "getValue",
            "encode",
            "decode",
            "exit"
        };

        int action = prompt(options);

        while (action != 6) {

            if (action == 1) testConstructora();
            else if (action == 2) testIsLeaf();
            else if (action == 3) testGetValue();
            else if (action == 4) testEncode();
            else if (action == 5) decode();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

import domini.JPEGBlock;

import java.util.Scanner;

class JPEGBlockDriver {
    private static Scanner scanner;

    private static void testEncode() {
        System.out.print("Quality: ");
        short quality = scanner.nextShort();

        System.out.print("Is Chrominance (true/false): ");
        boolean isChrominance = scanner.nextBoolean();

        System.out.println("Enter 8x8 byte block (in hex):");

        byte[][] block = new byte[8][8];

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                block[i][j] = scanner.nextByte(16);
            }
        }


        try {
            short[] encoded = JPEGBlock.encode(quality, isChrominance, block);
            for (int i=0; i < encoded.length; ++i) {
                System.out.printf("%04x ", encoded[i]);
            }
            System.out.println();
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("JPEGBlock.encode() failed!");
            e.printStackTrace();
        }
    }

    private static void testDecode() {
        System.out.print("Quality: ");
        short quality = scanner.nextShort();

        System.out.print("Is Chrominance (true/false): ");
        boolean isChrominance = scanner.nextBoolean();

        System.out.print("Enter array length: ");
        int length = scanner.nextInt();
        System.out.println("Enter short array (in hex):");

        short[] data = new short[length];

        for (int i=0; i < data.length; ++i) {
            data[i] = scanner.nextShort(16);
        }

        try {
            byte[][] block =  JPEGBlock.decode(quality, isChrominance, data);
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    System.out.printf("%04x ", block[i][j]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("JPEGBlock.decode() failed!");
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

        String[] options = {"encode", "decode", "exit"};

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) testEncode();
            else if (action == 2) testDecode();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

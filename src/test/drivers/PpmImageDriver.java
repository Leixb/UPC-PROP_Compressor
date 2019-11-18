import domini.PpmImage;

import java.util.Scanner;

class PpmImageDriver {
    private static Scanner scanner;

    private static PpmImage image;

    public static void testSetDimensions() {
        System.out.print("Width: ");
        int width = scanner.nextInt();
        System.out.print("Heigth: ");
        int heigth = scanner.nextInt();

        image.setDimensions(width, heigth);
        System.out.println("DONE");
    }

    public static void testReadFile() {
        System.out.println("Filename: ");
        String filename = scanner.nextLine();

        try {
            image.readFile(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testWritefile() {
        System.out.println("Filename: ");
        String filename = scanner.nextLine();

        try {
            image.writeFile(filename);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }
    }

    public static void testToRGB() {
        image.toRGB();
        System.out.println("DONE");
    }

    public static void testToYCbCr() {
        image.toYCbCr();
        System.out.println("DONE");
    }

    public static void testReadBlock() {
        System.out.print("channel: ");
        int channel = scanner.nextInt();
        System.out.print("x: ");
        int x = scanner.nextInt();
        System.out.print("y: ");
        int y = scanner.nextInt();

        byte[][] block = image.readBlock(channel, x, y);

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                System.out.printf("%04x ", block[i][j]);
            }
            System.out.println();
        }
        System.out.println("DONE");
    }
    public static void testWriteBlock() {
        System.out.print("channel: ");
        int channel = scanner.nextInt();
        System.out.print("x: ");
        int x = scanner.nextInt();
        System.out.print("y: ");
        int y = scanner.nextInt();

        System.out.println("Enter 8x8 block (in hex):");

        byte[][] block = new byte[8][8];

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                block[i][j] = scanner.nextByte(16);
            }
        }

        image.writeBlock(block, channel, x, y);

        System.out.println("DONE");
    }

    public static void testWidth() {
        System.out.printf("width: %d\n", image.width());
        System.out.println("DONE");
    }
    public static void testHeight() {
        System.out.printf("height: %d\n", image.height());
        System.out.println("DONE");
    }
    public static void testColumns() {
        System.out.printf("columns: %d\n", image.columns());
        System.out.println("DONE");
    }
    public static void testRows() {
        System.out.printf("rows: %d\n", image.rows());
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
            "setDimensions",
            "readFile",
            "writefile",
            "toRGB",
            "toYCbCr",
            "readBlock",
            "writeBlock",
            "width",
            "height",
            "columns",
            "rows",
            "exit"
        };

        image = new PpmImage();

        int action = prompt(options);

        while (action != 12) {

            if (action == 1) testSetDimensions();
            else if (action == 2) testReadFile();
            else if (action == 3) testWritefile();
            else if (action == 4) testToRGB();
            else if (action == 5) testToYCbCr();
            else if (action == 6) testReadBlock();
            else if (action == 7) testWriteBlock();
            else if (action == 8) testWidth();
            else if (action == 9) testHeight();
            else if (action == 10) testColumns();
            else if (action == 11) testRows();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

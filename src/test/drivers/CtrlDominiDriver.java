import domini.CtrlDomini;
import domini.Statistics;

import java.util.Scanner;

class CtrlDominiDriver {
    private static Scanner scanner;

    private static CtrlDomini ctdom = CtrlDomini.getInstance();

    private static void testCompress () {
        System.out.println("Compression algorithm: ");
        String[] options = {"LZ78", "LZSS", "LZW", "JPEG"};
        int num = prompt(options);
        while (num < 1 || num > 4) {
            System.out.println("Invalid option.");
            num = prompt(options);
        }
        System.out.println("Input file (file to compress): ");
        String inputFile = scanner.next();
        System.out.println("Output file: ");
        String outputFile = scanner.next();
        Short quality = 0;
        if ((num - 1) == 4) {
            System.out.println("Compression quality: ");
            quality = scanner.nextShort();
        }
        try {
            ctdom.compress(num, inputFile, outputFile, quality);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("CtrlDomini compress failed!");
            e.printStackTrace();
        }
    }

    private static void testDecompress() {
        System.out.println("Input file (file to decompress): ");
        String inputFile = scanner.next();
        System.out.println("Output file: ");
        String outputFile = scanner.next();
        try {
            ctdom.decompress(inputFile, outputFile);
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("CtrlDomini decompress failed!");
            e.printStackTrace();
        }
    }

    private static void testGetTime(){
        System.out.println("getTime: " + ctdom.getTime());
        System.out.println("DONE");
    }

    private static void testGetDeflated(){
        System.out.println("getDeflated: " + ctdom.getDeflated());
        System.out.println("DONE");
    }

    private static void testGetSpeedCompress(){
        System.out.println("getSpeedCompress: " + ctdom.getSpeedCompress());
        System.out.println("DONE");
    }

    private static void testGetInflated(){
        System.out.println("getInflated: " + ctdom.getInflated());
        System.out.println("DONE");
    }

    private static void testGetSpeedDecompress(){
        System.out.println("getSpeedDecompress: " + ctdom.getSpeedDecompress());
        System.out.println("DONE");
    }

    private static void testGetFileIn(){
        System.out.println("getFileIn: " + ctdom.getFileIn());
        System.out.println("DONE");
    }

    private static void testGetFileOut(){
        System.out.println("getFileOut: " + ctdom.getFileOut());
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

        String[] options = {
                "compress", "decompress",
                "getTime",
                "getDeflated", "getInflated",
                "getSpeedCompress", "getSpeedDecompress",
                "getFileIn", "getFileOut",
                "exit"};

        int action = prompt(options);

        while (action != options.length) {

            if (action == 1) testCompress();
            else if (action == 2) testDecompress();
            else if (action == 3) testGetTime();
            else if (action == 4) testGetDeflated();
            else if (action == 5) testGetInflated();
            else if (action == 6) testGetSpeedCompress();
            else if (action == 7) testGetSpeedDecompress();
            else if (action == 8) testGetFileIn();
            else if (action == 9) testGetFileOut();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

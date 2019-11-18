import domini.CtrlDomini;
import domini.Statistics;

import java.util.Scanner;

class CtrlDominiDriver {
    private static Scanner scanner;

    public static void testCompress () {
        System.out.println("Compression algorithm: ");
        String[] options = {"LZ78", "LZSS", "LZW", "JPEG"};
        int num = prompt(options);
        while (num<1 || num>4){
            System.out.println("Invalid option.");
            num = prompt(options);
        }
        CtrlDomini.Alg alg = CtrlDomini.Alg.values()[num-1];
        System.out.println("Input file (file to compress): ");
        String inputFile = scanner.next();
        System.out.println("Output file: ");
        String outputFile = scanner.next();
        Short quality = 0;
        if ((num-1) == 4) {
            System.out.println("Compression quality: ");
            quality = scanner.nextShort();
        }


        try {
            Statistics stats = new Statistics();
            stats = CtrlDomini.compress(alg,inputFile,outputFile,quality);
            System.out.println("Compress stats:");
            System.out.println("time: " + stats.getTime());
            System.out.println("iniFileSize: " + stats.getIniFileSize());
            System.out.println("finFileSize: " + stats.getFinFileSize());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("CtrlDomini compress failed!");
            e.printStackTrace();
        }
    }

    public static void testDecompress  () {
        System.out.println("Input file (file to decompress): ");
        String inputFile = scanner.next();
        System.out.println("Output file: ");
        String outputFile = scanner.next();

        try {
            Statistics stats = new Statistics();
            stats = CtrlDomini.decompress(inputFile,outputFile);
            System.out.println("Decompress stats:");
            System.out.println("time: " + stats.getTime());
            System.out.println("iniFileSize: " + stats.getIniFileSize());
            System.out.println("finFileSize: " + stats.getFinFileSize());
            System.out.println("DONE");
        } catch (Exception e) {
            System.out.println("CtrlDomini decompress failed!");
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

        String[] options = {"compress", "decompress", "exit"};

        int action = prompt(options);

        while (action != 3) {

            if (action == 1) testCompress();
            else if (action == 2) testDecompress();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

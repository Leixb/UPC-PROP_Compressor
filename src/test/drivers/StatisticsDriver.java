import domini.Statistics;

import java.util.Scanner;

class StatisticsDriver {

    private static Statistics stats;

    private static Scanner scanner;

    public static void testConstructor() {
        stats = new Statistics();
        System.out.println("DONE");
    }

    public static void testSetStartingTime() {
        stats.setStartingTime();
        System.out.println("DONE");
    }

    public static void testSetEndingTime() {
        stats.setEndingTime();
        System.out.println("DONE");
    }

    public static void testSetIniFileSize() {
        System.out.print("Filename of the file you want to consult its size): ");
        String filename = scanner.next();

        stats.setIniFileSize(filename);
        System.out.println("DONE");
    }

    public static void testSetFinFileSize() {
        System.out.print("Filename (of the file you want to consult its size): ");
        String filename = scanner.next();

        stats.setFinFileSize(filename);
        System.out.println("DONE");
    }

    public static void testGetTime() {
        System.out.println("getTime: " + stats.getTime());
        System.out.println("DONE");
    }

    public static void testGetIniFileSize() {
        System.out.println("getIniFileSize: " + stats.getIniFileSize());
        System.out.println("DONE");
    }

    public static void testGetFinFileSize() {
        System.out.println("getFinFileSize: " + stats.getFinFileSize());
        System.out.println("DONE");
    }

    public static void testGetPercentageCompressed() {
        System.out.println("getPercentageCompressed: " + stats.getPercentageCompressed());
        System.out.println("DONE");
    }

    public static void testGetPercentageDecompressed() {
        System.out.println("getPercentageDecompressed: " + stats.getPercentageDecompressed());
        System.out.println("DONE");
    }

    public static void testGetSpeedCompressed() {
        System.out.println("getSpeedCompressed: " + stats.getSpeedCompressed());
        System.out.println("DONE");
    }

    public static void testGetSpeedDecompressed() {
        System.out.println("getSpeedDecompressed: " + stats.getSpeedDecompressed());
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

        stats = new Statistics();

        String[] options = {"constructor",
                "setStartingTime", "setEndingTime",
                "setIniFileSize", "setFinFileSize",
                "getTime",
                "getIniFileSize", "getFinFileSize",
                "getPercentageCompressed", "getPercentageDecompressed",
                "getSpeedCompressed", "getSpeedDecompressed",
                "exit"};

        int action = prompt(options);

        while (action != 13) {

            if (action == 1) testConstructor();
            else if (action == 2) testSetStartingTime();
            else if (action == 3) testSetEndingTime();
            else if (action == 4) testSetIniFileSize();
            else if (action == 5) testSetFinFileSize();
            else if (action == 6) testGetTime();
            else if (action == 7) testGetIniFileSize();
            else if (action == 8) testGetFinFileSize();
            else if (action == 9) testGetPercentageCompressed();
            else if (action == 10) testGetPercentageDecompressed();
            else if (action == 11) testGetSpeedCompressed();
            else if (action == 12) testGetSpeedDecompressed();
            else System.out.println("Invalid option");

            action = prompt(options);
        }

        scanner.close();
    }
}

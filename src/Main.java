import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Enter filename of ppm image:");

        //Scanner scanner = new Scanner(System.in);
        //String filename = scanner.nextLine();
        String filename = "/home/leix/16x16.ppm";
        String filename_out = "/home/leix/16x16_2.ppm";
        String filename_YCbCr = "/home/leix/16x16_YCbCr.ppm";
        String filename_RGB = "/home/leix/16x16_RGB.ppm";

        Image image = null;

        try {

            image = new Image();
            image.readFromPpmFile(filename);

            image.debug();
            image.writeToPpmFile(filename_out);

            image.toYCbCr();
            image.debug();
            image.writeToPpmFile(filename_YCbCr);

            image.toRGB();
            image.debug();
            image.writeToPpmFile(filename_RGB);
        } catch (IOException e) {
            System.err.println("IO Exception");
            e.printStackTrace();
        } catch (Image.InvalidFileFormat invalidFileFormat) {
            invalidFileFormat.printStackTrace();
        }
    }

}

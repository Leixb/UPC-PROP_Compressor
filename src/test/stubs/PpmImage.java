package domini;

public class PpmImage {
    public static class InvalidFileFormat extends Exception {}

    public void setDimensions(final int w, final int h) {
        System.out.printf("Llamda PpmImage.setDimensions(int w=%d, int h=%d)\n", w, h);
    }

    public void readFile(final String filename) {
        System.out.printf("Llamda PpmImage.readFile(String filename=%s)\n", filename);
    }

    public void writeFile(final String filename) {
        System.out.printf("Llamda PpmImage.writeFile(String filename=%s)\n", filename);
    }

    public void toRGB() {
        System.out.printf("Llamda PpmImage.toRGB()\n");
    }

    public void toYCbCr() {
        System.out.printf("Llamda PpmImage.toYCbCr()\n");
    }

    public byte[][] readBlock(final int channel, final int x, final int y) {
        System.out.printf("Llamda PpmImage.readBlock(int channel=%d, int x=%d, int y=%d) -> byte[8][8]\n", channel, x, y);
        return new byte[8][8];
    }

    public void writeBlock(final byte[][] block, final int channel, final int x, final int y) {
        System.out.printf("Llamda PpmImage.writeBlock(byte[][] block, int channel=%d, int x=%d, int y=%d)\n", channel, x, y);
    }

    public int width() {
        System.out.printf("Llamda PpmImage.width() -> 2\n");
        return 2;
    }

    public int height() {
        System.out.printf("Llamda PpmImage.height() -> 2\n");
        return 2;
    }

    public int columns() {
        System.out.printf("Llamda PpmImage.columns() -> 1\n");
        return 1;
    }

    public int rows() {
        System.out.printf("Llamda PpmImage.rows() -> 1\n");
        return 1;
    }
}

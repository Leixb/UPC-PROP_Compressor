import java.io.*;

public class Image {
    private byte[][][] pixels; // width * height * channel
    private int width, height;

    private enum Channel {RGB, YCbCr};
    private Channel channel;

    public void readFromPpmFile(String filename) throws IOException, InvalidFileFormat {
        FileInputStream file = new FileInputStream(filename);

        byte[] magic = new byte[2];
        if (2 != file.read(magic)) throw new EOFException();

        // TODO: support for P3 (and maybe grayscale)?
        if (magic[0] != 'P' || magic[1] != '6') throw new Image.InvalidFileFormat();

        this.width = readInt(file);
        this.height = readInt(file);
        int maxVal = readInt(file);

        // TODO: support >24bit images?
        if (maxVal >= 256) throw new Image.InvalidFileFormat();

        this.pixels = new byte[this.width][this.height][3];

        this.channel = Channel.RGB;

        System.err.printf("W %d H %d\n", this.width, this.height); // TODO: remove this

        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                if (file.read(this.pixels[i][j]) != 3) throw new EOFException();
            }
        }

        file.close();

    }

    public void writeToPpmFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fout = new FileOutputStream(file);

        final byte[] header = String.format("P6\n%d %d\n255\n", this.width, this.height).getBytes();
        fout.write(header);

        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                fout.write(pixels[i][j]);
            }
        }

        fout.flush();
        fout.close();
    }

    public class InvalidFileFormat extends Exception {
        public InvalidFileFormat() { super(); }
    }

    private int readInt(FileInputStream file) throws IOException {

        // Read till we find ascii integers
        char c;
        do {
            c = (char) file.read();
            if (c == '#') { // Comment, discard till newline
                while ( c != '\n') c = (char) file.read();
            }
        } while (c < '0' || c > '9');

        //Read till not ascii integer
        int n = 0;
        do {
            n = n * 10 + c - '0';
            c = (char) file.read();
        } while (c >= '0' && c <= '9');

        return n;
    }

    // Convert data from YCbCr to RGB TODO: this does not work
    public void toRGB() {
        // Do nothing if already RGB
        if (this.channel == Channel.RGB) return;
        this.channel = Channel.RGB;

        byte R, G, B;
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                //final byte Y = this.pixels[i][j][0], Cb = this.pixels[i][j][1], Cr = this.pixels[i][j][2];
                final int Y = Byte.toUnsignedInt(this.pixels[i][j][0]);
                final int Cb = Byte.toUnsignedInt(this.pixels[i][j][1]);
                final int Cr = Byte.toUnsignedInt(this.pixels[i][j][2]);

                R = (byte) (Y + 1.402*(Cr - 128));
                G = (byte) (Y - 0.34414*(Cb-128) - 0.71414*(Cr-128));
                B = (byte) (Y    + 1.772*(Cb-128));

                this.pixels[i][j][0] = R;
                this.pixels[i][j][1] = G;
                this.pixels[i][j][2] = B;
            }
        }

    }
    // Convert data from RGB to YCbCr TODO: this does not work
    public void toYCbCr() {
        // Do nothing if already YCbCr
        if (this.channel == Channel.YCbCr) return;
        this.channel = Channel.YCbCr;

        byte Y, Cb, Cr;
        for (int i = 0; i < this.width; ++i) {
            for (int j = 0; j < this.height; ++j) {
                final int R = Byte.toUnsignedInt(this.pixels[i][j][0]);
                final int G = Byte.toUnsignedInt(this.pixels[i][j][1]);
                final int B = Byte.toUnsignedInt(this.pixels[i][j][2]);

                Y   = (byte) (0.299*R + 0.587*G + 0.114*B);
                Cb  = (byte) (-0.1687*R -0.3313*G + 0.5*B + 128);
                Cr  = (byte) (0.5*R -0.4187*G -0.0813*B + 128);

                this.pixels[i][j][0] = Y;
                this.pixels[i][j][1] = Cb;
                this.pixels[i][j][2] = Cr;
            }
        }
    }

    //TODO: remove this
    public void debug() {
        for (int k = 0; k < 3; ++k) {
            System.out.println(k);
            for (int i = 0; i < this.width; ++i) {
                for (int j = 0; j < this.height; ++j) {
                    int n = Byte.toUnsignedInt(
                        this.pixels[i][j][k]
                    );
                    System.out.printf("%03d ", n);
                }
                System.out.println();
            }
        }
    }

}

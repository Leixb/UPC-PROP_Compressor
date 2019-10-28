import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Dct {
    static double[][] forwardDct(byte[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }

        double[][]G = new double[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {

                G[u][v] = 0;

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        G[u][v] += block[x][y] * Math.cos((2 * x + 1) *u*Math.PI/16)
                                               * Math.cos((2 * y + 1) *v*Math.PI/16);
                    }
                }

                double ortho = 0.25;

                // Make orthonormal
                if (u == 0) ortho *= 1/Math.sqrt(2);
                if (v == 0) ortho *= 1/Math.sqrt(2);

                G[u][v]*=ortho;

            }
        }

        return G;

    }

    static byte[][] backwardDct(double[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }

        byte[][]f = new byte[8][8];


        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

                float fxy = 0;

                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {

                        // Make orthonormal
                        double ortho = 1;
                        if (u == 0) ortho *= 1/Math.sqrt(2);
                        if (v == 0) ortho *= 1/Math.sqrt(2);

                        fxy += ortho * block[u][v] * Math.cos((2 * x + 1) *u*Math.PI/16)
                                                   * Math.cos((2 * y + 1) *v*Math.PI/16);
                    }
                }

                fxy *= 0.25;

                //TODO: doubleToInt
                f[x][y] = (byte) fxy;

            }
        }

        return f;

    }

    public static byte[] unfoldFromBlock(byte[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }
        byte[][] RLE_table = makeRleTable();

        byte[] L = new byte[8*8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                L[RLE_table[i][j]] = block[i][j];
            }
        }

        return L;
    }

    public static byte[][] foldToBlock(byte[] L) throws IllegalArgumentException {
        if (L.length != 8*8) {
            throw new IllegalArgumentException("block must be 8x8");
        }
        byte[][] RLE_table = makeRleTable();

        byte[][] block = new byte[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                block[i][j] = L[RLE_table[i][j]];
            }
        }

        return block;

    }

    private static byte[][] makeRleTable() {

        byte[][] RLE_table = new byte[8][8];

        int i=0, j=0;
        boolean up = true;
        for (byte pos = 0; pos < 8*8; ++pos) {
            RLE_table[i][j] = pos;

            if (up) {
                --i; ++j;
            } else {
                ++i; --j;
            }

            boolean change_dir = true;

            if (i == 8 && j == -1) {
                i = 7;
                j = 1;
            } else if (i < 0) {
                i = 0;
            } else if (j < 0) {
                j = 0;
            } else if (i >= 8) {
                i = 7;
                j += 2;
            } else if (j >= 8) {
                j = 7;
                i += 2;
            } else {
                change_dir = false;
            }

            if (change_dir) {
                up = !up;
            }
        }
        return RLE_table;
    }

    final static private byte[][] QTable = {
        { 16 , 11 , 10 , 16 ,  24 ,  40 ,  51 ,  61 },
        { 12 , 12 , 14 , 19 ,  26 ,  58 ,  60 ,  55 },
        { 14 , 13 , 16 , 24 ,  40 ,  57 ,  69 ,  56 },
        { 14 , 17 , 22 , 29 ,  51 ,  87 ,  80 ,  62 },
        { 18 , 22 , 37 , 56 ,  68 , 109 , 103 ,  77 },
        { 24 , 35 , 55 , 64 ,  81 , 104 , 113 ,  92 },
        { 49 , 64 , 78 , 87 , 103 , 121 , 120 , 101 },
        { 72 , 92 , 95 , 98 , 112 , 100 , 103 ,  99 }
    };

    static double QuantizationValue(short quality, short x, short y) throws IllegalArgumentException {
        if (quality < 1 || quality > 100) throw new IllegalArgumentException("Quality must be between 1 and 100");
        if (x < 0 || x >= 8) throw new IllegalArgumentException("0 < x < 8");
        if (y < 0 || y >= 8) throw new IllegalArgumentException("0 < y < 8");

        double q = 5000.0/quality;
        if (quality >= 50) q = 200 - 2*quality;

        return (q*QTable[x][y] + 50)/100;
    }

    static byte[][] Quanitzate(short quality, double[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }
        byte[][] data = new byte[8][8];
        for (short i = 0; i < 8; ++i) {
            for (short j = 0; j < 8; ++j) {
                //data[i][j] = (byte)(block[i][j]/QuantizationValue(quality, i, j));
                data[i][j] = (byte)(block[i][j]);
            }
        }
        return data;
    }

    static double[][] DeQuanitzate(short quality, byte[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }
        double[][] data = new double[8][8];
        for (short i = 0; i < 8; ++i) {
            for (short j = 0; j < 8; ++j) {
                //data[i][j] = block[i][j]*QuantizationValue(quality, i, j);
                data[i][j] = block[i][j];
            }
        }
        return data;
    }

    // Codifica en RLE (primer byte = nombre de 0 precedents, segon valor.
    // Acaba amb 0,0
    static byte[] RLE(byte[] data) {
        ByteArrayOutputStream rleData = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; ++i) {
            byte count = 0;
            while (i < data.length && data[i] == 0) {
                ++i;
                ++count;
            }

            if (i >= data.length) break;

            rleData.write(count);
            rleData.write(data[i]);
        }
        rleData.write((byte)0);
        rleData.write((byte)0);
        return rleData.toByteArray();
    }

    static byte[] undoRLE(byte[] rleData) throws IOException {
        byte[] data = new byte[64];
        ByteArrayInputStream inputData = new ByteArrayInputStream(rleData);
        byte[] RLEtuple = new byte[2];
        int i = 0;
        while (inputData.read(RLEtuple) == 2) {
            if (RLEtuple[0] == 0 && RLEtuple[1] == 0) break;
            i += RLEtuple[0];
            data[i] = RLEtuple[1];
        }
        return data;
    }
}

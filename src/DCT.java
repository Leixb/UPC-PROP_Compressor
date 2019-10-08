public class DCT {
    double[][] forwardDCT(byte[][] block) throws IllegalArgumentException {
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

    byte[][] backwardDCT(double[][] block) throws IllegalArgumentException {
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
}

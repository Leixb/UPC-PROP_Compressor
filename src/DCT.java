public class DCT {
    double[][] forwardDCT(byte[][] block) throws IllegalArgumentException {
        if (block.length != 8 || block[0].length != 8) {
            throw new IllegalArgumentException("block must be 8x8");
        }

        double[][]G = new double[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {

                G[u][v] = 0.25;

                // Make orthonormal
                if (u == 0) G[u][v] *= 1/Math.sqrt(2);
                if (v == 0) G[u][v] *= 1/Math.sqrt(2);

                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        G[u][v] += block[x][y] * Math.cos((2 * x + 1) *u*Math.PI/16)
                                               * Math.cos((2 * y + 1) *v*Math.PI/16);
                    }
                }

            }
        }

        return G;

    }
}

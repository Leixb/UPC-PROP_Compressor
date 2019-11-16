package domini;

import java.util.HashMap;

import java.io.IOException;

public class Huffman {

    Node root;
    HashMap<Short, BitSetL> table;

    public class Node {
        Short value;
        // L -> 0; R -> 1
        boolean leaf;
        Node L, R;

        public boolean isLeaf() {
            return leaf;
        }

        public Short getValue() {
            return value;
        }
    }

    public Huffman(final boolean isAC, final boolean isChrominance) throws IOException {
        String filename;
        if (isAC)
            filename = "AC_";
        else
            filename = "DC_";

        if (isChrominance)
            filename += "Chrom";
        else
            filename += "Lum";

        filename += ".table";

        filename = this.getClass().getClassLoader().getResource(filename).getPath();

        readTable(filename);
    }

    private void readTable(final String filename) throws IOException {
        table = new HashMap<Short, BitSetL>();
        try (IO.Char.reader input = new IO.Char.reader(filename)) {
            String s = input.readLine();
            while (s != null) {
                final String[] sp = s.split(" ", 2);

                if (sp.length != 2)
                    throw new IOException("Invalid input table");

                final Short value = Short.parseShort(sp[0], 16);
                final BitSetL bs = new BitSetL(sp[1]);

                table.put(value, bs);
                addToTree(value, bs);

                s = input.readLine(); // Read next line
            }
        }
    }

    private void addToTree(final Short value, final BitSetL bs) {
        if (root == null)
            root = new Node();
        Node n = root;

        for (int i = 0; i < bs.length(); ++i) {
            if (bs.get(i)) {
                if (n.R == null)
                    n.R = new Node();
                n = n.R;
            } else {
                if (n.L == null)
                    n.L = new Node();
                n = n.L;
            }
        }

        n.leaf = true;
        n.value = value;
    }

    public BitSetL encode(final Short value) {
        return table.get(value);
    }

    public Node decode(final boolean b) {
        return decode(root, b);
    }

    public Node decode(final Node n, final boolean b) {
        if (b) return n.R;
        else return n.L;
    }

}

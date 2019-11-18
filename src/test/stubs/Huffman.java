package domini;

public class Huffman {

    /// Nodo del arbol huffman
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

    static public class HuffmanLookupException extends Exception { }

    public Huffman(final boolean isAC, final boolean isChrominance) {
        System.out.printf("Llamda Huffman(boolean isAC=%b, boolean isChrominance=%b)\n", isAC, isChrominance);
    }

    public BitSetL encode(final Short value) {
        System.out.printf("Llamda Huffman.encode(short value=%d) -> BitSetL(5)\n", value);
        return new BitSetL(5);
    }

    public Node decode(final boolean b) {
        System.out.printf("Llamda Huffman.decode(boolean b=%b) -> Node() (leaf=false, value=0xff)\n", b);
        Node n = new Node();
        n.value = 0xFF;
        n.leaf = false;
        return n;
    }

    public Node decode(final Node n, final boolean b) {
        System.out.printf("Llamda Huffman.decode(Node n, boolean b=%b) -> Node() (leaf=true, value=0xff)\n", b);
        Node nn = new Node();
        nn.value = 0xAA;
        nn.leaf = true;
        return nn;
    }

}

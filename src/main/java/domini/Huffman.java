package domini;

import java.util.HashMap;

import java.io.IOException;

/** 
 * @author Aleix Boné
 * 
 * @brief codificación y decodificación huffman con tablas predefinidas
 */
public class Huffman {

    private Node root; //< Raíz del arbol Huffman
    private HashMap<Short, BitSetL> table; //< Tabla de Huffman

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

    /**
     * @brief Leer la tabla del disco en memoria y construye el arbol.
     * 
     * @param isAC  si cierto se lee la tabla AC, sino la DC.
     * @param isChrominance  si cierto se lee la tabla de Chrominance, sino Luminance
     * @throws IOException si se produce un error en la lectura de las tablas.
     */
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

    /**
     * Añade el valor value en el àrbol siguiendo el camino marcado por
     * el BitSetL bs.
     * 
     * @param value valor a anadir
     * @param bs codigo huffman correspondiente
     */
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

    /**
     * @brief deuelve el còdigo Huffman associado a un valor
     * 
     * @param value valor a buscar en la tabla
     * @return código huffman
     * @throws HuffmanLookupException si el valor no se encuentra en la tabla
     */
    public BitSetL encode(final Short value) throws HuffmanLookupException {
        BitSetL bs = table.get(value);
        if (bs == null) {
            throw new HuffmanLookupException(value);
        }
        return bs;
    }

    public static class HuffmanLookupException extends Exception {
        private static final long serialVersionUID = 716585856065058709L;

        HuffmanLookupException() {
            super();
        }
        HuffmanLookupException(String s) {
            super(s);
        }
        HuffmanLookupException(Short n) {
            super(String.format("Huffman lookup failed for: %d", n));
        }
    }

    /**
     * @param b booleano
     * @return Siguiente nodo del arbol des de la raíz
     */
    public Node decode(final boolean b) {
        return decode(root, b);
    }

    /**
     * @param n Nodo del arbol
     * @param b booleano
     * @return Siguiente nodo en el arbol
     */
    public Node decode(final Node n, final boolean b) {
        if (b) return n.R;
        else return n.L;
    }

}

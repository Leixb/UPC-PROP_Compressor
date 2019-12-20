/**
 * @file ./src/main/java/domini/Huffman.java
 * @author ***REMOVED***
 */
package domini;

import persistencia.IO;

import java.net.URL;
import java.net.URLDecoder;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

/**
 * @brief codificación y decodificación Huffman con tablas predefinidas
 */
public class Huffman {

    /// Raíz del árbol Huffman
    private Node root;

    /// Tabla de Huffman
    private Map<Short, BitSetL> table;

    /// Nodo del arbol huffman
    public static class Node {
        Short value;
        // L -> 0; R -> 1
        boolean leaf;
        Node L, R;

        boolean isLeaf() {
            return leaf;
        }

        public Short getValue() {
            return value;
        }
    }

    /**
     * @brief Leer la tabla del disco en memoria y construye el árbol
     *
     * @param isAC  si cierto se lee la tabla AC, sino la DC
     * @param isChrominance  si cierto se lee la tabla de Chrominance, sino Luminance
     * @throws IOException si se produce un error en la lectura de las tablas
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

        final URL resource = this.getClass().getClassLoader().getResource(filename);

        readTable(URLDecoder.decode(resource.getFile(), "UTF-8"));
    }

    /**
     * @brief Lee una tabla Huffman
     *
     * @param filename nombre del archivo a leer
     * @throws IOException Lanza cualquier excepción generada al leer
     */
    private void readTable(final String filename) throws IOException {
        table = new HashMap<Short, BitSetL>();
        try (IO.Char.reader input = new IO.Char.reader(filename)) {
            String s = input.readLine();
            while (s != null) {
                final String[] sp = s.split(" ", 2);

                if (sp.length != 2)
                    throw new InvalidTableException(filename);

                final Short value = Short.parseShort(sp[0], 16);
                final BitSetL bs = new BitSetL(sp[1]);

                table.put(value, bs);
                addToTree(value, bs);

                s = input.readLine(); // Read next line
            }
        }
    }

    /**
     * @brief añade valor al árbol
     *
     * Añade el valor value en el árbol siguiendo el camino marcado por
     * el BitSetL bs
     *
     * @param value valor a añadir
     * @param bs código Huffman correspondiente
     */
    private void addToTree(final Short value, final BitSetL bs) {
        if (root == null)
            root = new Node();
        Node n = root;

        final int length = bs.length();

        for (int i = 0; i < length; ++i) {
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
     * @brief devuelve el código Huffman asociado a un valor
     *
     * @param value valor a buscar en la tabla
     * @return código Huffman
     */
    public BitSetL encode(final Short value) {
        BitSetL bs = table.get(value);
        if (bs == null) {
            throw new LookupException(value);
        }
        return bs;
    }

    /** Excecpión de búqueda de un valor en la tabla */
    public static class LookupException extends RuntimeException {
        private static final long serialVersionUID = 716585856065058709L;

        LookupException(Short n) {
            super(String.format("Huffman lookup failed for value: %d", n));
        }
    }

    /** Excepción de tabla invalida */
    public static class InvalidTableException extends IOException {
        private static final long serialVersionUID = 1547862892655535861L;

        InvalidTableException(final String filename) {
            super(String.format("Invalid Table on file: %s", filename));
        }
    }

    /**
     * @brief Devuelve siguiente nodo del árbol des de la raíz
     *
     * @param b bit a decodificar
     * @return Siguiente nodo del árbol des de la raíz
     */
    public Node decode(final boolean b) {
        return decode(root, b);
    }

    /**
     * @brief Dado un node, devuelve siguiente nodo del árbol
     *
     * @param n Nodo del árbol
     * @param b bit a decodificar
     * @return Siguiente nodo en el árbol
     */
    public Node decode(final Node n, final boolean b) {
        if (b) return n.R;
        else return n.L;
    }

}

/**
 * @file ./src/main/java/domini/BitSetL.java
 * @author ***REMOVED***
 */
package domini;

import java.util.BitSet;

/**
 * @brief BitSet con length() específica
 *
 * BitSet con length distinta al BitSet estandard.
 * El length de java.util.BitSet devuelve el numero de bits hasta el
 * último 1. BitSetL devuelve el numero de bits del último bit modificado
 * o el especificado al construirse el BitSetL.
 */
public class BitSetL extends BitSet {
    private static final long serialVersionUID = 1L;

    private int _length_;

    /** Constructora vacía, inicializa length a -1 */
    public BitSetL() {
        super();
        _length_ = -1;
    }

    /**
     * Constructora con longitud, inicializa length a la longitud dada
     * @param l longitud del bitset
     */
    public BitSetL(final int l) {
        super(l);
        _length_ = l;
    }

    /** Constructora con entero y longitud, inicializa length a la longitud dada
     * y mete los l últimos bits del entero en el bitset.
     * @param n entero a codificar
     * @param l longitud del bitset
     */
    public BitSetL(final int n, final int l) {
        super(l);
        int mask = 1 << (l - 1);
        for (int i = 0; i < l; ++i) {
            if ((n & mask) != 0)
                set(i);
            mask >>>= 1;
        }
        _length_ = l;
    }

    /** Constructora con String, inicializa length al tamaño del String
     * y convierte la string en un bitset.
     * @param s string de 0s y 1s
     */
    public BitSetL(final String s) {
        super(s.length());
        for (int i = 0; i < s.length(); i++) {
            set(i, s.charAt(i) == '1');
        }
    }

    /**
     * @brief Devuelve el bitset como un entero (32 bits)
     * @pre el bitset no tiene más de 32 bits
     * @return bitset como entero
     */
    public int asInt() {
        int v = 0;
        for (int i = 0; i < _length_; ++i) {
            v <<= 1;
            if (get(i))
                v |= 1;
        }
        return v;
    }

    /**
     * @brief Invierte los bits del bitset. (Solo hasta length)
     */
    public void flip() {
        for (int i = 0; i < _length_; i++) {
            flip(i);
        }
    }

    /**
     * @brief pone a true el bit en la posición dada
     * @param pos posición del bit que se pone a true
     */
    public void set(final int pos) {
        super.set(pos);
        if (pos + 1 > _length_)
            _length_ = pos + 1;
    }

    /**
     * @brief pone a true o false el bit en la posición dada en función del booleano val
     * @param pos posición del bit a modificar
     * @param val valor booleano que se asigna al bit
     */
    public void set(final int pos, final boolean val) {
        super.set(pos, val);
        if (pos + 1 > _length_)
            _length_ = pos + 1;
    }

    /**
     * @brief pone a false el bit en la posición dada
     * @param pos posicion del bit que se pone a true
     */
    public void clear(final int pos) {
        super.clear(pos);
        if (pos + 1 > _length_)
            _length_ = pos + 1;
    }

    /**
     * @brief devuelve la longitud del bitset
     * @return longitud del bitset
     */
    public int length() {
        return _length_;
    }
}

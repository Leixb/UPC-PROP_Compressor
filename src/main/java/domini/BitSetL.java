package domini;

import java.util.BitSet;

/** 
 * @author Aleix Boné
 */
public class BitSetL extends BitSet {
    private static final long serialVersionUID = 1L;

    int _length_;
    int l;

    public BitSetL() {
        super();
        _length_ = -1;
    }

    public BitSetL(final int l) {
        super(l);
        _length_ = l;
    }

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

    public BitSetL(final String s) {
        super(s.length());
        for (int i = 0; i < s.length(); i++) {
            set(i, s.charAt(i) == '1');
        }
    }

    public int asInt() {
        int v = 0;
        for (int i = 0; i < _length_; ++i) {
            v <<= 1;
            if (get(i))
                v |= 1;
        }
        return v;
    }

    public void flip() {
        for (int i = 0; i < _length_; i++) {
            flip(i);
        }
    }

    public void set(final int pos, final boolean val) {
        super.set(pos, val);
        if (pos + 1 > _length_)
            _length_ = pos + 1;
    }

    public int length() {
        return _length_;
    }

    public BitSetL(final byte b, final int l) {
        super(l);
        _length_ = l - 1;
        int bint = (int) b;
        int n=0;
        while (bint>0) {
            if (bint%2==1) this.set(n);
            ++n;
            bint/=2;
        }
    }

}

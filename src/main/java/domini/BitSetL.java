package domini;

import java.util.BitSet;

public class BitSetL extends BitSet {
    private static final long serialVersionUID = 1L;

    int _length_;

    public BitSetL() {
        super();
        _length_ = -1;
    }

    public BitSetL(int l) {
        super(l);
        _length_ = l;
    }

    public BitSetL(int n, int l) {
        super(l);
        int mask = 1<<(l-1);
        for (int i = 0; i < l; ++i) {
            if ((n & mask) != 0) set(i);
            mask >>= 1;
        }
        _length_ = l;
    }

    public BitSetL(String s) {
        super(s.length());
        for (int i = 0; i < s.length(); i++) {
            set(i, s.charAt(i) == '1');
        }
    }

    public int asInt() {
        int v = 0;
        for (int i = 0; i < _length_; ++i) {
            v <<= 1;
            if (get(i)) v |= 1;
        }
        return v;
    }

    public void flip() {
        for (int i = 0; i < _length_; i++) {
            flip(i);
        }
    }

    public void set(int pos, boolean val) {
        super.set(pos, val);
        if (pos+1 > _length_) _length_ = pos+1;
    }

    public int length() {
        return _length_;
    }
}

package Domini;

import java.util.BitSet;

public class BitSetL extends BitSet {
    int _length_;

    public BitSetL() {
        super();
        _length_ = -1;
    }

    public BitSetL(int n) {
        super(n);
        _length_ = -1;
    }

    public BitSetL(String s) {
        super(s.length());
        for (int i = 0; i < s.length(); i++) {
            set(i, s.charAt(i) == '1');
        }
    }

    public void set(int pos, boolean val) {
        super.set(pos, val);
        if (pos > _length_) _length_ = pos;
    }

    public int length() {
        return _length_ + 1;
    }
}

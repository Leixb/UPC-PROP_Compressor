package domini;

import java.util.BitSet;

public class BitSetL extends BitSet {
    private static final long serialVersionUID = 1L;

    int _length_;
    int l;

    public BitSetL() {
        super();
        _length_ = -1;
    }

    public BitSetL(int n) {
        super(n);
        _length_ = n-1;
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

    public BitSetL(byte b, int l){
        super(l);
        _length_= l-1;
        int bint = (int) b;
        int n=0;
        while (bint>0) {
            if (bint%2==1) this.set(n);
            ++n;
            bint/=2;
        }
    }

}

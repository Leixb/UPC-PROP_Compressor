package domini;

/** Stub BitSet */
public class BitSetL {
    public BitSetL() {
        System.out.printf("Llamda BitSetL()\n");
    }

    public BitSetL(final int l) {
        System.out.printf("Llamda BitSetL(int l=%d)\n", l);
    }

    public BitSetL(final int n, final int l) {
        System.out.printf("Llamda BitSetL(int n=%d, int l=%d)\n", n, l);
    }

    public BitSetL(final String s) {
        System.out.printf("Llamda BitSetL(String s=\"%s\")\n", s);
    }

    public int asInt() {
        System.out.printf("Llamda BitSetL.asInt() -> %d\n", 0);
        return 0;
    }

    public void flip() {
        System.out.printf("Llamda BitSetL.flip()\n");
    }

    public void set(final int pos) {
        System.out.printf("Llamda BitSetL.set(int pos=%d)\n", pos);
    }

    public void set(final int pos, final boolean val) {
        System.out.printf("Llamda BitSetL.set(int pos=%d, boolean val=%b)\n", pos, val);
    }

    public boolean get(final int pos) {
        System.out.printf("Llamda BitSetL.get(int pos=%d) -> true\n", pos);
        return true;
    }

    public void clear(final int pos) {
        System.out.printf("Llamda BitSetL.clear(int pos=%d)\n", pos);
    }

    public int length() {
        System.out.printf("Llamda BitSetL.length() -> %d\n", 10);
        return 10;
    }
}

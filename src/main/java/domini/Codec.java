package domini;

interface Codec<A, B> {
    static <A, B> B encode(A data) {
        return null;
    }
    static <A, B> A decode(B data) {
        return null;
    }
}

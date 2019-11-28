/**
 * @file ./src/main/java/domini/Codec.java
 * @author ***REMOVED***
 */
package domini;

/** 
 * @brief codec (Code-Decode)
 */
interface Codec<A, B> {
    static <A, B> B encode(A data) {
        return null;
    }
    static <A, B> A decode(B data) {
        return null;
    }
}

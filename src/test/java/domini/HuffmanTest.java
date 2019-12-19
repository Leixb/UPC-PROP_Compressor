package domini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTest {
    @Test
    void encode() {

        Huffman huf;
        try {
            huf = new Huffman(true, true);
            assertEquals( huf.encode(Short.parseShort("18", 16)),
                          new BitSetL("1111111110001001") );
        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }

    }

    @Test
    void decode() {
        Huffman huf;
        try {
            huf = new Huffman(true, true);

            String original = "1111111110001001";
            String random = "0101110101101110101";

            String padded = original + random;

            Huffman.Node n = huf.decode(padded.charAt(0) == '1');
            int i = 1;
            for (; !n.isLeaf() && i < padded.length(); ++i) {
                n = huf.decode(n, padded.charAt(i) == '1');
            }

            assertEquals(i, original.length());

            Short num = n.getValue();

            assertEquals((short)num, (short)0x18);

        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }
    }
}

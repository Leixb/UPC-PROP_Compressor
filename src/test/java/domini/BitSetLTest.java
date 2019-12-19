package domini;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitSetLTest {
    @Test
    void BitSet() {
        BitSetL bs = new BitSetL("101001");
        assertEquals(0b101001, bs.asInt());

        bs = new BitSetL("101");
        assertEquals(0b101, bs.asInt());

        bs = new BitSetL("1010110010");
        assertEquals(0b1010110010, bs.asInt());

        bs = new BitSetL("1010110010");
        bs.flip();
        assertEquals(0b0101001101, bs.asInt());

        bs = new BitSetL(0b101_0011_1010, 11);
        assertEquals(0b101_0011_1010, bs.asInt());
    }
}

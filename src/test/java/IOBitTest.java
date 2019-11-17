import domini.BitSetL;
import domini.IO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class IOBitTest {
    @Test
    void writeRead() {
        new File("generated/").mkdirs();
        try {
            try(IO.Bit.writer file = new IO.Bit.writer("generated/test")) {
                file.write((byte) 0xFF);

                file.write(new BitSetL(0xeeeeaaaa, 32));
                file.write((int)0xabcd1234);
            }
            try(IO.Bit.reader file = new IO.Bit.reader("generated/test")) {
                int r = file.readByte();
                int n = file.readInt();
                int m = file.readInt();
                assertEquals(0xFF, r);
                assertEquals(0xeeeeaaaa, n);
                assertEquals(0xabcd1234, m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
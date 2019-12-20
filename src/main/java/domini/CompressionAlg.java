package domini;

import persistencia.IO;

import java.io.IOException;

/**
 * @brief CompressionAlg
 */
interface CompressionAlg {
    void compress(final IO.Byte.reader input, final IO.Bit.writer output) throws IOException;
    void decompress(final IO.Bit.reader input, final IO.Byte.writer output) throws IOException;
    byte getMagicByte();
}

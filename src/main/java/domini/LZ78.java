package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

public final class LZ78 extends LZ {

    private LZ78() {}

    private static HashMap<String, Integer> compress_dict = new HashMap<String, Integer>();
    private static HashMap<Integer, String> decompress_dict = new HashMap<Integer, String>();
    public final static byte MAGIC_BYTE = 0x78;

    public static void compress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Char.reader input = new IO.Char.reader(inputFilename);
                IO.Bit.writer output = new IO.Bit.writer(outputFilename)) {
            compress(input, output);
        }
    }

    private static void compress(final IO.Char.reader input, final IO.Bit.writer output) throws IOException {
        output.write(MAGIC_BYTE);

        String chars = "";
        int num = 1;
        int nchar = 0;
        int codnum = 0;
        boolean newchar = true;
        int chin = input.read();
        while (chin != -1) {
            final char last_char = (char) chin;
            final String charac = chars + last_char;
            if (compress_dict.get(charac) != null) {
                newchar = false;
                chars = charac;
            } else {
                if (newchar) {
                    codnum = 0;
                } else {
                    newchar = true;
                    codnum = compress_dict.get(chars);
                    chars = "";
                }
                compress_dict.put(charac, num);
                ++num;
                final int nbits = bits_needed(nchar); // Numero de bits en que hay que codificar el nchar
                final BitSetL bs_num = new BitSetL(codnum, nbits);
                output.write(bs_num);
                final BitSetL bs_char = new BitSetL((int) last_char, 16);
                output.write(bs_char);
                ++nchar;
            }
            chin = input.read();
        }
        // Si aun quedan letras por codificar estas ya estan en el diccionario,
        // simplemente
        // se obtiene el value del diccionario y se escribe junto con un char vacio, 16
        // bits a 0.
        if (newchar == false) {
            codnum = compress_dict.get(chars);
            final int nbits = bits_needed(nchar); // Numero de bits en que hay que codificar el nchar
            final BitSetL bs_num = new BitSetL(codnum, nbits);
            output.write(bs_num);
            for (int i = 0; i < 16; ++i)
                output.write(false);
        }
        input.close();
    }

    private static int bits_needed(final int n) {
        if (n <= 0)
            return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10) + 1;
    }

    public static void decompress(final String inputFilename, final String outputFilename) throws IOException {
        try (IO.Bit.reader input = new IO.Bit.reader(inputFilename);
                IO.Char.writer output = new IO.Char.writer(outputFilename)) {
            decompress(input, output);
        }
    }

    private static void decompress(final IO.Bit.reader input, final IO.Char.writer output) throws IOException {
        input.readByte();

        int num = 1;
        int nchar = 0;
        int number;
        String charac = "";
        final int first_char = input.readBitSet(16).asInt();
        charac += (char) first_char;
        output.write((char) first_char);
        decompress_dict.put(num, charac);
        ++num;
        ++nchar;
        int nbits = bits_needed(nchar);
        try {
            for (;;) {
                number = input.readBitSet(nbits).asInt();

                final int last_char = input.readBitSet(16).asInt();
                charac = "";

                if (number > 0) {
                    if (last_char > 0)
                        charac = decompress_dict.get(number) + (char) last_char;
                    else
                        charac = decompress_dict.get(number);
                    output.write(charac);
                } else {
                    charac += (char) last_char;
                    output.write((char) last_char);
                }
                decompress_dict.put(num, charac);
                ++num;
                ++nchar;
                nbits = bits_needed(nchar);
            }

        } catch (final EOFException e) {
            // EOF!
        }
    }

}

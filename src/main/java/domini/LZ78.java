package domini;

import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

public class LZ78 {
    private static HashMap<String, Byte> compress_dict = new HashMap<String, Byte>();
    private static HashMap<Byte, String> decompress_dict = new HashMap<Byte, String>();

    public static void compressor(IO.Char.reader input, IO.Bit.writer output) throws IOException {

        /*char patata = 5;
        boolean [] bits = intToNBits(patata, 6);
        for(int j = bits.length-1; j >= 0; --j) output.write(bits[j]);*/

        String chars = "";
        byte num = 1;
        int nchar = 0;
        byte codnum = 0;
        boolean newchar = true;
        int chin = input.read();
        while (chin != -1) {
            char last_char = (char) chin;
            String charac = chars + last_char;
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
                //byte letter = (byte) last_char; //String convertido a bytes
                int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
                //BitSetL bs = new BitSetL(codnum, nbits);
                boolean [] bits_num = intToNBits(codnum, nbits);
                for(int j = bits_num.length-1; j >= 0; --j) output.write(bits_num[j]); //Escribe el codnum con los bits necesarios
                //BitSetL lt = new BitSetL((int)last_char, 16);
                boolean[] bits_char = intToNBits((int)last_char, 16);
                for(int j = 15; j >= 0; --j) output.write(bits_char[j]);
                ++nchar;
            }
            chin = input.read();
        }
        //Si aun quedan letras por codificar estas ya estan en el diccionario, simplemente
        //se obtiene el value del diccionario y se escribe junto con un byte vacio, 8 bits a 0.
        if (newchar == false) {
            codnum = compress_dict.get(chars);
            int nbits = bits_needed(nchar); //Numero de bits en que hay que codificar el nchar
            //BitSetL bs = new BitSetL(codnum, nbits);
            boolean [] bits_num = intToNBits(codnum, nbits);
            for(int j = bits_num.length-1; j >= 0; --j) output.write(bits_num[j]);  //Escribe el codnum con los bits necesarios
            for(int i=0; i<16; ++i) output.write(false);
        }
        input.close();
    }

    private static int bits_needed(int n){
        if (n<=0) return 0;
        return (int) (Math.log(n) / Math.log(2) + 1e-10)+1;
    }

    private static boolean[] intToNBits(int cInt, int n) {
        boolean[] bits = new boolean[n];
        for(int i = 0; i < n; ++i) {
            int aux = cInt % 2;
            if(aux == 1) bits[i] = true;
            cInt /= 2;
        }
        return bits;
    }

    public static void decompressor(IO.Bit.reader input, IO.Char.writer output) throws IOException{

        byte num = 1;
        int nchar=0;
        int number;
        boolean first;
        try {
            for (;;) {
                if (input.read()) first=true;
                else first=false;
                String charac="";
                int nbits = bits_needed(nchar);
                boolean [] n= new boolean[nbits];
                for (int i = 1; i<nbits; ++i) {
                    if (input.read()) n[i]=true;
                    else n[i]=false;
                }
                number=bit_to_int(n);
                //number = Integer.parseInt(n,2);
                boolean[] character = new boolean[16];
                if (nbits==0){
                    if (first) character[0]=true;
                    else character[0]=false;
                    for(int i=1; i<16; ++i){
                        if (input.read()) character[i]=true;
                        else character[i]=false;
                    }
                }else{
                    for(int i=0; i<16; ++i){
                        if (input.read()) character[i]=true;
                        else character[i]=false;
                    }
                }
                //String charact="";

                //for(int i=0; i<16; ++i) System.out.println("character["+ i +"]" + "="+character[i]);
                int last_char = bit_to_int(character);
                //if (nbits>0 && nbits%2==0) ++last_char;
                //System.out.println("number="+number);



                //int last_char = Integer.parseInt(charact,2);
                //System.out.println("last_char="+last_char);
                //System.out.println("que en char="+(char)last_char);
                if (number > 0){
                    charac = decompress_dict.get((byte)number) + (char) last_char;
                    //for (int i=0; i<charac.length(); ++i)  output.write(charac.charAt(i));
                    output.write(charac);
                } else {
                    charac += (char) last_char;
                    output.write((char) last_char);
                }
                decompress_dict.put(num, charac);
                ++num;
                ++nchar;
            }
        }catch (EOFException e) {
            //EOF!
        }
        input.close();
    }

    public static int bit_to_int (boolean[] b){
        int result=0;
        for (int i=0; i<b.length-1; ++i) {
            if (b[i]) result += Math.pow(2,b.length - 1 - i);
        }
        return result;
    }

}

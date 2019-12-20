import domini.*;

import java.io.File;

class AutoSelectionTest {
    public static void main(String[] args) {
        new File("generated/").mkdirs();
        final String[] fileIn = {"texts/Blank.txt","texts/Prova.txt","texts/DonQuijote.txt","texts/Large.txt",
                "texts/HolyBible.pdf",
                "images/gradient8.ppm","images/gonza.ppm","images/lake.ppm",
                "images/cat.jpg","images/lizard.jpg",
                "images/chickenJoe.png",
                "audio/APM_Parlant_clar_i_catala.mp3","audio/marenostrum.mp3",
                "video/Frases_Rajoy.mp4","video/Gatos_Vs_Pepinos.mp4","video/ShinChan.mp4"
        };
        final String fileComp = "generated/out.piz";

        final String algName[] = {"LZ78","LZSS","LZW "};

        for (int i=0; i<fileIn.length; ++i) {
            System.out.println("[" + (i+1) + "] Archivo: " + fileIn[i]);
            for (int j = 0; j < algName.length; ++j) {
                try {
                    CtrlDomini.getInstance().compress(j+1, fileIn[i], fileComp, (short) 0);

                    System.out.println(" - " + algName[j] + ": " + CtrlDomini.getInstance().getDeflated());
                } catch (Exception e){

                }
            }
            System.out.println();
        }
    }
}

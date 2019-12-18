import domini.*;

class AutoSelectionTest {
    public static void main(String[] args) {
        new File("generated/").mkdirs();
        final String[] fileIn = {"texts/Blank.txt","texts/DonQuijote.txt","texts/Large.txt",
                "images/gonza.ppm","images/lake.ppm",
                "images/cat.jpg","images/lizard.jpg",
                "images/chickenJoe.png",
                "audio/APM_Parlant_clar_i_catala.mp3","audio/marenostrum.mp3",
                "video/Frases_Rajoy.mp4","video/Gatos_Vs_Pepinos.mp4","video/ShinChan.mp4"
        };
        final String fileComp = "generated/out.piz";

        final String algName[] = {"LZ78","LZSS","LZW "};

        for (int i=0; i<fileIn.length; ++i) {
            System.out.println("[" + (i+1) + "] Archivo: " + fileIn[i]);
            for (int j = 1; j <= algName.length; ++j) {
                CtrlDomini ctrlDom = new CtrlDomini();
                ctrlDom.compress(j, fileIn[i], fileComp, (short) 0);

                System.out.println(" - " + algName[j] + ": " + ctrlDom.getDeflated());
            }
            System.out.println();
        }
    }
}
package hepl.test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ImageHandlerSax extends DefaultHandler {
    private static final boolean DEBUG = false; // passe à true si tu veux voir les logs

    private int imageCount = 0;
    //Savoir si on est dans une balise Localization
    private boolean inLocalization = false;
    //Savoir si le record courant contient une Localization ou non ? Remit a false a chaque début de Record
    private boolean hasLocRight = false;
    //Savoir si on est dans une balise Record
    private boolean inRecord = false;
    private StringBuilder buffer = new StringBuilder();

    public int getImageCount() {
        return imageCount;
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("record")) {
            inRecord = true;
            hasLocRight = false; //reset le flag pour ce record, aucun locright trouvé tq mtn
            if (DEBUG) System.out.println("→ record balise");
        }

        if (qName.equals("Localizations")) {
            inLocalization = true;
            buffer.setLength(0); //vidage buffer pour accumulé txt de cette localization
            if (DEBUG) System.out.println("→ Localizations balise");
        }
    }
//Appelées à chaque fois que Sax rencontre du texte
    @Override
    public void characters(char[] ch, int start, int length) {
        if (inLocalization) { //si true alors on ajoute le code au buffer
            buffer.append(ch, start, length);//buffer,index debut,taille
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("Localizations")) {
            inLocalization = false; //on sort de la balise localization

            String loc = buffer.toString().trim().toLowerCase(); //on recup tout le txt

            if (loc.contains("loc right")) { //on vérif si y'a des loc right
                hasLocRight = true;
                if (DEBUG) System.out.println("→ loc right trouvée dans ce record");
            }
        }

        if (qName.equals("record")) {
            // Quand on sort du Record, on vérifie le flag
            if (hasLocRight) {
                imageCount++;
                if (DEBUG) System.out.println("→ record avec loc right, imageCount = " + imageCount);
            }
            inRecord = false;
        }
    }
}

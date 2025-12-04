package hepl.test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import java.util.HashMap;
import java.util.Map;

public class LabelHandlerSax extends DefaultHandler {
    //Est-on dans une balise Label?
    private boolean isTargetElement = false;
    //accumule le texte trouvé dans un label
    private StringBuilder currentText = new StringBuilder();
  // map associe la clé ( txt du label ) avec une valeur   ( cmb de fois il apparait ) s
    private final Map<String, Integer> labelcount = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("Label")) {
            isTargetElement = true; // on dit que ce qui va suivre dans characters() est du txt label
            currentText.setLength(0); //vide buffer
        }
        // On récupère la valeur de l'attribut "Labels"
        String raw = attributes.getValue("Labels");
        if (raw != null) {
            // On nettoie un peu : on enlève guillemets, crochets, apostrophes
            raw = raw.replace("\"", "")
                    .replace("'", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();
            // On met le résultat dans le buffer, comme si c'était du texte de la balise
            currentText.append(raw);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if(isTargetElement) {
            //construit un string sur base du txt recuo
            currentText.append(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equals("Label")) {
            isTargetElement = false; //sort label
            String label = currentText.toString().trim().toLowerCase(); //trim enleve les espaces en debut & fin

            label = label.replaceAll("\\s+", " "); //remplace séquence d'espace par 1 seul

            if(!label.isEmpty()) {
                //on incrémente le compteur ici, on vérifie qu'il soit pas déja dans la map, si oui on envoie la valeur, sinon ca renvoie 0  + 1 = on add un occurence
                labelcount.put(label, labelcount.getOrDefault(label, 0) + 1);
            }
        }
    }

    public Map<String, Integer> getLabelCount(){
        return labelcount;
    }
}

package hepl.test;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import java.util.HashMap;
import java.util.Map;

public class LabelHandlerSax extends DefaultHandler {
    // Est-on dans une balise LabelItem ?
    private boolean isTargetElement = false;
    // Accumule le texte trouvé dans un LabelItem
    private StringBuilder currentText = new StringBuilder();
    // map : label -> nombre d'occurrences
    private final Map<String, Integer> labelcount = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("LabelItem")) {
            isTargetElement = true;
            currentText.setLength(0); // reset buffer
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isTargetElement) {
            currentText.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("LabelItem")) {
            isTargetElement = false;

            String label = currentText.toString().trim().toLowerCase();

            // Nettoyage léger : on enlève les quotes simples/doubles
            label = label.replace("'", "").replace("\"", "");
            // Normalisation des espaces
            label = label.replaceAll("\\s+", " ");

            if (!label.isEmpty()) {
                labelcount.put(label, labelcount.getOrDefault(label, 0) + 1);
            }
        }
    }

    public Map<String, Integer> getLabelCount() {
        return labelcount;
    }
}

package hepl.test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import java.util.HashMap;
import java.util.Map;

public class LabelHandlerSax extends DefaultHandler {
    private boolean isTargetElement = false;
    private StringBuilder currentText = new StringBuilder();

    private final Map<String, Integer> labelcount = new HashMap<>();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equals("Label")) {
            isTargetElement = true;
            currentText.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if(isTargetElement) {
            currentText.append(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equals("Label")) {
            isTargetElement = false;
            String label = currentText.toString().trim().toLowerCase();

            label = label.replaceAll("\\s+", " ");

            if(!label.isEmpty()) {
                labelcount.put(label, labelcount.getOrDefault(label, 0) + 1);
            }
        }
    }

    public Map<String, Integer> getLabelCount(){
        return labelcount;
    }
}

package hepl.test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ImageHandlerSax extends DefaultHandler {
    private int imageCount = 0;

    private boolean inLocalization = false;
    private boolean hasLocRight = false;
    private boolean inRecord = false;
    private StringBuilder buffer = new StringBuilder();

    public int getImageCount() {
        return imageCount;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("Record")) {
            inRecord = true;
            hasLocRight = false;
        }

        if (qName.equals("Localization")) {
            inLocalization = true;
            buffer.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (inLocalization) {
            buffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("Localization")) {
            inLocalization = false;

            String loc = buffer.toString().trim().toLowerCase();

            if (loc.contains("loc right")) {
                hasLocRight = true;
            }
        }

        if (qName.equals("Record")) {
            // Quand on sort du Record, on vérifie le flag
            if (hasLocRight) {
                imageCount++;
            }
            inRecord = false;
        }
    }
}

package hepl.test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class CompositeHandler extends DefaultHandler {
    private final List<DefaultHandler> handlers = new ArrayList<>();

    public void addHandler(DefaultHandler h) {
        handlers.add(h);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        for (DefaultHandler h : handlers) {
            h.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        for (DefaultHandler h : handlers) {
            h.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (DefaultHandler h : handlers) {
            h.endElement(uri, localName, qName);
        }
    }
}

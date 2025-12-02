package hepl.test;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Sax extends DefaultHandler {

    public static void main(String[] args) {
        String XmlFile = "src/main/resources/csv.xml";
        try {
            //Code repris du cours
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser sp = factory.newSAXParser();

            sp.getXMLReader().parse(XmlFile);
            //Création des Handler comme demamndé dans énoncé
            LabelHandlerSax labelhandler = new LabelHandlerSax();
            ImageHandlerSax imagehandler = new ImageHandlerSax();
            //On peut pas attacher plusieurs Handler a un seul Sax il est donc nécessaire d'utiliser un Composit
            CompositeHandlerSax composite = new CompositeHandlerSax();
            composite.addHandler(labelhandler);
            composite.addHandler(imagehandler);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

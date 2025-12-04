package hepl.test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Sax extends DefaultHandler {

    public static void main(String[] args) {
        // Désactivation des limites de sécurité JAXP
        System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");  // taille max d'une entité
        System.setProperty("jdk.xml.totalEntitySizeLimit", "0");       // taille totale cumulée des entités
        System.setProperty("jdk.xml.entityExpansionLimit", "0");       // nombre d'expansions d'entités
        System.setProperty("jdk.xml.maxElementDepth", "0");            // profondeur max de balises


        String XmlFile = "src/main/resources/csv.xml";
        try {
            //Code repris du cours
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser sp = factory.newSAXParser();
            XMLReader reader = sp.getXMLReader();

            //Création des Handler comme demamndé dans énoncé
            LabelHandlerSax labelhandler = new LabelHandlerSax();
            ImageHandlerSax imagehandler = new ImageHandlerSax();
            //On peut pas attacher plusieurs Handler a un seul Sax il est donc nécessaire d'utiliser un Composit
            CompositeHandler composite = new CompositeHandler();
            composite.addHandler(labelhandler);
            composite.addHandler(imagehandler);

            reader.setContentHandler(composite);
            //Attache le SimpleErrorHandler
            reader.setErrorHandler(new SimpleErrorHandler());
            //On parse le doc
            reader.parse(XmlFile);
            System.out.println("[SAX] Validation terminée ! (DTD OK)");
            // -------- Top 10 labels les plus fréquents --------
            System.out.println("---- Top 10 labels les plus fréquents ----");

            Map<String, Integer> counts = labelhandler.getLabelCount();

            // On met les entrées de la map dans une liste
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());

            // On trie par valeur décroissante (du plus fréquent au moins fréquent)
            entries.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

            // On affiche au maximum les 10 premiers
            int limit = Math.min(10, entries.size());
            for (int i = 0; i < limit; i++) {
                Map.Entry<String, Integer> entry = entries.get(i);
                System.out.println((i + 1) + ". " + entry.getKey() + " : " + entry.getValue());
            }

// (optionnel) affichage du nombre total de labels distincts
            System.out.println("Nombre total de labels distincts : " + counts.size());
            System.out.println("Images avec loc right = " + imagehandler.getImageCount());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

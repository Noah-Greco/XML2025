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
        System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
        System.setProperty("jdk.xml.totalEntitySizeLimit", "0");
        System.setProperty("jdk.xml.entityExpansionLimit", "0");
        System.setProperty("jdk.xml.maxElementDepth", "0");


        String XmlFile = "src/main/resources/csv.xml";
        try {
            //Code repris du cours
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(true);//Valide le doc par rapport au DTD
            factory.setNamespaceAware(true);//Si Naspace, Sax les comprendra

            SAXParser sp = factory.newSAXParser();
            //Créer le XMLReader qui permet de lire le XML et envoyer les event au Handler
            XMLReader reader = sp.getXMLReader();

            //Création des Handler comme demamndé dans énoncé
            LabelHandlerSax labelhandler = new LabelHandlerSax();
            ImageHandlerSax imagehandler = new ImageHandlerSax();
            CompositeHandler composite = new CompositeHandler();
            composite.addHandler(labelhandler);
            composite.addHandler(imagehandler);

            reader.setContentHandler(composite); //envoie-les events au composite et donc au 2 autres aussi
            //Attache le SimpleErrorHandler
            reader.setErrorHandler(new SimpleErrorHandler());
            //On lis le xml en entier
            reader.parse(XmlFile);
            System.out.println("[SAX] Validation terminée ! (DTD OK)");

            System.out.println("---- Top 10 labels les plus fréquents ----");
            //Recup la map des compteurs
            Map<String, Integer> counts = labelhandler.getLabelCount();
            // Tranforme map en liste retourne string pour avoir paire clé(nom) et int pour valeur(nb occruence)
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
            // On trie par valeur décroissante (du plus fréquent au moins fréquent)
            //e1 et e2 sont 2 map.entry, on compare les 2  et si e2 > e1 alors ca renvoie un int positif et echange leur place
            entries.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));
            // On affiche 10 premiers, entries.size est la taille de notre map, math.min renvoie le + ptit des 2 donc ici 10 max
            int limit = Math.min(10, entries.size());
            for (int i = 0; i < limit; i++) {
                Map.Entry<String, Integer> entry = entries.get(i);//recup les 10
                System.out.println((i + 1) + ". " + entry.getKey() + " : " + entry.getValue());
            }

            System.out.println("Nombre total de labels distincts : " + counts.size());
            System.out.println("Images avec loc right = " + imagehandler.getImageCount());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

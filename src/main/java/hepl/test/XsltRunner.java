package hepl.test;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XsltRunner {
    public static void main(String[] args) throws Exception {
        // Désactivation des limites de sécurité JAXP
        System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
        System.setProperty("jdk.xml.totalEntitySizeLimit", "0");
        System.setProperty("jdk.xml.entityExpansionLimit", "0");
        System.setProperty("jdk.xml.maxElementDepth", "0");

        File xml = new File("src/main/resources/csv.xml");
        File xslt = new File("src/main/resources/xslt.xslt");
        File html = new File("C:/xampp/htdocs/PadChest/result.html");

        // Prépare la transformation xml--> html grâce a la feuille de style xslt
        TransformerFactory factory = TransformerFactory.newInstance(); //usine qui créer des transformateurs XSLT
        Source xsltSource = new StreamSource(xslt);//englobe dans streamsource pour que api puisse le lire
        Transformer transformer = factory.newTransformer(xsltSource);// charge xslt.xslt et donne un transformeur pret a utilisation

        Source xmlSource = new StreamSource(xml);
        Result output = new StreamResult(html);

        // Lance la transformation
        transformer.transform(xmlSource, output);

        System.out.println("HTML généré dans: " + html.getAbsolutePath());
    }
}

package hepl.test;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {

    private boolean valid = true;   // passe à false dès qu'on a une erreur

    public boolean isValid() {
        return valid;
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("AVERTISSEMENT (ligne " + e.getLineNumber() + ") : " + e.getMessage());
        // un warning n'invalide pas forcément le document, donc on ne touche pas à valid
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        System.out.println("ERREUR (ligne " + e.getLineNumber() + ") : " + e.getMessage());
        valid = false;
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("ERREUR FATALE (ligne " + e.getLineNumber() + ") : " + e.getMessage());
        valid = false;
        throw e; // en général on arrête le parsing sur une fatalError
    }
}

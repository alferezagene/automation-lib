package AutomationLibrary.Utilities;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class NameSpace implements NamespaceContext {
    private Document sourceDocument;
    private String prefix;


    public NameSpace(Document document) {
        sourceDocument = document;

    }

    public NameSpace(Document document, String prefix) {
        sourceDocument = document;
        this.prefix = prefix;

    }

    public String getNamespaceURI(String prefix) {
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return sourceDocument.lookupNamespaceURI(null);
        } else {
            return sourceDocument.lookupNamespaceURI(prefix);
        }
    }

    public String getPrefix(String namespaceURI) {
        return sourceDocument.lookupPrefix(namespaceURI);
    }

    public Iterator getPrefixes(String namespaceURI) {
        // not implemented yet
        return null;
    }

}

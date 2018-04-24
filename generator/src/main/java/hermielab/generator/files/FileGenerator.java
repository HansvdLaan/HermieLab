package hermielab.generator.files;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import hermielab.generator.settings.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.Thread.sleep;


public class FileGenerator {
    
    private Settings settings;
    private List<BiFunction<Settings, Element, Element>> transformations;
    private Document document;
    private Element root;

    public FileGenerator(String documentName, String rootName){
        this.document = DocumentHelper.createDocument();
        document.setName(documentName);
        root = document.addElement(rootName);
        transformations = new ArrayList<>();
        this.settings = new Settings();
    }

    public List<BiFunction<Settings, Element, Element>> getTransformations() {
        return transformations;
    }

    
    public void setTransformations(List<BiFunction<Settings, Element, Element>> transformations) {
        this.transformations = transformations;
    }

    public void addTransformation(BiFunction<Settings, Element, Element> transformation) {
        transformations.add(transformation);
    }

    public void addAllTransformations(Collection<BiFunction<Settings, Element, Element>> transformations) {
        this.transformations.addAll(transformations);
    }

    public void applyTransformations() {
        Element elem = root;
        for (BiFunction<Settings, Element, Element> function: transformations){
            elem = function.apply(settings,elem);
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Document getDocument() {
        return document;
    }

    @Deprecated
    public void writeToFile(String path){
        // Pretty print the document to a file
        if (!path.equals("")){
            path += "/";
        }
        File file = new File(path + document.getName() + ".xml");
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
        XMLWriter writer;
        try {
            FileOutputStream fop = new FileOutputStream(file);
            writer = new XMLWriter( fop, format );
            writer.write( document );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

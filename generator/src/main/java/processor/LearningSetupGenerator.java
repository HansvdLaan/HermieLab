package processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import component.ComponentGenerator;
import component.GeneratedSpecContainer;
import files.FileGenerator;
import org.dom4j.Document;
import org.dom4j.io.XMLWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LearningSetupGenerator {
    
    private List<FileGenerator> fileGenerators;
    private List<Document> generatedDocuments;
    private List<ComponentGenerator> componentGenerators;
    private List<TypeSpec> generatedComponents;
    private ProcessingEnvironment processingEnv;

    public LearningSetupGenerator(ProcessingEnvironment processingEnv){
        this.processingEnv = processingEnv;
    }
    
    public abstract List<FileGenerator> initializeFileGenerators();
    
    public List<Document> generateDocument(){
        List<Document> generatedDocuments = new ArrayList<>();
        for (FileGenerator generator: fileGenerators){
            generator.applyTransformations();
            generatedDocuments.add(generator.getDocument());
        }
        return generatedDocuments;
    }

    
    public abstract List<ComponentGenerator> initializeCompontentGenerators();
    
    public List<TypeSpec> generateCompontents(){
        List<TypeSpec> generatedComponents = new ArrayList<>();
        GeneratedSpecContainer generatedSpecs = new GeneratedSpecContainer();
        for (ComponentGenerator generator: componentGenerators){
            generator.setGeneratedSpecs(generatedSpecs);
            generator.applyTransformations();
            generatedSpecs = generator.getGeneratedSpecs();
            TypeSpec component = generator.generateComponent();
            generatedComponents.add(component);
        }
        return generatedComponents;
    }
    
    public void generateLearningSetup(){
        this.fileGenerators = initializeFileGenerators();
        this.generatedDocuments = generateDocument();
        this.componentGenerators = initializeCompontentGenerators();
        this.generatedComponents = generateCompontents();

        for (Document document: generatedDocuments){
            writeToFile(document);
        }
        for (TypeSpec typeSpec: generatedComponents){
            writeToFile(typeSpec);
        }
    }

    public List<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    public void setFileGenerators(List<FileGenerator> fileGenerators) {
        this.fileGenerators = fileGenerators;
    }

    public List<Document> getGeneratedDocuments() {
        return generatedDocuments;
    }

    public void setGeneratedDocuments(List<Document> generatedDocuments) {
        this.generatedDocuments = generatedDocuments;
    }

    public List<ComponentGenerator> getComponentGenerators() {
        return componentGenerators;
    }

    public void setComponentGenerators(List<ComponentGenerator> componentGenerators) {
        this.componentGenerators = componentGenerators;
    }

    public void setGeneratedComponents(List<TypeSpec> generatedComponents) {
        this.generatedComponents = generatedComponents;
    }

    public ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }

    public void setProcessingEnv(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public void writeToFile(TypeSpec typeSpec){
        JavaFile javaFile = JavaFile.builder("hermielab", typeSpec).indent("\t").build();
        String content = javaFile.toString();
        try {
            try (Writer writer = generateResource(javaFile.packageName, javaFile.typeSpec.name).openWriter()) {
                writer.write(content);
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    public void writeToFile(Document document) {
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
        try {
            OutputStream outputStream = generateResource("hermielab", document.getName()).openOutputStream();
            XMLWriter writer = new XMLWriter(outputStream, format);
            writer.write(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileObject generateResource(String packageName, String fileName) throws IOException {
        return processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, packageName, fileName);
    }

}

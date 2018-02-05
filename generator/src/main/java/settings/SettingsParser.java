package settings;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import settings.containers.GeneratorInformationElement;
import settings.containers.GeneratorInformationElementParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsParser {

    public static List<Settings> readSettings(List<Document> documents) {
        List<Settings> settingsList = new ArrayList<>();
        for (Document document : documents) {
            settingsList.add(readSettings(document));
        }
        return settingsList;
    }

    public static Settings readSettings(Document document){
        Settings settings = new Settings();
        GeneratorInformationElementParser parser = new GeneratorInformationElementParser();
        List<Node> classNodes = document.selectNodes("/settings/child::*");
        for (Node node : classNodes) {
            GeneratorInformationElement element = parser.parse(node);
            settings.getAllSettings().putIfAbsent(element.getType(), new HashMap<>());
            settings.addSettingsByTypeAndID(element.getType(), element.getId(), element);
        }
        return settings;
    }

    public static List<Document> findSettings() {
        List<Document> settingsDocuments = new LinkedList<>();
        try {
            List<Path> paths = Files.find(fetchSourcePath(),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) -> path.toFile().getName().matches("settings[0-9]*.xml")
            ).collect(Collectors.toList());
            for (Path path : paths) {
                settingsDocuments.add(toDocument(path.toFile()));
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return settingsDocuments;
    }

    public static Document toDocument(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    //Dirty method: see https://stackoverflow.com/questions/22494596/eclipse-annotation-processor-get-project-path for discussoin
    private static Path fetchSourcePath() {
        return Paths.get(System.getProperty("user.dir"));
    }
}

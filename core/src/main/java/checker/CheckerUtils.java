package checker;

import checker.CheckerSettings;
import checker.nfa.NFABuilder;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CheckerUtils {

    public static List<CheckerSettings> loadCheckerSettings(){
        List<Document> checkerSettingsDocuments = findSettings();
        return readSettings(checkerSettingsDocuments);
    }

    public static List<CheckerSettings> readSettings(List<Document> documents) {
        List<CheckerSettings> settingsList = new ArrayList<>();
        for (Document document : documents) {
            settingsList.add(readSettings(document));
        }
        return settingsList;
    }

    public static CheckerSettings readSettings(Document document){
        CheckerSettings settings = new CheckerSettings();
        //GeneratorInformationElementParser parser = new GeneratorInformationElementParser();
        List<Node> checkerNodes = document.selectNodes("/checkers/child::*");
        List<Node> nfaCheckerNodes = checkerNodes.subList(0, checkerNodes.size()-1);
        Node predicateCheckerNode = checkerNodes.get(checkerNodes.size()-1);
        for (Node nfaCheckerNode: nfaCheckerNodes) {
            for (DefaultElement groupNode : getChildren((DefaultElement) nfaCheckerNode)) {
                CheckerSettings groupSettings = parseGroup(((DefaultElement) nfaCheckerNode).getName(), groupNode);
                settings.getAllNFAs().putAll(groupSettings.getAllNFAs());
            }
        }
        for (DefaultElement inputNode: getChildren((DefaultElement) predicateCheckerNode)) {
            String id = "";
            List<String> predicates = new ArrayList<>();
            for (DefaultElement inputParam: getChildren(inputNode)) {
                if (inputParam.getQName().getName().equals("id")) {
                    id = inputParam.getText();
                } else if (inputParam.getQName().getName().equals("predicate")) {
                    predicates.add(inputParam.getText());
                }
            }
            settings.addPredicates(id,predicates);
        }
        return settings;
    }

    public static List<Document> findSettings() {
        List<Document> settingsDocuments = new LinkedList<>();
        try {
            List<Path> paths = Files.find(fetchSourcePath(),
                    Integer.MAX_VALUE,
                    (path, basicFileAttributes) -> path.toFile().getName().matches("checkers[0-9]*.xml")
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

    public static List<DefaultElement> getChildren(DefaultElement node){
        return node.content().stream().filter(elem -> elem instanceof DefaultElement)
                .map(elem -> (DefaultElement) elem).collect(Collectors.toList());
    }

    public static CheckerSettings parseGroup(String checkerID, DefaultElement groupNode){
        CheckerSettings settings = new CheckerSettings();
        String groupID = "";
        for (DefaultElement param: getChildren(groupNode)) {

            if (param.getQName().getName().equals("id")) {
                groupID = param.getText();

            } else if (param.getQName().getName().equals("nfa")) {
                String nfaID = "";
                String fileName = "";
                List<String> transitions = new ArrayList<>();

                List<DefaultElement> nfaParams = getChildren(param);
                for (DefaultElement nfaParam : nfaParams) {
                    if (nfaParam.getQName().getName().equals("name")) {
                        nfaID = nfaParam.getText();
                    } else if (nfaParam.getQName().getName().equals("file")) {
                        fileName = nfaParam.getText();
                    } else if (nfaParam.getQName().getName().equals("transitions")) {
                        transitions = getChildren(nfaParam).stream()
                                .map(transition -> transition.getText()).collect(Collectors.toList());
                    }
                }

                Map<String, Set<String>> transitionMap = new HashMap<>();
                for (String transition : transitions) {
                    String originalEdge = transition.split(",")[0];
                    String newEdge = transition.split(",")[1];
                    transitionMap.putIfAbsent(originalEdge, new HashSet<>());
                    transitionMap.get(originalEdge).add(newEdge);
                }

                FiniteStateAutomaton nfa = NFABuilder.generateNFA(new File(fileName), transitionMap);
                settings.addNFA(checkerID, groupID, nfaID, nfa);
            }
        }
        return settings;
    }
}

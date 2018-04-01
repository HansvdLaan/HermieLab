package learningsetup;

import checker.CheckerSettings;
import checker.nfa.NFABuilder;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;
import edu.duke.cs.jflap.file.xml.AutomatonTransducer;
import mapper.ConcreteInvocation;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.SimpleAlphabet;
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

import static checker.CheckerUtils.getChildren;

/**
 * Created by Hans on 2-3-2018.
 */
public class LearningSetupUtils {

    public static Map<String, Map<String, Object>> parseMappings(Document document) {
        Map<String, Map<String, Object>> mappings = new HashMap<>();
        List<Node> classNodes = document.selectNodes("/mappings/child::*");

        for (Node node : classNodes) {
            String elementType = node.getName();
            switch (elementType) {
                case "mapping":
                    Map<String, Map<String, Object>> mapping = parseMapping(node);
                    for (String key : mapping.keySet()) {
                        mappings.putIfAbsent(key, new HashMap<>());
                        mappings.get(key).putAll(mapping.get(key));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unkown element of type:" + elementType + " in the mappings");
            }
        }

        return mappings;
    }

    public static Map<String, Map<String, Object>> parseMapping(Node node){
        Map<String,Map<String,Object>> inputSymbolMap = new HashMap<>();
        List<Node> attributeNodes = node.selectNodes("./child::*");
        Map<String,String> nodeAttributes = new HashMap<>();
        for (Node attributeNode: attributeNodes){
            nodeAttributes.put(attributeNode.getName(), attributeNode.getText());
        }
        String ao = nodeAttributes.get("ao");
        String ai = nodeAttributes.get("ai");
        String co = nodeAttributes.get("co");
        String ci = nodeAttributes.get("ci");

        if (ao == null || ai == null || co == null || ci == null) {
            throw new IllegalArgumentException("mapping: " + node.toString() + " cannot be parsed");
        } else {
            inputSymbolMap.put("inputmapping",new HashMap<>());
            inputSymbolMap.get("inputmapping").put(ai, new ConcreteInvocation(ci,co, new LinkedList<>()));
            return inputSymbolMap;
        }
    }

    public static CheckerSettings parseCheckers(Document document, Path nfaFolder){
        CheckerSettings settings = new CheckerSettings();
        List<Node> checkerNodes = document.selectNodes("/checkers/child::*");
        List<Node> nfaCheckerNodes = checkerNodes.subList(0, checkerNodes.size()-1);
        Node predicateCheckerNode = checkerNodes.get(checkerNodes.size()-1);
        for (Node nfaCheckerNode: nfaCheckerNodes) {
            for (DefaultElement groupNode : getChildren((DefaultElement) nfaCheckerNode)) {
                CheckerSettings groupSettings = parseGroup(((DefaultElement) nfaCheckerNode).getName(), groupNode, nfaFolder);
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

    public static CheckerSettings parseGroup(String checkerID, DefaultElement groupNode, Path nfaFolder){
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

                FiniteStateAutomaton nfa = NFABuilder.generateNFA(Paths.get(nfaFolder.toString(), fileName).toFile(), transitionMap);
                settings.addNFA(checkerID, groupID, nfaID, nfa);
            }
        }
        return settings;
    }

    public static Map<String, AbstractExperiment> parseExperiments(Document document) {
        Map<String, AbstractExperiment> experiments = new HashMap<>();
        List<Node> experimentNodes = document.selectNodes("/experiments/child::*");

        for (Node node : experimentNodes) {
            String elementType = node.getName();
            switch (elementType) {
                case "experiment":
                    AbstractExperiment experiment = parseExperiment(node);
                    experiments.put(experiment.getID(),experiment);
                    break;
                default:
                    throw new IllegalArgumentException("Unkown element of type:" + elementType + " in the experiments");
            }
        }

        return experiments;
    }

    public static AbstractExperiment parseExperiment(Node node){
        Map<String,Map<String,Object>> inputSymbolMap = new HashMap<>();
        List<Node> attributeNodes = node.selectNodes("./child::*");
        List<Node> alphabetNodes = node.selectNodes("./alphabet/child::*");
        Map<String,String> nodeAttributes = new HashMap<>();
        Alphabet<String> alphabet = new SimpleAlphabet();
        for (Node attributeNode: attributeNodes){
            nodeAttributes.put(attributeNode.getName(), attributeNode.getText());
        }
        for (Node aiNode: alphabetNodes){
            alphabet.add(aiNode.getText());
        }

        String id = nodeAttributes.get("id");
        AutomatonType automaton = parseAutomatonType(nodeAttributes.get("automaton"));
        String membershiporacle = nodeAttributes.get("membershiporacle");
        String equivilanceoracle = nodeAttributes.get("equivilanceoracle");

        return new AbstractExperiment(id, automaton, membershiporacle,equivilanceoracle, alphabet);
    }

    public static Document toDocument(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    public static AutomatonType parseAutomatonType(String automatonType) {
        if (automatonType.toLowerCase().equals("dfa")) {
            return AutomatonType.DFA;
        } else if (automatonType.toLowerCase().equals("mealymachine")) {
            return AutomatonType.MEALYMACHINE;
        } else {
            throw new IllegalArgumentException("cannot parse automatontype:" + automatonType);
        }
    }


}

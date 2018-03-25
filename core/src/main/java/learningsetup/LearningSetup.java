package learningsetup;

import adapter.Adapter;
import checker.Checker;
import checker.CheckerSettings;
import checker.nfa.GUINFAChecker;
import checker.nfa.SingleNFAChecker;
import checker.predicate.PredicateChecker;
import mapper.ConcreteInvocation;
import mapper.SULMapper;
import net.automatalib.words.Alphabet;
import org.dom4j.Document;
import proxy.Proxy;
import testdriver.TestDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class LearningSetup<AO,CO> {


    private Adapter<AO,CO> adapter;
    private Proxy<CO> proxy;
    private SULMapper<String,AO,ConcreteInvocation,CO> mapper;
    private List<Checker> checkers;
    private Alphabet alphabet;
    private AbstractExperiment abstractExperiment;
    private TestDriver testDriver;
    private Path nfaFolder;


    public LearningSetup(Adapter adapter, Document mappingsDocument, Document checkersDocument, Document experimentsDocument,
                         String experimentID, Path nfaFolder) {
        this.adapter = adapter;
        this.proxy = new Proxy(adapter);
        this.mapper = initializeMapper(mappingsDocument, adapter);
        this.checkers = initializeCheckers(adapter, mapper, checkersDocument, nfaFolder);
        this.nfaFolder = nfaFolder;

        Map<String,AbstractExperiment> abstractExperiments = loadExperiments(experimentsDocument);

        if (abstractExperiments.containsKey(experimentID)) {
            abstractExperiment = abstractExperiments.get(experimentID);
            alphabet = abstractExperiment.getAlphabet();
        } else {
            throw new IllegalArgumentException("No experiment with ID:" + experimentID + " can be found");
        }
    }

    public abstract void initializeOracles();

    public abstract void executeExperiment();

    public SULMapper initializeMapper(Document mappingsDocument, Adapter adapter) {
        SULMapper<String,Boolean, ConcreteInvocation, Boolean> mapper = new SULMapper(adapter);
        Map<String, Map<String, Object>> mappings = LearningSetupUtils.parseMappings(mappingsDocument);
        for (String ai: mappings.get("inputmapping").keySet()) {
            mapper.getInputMappings().put(ai, (ConcreteInvocation) mappings.get("inputmapping").get(ai));
        }
        return mapper;
    }

    public List<Checker> initializeCheckers(Adapter adapter, SULMapper<String,AO,ConcreteInvocation,CO> mapper,
                                            Document checkersDocument, Path nfaFolder){
        CheckerSettings settings = LearningSetupUtils.parseCheckers(checkersDocument, nfaFolder);
        List<Checker> checkers = new LinkedList<>();
        if (settings.getAllNFAs().containsKey("guichecker")) {
            GUINFAChecker guiNFAChecker = new GUINFAChecker();
            guiNFAChecker.addNFAs(settings.getNFAs("guichecker"));
            checkers.add(guiNFAChecker);
        }
        if (settings.getAllNFAs().containsKey("nfachecker")) {
            SingleNFAChecker nfaChecker = new SingleNFAChecker();
            nfaChecker.addNFAs(settings.getNFAs("nfachecker"));
            checkers.add(nfaChecker);
        }
        if (!settings.getAllPredicates().isEmpty()) {
            PredicateChecker predicateChecker = new PredicateChecker(adapter, settings.getAllPredicates());
            checkers.add(predicateChecker);
            for (String ci: settings.getAllPredicates().keySet()) {
                for (ConcreteInvocation invocation: mapper.getInputMappings().values()) {
                    if (invocation.getInputInstanceID().equals(ci)) {
                        invocation.getPredicateIDs().addAll(settings.getPredicates(ci)); //TODO: Is this really necessary?
                    }
                }
            }
        }
        return checkers;
    }

    public Map<String,AbstractExperiment> loadExperiments(Document experimentsDocument){
        return LearningSetupUtils.parseExperiments(experimentsDocument);
    }

    public Adapter<AO, CO> getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter<AO, CO> adapter) {
        this.adapter = adapter;
    }

    public Proxy<CO> getProxy() {
        return proxy;
    }

    public void setProxy(Proxy<CO> proxy) {
        this.proxy = proxy;
    }

    public SULMapper<String, AO, ConcreteInvocation, CO> getMapper() {
        return mapper;
    }

    public void setMapper(SULMapper<String, AO, ConcreteInvocation, CO> mapper) {
        this.mapper = mapper;
    }

    public List<Checker> getCheckers() {
        return checkers;
    }

    public void setCheckers(List<Checker> checkers) {
        this.checkers = checkers;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }


    public AbstractExperiment getAbstractExperiment() {
        return abstractExperiment;
    }

    public void setAbstractExperiment(AbstractExperiment abstractExperiment) {
        this.abstractExperiment = abstractExperiment;
    }

    public TestDriver getTestDriver() {
        return testDriver;
    }

    public void setTestDriver(TestDriver testDriver) {
        this.testDriver = testDriver;
    }
}

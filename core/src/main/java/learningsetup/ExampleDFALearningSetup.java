package learningsetup;

import adapter.Adapter;
import checker.Checker;
import checker.CheckerSettings;
import checker.nfa.GUINFAChecker;
import checker.nfa.SingleNFAChecker;
import checker.predicate.PredicateChecker;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.dfa.ExtensibleLStarDFA;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.MembershipOracle;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.experiments.Experiment;
import mapper.ConcreteInvocation;
import mapper.SULMapper;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.Path;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.impl.ArrayAlphabet;
import oracle.DFASULOracle;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import testdriver.TestDriver;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.*;

public class ExampleDFALearningSetup extends LearningSetup<Boolean,Boolean> {

    private int eqOracleMinLenght;
    private int eqOracleMaxLength;
    private int eqOracleMaxTests;
    private Random seed;

    public ExampleDFALearningSetup(Adapter adapter, Path root, String experimentID,
                                   int eqOracleMinLength, int eqOracleMaxLength, int eqOracleMaxTests, Random seed) throws DocumentException {
        super(adapter,
                LearningSetupUtils.toDocument(Paths.get(root.toString(), "mappings.xml").toFile()),
                LearningSetupUtils.toDocument(Paths.get(root.toString(), "checkers.xml").toFile()),
                LearningSetupUtils.toDocument(Paths.get(root.toString(), "experiments.xml").toFile()),
                experimentID,
                Paths.get(root.toString(), "nfa"));
        this.eqOracleMinLenght = eqOracleMinLength;
        this.eqOracleMaxLength = eqOracleMaxLength;
        this.eqOracleMaxTests = eqOracleMaxTests;
        this.seed = seed;

    }

    @Override
    public void initializeOracles() {
        if (Objects.equals(getAbstractExperiment().getAutomatonType(), AutomatonType.DFA)) {
            MembershipOracle.DFAMembershipOracle dfaMembershipOracle = new DFASULOracle(getProxy(), getMapper(), getCheckers());
            EquivalenceOracle.DFAEquivalenceOracle dfaEquivalenceOracle = new RandomWordsEQOracle.DFARandomWordsEQOracle(
                    dfaMembershipOracle, eqOracleMinLenght, eqOracleMaxLength, eqOracleMaxTests, seed);

            setTestDriver(new TestDriver(dfaMembershipOracle, dfaEquivalenceOracle));
        }
        else {
            if (getAbstractExperiment().getAutomatonType() == null) {
                throw new IllegalArgumentException("Automata Type of abstract experiment cannot be null");
            } else {
                throw new IllegalArgumentException("Cannot initialize an learning experiment to learn an automata of type: " + getAbstractExperiment().getAutomatonType().toString());
            }
        }
    }

    @Override
    public void executeExperiment() {
        if (getTestDriver() == null) {
            throw new IllegalStateException("TestDriver is null. Have the oracles already be initialized?");
        }
        ExtensibleLStarDFA dfaLearningAlgorithm = new ExtensibleLStarDFA(
                getAlphabet(),
                (MembershipOracle.DFAMembershipOracle) getTestDriver(),
                new ArrayAlphabet<>(),
                ObservationTableCEXHandlers.CLASSIC_LSTAR,
                ClosingStrategies.CLOSE_FIRST
        );

        Experiment.DFAExperiment experiment = (Experiment.DFAExperiment) getTestDriver().generateExperiment(dfaLearningAlgorithm, getAlphabet());

        experiment.setProfile(true);
        experiment.setLogModels(true);
        getAdapter().start();
        getAdapter().initialize();
        experiment.run();
        getAdapter().shutdown();
        DFA result = (DFA) experiment.getFinalHypothesis();
        try {
            GraphDOT.write(result,getAlphabet(),System.out);
            Writer w = DOT.createDotWriter(true);
            GraphDOT.write(result,getAlphabet(),w);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

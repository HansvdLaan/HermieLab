package hermielab.core.learningsetup;

import hermielab.core.adapter.Adapter;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.dfa.ExtensibleLStarDFA;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.MembershipOracle;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.experiments.Experiment;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.Path;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.impl.ArrayAlphabet;
import hermielab.core.oracle.DFASULOracle;
import org.dom4j.DocumentException;
import hermielab.core.testdriver.TestDriver;

import java.nio.file.Paths;
import java.util.*;

public class BasicDFALearningSetup extends LearningSetup<Boolean,Boolean> {

    private int eqOracleMinLenght;
    private int eqOracleMaxLength;
    private int eqOracleMaxTests;
    private Random seed;

    public BasicDFALearningSetup(Adapter adapter, Path root, String experimentID,
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
    public DFA executeExperiment() {
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
        return  (DFA) experiment.getFinalHypothesis();

    }

}

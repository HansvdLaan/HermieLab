package learningsetup;

import adapter.Adapter;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.MembershipOracle;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.experiments.Experiment;
import mapper.ConcreteInvocation;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.commons.dotutil.DOT;
import net.automatalib.util.graphs.dot.GraphDOT;
import net.automatalib.words.impl.ArrayAlphabet;
import oracle.MealySULOracle;
import org.dom4j.DocumentException;
import testdriver.TestDriver;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ExampleMealyMachineLearningSetup extends LearningSetup<String,String> {

    private int eqOracleMinLenght;
    private int eqOracleMaxLength;
    private int eqOracleMaxTests;
    private Random seed;

    public ExampleMealyMachineLearningSetup(Adapter adapter, Path root, String experimentID,
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
        if (Objects.equals(getAbstractExperiment().getAutomatonType(), AutomatonType.MEALYMACHINE)) {
            MembershipOracle.MealyMembershipOracle mealyMembershipOracle = new MealySULOracle<String,String,String>(getProxy(), getMapper(),
                  getCheckers());
            EquivalenceOracle.MealyEquivalenceOracle mealyEquivalenceOracle = new RandomWordsEQOracle.MealyRandomWordsEQOracle(
                    mealyMembershipOracle,1,20,1000,new Random(42)  );

            setTestDriver(new TestDriver(mealyMembershipOracle, mealyEquivalenceOracle));
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
        ExtensibleLStarMealy<String,String> mealyLearningAlgorithm =
                new ExtensibleLStarMealy(
                        getAlphabet(),
                        (MembershipOracle.MealyMembershipOracle) getTestDriver().getMembershipOracle(),
                        new ArrayAlphabet<>(),
                        ObservationTableCEXHandlers.CLASSIC_LSTAR,
                        ClosingStrategies.CLOSE_FIRST);


        Experiment.MealyExperiment experiment = (Experiment.MealyExperiment) getTestDriver().generateExperiment(mealyLearningAlgorithm, getAlphabet());

        experiment.setProfile(true);
        experiment.setLogModels(true);
        getAdapter().start();
        getAdapter().initialize();
        experiment.run();
        getAdapter().shutdown();
        MealyMachine result = (MealyMachine) experiment.getFinalHypothesis();
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

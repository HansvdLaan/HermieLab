package hermielab.core.learningsetup;

import hermielab.core.adapter.Adapter;
import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealy;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.MembershipOracle;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.experiments.Experiment;
import net.automatalib.automata.transout.MealyMachine;
import net.automatalib.words.impl.ArrayAlphabet;
import hermielab.core.oracle.MealySULOracle;
import org.dom4j.DocumentException;
import hermielab.core.testdriver.TestDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BasicMealyMachineLearningSetup extends LearningSetup<String,String> {

    private int eqOracleMinLenght;
    private int eqOracleMaxLength;
    private int eqOracleMaxTests;
    private Random seed;

    public BasicMealyMachineLearningSetup(Adapter adapter, Path root, String experimentID,
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
                    mealyMembershipOracle,eqOracleMinLenght,eqOracleMaxLength,eqOracleMaxTests,seed );

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
    public MealyMachine executeExperiment() {
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
        return (MealyMachine) experiment.getFinalHypothesis();
    }

}

package hermielab.core.testdriver;

import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategies;
import de.learnlib.algorithms.lstargeneric.dfa.ExtensibleLStarDFA;
import de.learnlib.algorithms.lstargeneric.mealy.ExtensibleLStarMealy;
import de.learnlib.api.*;
import de.learnlib.eqtests.basic.RandomWordsEQOracle;
import de.learnlib.eqtests.basic.mealy.RandomWalkEQOracle;
import de.learnlib.oracles.SULOracle;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.SimpleAlphabet;
import hermielab.core.oracle.DFASULOracle;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public final class TestDriverTests {

    static Alphabet alphabet = new SimpleAlphabet<>();
    static LearningAlgorithm dfaLearningAlgorithm;
    static LearningAlgorithm mealyLearningAlgorithm;
    static TestDriver testDriverDFA;
    static TestDriver testDriverMealy;
    static TestDriver testDriverFaulty;

    @Rule public final ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUp() throws NoSuchMethodException {
        alphabet = new SimpleAlphabet<>();
        List<String> suffixes = new ArrayList();
        SUL sul = new SUL() {
            @Override
            public void pre() {

            }

            @Override
            public void post() {

            }

            @Nullable
            @Override
            public Object step(@Nullable Object in) throws SULException {
                return null;
            }
        };

        MembershipOracle.DFAMembershipOracle dfaMembershipOracle = new DFASULOracle(sul,null,null);
        EquivalenceOracle.DFAEquivalenceOracle dfaEquivalenceOracle = new RandomWordsEQOracle.DFARandomWordsEQOracle(
                dfaMembershipOracle,10,20,100,new Random(42)  );
        dfaLearningAlgorithm =
                new ExtensibleLStarDFA(
                        alphabet,
                        dfaMembershipOracle,
                        suffixes,
                        ObservationTableCEXHandlers.CLASSIC_LSTAR,
                        ClosingStrategies.CLOSE_FIRST
                );
        testDriverDFA = new TestDriver(dfaMembershipOracle, dfaEquivalenceOracle);


        SULOracle mealyMembershipOracle = new SULOracle<>(sul);
        mealyLearningAlgorithm =
                new ExtensibleLStarMealy(
                        alphabet,
                        mealyMembershipOracle,
                        suffixes, ObservationTableCEXHandlers.CLASSIC_LSTAR, ClosingStrategies.CLOSE_FIRST
                );
        EquivalenceOracle.MealyEquivalenceOracle mealyEquivalenceOracle = new RandomWalkEQOracle<>(
                0,
                10000,
                false,
                new Random(42),
                sul);
        testDriverMealy = new TestDriver(mealyMembershipOracle, mealyEquivalenceOracle);

        testDriverFaulty = new TestDriver(mealyMembershipOracle, dfaEquivalenceOracle);
    }

    @Test
    public void DFATestDriverTest(){
        testDriverDFA.generateExperiment(dfaLearningAlgorithm, alphabet);
    }

    @Test
    public void MealyTestDriverTest(){
        testDriverDFA.generateExperiment(dfaLearningAlgorithm, alphabet);
    }


    @Test
    public void FaultyTestDriverTest(){
        exception.expect(IllegalArgumentException.class);
        testDriverFaulty.generateExperiment(dfaLearningAlgorithm, alphabet);
    }
}
package testdriver;

import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.experiments.Experiment;
import net.automatalib.words.Alphabet;

public class TestDriver {

    private MembershipOracle membershipOracle;
    private EquivalenceOracle equivalenceOracle;

    public TestDriver(MembershipOracle membershipOracle, EquivalenceOracle equivalenceOracle){
        this.membershipOracle = membershipOracle;
        this.equivalenceOracle = equivalenceOracle;
    }

    public Experiment generateExperiment(LearningAlgorithm learningAlgorithm, Alphabet alphabet){
        if (membershipOracle instanceof MembershipOracle.DFAMembershipOracle &&
                equivalenceOracle instanceof EquivalenceOracle.DFAEquivalenceOracle
                && learningAlgorithm instanceof LearningAlgorithm.DFALearner){
            return new Experiment.DFAExperiment(learningAlgorithm,equivalenceOracle,alphabet);
        } else if(membershipOracle instanceof MembershipOracle.MealyMembershipOracle &&
                equivalenceOracle instanceof EquivalenceOracle.MealyEquivalenceOracle
                && learningAlgorithm instanceof LearningAlgorithm.MealyLearner) {
            return new Experiment.MealyExperiment<>(learningAlgorithm,equivalenceOracle,alphabet);
        } else {
            throw new IllegalArgumentException("The membership & equivilance oracle and the learning algorithm aren't " +
                    "suited for the same type of automata learning scenario");
        }
    }
}

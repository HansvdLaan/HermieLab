package checker.nfa;

import edu.duke.cs.jflap.automata.ClosureTaker;
import edu.duke.cs.jflap.automata.Configuration;
import edu.duke.cs.jflap.automata.State;
import edu.duke.cs.jflap.automata.fsa.FSAConfiguration;
import edu.duke.cs.jflap.automata.fsa.FSAStepWithClosureSimulator;
import edu.duke.cs.jflap.automata.fsa.FiniteStateAutomaton;

import java.util.*;

public class StepFSASimulator extends FSAStepWithClosureSimulator {

    private boolean valid;

    public StepFSASimulator(FiniteStateAutomaton automaton) {
        super(automaton);
        initialize();
    }

    public void initialize(){
        if (getAutomaton().getInitialState() != null) {
            setInitialConfigs(this.getInitialConfigurations(""));
            valid = true;
        } else {
            valid = false;
        }
    }

    public boolean checkStep(String symbol){
        boolean step_taken = false;
        if (!valid){
            return false;
        }

        Set<FSAConfiguration> newConfigurations = new HashSet<FSAConfiguration>();
        for(Object config: myConfigurations) {
            State state = ((FSAConfiguration) config).getCurrentState();
            newConfigurations.add(
                    new FSAConfiguration(
                            state,
                            null,
                            symbol,
                            symbol)
            );
        };

        this.myConfigurations = newConfigurations;

        while(!this.myConfigurations.isEmpty()) {

            if(this.isAccepted() && step_taken) {
                valid = true;
                return true;
            }

            step_taken = true;

            ArrayList configurationsToAdd = new ArrayList();
            Iterator it = this.myConfigurations.iterator();

            while(it.hasNext()) {
                FSAConfiguration configuration = (FSAConfiguration)it.next();
                ArrayList configsToAdd = this.stepConfiguration(configuration);
                configurationsToAdd.addAll(configsToAdd);
                it.remove();
            }

            this.myConfigurations.addAll(configurationsToAdd);
        }
        valid = false;
        return false;
    }

    public void reset(){
        initialize();
    }

    public void adapt(FiniteStateAutomaton adaptedAutomaton){
        Set<FSAConfiguration> newConfigurations = new HashSet<FSAConfiguration>();
        for(Object config: myConfigurations){
            State currentState = ((FSAConfiguration) config).getCurrentState();
            String name = currentState.getName();
            for(State state: adaptedAutomaton.getStates()){ // to acomodate the switching of automatons
                if(name.equals(state.getName())){
                    newConfigurations.add(
                            new FSAConfiguration(
                                    state,
                                    null,
                                    "",
                                    "")
                    );
                }
            }
        };
        this.myAutomaton = adaptedAutomaton;
        if(newConfigurations.isEmpty()){
            setInitialConfigs(this.getInitialConfigurations(""));
        } else {
            this.myConfigurations.clear();
            for (Configuration config: newConfigurations) {
                State state = config.getCurrentState();
                State[] closure = ClosureTaker.getClosure(state, this.myAutomaton);
                for (int k = 0; k < closure.length; ++k) {
                    Configuration newconfig = new FSAConfiguration(closure[k], (FSAConfiguration) null, "", "");
                    if (myConfigurations.contains(newconfig)){
                        myConfigurations.add(newconfig);
                    }
                }
            }
        }
    }

    private void setInitialConfigs(Configuration[] initialConfigs){
        this.myConfigurations.clear();

        for(int k = 0; k < initialConfigs.length; ++k) {
            FSAConfiguration initialConfiguration = (FSAConfiguration)initialConfigs[k];
            this.myConfigurations.add(initialConfiguration);
        }
    }
}

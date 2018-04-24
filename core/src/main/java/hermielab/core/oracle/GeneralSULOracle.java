package hermielab.core.oracle;

import hermielab.core.checker.Checker;
import de.learnlib.api.Query;
import de.learnlib.api.SUL;
import hermielab.core.mapper.ConcreteInvocation;
import hermielab.core.mapper.SULMapper;
import net.automatalib.words.Word;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class GeneralSULOracle<AI,AO,CO> {

    SUL<ConcreteInvocation,CO> sul;
    private final ThreadLocal<SUL<ConcreteInvocation,CO>> localSul;
    private SULMapper<AI, AO, ConcreteInvocation, CO> mapper;
    private List<Checker> checkers;

    public GeneralSULOracle(SUL<ConcreteInvocation,CO> sul, SULMapper mapper, List<Checker> checkers) {
        this.sul = sul;
        this.mapper = mapper;
        if (sul.canFork()) {
            this.localSul = new ThreadLocal<SUL<ConcreteInvocation,CO>>() {
                @Override
                protected SUL<ConcreteInvocation, CO> initialValue() {
                    return sul.fork();
                }
            };
        }
        else {
            this.localSul = null;
        }
        this.checkers = checkers;
    }
    
    public void processQueries(Collection collection) {
        synchronized(sul) {
            processQueries(sul, collection);
        }
    }

    private void processQueries(SUL<ConcreteInvocation, CO> sul, Collection collection) {
        for (Object o : collection) {
            Query q = (Query) o;
            Object output = answerQuery(q.getPrefix(), q.getSuffix());
            q.answer(output);
        }
    }
    
    public abstract Object answerQuery(Word prefix, Word suffix);

    public List<Checker> getCheckers(){
        return this.checkers;
    }

    public void setCheckers(List<Checker> checkers) {
        this.checkers = checkers;
    }

    public void addChecker(Checker checker){
        this.checkers.add(checker);
    }

    public Optional<AO> processSymbol(AI inputsymbol) {
        boolean valid = true;
        for (Checker checker : getCheckers()) {
            valid = valid && checker.checkAbstractInput(inputsymbol);
        }
        ConcreteInvocation concreteInput = mapper.mapInput(inputsymbol);
        for (Checker checker : getCheckers()) {
            valid = valid && checker.checkConcreteInput(concreteInput.getInputInstanceID());
        }
        if (valid) {
            CO concreteOutput = sul.step(concreteInput);
            AO abstractOutput = mapper.mapOutput(concreteOutput);
            return Optional.of(abstractOutput);
        } else {
            return Optional.empty();
        }
    }
}

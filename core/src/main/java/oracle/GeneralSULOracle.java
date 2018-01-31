package oracle;

import checker.Checker;
import de.learnlib.api.Query;
import de.learnlib.api.SUL;
import mapper.SULMapper;
import net.automatalib.words.Word;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class GeneralSULOracle<AI,AO,CI,CO> {

    SUL<CI,CO> sul;
    private final ThreadLocal<SUL<CI,CO>> localSul;
    private SULMapper<AI, AO, CI, CO> mapper;
    private List<Checker> checkers;

    public GeneralSULOracle(SUL<CI,CO> sul, SULMapper mapper, List<Checker> checkers) {
        this.sul = sul;
        this.mapper = mapper;
        if (sul.canFork()) {
            this.localSul = new ThreadLocal<SUL<CI,CO>>() {
                @Override
                protected SUL<CI, CO> initialValue() {
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

    private void processQueries(SUL<CI, CO> sul, Collection collection) {
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
        CI concreteInput = mapper.mapInput(inputsymbol);
        for (Checker checker : getCheckers()) {
            valid = valid && checker.checkConcreteInput(concreteInput);
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

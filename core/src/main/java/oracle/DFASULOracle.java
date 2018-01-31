package oracle;

import checker.Checker;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import mapper.SULMapper;
import net.automatalib.words.Word;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DFASULOracle<AI,CI,CO> extends GeneralSULOracle<AI,Boolean,CI,CO> implements MembershipOracle.DFAMembershipOracle {

    private boolean valid;

    public DFASULOracle(SUL<CI,CO> sul, SULMapper mapper, List<Checker> checkers) {
       super(sul, mapper,checkers);
    }

    @Override
    public void processQueries(Collection collection) {

    }

    @Override
    public Boolean answerQuery(Word prefix, Word suffix) {
        sul.pre();
        valid = true;
        Boolean result = null;

        try {
            // Prefix: Execute symbols, don't record output
            for (Object sym : prefix) {
                if(valid) {
                    Optional<Boolean> answer = processSymbol((AI) sym);
                    if (answer.isPresent()) {
                        result = answer.get();
                    } else {
                        valid = false;
                    }
                }
            }

            // Suffix: Execute symbols, outputs constitute output word
            for (Object sym : suffix) {
                if (valid) {
                    Optional<Boolean> answer = processSymbol((AI) sym);
                    if (answer.isPresent()) {
                        result = answer.get();
                    } else {
                        valid = false;
                    }
                }
            }

            if (!valid || result == null){
                result = false;
            }
            return result;

        } finally {

            sul.post();
            for (Checker checker: getCheckers()){
                checker.reset();
            }
            //return result;

        }
    }

}

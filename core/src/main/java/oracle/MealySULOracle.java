package oracle;

import checker.Checker;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.SUL;
import mapper.SULMapper;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.util.List;
import java.util.Optional;

public class MealySULOracle<AI,AO,CI,CO> extends GeneralSULOracle implements MembershipOracle.MealyMembershipOracle {

    private boolean valid;

    public MealySULOracle(SUL<CI,CO> sul, SULMapper mapper, List<Checker> checkers) {
        super(sul, mapper,checkers);
    }


    public Object answerQuery(Word prefix, Word suffix) {
        sul.pre();
        valid = true;

        try {
            for(Object sym : prefix) {
                if (valid) {
                    Optional<AO> answer = processSymbol((AI) sym);
                    if (!answer.isPresent())
                        valid = false;
                }
            }

            // Suffix: Execute symbols, outputs constitute output word
            WordBuilder<AO> wb = new WordBuilder<>(suffix.length());
            for(Object sym : suffix) {
                if (valid) {
                    Optional<AO> answer = processSymbol((AI) sym);
                    if (answer.isPresent()) {
                        wb.add(answer.get());
                    } else {
                        valid = false;
                        wb.add(null);
                    }
                } else {
                    wb.add(null);
                }
            }

            return wb.toWord();
        } finally {

            sul.post();
            for (Object checker: getCheckers()){
                ((Checker) checker).reset(); //Why is this cast necessary?
            }
            //return result;

        }
    }
}

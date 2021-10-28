package service.dataProvide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import domain.Outcome;

@Component
public class OutcomeParser implements DataParser<Outcome> {
    public static final int POSITION_OF_DESCRIPTION = 1;
    public static final int POSITION_OF_ODD = 2;
    private static final int ERROR_CODE_OF_NUMBER_FORMAT_EXCEPTION = -1;

    @Override
    public Outcome parseLineToGeneric(List<String> line) {
        Outcome currentOutcome = new Outcome();
        if (line.size() > 2) {
            currentOutcome.setDescription(line.get(POSITION_OF_DESCRIPTION));
            try {
                currentOutcome.setOdd(BigDecimal.valueOf(Integer.parseInt(line.get(POSITION_OF_ODD))));
            }catch (NumberFormatException e){
                currentOutcome.setOdd(BigDecimal.valueOf(ERROR_CODE_OF_NUMBER_FORMAT_EXCEPTION));
            }
        } else {
            currentOutcome.setDescription("Missing Values");
            currentOutcome.setOdd(BigDecimal.valueOf(0));
        }
        return currentOutcome;
    }

    @Override
    public List<Outcome> parseGenericToList(List<List<String>> lines) {
        List<Outcome> outcomes = new ArrayList<>();

        for (List<String> line : lines) {
            outcomes.add(parseLineToGeneric(line));
        }

        return outcomes;
    }
}

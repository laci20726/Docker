package service.dataProvide;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import domain.Bet;

@Component
public class BetParser implements DataParser<Bet> {
    public static final int POSITION_OF_DESCRIPTION = 1;

    @Override
    public Bet parseLineToGeneric(List<String> line) {
        Bet currentBet = new Bet();
        if (!line.isEmpty()) {
            currentBet.setDescription(line.get(POSITION_OF_DESCRIPTION));
        } else {
            currentBet.setDescription("");
        }
        return currentBet;
    }

    @Override
    public List<Bet> parseGenericToList(List<List<String>> lines) {
        List<Bet> bets = new ArrayList<>();

        for (List<String> line : lines) {
            bets.add(parseLineToGeneric(line));
        }

        return bets;
    }
}

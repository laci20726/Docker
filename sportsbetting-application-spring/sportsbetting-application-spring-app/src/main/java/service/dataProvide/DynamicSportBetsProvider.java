package service.dataProvide;

import java.util.ArrayList;
import java.util.List;

import domain.Bet;
import domain.Outcome;
import domain.SportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicSportBetsProvider implements SportDataProvider<Bet> {
    DataParser<SportEvent> sportEventDataParser;
    DataParser<Outcome> outcomeDataParser;
    DataParser<Bet> betParser;

    @Autowired
    public void setBetParser(DataParser<Bet> betParser) {
        this.betParser = betParser;
    }
    @Autowired
    public void setSportEventDataParser(DataParser<SportEvent> sportEventDataParser) {
        this.sportEventDataParser = sportEventDataParser;
    }
    @Autowired
    public void setOutcomeDataParser(DataParser<Outcome> outcomeDataParser) {
        this.outcomeDataParser = outcomeDataParser;
    }

    public DynamicSportBetsProvider() {
    }

    public DynamicSportBetsProvider(DataParser<SportEvent> sportEventDataParser, DataParser<Outcome> outcomeDataParser, DataParser<Bet> betParser) {
        this.sportEventDataParser = sportEventDataParser;
        this.outcomeDataParser = outcomeDataParser;
        this.betParser = betParser;
    }

    private List<Outcome> createOutcomeSubList(int betIndex, List<Outcome> outcomes, List<List<String>> outcomeLines) {
        List<Outcome> outcomeSubList = new ArrayList<>();
        for (int i = 0; i < outcomeLines.size(); i++) {
            if (betIndex == Integer.parseInt(outcomeLines.get(i).get(0))) {
                outcomeSubList.add(outcomes.get(i));
            }
        }
        return outcomeSubList;
    }

    private void dataConnect(List<Bet> bets,
                             List<SportEvent> sportEvents,
                             List<List<String>> betLines,
                             List<List<String>> outcomeLines) {

        List<Outcome> outcomes = outcomeDataParser.parseGenericToList(outcomeLines);

        for (int i = 0; i < betLines.size(); i++) {
            bets.get(i).setEvent(sportEvents.get(Integer.parseInt(betLines.get(i).get(0))));
        }
        for (int i = 0; i < bets.size(); i++) {
            Bet currentBet = bets.get(i);
            List<Outcome> outcomeSubList = createOutcomeSubList(i, outcomes, outcomeLines);
            outcomeSubList.forEach(outcome -> outcome.setBet(currentBet));
            currentBet.setOutcomes(outcomeSubList);
        }
    }

    @Override
    public List<Bet> provideSportData(List<List<String>> sportEventLines,
                                      List<List<String>> betLines,
                                      List<List<String>> outcomeLines) {

        List<Bet> bets = betParser.parseGenericToList(betLines);
        List<SportEvent> sportEvents = sportEventDataParser.parseGenericToList(sportEventLines);

        dataConnect(bets, sportEvents, betLines,outcomeLines);

        return bets;
    }
}

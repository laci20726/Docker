package service.App;

import domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JDOMCreatorTest {
    @Test
    public void testSportEventListFromWagers() {
        JDOMCreator jdomCreator = new JDOMCreator();
        List<Wager> wagerList = new ArrayList<>();
        Wager wagerFirst = new Wager();
        Wager wagerSecond = new Wager();
        Outcome outcomeFirst = new Outcome();
        Outcome outcomeSecond = new Outcome();
        Bet betFirst = new Bet();
        Bet betSecond = new Bet();
        SportEvent sportEventFirst = new FootballSportEvent();
        SportEvent sportEventSecond = new FootballSportEvent();

        betFirst.setEvent(sportEventFirst);
        outcomeFirst.setBet(betFirst);
        wagerFirst.setOdd(outcomeFirst);

        betSecond.setEvent(sportEventSecond);
        outcomeSecond.setBet(betSecond);
        wagerSecond.setOdd(outcomeSecond);

        wagerList.add(wagerFirst);
        wagerList.add(wagerSecond);

        assertEquals(2, jdomCreator.sportEventListFromWagers(wagerList).size());
    }
}

package service.dataProvide;

import domain.Bet;
import domain.Outcome;
import domain.SportEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TestLists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DynamicSportBetsProviderTest {
    private final DataParser<SportEvent> sportEventDataParser = new SportEventParser();
    private final DataParser<Outcome> outcomeDataParser = new OutcomeParser();
    private final DataParser<Bet> betParser = new BetParser();
    SportDataProvider<Bet> DSBPTest;

    TestLists testLists = new TestLists();
    List<List<String>> sportEventSample = testLists.getSportEventSample();
    List<List<String>> outcomeSample = testLists.getOutcomeSample();
    List<List<String>> betSample = testLists.getBetSample();

    @BeforeEach
    public void setUp() {
        DSBPTest = new DynamicSportBetsProvider(sportEventDataParser, outcomeDataParser, betParser);
    }

    @Test
    public void testFetchDataShouldWorkFineWhenTheFilesAreCorrect() {
        List<Bet> bets = DSBPTest.provideSportData(sportEventSample,betSample,outcomeSample);

        assertEquals(6, bets.size());
    }
    @Test
    public void testSetters() {
        DynamicSportBetsProvider DSBPTestEmptyConstructor = new DynamicSportBetsProvider();
        DataParser<SportEvent> sportEventDataParser = new SportEventParser();
        DataParser<Outcome> outcomeDataParser = new OutcomeParser();
        DataParser<Bet> betParser = new BetParser();

        assertNull(DSBPTestEmptyConstructor.betParser);
        assertNull(DSBPTestEmptyConstructor.outcomeDataParser);
        assertNull(DSBPTestEmptyConstructor.sportEventDataParser);

        DSBPTestEmptyConstructor.setBetParser(betParser);
        DSBPTestEmptyConstructor.setOutcomeDataParser(outcomeDataParser);
        DSBPTestEmptyConstructor.setSportEventDataParser(sportEventDataParser);

        assertEquals(sportEventDataParser, DSBPTestEmptyConstructor.sportEventDataParser);
        assertEquals(outcomeDataParser, DSBPTestEmptyConstructor.outcomeDataParser);
        assertEquals(betParser, DSBPTestEmptyConstructor.betParser);
    }
}
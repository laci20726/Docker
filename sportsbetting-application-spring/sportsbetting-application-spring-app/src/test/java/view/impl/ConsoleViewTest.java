package view.impl;

import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TestLists;
import service.dataProvide.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsoleViewTest {
    private final DataParser<SportEvent> sportEventDataParser = new SportEventParser();
    private final DataParser<Outcome> outcomeDataParser = new OutcomeParser();
    private final DataParser<Bet> betParser = new BetParser();
    private final SportDataProvider<Bet> DSBPTest = new DynamicSportBetsProvider(sportEventDataParser, outcomeDataParser, betParser);
    private final WagerPrintBuilder wagerPrintBuilder = new WagerPrintBuilder();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
    private final PrintStream originalOut = System.out;
    private final InputStream sysInBackup = System.in;
    private ConsoleView consoleView;
    private List<Bet> bets;
    TestLists testLists = new TestLists();
    List<List<String>> sportEventSample = testLists.getSportEventSample();
    List<List<String>> outcomeSample = testLists.getOutcomeSample();
    List<List<String>> betSample = testLists.getBetSample();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setIn(in);
        bets = DSBPTest.provideSportData(sportEventSample,betSample,outcomeSample);
        consoleView = new ConsoleView(wagerPrintBuilder);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(sysInBackup);
    }

    @Test
    public void testReadCredentials() {
        User testResultUser = new User();
        testResultUser.setEmail("admin");
        testResultUser.setPassword("admin");

        ConsoleView testedConsoleView = mock(ConsoleView.class);
        when(testedConsoleView.readCredentials()).thenReturn(testResultUser);

        assertEquals(testResultUser, testedConsoleView.readCredentials());
    }

    @Test
    public void testPrintWelcomeMessageShouldPrintTheRightMessageWhenPlayerGiven() {
        Player testSubjectPlayer = new Player();
        testSubjectPlayer.setName("Laszlo");
        testSubjectPlayer.setBalance(BigDecimal.valueOf(1000));
        testSubjectPlayer.setCurrency(Currency.EUR);

        consoleView.printWelcomeMessage(testSubjectPlayer);
        assertEquals("Welcome Laszlo!" + System.lineSeparator()
                + "Your balance is 1000 EUR" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWelcomeMessageShouldPrintTheMessageWhenPlayerDataHasNotBeenSet() {
        Player testSubjectPlayerWithNoData = new Player();
        consoleView.printWelcomeMessage(testSubjectPlayerWithNoData);
        assertEquals("Welcome null!" + System.lineSeparator()
                + "Your balance is null null" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintOutcomesShouldWorkFineWhenDataIsCorrect() {
        String consoleOutputForTest = "1: Sport event: Arsenal vs Chelsea (start: 2016-10-27 19:00:00), Bet: player Oliver Giroud score, Outcome: 1, Actual odd: 2." + System.lineSeparator() +
                "2: Sport event: Arsenal vs Chelsea (start: 2016-10-27 19:00:00), Bet: player Oliver Giroud score, Outcome: 2, Actual odd: 4." + System.lineSeparator() +
                "3: Sport event: Arsenal vs Chelsea (start: 2016-10-27 19:00:00), Bet: number of scored goals, Outcome: 3, Actual odd: 3." + System.lineSeparator() +
                "4: Sport event: Arsenal vs Chelsea (start: 2016-10-27 19:00:00), Bet: winner, Outcome: Arsenal, Actual odd: 2." + System.lineSeparator() +
                "5: Sport event: Arsenal vs Chelsea (start: 2016-10-27 19:00:00), Bet: winner, Outcome: Chelsea, Actual odd: 3." + System.lineSeparator() +
                "6: Sport event: Real Madrid vs Manchester (start: 2016-11-27 20:00:00), Bet: player Ronaldo score, Outcome: 2, Actual odd: 4." + System.lineSeparator() +
                "7: Sport event: Real Madrid vs Manchester (start: 2016-11-27 20:00:00), Bet: number of scored goals, Outcome: 2, Actual odd: 2." + System.lineSeparator() +
                "8: Sport event: Real Madrid vs Manchester (start: 2016-11-27 20:00:00), Bet: winner, Outcome: Real Madrid, Actual odd: 2." + System.lineSeparator() +
                "9: Sport event: Real Madrid vs Manchester (start: 2016-11-27 20:00:00), Bet: winner, Outcome: Manchester, Actual odd: 3." + System.lineSeparator();
        consoleView.printOutcomes(bets);

        assertEquals(consoleOutputForTest, outContent.toString());
    }

    @Test
    public void testPrintOutcomesShouldWorkFineAndOutputWillEmptyWhenDataIsEmpty() {
        List<Bet> bets = new ArrayList<>();

        consoleView.printOutcomes(bets);

        assertEquals("", outContent.toString());
    }

    @Test
    public void testQueryOutcomesShouldWorkFineWhenInputIsCorrect() {
        assertEquals(9, consoleView.queryOutcomes(bets).size());
    }

    @Test
    public void testSelectOutcomeShouldWorkFine() {
        System.setOut(originalOut);
        List<Outcome> outcomes = consoleView.queryOutcomes(bets);
        assertEquals(outcomes.get(1), consoleView.selectOutcome(bets));
    }

    @Test
    public void testIsQuiteCommandShouldReturnTrueWhenIncomingStringIsQOrq() {
        assertTrue(consoleView.isQuiteCommand("q"));
        assertTrue(consoleView.isQuiteCommand("Q"));
    }

    @Test
    public void testIsQuiteCommandShouldReturnFalseWhenIncomingStringIsNotQOrq() {
        assertFalse(consoleView.isQuiteCommand("A"));
        assertFalse(consoleView.isQuiteCommand("5"));
    }

    @Test
    public void testSelectOutcomeIfPossibleShouldReturnNullWhenIncomingStringValueGreaterThanSizeOfOutcomeList() {
        AtomicBoolean testSuccessReturn = new AtomicBoolean(false);
        List<Outcome> testOutcomes = consoleView.queryOutcomes(bets);
        Outcome testOutcome = consoleView.selectOutcomeIfPossible("11", testSuccessReturn, testOutcomes);
        assertNull(testOutcome);
        assertFalse(testSuccessReturn.get());
        assertEquals("Out of the bets size! Try a lesser number." + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testSelectOutcomeIfPossibleShouldReturnOutcomeWhenIncomingStringValueCorrect() {
        AtomicBoolean testSuccessReturn = new AtomicBoolean(false);
        List<Outcome> testOutcomes = consoleView.queryOutcomes(bets);
        Outcome testOutcome = consoleView.selectOutcomeIfPossible("1", testSuccessReturn, testOutcomes);
        assertEquals(testOutcomes.get(0), testOutcome);
        assertTrue(testSuccessReturn.get());
    }

    @Test
    public void testRequestAmountForBetShouldReturnBigDecimalWhenIncomingParamCouldBeConvertToBigDecimal() {
        BigDecimal testValue = consoleView.requestAmountForBet();
        assertEquals("What amount do you wish to bet on it?" + System.lineSeparator(), outContent.toString());
        assertEquals(testValue, BigDecimal.valueOf(2));
    }

    @Test
    public void testPrintLowBalanceMessage() {
        Player player = new Player();
        player.setBalance(BigDecimal.valueOf(1500));
        player.setCurrency(Currency.EUR);
        String actual = "You don't have enough money, your balance is 1500 EUR";

        consoleView.printLowBalanceMessage(player);

        assertEquals(actual + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testPrintWagerSaved() {
        Bet bet = new Bet();
        Outcome outcome = new Outcome();
        Wager wager = new Wager();
        SportEvent sportEvent = new FootballSportEvent();
        Player player = new Player();
        String expected = "Wager 'player Oliver Giroud score=1' of Arsenal vs Chelsea [odd: 2, amount: 100] saved!" +
                System.lineSeparator() + "Your balance is 1000 EUR" + System.lineSeparator();

        sportEvent.setTitle("Arsenal vs Chelsea");

        bet.setEvent(sportEvent);
        bet.setDescription("player Oliver Giroud score");

        outcome.setBet(bet);
        outcome.setOdd(BigDecimal.valueOf(2));
        outcome.setDescription("1");

        player.setBalance(BigDecimal.valueOf(1000));
        player.setCurrency(Currency.EUR);

        wager.setOdd(outcome);
        wager.setPlayer(player);
        wager.setAmount(BigDecimal.valueOf(100));

        consoleView.printWagerSaved(wager);
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintResult() {
        Bet bet = new Bet();
        Outcome outcome = new Outcome();
        Wager wager = new Wager();
        SportEvent sportEvent = new FootballSportEvent();
        Player player = new Player();
        List<Wager> wagers = new ArrayList<>();
        String expected = "Results:"
                + System.lineSeparator()
                + "Wager 'player Oliver Giroud score=1' of Arsenal vs Chelsea [odd: 2, amount: 100], win: false."
                + System.lineSeparator()
                + "Your new balance is 1000 EUR."
                + System.lineSeparator();

        sportEvent.setTitle("Arsenal vs Chelsea");

        bet.setEvent(sportEvent);
        bet.setDescription("player Oliver Giroud score");

        outcome.setBet(bet);
        outcome.setOdd(BigDecimal.valueOf(2));
        outcome.setDescription("1");

        player.setBalance(BigDecimal.valueOf(1000));
        player.setCurrency(Currency.EUR);

        wager.setOdd(outcome);
        wager.setPlayer(player);
        wager.setAmount(BigDecimal.valueOf(100));

        wagers.add(wager);

        consoleView.printResult(player, wagers);
        assertEquals(expected, outContent.toString());
    }
}

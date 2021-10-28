package service.impl;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthenticationException;
import service.dataProvide.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class DefaultSportsBettingServiceTest {
    PlayerProvider playerProvider = mock(PlayerProvider.class);
    CSVReader csvReader = mock(CSVReader.class);
    DynamicSportBetsProvider dynamicSportBetsProvider = mock(DynamicSportBetsProvider.class);
    DefaultSportsBettingService DSBSTest;
    Bet testBetFirst = new Bet();
    Bet testBetSecond = new Bet();
    List<Bet> bets;
    Player testPlayer = new Player();

    @BeforeEach
    public void setUp(){
        DSBSTest = new DefaultSportsBettingService(dynamicSportBetsProvider, playerProvider,  csvReader);
        testPlayer.setEmail("admin");
        testPlayer.setPassword("admin");
        testPlayer.setCurrency(Currency.HUF);
        testPlayer.setBalance(BigDecimal.valueOf(10000));
        testBetFirst.setDescription("TestEventFirst");
        testBetFirst.setDescription("TestEventSecond");
        bets = new ArrayList<>(Arrays.asList(testBetFirst, testBetSecond));
    }



    @Test
    public void testAuthenticateUserValidUserShouldReturnPlayer() {
        assertNotNull(playerProvider);

        User user = new User();
        user.setEmail("admin");
        user.setPassword("admin");
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);

        when(playerProvider.getRegisteredPlayers()).thenReturn(players);
        assertEquals(players.get(0), DSBSTest.authenticateUser(user));
    }

    @Test
    public void testAuthenticateUserValidUserShouldThrowExceptionWhenUserNotMatchWithThePreDefPlayers() {
        assertNotNull(playerProvider);

        User user = new User();
        user.setEmail("admin");
        user.setPassword("adman");
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);

        when(playerProvider.getRegisteredPlayers()).thenReturn(players);
        assertThrows(AuthenticationException.class, () -> DSBSTest.authenticateUser(user));
    }

    @Test
    public void testFindAllBetsShouldReturnValidBetListWhenEverythingIsCorrect(){
        assertNotNull(dynamicSportBetsProvider);

        when(dynamicSportBetsProvider.provideSportData(anyList(),anyList(),anyList())).thenReturn(bets);
        bets = DSBSTest.findAllBets();
        assertEquals(testBetFirst.getDescription(), bets.get(0).getDescription());
        assertEquals(testBetFirst, bets.get(0));
    }

    @Test
    public void testCreateWagerShouldReturnProperWagerWhenParamsCorrect() {
        Wager actualWager = new Wager();
        Outcome outcome = new Outcome();
        BigDecimal amount = new BigDecimal(500);
        actualWager.setPlayer(testPlayer);
        actualWager.setOdd(outcome);
        actualWager.setAmount(amount);
        actualWager.setCurrency(testPlayer.getCurrency());

        Wager testedWager = DSBSTest.createWager(testPlayer, outcome, amount);

        assertEquals(actualWager.getAmount(), testedWager.getAmount());
        assertEquals(actualWager.getOutcome(), testedWager.getOutcome());
        assertEquals(actualWager.getCurrency(), testedWager.getCurrency());
        assertEquals(BigDecimal.valueOf(9500), testedWager.getPlayer().getBalance());
    }

    @Test
    public void testFindAllWagersShouldReturnAnArrayWhichContainsAllOfTheWagersWhenEveryThingIsFine() {
        Outcome outcome = new Outcome();
        BigDecimal amount = new BigDecimal(500);
        DSBSTest.createWager(testPlayer, outcome, amount);
        DSBSTest.createWager(testPlayer, outcome, amount);

        assertEquals(2, DSBSTest.findAllWager().size());
        assertNotEquals(DSBSTest.findAllWager().get(0), DSBSTest.findAllWager().get(1));
    }

    @Test
    public void testCalculateResult() {
        List<Outcome> outcomeListForFirstBet = new ArrayList<>();
        List<Outcome> outcomeListForSecondBet = new ArrayList<>();
        Bet betOne = new Bet();
        Bet betTwo = new Bet();
        Outcome outcomeForBetOne = new Outcome();
        Outcome outcomeForBetTwo = new Outcome();
        Outcome outcomeForBetTwoAnother = new Outcome();

        outcomeForBetOne.setBet(betOne);
        outcomeForBetOne.setOdd(BigDecimal.valueOf(2));
        outcomeForBetTwo.setBet(betTwo);
        outcomeForBetTwo.setOdd(BigDecimal.valueOf(2));
        outcomeForBetTwoAnother.setBet(betTwo);
        outcomeForBetTwoAnother.setOdd(BigDecimal.valueOf(2));

        outcomeListForFirstBet.add(outcomeForBetOne);
        outcomeListForSecondBet.add(outcomeForBetTwo);
        outcomeListForSecondBet.add(outcomeForBetTwoAnother);

        betOne.setOutcomes(outcomeListForFirstBet);
        betTwo.setOutcomes(outcomeListForSecondBet);

        DSBSTest.createWager(testPlayer, outcomeForBetOne, BigDecimal.valueOf(1000));
        DSBSTest.createWager(testPlayer, outcomeForBetTwo, BigDecimal.valueOf(1000));
        DSBSTest.createWager(testPlayer, outcomeForBetTwoAnother, BigDecimal.valueOf(0));

        assertEquals(3, DSBSTest.extractBets().size());
        DSBSTest.calculateResults();
        assertEquals( BigDecimal.valueOf(10000), testPlayer.getBalance());

    }
}

package service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import domain.Bet;
import domain.Outcome;
import domain.Player;
import domain.User;
import domain.Wager;
import service.AuthenticationException;
import service.SportsBettingService;
import service.dataProvide.CSVReader;
import service.dataProvide.PlayerProvider;
import service.dataProvide.SportDataProvider;

@Component
public class DefaultSportsBettingService implements SportsBettingService {
    @Value("${data.sportEventPath}")
    private String sportEventPath;
    @Value("${data.betsPath}")
    private String betsPath;
    @Value("${data.outcomesPath}")
    private String outcomesPath;
    protected SportDataProvider<Bet> sportDataProvider;
    protected PlayerProvider playerProvider;
    protected CSVReader csvRead;
    private final List<Wager> wagers = new ArrayList<>();
    private final AtomicBoolean isValid = new AtomicBoolean(false);
    private final AtomicInteger playerIndex = new AtomicInteger();

    public DefaultSportsBettingService() {
    }

    @Autowired
    public DefaultSportsBettingService(SportDataProvider<Bet> sportDataProvider, PlayerProvider playerProvider, CSVReader csvRead) {
        this.sportDataProvider = sportDataProvider;
        this.playerProvider = playerProvider;
        this.csvRead = csvRead;
    }

    private void authenticate(List<Player> registeredPlayers, User user) {
        AtomicInteger iter = new AtomicInteger(0);
        registeredPlayers.forEach(regisPlayer -> {
            if (regisPlayer.getEmail().equals(user.getEmail())
                    && regisPlayer.getPassword().equals(user.getPassword())) {
                isValid.set(true);
                playerIndex.set(iter.get());
            }
            iter.getAndIncrement();
        });
    }

    @Override
    public Player authenticateUser(User user) {
        List<Player> registeredPlayers = playerProvider.getRegisteredPlayers();
        authenticate(registeredPlayers, user);
        if (isValid.get()) {
            return registeredPlayers.get(playerIndex.get());
        } else {
            throw new AuthenticationException();
        }
    }

    @Override
    public List<Bet> findAllBets() {
        List<List<String>> sportEventLines = csvRead.read(sportEventPath);
        List<List<String>> betLines = csvRead.read(betsPath);
        List<List<String>> outcomeLines = csvRead.read(outcomesPath);
        return sportDataProvider.provideSportData(sportEventLines,betLines,outcomeLines);
    }

    private void refreshPlayerBalance(Player player, BigDecimal amount) {
        player.setBalance(player.getBalance().subtract(amount));
    }

    private void saveWager(Wager wager){
        wagers.add(wager);
    }

    @Override
    public Wager createWager(Player player, Outcome outcome, BigDecimal amount) {
        Wager wager = new Wager();
        wager.setPlayer(player);
        wager.setOdd(outcome);
        wager.setCurrency(player.getCurrency());
        wager.setAmount(amount);
        refreshPlayerBalance(player,amount);
        saveWager(wager);
        return wager;
    }

    @Override
    public List<Wager> findAllWager() {
        return wagers;
    }

    private void calculatePlayerBalance() {
        wagers.forEach(wager -> {
            wager.setWin(wager.getOutcome().isWin());
            if (wager.isWin()) {
                Player p = wager.getPlayer();
                p.setBalance(p.getBalance().add(wager.getAmount().multiply(wager.getOutcome().getOdd())));
            }
        });
    }

    public List<Bet> extractBets() {
        List<Bet> bets = new ArrayList<>();
        wagers.forEach(wager -> bets.add(wager.getOutcome().getBet()));
        return bets;
    }

    private void calculateBetsOutcome(List<Bet> bets) {
        bets.forEach(bet -> {
            if (bet.getOutcomes().size() <= 1) {
                bet.getOutcomes().get(0).setWin(true);
            } else {
                bet.getOutcomes().get(1).setWin(true);
            }
        });
    }

    @Override
    public void calculateResults() {
        List<Bet> bets = extractBets();
        calculateBetsOutcome(bets);
        calculatePlayerBalance();
    }
}

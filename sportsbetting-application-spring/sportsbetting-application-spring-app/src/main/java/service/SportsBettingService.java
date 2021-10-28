package service;

import java.math.BigDecimal;
import java.util.List;

import domain.Bet;
import domain.Outcome;
import domain.Player;
import domain.User;
import domain.Wager;

public interface SportsBettingService {
    Player authenticateUser(User user);

    List<Bet> findAllBets();

    Wager createWager(Player player, Outcome outcome, BigDecimal amount);

    List<Wager> findAllWager();

    void calculateResults();
}

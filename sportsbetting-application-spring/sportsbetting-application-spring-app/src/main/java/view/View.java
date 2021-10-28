package view;

import java.math.BigDecimal;
import java.util.List;

import domain.Bet;
import domain.Outcome;
import domain.Player;
import domain.User;
import domain.Wager;

public interface View {
    User readCredentials();

    void printWelcomeMessage(Player player);

    void printOutcomes(List<Bet> bets);

    Outcome selectOutcome(List<Bet> bets);

    BigDecimal requestAmountForBet();

    void printLowBalanceMessage(Player player);

    void printWagerSaved(Wager wager);

    void printResult(Player player, List<Wager> wagers);
}

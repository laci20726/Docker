package view.impl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Bet;
import domain.Outcome;
import domain.Player;
import domain.User;
import domain.Wager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import view.View;

@Component
public class ConsoleView implements View {
    private static final String EMAIL = "Enter player email address:";
    private static final String PWD = "Enter player password:";
    private static final String SELECT_OUTCOME = "What do you want to bet on? (choose a number or press q for quit)";
    private final User user = new User();
    private final Scanner scanner = new Scanner(System.in);
    private WagerPrintBuilder wagerPrintBuilder;

    public ConsoleView() {
    }

    public ConsoleView(WagerPrintBuilder wagerPrintBuilder) {
        this.wagerPrintBuilder = wagerPrintBuilder;
    }

    @Autowired
    public void setWagerPrintBuilder(WagerPrintBuilder wagerPrintBuilder) {
        this.wagerPrintBuilder = wagerPrintBuilder;
    }

    @Override
    public User readCredentials() {
        System.out.println(EMAIL);
        String email = scanner.nextLine();
        user.setEmail(email);

        System.out.println(PWD);
        String pwd = scanner.nextLine();
        user.setPassword(pwd);

        return user;
    }

    @Override
    public void printWelcomeMessage(Player player) {
        System.out.println("Welcome " + player.getName() + "!");
        System.out.println("Your balance is " + player.getBalance() + " " + player.getCurrency());
    }

    @Override
    public void printOutcomes(List<Bet> bets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AtomicInteger betIndex = new AtomicInteger(1);
        if (!bets.isEmpty()) {
            bets.forEach(bet -> bet.getOutcomes().forEach(outcome -> {
                System.out.println(betIndex.get() + ": Sport event: " + outcome.getBet().getEvent().getTitle()
                        + " (start: " + outcome.getBet().getEvent().getStartDate().format(formatter)
                        + "), Bet: " + outcome.getBet().getDescription() + ", Outcome: " + outcome.getDescription()
                        + ", Actual odd: " + outcome.getOdd() + "."
                );
                betIndex.getAndIncrement();
            }));
        }

    }

    protected List<Outcome> queryOutcomes(List<Bet> bets){
        List<Outcome> outcomes = new ArrayList<>();
        bets.forEach(bet -> outcomes.addAll(bet.getOutcomes()));
        return outcomes;
    }

    protected boolean isQuiteCommand(String incomingCommand) {
        return "q".equals(incomingCommand) || "Q".equals(incomingCommand);
    }

    protected Outcome selectOutcomeIfPossible(String incomingCommand, AtomicBoolean successReturn, List<Outcome> outcomes){
        int selected;
        Outcome outcome = null;
        try {
            selected = Integer.parseInt(incomingCommand);
            if (selected <= outcomes.size()) {
                successReturn.set(true);
                outcome = outcomes.get(selected - 1);
            } else {
                System.out.println("Out of the bets size! Try a lesser number.");
            }
        } catch (NumberFormatException ignored) {
            System.out.println("Incorrect input!(choose a number or press q for quit)");
        }

        return outcome;
    }

    protected Outcome processIncomingCommand(List<Outcome> outcomes){
        Outcome outcome = null;
        AtomicBoolean successReturn = new AtomicBoolean(false);
        System.out.println(SELECT_OUTCOME);
        while (!successReturn.get()){
            String incomingCommand = scanner.nextLine();
            if (!isQuiteCommand(incomingCommand)) {
                outcome = selectOutcomeIfPossible(incomingCommand, successReturn, outcomes);
            } else {
                successReturn.set(true);
            }
        }
        return outcome;
    }

    @Override
    public Outcome selectOutcome(List<Bet> bets) {
        List<Outcome> outcomes = queryOutcomes(bets);

        return processIncomingCommand(outcomes);
    }

    @Override
    public BigDecimal requestAmountForBet() {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        boolean acceptableBigDecimal = false;
        while(!acceptableBigDecimal) {
            System.out.println("What amount do you wish to bet on it?");
            try {
                bigDecimal = scanner.nextBigDecimal();
                acceptableBigDecimal = true;
            } catch (InputMismatchException ignored) {
                System.out.println("Incorrect input!");
                String ignoredString = scanner.nextLine();
            }
        }
        return bigDecimal;
    }

    @Override
    public void printLowBalanceMessage(Player player) {
        System.out.println("You don't have enough money, your balance is " + player.getBalance()
                + " " + player.getCurrency());
    }

    @Override
    public void printWagerSaved(Wager wager) {
        System.out.println(wagerPrintBuilder.setBetDescription(wager)
                .setOutcomeDescription(wager)
                .setEventTitle(wager)
                .setOutcomeOdd(wager)
                .setWagerAmount(wager)
                .setBase()
                .getPrintedWager());
        System.out.println("Your balance is " + wager.getPlayer().getBalance() + " " + wager.getPlayer().getCurrency());
    }

    @Override
    public void printResult(Player player, List<Wager> wagers) {
        System.out.println("Results:");
        wagers.forEach(wager -> System.out.println(wagerPrintBuilder.setBetDescription(wager)
                .setOutcomeDescription(wager)
                .setEventTitle(wager)
                .setOutcomeOdd(wager)
                .setWagerAmount(wager)
                .setWagerIsWin(wager)
                .setBase()
                .getPrintedWager()));
        System.out.println("Your new balance is " + player.getBalance() + " " + player.getCurrency() + ".");
    }
}

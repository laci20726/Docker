package service.App;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import domain.Bet;
import domain.Outcome;
import domain.Player;
import domain.User;
import domain.Wager;
import service.AuthenticationException;
import service.SportsBettingService;
import view.View;

public class App {
    @Value("${output.filePath}")
    private String outputFilePath;
    @Autowired
    private SportsBettingService sportsbettingService;
    @Autowired
    private View view;
    @Autowired
    private JDOMCreator jdomCreator;
    private Player player;
    private List<Bet> bets;
    private List<Wager> wagers;

    public void init(){
        this.bets = sportsbettingService.findAllBets();
        this.wagers = sportsbettingService.findAllWager();
    }

    private void checkValidity() {
        do {
            User user = view.readCredentials();
            try {
                player = sportsbettingService.authenticateUser(user);
            } catch (AuthenticationException a) {
                System.out.println("Your email address or password is incorrect!\n");
            }
        } while (player == null);
    }

    private void doBetting() {
        Outcome outcome;
        do {
            view.printOutcomes(bets);
            outcome = view.selectOutcome(bets);
            if (outcome != null) {
                BigDecimal amount;
                do {
                    amount = view.requestAmountForBet();
                    if (amount.intValue() > player.getBalance().intValue()) {
                        view.printLowBalanceMessage(player);
                    }
                } while (amount.intValue() > player.getBalance().intValue());

                Wager wager = sportsbettingService.createWager(player, outcome, amount);
                view.printWagerSaved(wager);
            }
        } while (outcome != null);
    }
    public void play() throws IOException {
        checkValidity();
        view.printWelcomeMessage(player);
        doBetting();
        sportsbettingService.calculateResults();
        view.printResult(player, wagers);

        Document doc = jdomCreator.createXMLDoc(player, wagers);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent("    "));
        FileWriter writer = new FileWriter(outputFilePath);

        outputter.output(doc, writer);
    }
}

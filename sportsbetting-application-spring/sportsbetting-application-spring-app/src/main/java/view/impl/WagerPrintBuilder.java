package view.impl;

import org.springframework.stereotype.Component;

import domain.Wager;

@Component
 public class WagerPrintBuilder {
    private String base = "";
    private String betDescription;
    private String outcomeDescription;
    private String eventTitle;
    private String outcomeOdd;
    private String wagerAmount;
    private String wagerIsWin;

    public WagerPrintBuilder() {
    }

    public WagerPrintBuilder setBase() {
        this.base = "Wager '" + betDescription + "=" + outcomeDescription
                + "' of " + eventTitle + " [odd: "
                + outcomeOdd + ", amount: " + wagerAmount;
        if (wagerIsWin == null){
            this.base = this.base + "] saved!";
        } else {
            this.base = this.base + "], win: " + wagerIsWin + ".";
        }
        return this;
    }

    public WagerPrintBuilder setBetDescription(Wager wager) {
        this.betDescription = wager.getOutcome().getBet().getDescription();
        return this;
    }

    public WagerPrintBuilder setOutcomeDescription(Wager wager) {
        this.outcomeDescription = wager.getOutcome().getDescription() ;
        return this;
    }

    public WagerPrintBuilder setEventTitle(Wager wager) {
        this.eventTitle = wager.getOutcome().getBet().getEvent().getTitle();
        return this;
    }

    public WagerPrintBuilder setOutcomeOdd(Wager wager) {
        this.outcomeOdd = wager.getOutcome().getOdd().toString();
        return this;
    }

    public WagerPrintBuilder setWagerAmount(Wager wager) {
        this.wagerAmount = wager.getAmount().toString();
        return this;
    }

    public WagerPrintBuilder setWagerIsWin(Wager wager) {
        this.wagerIsWin = Boolean.toString(wager.isWin());
        return this;
    }

    public String getPrintedWager(){
        return this.base;
    }
}

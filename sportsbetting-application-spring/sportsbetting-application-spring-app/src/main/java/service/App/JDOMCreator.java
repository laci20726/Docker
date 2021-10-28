package service.App;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;

import domain.Outcome;
import domain.Player;
import domain.SportEvent;
import domain.Wager;
import org.springframework.stereotype.Component;

@Component
public class JDOMCreator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public JDOMCreator() {
    }

    private void addChildElement(Element parent, String elementName, String textValue) {
        Element child = new Element(elementName);
        child.setText(textValue);
        parent.addContent(child);
    }

    private void createRoot(Element root, Player player) {
        addChildElement(root, "name", player.getName());
        addChildElement(root, "email", player.getEmail());
        addChildElement(root, "balance", player.getBalance().toString());
        addChildElement(root, "currency", player.getCurrency().toString());
    }

    protected List<SportEvent> sportEventListFromWagers(List<Wager> wagers) {
        List<SportEvent> finalSportEvents = new ArrayList<>();
        wagers.forEach(wager -> {
            SportEvent sp1 = wager.getOutcome().getBet().getEvent();
            finalSportEvents.add(sp1);
        });
        return finalSportEvents.stream().distinct().collect(Collectors.toList());
    }

    private void buildXMLTree(Element sportEventRoot, List<SportEvent> sportEvents, List<Wager> wagers) {
        sportEvents.forEach(sp -> {
            Element exportedSportEvent = new Element("exportedSportEvent");

            addChildElement(exportedSportEvent, "title", sp.getTitle());
            addChildElement(exportedSportEvent, "startDate", sp.getStartDate().format(FORMATTER));
            addChildElement(exportedSportEvent, "type", sp.toString());

            Element wagersRoot = new Element("exportedWagers");
            wagers.forEach(wager -> {
                if (sp.equals(wager.getOutcome().getBet().getEvent())) {
                    buildWagerLeaf(wagersRoot, wager);
                }
            });

            exportedSportEvent.addContent(wagersRoot);
            sportEventRoot.addContent(exportedSportEvent);
        });
    }

    private void buildWagerLeaf(Element wagersRoot, Wager wager) {
        Element exportedWager = new Element("exportedWager");
        Outcome o = wager.getOutcome();

        addChildElement(exportedWager, "betType", o.getBet().getDescription());
        addChildElement(exportedWager, "outcomeValue", o.getDescription());
        addChildElement(exportedWager, "amount", wager.getAmount().toString());
        addChildElement(exportedWager, "currency", wager.getCurrency().toString());
        addChildElement(exportedWager, "odd", o.getOdd().toString());
        addChildElement(exportedWager, "win", Boolean.toString(wager.isWin()));

        wagersRoot.addContent(exportedWager);
    }

    public Document createXMLDoc(Player player, List<Wager> wagers) {
        Document doc = new Document();
        Element root = new Element("exportedPlayerInfo");
        Element sportEventRoot = new Element("exportedSportEvents");
        List<SportEvent> sportEvents = sportEventListFromWagers(wagers);

        createRoot(root, player);
        buildXMLTree(sportEventRoot, sportEvents, wagers);

        root.addContent(sportEventRoot);
        doc.addContent(root);

        return doc;
    }
}

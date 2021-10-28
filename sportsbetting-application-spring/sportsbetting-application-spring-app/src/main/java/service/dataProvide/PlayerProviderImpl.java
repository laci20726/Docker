package service.dataProvide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import domain.Currency;
import domain.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlayerProviderImpl implements PlayerProvider {
    @Value("${data.playerPath}")
    private String filePath;
    public static final int POS_PLAYER_NAME = 0;
    public static final int POS_PLAYER_EMAIL = 1;
    public static final int POS_PLAYER_PASSWORD = 2;
    public static final int POS_PLAYER_BALANCE = 3;
    public static final int POS_PLAYER_CURRENCY = 4;

    public PlayerProviderImpl() {
    }

    public PlayerProviderImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Player> getRegisteredPlayers() {
        CSVReader csvReader = new CSVReader();
        List<Player> preDefPlayers = new ArrayList<>();
        List<List<String>> listOfPlayerData = csvReader.read(filePath);
        listOfPlayerData.forEach(data -> {
            Player p = new Player();
            p.setName(data.get(POS_PLAYER_NAME));
            p.setEmail(data.get(POS_PLAYER_EMAIL));
            p.setPassword(data.get(POS_PLAYER_PASSWORD));
            p.setBalance(BigDecimal.valueOf(Integer.parseInt(data.get(POS_PLAYER_BALANCE))));
            p.setCurrency(Currency.valueOf(data.get(POS_PLAYER_CURRENCY)));
            preDefPlayers.add(p);
        });

        return preDefPlayers;
    }
}

package service.dataProvide;

import domain.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerProviderImplTest {
    PlayerProviderImpl playerProvider = new PlayerProviderImpl("C:\\Prog_Java\\GitEpam\\mep\\sportsbetting-application-maven\\sportsbetting-application-app\\src\\test\\java\\service\\dataProvide\\SamplePlayer.csv");

    @Test
    public void testPlayerProviderShouldWorkFineWhenDataIsCorrect() {
        List<Player> players = playerProvider.getRegisteredPlayers();

        assertEquals(3, players.size());
        assertEquals("admin",players.get(2).getEmail());
        assertEquals("admin",players.get(2).getPassword());
    }

}

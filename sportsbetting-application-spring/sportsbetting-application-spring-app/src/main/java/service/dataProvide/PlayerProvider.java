package service.dataProvide;

import java.util.List;

import domain.Player;


public interface PlayerProvider {
    List<Player> getRegisteredPlayers();
}

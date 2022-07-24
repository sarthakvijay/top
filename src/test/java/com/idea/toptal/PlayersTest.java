package com.idea.toptal;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.repository.PlayerRespository;
import com.idea.toptal.service.PlayerService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class PlayersTest {

    @InjectMocks
    PlayerService playerService;

    @Mock
    PlayerRespository playerRespository;

    @BeforeEach
    protected void setupControllers() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreatePlayer() throws RecordNotFoundException {
        Player player = getPlayer(1200.00, 35, "John", "H", "Africa");
        Player player2 = getPlayer(1500.00, 32, "Dara", "O'Brein", "France");

        when(playerService.createOrUpdatePlayer(player)).thenReturn(player);
        when(playerService.createOrUpdatePlayer(player2)).thenReturn(player2);


        when(playerService.getAllPlayers()).thenReturn(new ArrayList<>(Collections.singletonList(player)));
        List<Player> list_player= playerService.getAllPlayers();
        Assert.assertTrue(list_player.size() == 1);
        Assert.assertTrue(list_player.contains(player));
    }

    @Test
    void testUpdateMarketValue(){
        Double initialMarketValue = 1200.00;
        Player player = getPlayer(initialMarketValue, 34, "A", "B", "India");

        Double askValue = 1700.00;
        player.updateMarketValue(askValue);

        Assert.assertTrue(player.getMarketvalue() > 1200.00);
        Assert.assertTrue(player.getMarketvalue() < askValue*2);
    }

    private Player getPlayer(Double marketValue, int age, String firstName, String lastName, String country){
        Player player = new Player();
        player.setTeamId("unitTestPlayer");
        player.setMarketvalue(marketValue);
        player.setAge(age);
        player.setCountry("Africa");
        player.setFirst_name(firstName);
        player.setLast_name(lastName);

        return player;
    }
}

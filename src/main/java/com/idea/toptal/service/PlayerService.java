package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.repository.PlayerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRespository playerRespository;

    public List<Player> getAllPlayers() {
        return playerRespository.findAll();
    }

    public Player getPlayerById(Long id) throws RecordNotFoundException {
        Optional<Player> player = playerRespository.findById(id);
        if(player.isPresent()){
            return player.get();
        }
        throw new RecordNotFoundException("No player with the given id present in Database");
    }

    public Player createOrUpdatePlayer(Player player) {
        if(player.getId() != null){
            Optional<Player> playerEntity = playerRespository.findById(player.getId());
            if(playerEntity.isPresent()){
                Player newEntity = playerEntity.get();
                newEntity.setFirst_name(player.getFirst_name());
                newEntity.setLast_name(player.getLast_name());
                newEntity.setPositions(player.getPositions());
                newEntity.setCountry(player.getCountry());
                newEntity.setAge(player.getAge());
                newEntity.setTeamId(player.getTeamId());
                player = newEntity;
            }
        }
        player = playerRespository.save(player);
        return player;
    }


    public void deletePlayerById(Long id) throws RecordNotFoundException {
        Optional<Player> player = playerRespository.findById(id);
        if(player.isPresent()){
            playerRespository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No player found with given in the database");
        }

    }
}

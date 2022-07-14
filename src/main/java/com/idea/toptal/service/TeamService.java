package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.models.Position;
import com.idea.toptal.models.Team;
import com.idea.toptal.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private static final Double DEFAULT_MARKET_VALUE = 1000.00;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerService playerService;

    public List<Team> getAllTeams(){
        List<Team> teams = teamRepository.findAll();
        return teams;
    }

    public Team getTeamById(Long id) throws RecordNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()) {
            return team.get();
        }
        throw new RecordNotFoundException("No team record exist for given id");
    }

    public Team createOrUpdateTeam(Team team) {
        if(team.getId() != null) {
            Optional<Team> teamEntity = teamRepository.findById(team.getId());
            if (teamEntity.isPresent()) {
                Team newEntity = teamEntity.get();
                newEntity.setName(team.getName());
                newEntity.setCountry(team.getCountry());
                newEntity.setMarketvalue(team.getMarketvalue());
                newEntity = teamRepository.save(newEntity);
                return newEntity;
            }
        }
        team = teamRepository.save(team);
        createDefaultPlayers(team);
        return team;
    }

    private void createDefaultPlayers(Team team) {
        Arrays.stream(Position.values()).forEach(e -> createPositionPlayers(e, team));
    }

    private void createPositionPlayers(Position position, Team team){
        for(int i=0;i<5;i++){
            Player player = new Player(
                    "first"+ team.getId(), "last" + team.getId(), team.getCountry(), team.getId(),
                    position, DEFAULT_MARKET_VALUE);
            playerService.createOrUpdatePlayer(player);
        }
    }

    public void deleteTeamById(Long id) throws RecordNotFoundException{
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            teamRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No team record exist for given id");
        }
    }

}

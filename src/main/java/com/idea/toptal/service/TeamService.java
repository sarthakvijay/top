package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.models.Position;
import com.idea.toptal.models.Team;
import com.idea.toptal.repository.TeamRepository;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class TeamService {

    @Value("${player.position}")
    String[] player_position;

    @Value("${player.count}")
    Integer[] player_count;

    @Value("${team.initial.budget}")
    Double team_budget;

    @Value("${player.initial.budget}")
    Double player_budget;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerService playerService;

    public List<Team> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams;
    }

    public Team getTeamById(String id) throws RecordNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        }
        throw new RecordNotFoundException("No team record exist for given id");
    }

    public Team createTeam(Team team) throws IllegalArgumentException {
        team.setBudget(team_budget);
        createDefaultPlayers(team);
        team = teamRepository.save(team);
        return team;
    }


    public Team updateTeam(Team team) throws IllegalArgumentException {
        if (team.getId() != null) {
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
        return team;
    }

    private void createDefaultPlayers(Team team) throws IllegalArgumentException {
        IntStream.range(0, player_position.length)
                .forEach(index -> createPositionPlayers(Position.valueOf(player_position[index]), player_count[index], team));
    }

    private void createPositionPlayers(Position position, Integer count, Team team) {
        for (int i = 0; i < count; i++) {
            Player player = new Player(
                    "first" + team.getId(), "last" + team.getId(), team.getCountry(), team.getId(),
                    position, player_budget);
            player = playerService.createOrUpdatePlayer(player);
            team.setMarketvalue(team.getMarketvalue() + player.getMarketvalue());
        }
    }

    public void deleteTeamById(String id) throws RecordNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            teamRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No team record exist for given id");
        }
    }

    public void updateBudget(String username, Double ask_value, boolean buy) {
        Team team = teamRepository.getById(username);
        team.setMarketvalue(team.getMarketvalue() + (buy ? ask_value : -ask_value));
        team.setBudget(team.getBudget() + (buy ? -ask_value : ask_value));
        teamRepository.save(team);
    }
}

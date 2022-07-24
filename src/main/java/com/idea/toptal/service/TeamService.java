package com.idea.toptal.service;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Player;
import com.idea.toptal.models.Position;
import com.idea.toptal.models.Team;
import com.idea.toptal.payload.response.MessageResponse;
import com.idea.toptal.repository.TeamRepository;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class TeamService {

    private static final String first_name = "first";
    private static final String last_name = "last";

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

    /**
     getting All team ( only for accessible to Admin */
    public List<Team> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams;
    }

    /**
     * Get the team details from database*/
    public Team getTeamById(String id) throws RecordNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        }
        throw new RecordNotFoundException("No team record exist for given id");
    }

    /**
     * creating team logic*/
    public Team createTeam(Team team) throws IllegalArgumentException {
        team.setBudget(team_budget);
        createDefaultPlayers(team);
        team = teamRepository.save(team);
        return team;
    }

    /**
     * Team update & persisting logic.*/
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

    /**
     * iteration over creating different set of position.*/
    private void createDefaultPlayers(Team team) throws IllegalArgumentException {
        IntStream.range(0, player_position.length)
                .forEach(index -> createPositionPlayers(Position.valueOf(player_position[index]), player_count[index], team));
    }

    /**
     * Creating & persisting each position set of player for new team in the database */
    private void createPositionPlayers(Position position, Integer count, Team team) {
        for (int i = 0; i < count; i++) {
            Player player = new Player(
                    first_name + team.getId(), last_name + team.getId(), team.getCountry(), team.getId(),
                    position, player_budget);
            player = playerService.createOrUpdatePlayer(player);
            team.setMarketvalue(team.getMarketvalue() + player.getMarketvalue());
        }
    }

    /**
     * deletion logic for team.*/
    public HttpStatus deleteTeamById(String id, String username) throws RecordNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            if(!id.equals(username)){
                ResponseEntity.ok(new MessageResponse("Error: Not your team."));
            }
            teamRepository.deleteById(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

    /**
     * updating the budget and Market Value of SELL team in the database.
     * @param username
     * @param ask_value
     */
    public void updateBudget(String username, Double ask_value) {
        Team team = teamRepository.getById(username);
        team.setMarketvalue(team.getMarketvalue() -ask_value );
        team.setBudget(team.getBudget() + ask_value);
        teamRepository.save(team);
    }

    /**
     * Logic for updating the Budget & Market Value of BUY team in the database.
     * @param username
     * @param ask_value
     * @param market_value
     */
    public void updateBudget(String username, Double ask_value, Double market_value) {
        Team team = teamRepository.getById(username);
        team.setMarketvalue(team.getMarketvalue() + market_value);
        team.setBudget(team.getBudget() - ask_value);
        teamRepository.save(team);
    }
}

package com.idea.toptal;

import com.idea.toptal.exception.RecordNotFoundException;
import com.idea.toptal.models.Team;
import com.idea.toptal.repository.TeamRepository;
import com.idea.toptal.service.TeamService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class TeamTest {

    @InjectMocks
    TeamService teamService;

    @Mock
    TeamRepository teamRespository;

    @BeforeEach
    protected void setupControllers() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateTeam() throws RecordNotFoundException {
        Team team1 = getTeam("testTeam", "India");
        Team team2 = getTeam("testTeam2", "Australia");

        when(teamService.updateTeam(team1)).thenReturn(team1);
        when(teamService.updateTeam(team2)).thenReturn(team2);

        when(teamService.getAllTeams()).thenReturn(new ArrayList<>(Collections.singletonList(team1)));
        List<Team> list_team = teamService.getAllTeams();
        Assert.assertTrue(list_team.size() == 1);
        Assert.assertTrue(list_team.contains(team1));
    }

    private Team getTeam(String name, String country) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);

        return team;
    }

}
